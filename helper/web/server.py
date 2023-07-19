from flask import Flask, request, send_from_directory,redirect,url_for,jsonify
from flask_cors import *
from flask_restful import Resource, Api
from flask_restful import reqparse
import os
import string
import random
from pathlib import Path
import werkzeug
from datetime import datetime
import hashlib
import RSA
import pymysql
import configparser
from werkzeug.utils import secure_filename
import json
import zipfile
import hashlib
import re
import six
import base64
from PIL import Image
import tempfile

auth_days = 1


app = Flask(__name__)
CORS(app, supports_credentials=True)
api = Api(app)

ROOT_DIR = "/app/uploadfile/"
ROOT_DIR = "/home/chen/workspace/devDoc/"
SERVER = "http://doc.upupcat.com/"

config = configparser.ConfigParser()
config.read('example.ini')


ip = config['DEFAULT']['ip']
port = int(config['DEFAULT']['port'])
user = config['DEFAULT']['user']
passwd = config['DEFAULT']['passwd']
db = config['DEFAULT']['db']

# Connect to the database
try:
    connection = pymysql.connect(host=ip,
                                 user=user,
                                 port=port,
                                 password=passwd,
                                 database=db,
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)
except Exception as e:
    print(e)
    print("连接数据库失败了哦!,没办法那就暂时不入库保证生产")
    connection = None

def record(clientName, cpuId, days, token, start_date, end_date):
    token = token_to_name(token)
    print(token)
    connection.ping(reconnect=True)
    with connection:
        with connection.cursor() as cursor:
            sql = "select clientName,cpuId, days, token from License where cpuId = '%s'"%cpuId
            #print(sql)
            cursor.execute(sql)
            row = cursor.fetchone()
            #print('新授权信息: 天数(%s), 客户名(%s), token(%s), cpuId(%s)'%(days,clientName,token,cpuId))
            if row is None:
                sql = "insert into License (clientName, cpuId, days, token, start_date, end_date) values ('%s','%s',%d,'%s','%s','%s')"%(clientName,cpuId,days,token,start_date,end_date)
                cursor.execute(sql)
            else:
                print(row)
                #print('旧授权信息',row)
                days += row['days']
                print(start_date,end_date)
                sql = "update License set days = %d,clientName = '%s',token = '%s',end_date='%s' where cpuId = '%s'"%(days,clientName,token,end_date,cpuId)
                cursor.execute(sql)
        connection.commit()

tokens = ["drizzt@2022","maozi@2023"]

def token_to_name(token):
    print('token is :'+ token)
    if token == 'chy@2022':
        return "陈华友"
    if token == 'lgh@2022':
        return '梁观浩'
    if token == 'drizzt@2022':
        return '门士松'
    if token == 'maozi@2023':
        return '毛海滨'

#兼容PDA跟新的项目
def record_2023(clientName, hard_fingerprint, token, start_date, end_date, authorize_type, project_name):
    token = token_to_name(token)
    print(token)
    connection.ping(reconnect=True)
    with connection:
        with connection.cursor() as cursor:
            sql = "select clientName,hard_fingerprint, token, start_date from License_2023 where hard_fingerprint = '%s'"%hard_fingerprint
            #print(sql)
            cursor.execute(sql)
            row = cursor.fetchone()
            #print('新授权信息: 天数(%s), 客户名(%s), token(%s), cpuId(%s)'%(days,clientName,token,cpuId))
            if row is None:
                sql = "insert into License_2023 (clientName, hard_fingerprint, token, start_date, end_date, authorize_type, project_name) values ('%s','%s','%s','%s','%s','%s','%s')"%(clientName,hard_fingerprint,token,start_date,end_date,authorize_type,project_name)
                cursor.execute(sql)
            else:
                print(row)
                #print('旧授权信息',row)
                print(start_date,end_date)
                sql = "update License_2023 set token = '%s',end_date='%s',authorize_type='%s' where hard_fingerprint = '%s'"%(token,end_date,authorize_type,hard_fingerprint)
                cursor.execute(sql)
        connection.commit()


def patch_list():
    parser = reqparse.RequestParser()
    parser.add_argument("uri")
    parser.add_argument('files')
    parser.add_argument('checksums')
    args = parser.parse_args()
    files = args["files"].split(",")
    checksums = args["checksums"].split(",")
    uri = args["uri"]
    new_files = ""
    #print(files,checksums)
    server_files = []
    for path in Path(ROOT_DIR+uri).glob("*.*"):
        server_files.append(path.name)
        #print(path.name,path)
        if path.name not in files:
            new_files += SERVER + uri + path.name + ","
            continue
        checksum_server = get_md5(str(path))
        checksum_client = checksums[files.index(path.name)]
        #print(checksum_server,checksum_client)
        if checksum_server != checksum_client:
            new_files += SERVER + uri + path.name + ","
    old_files = ""
    for _file in files:
        if _file not in server_files:
            old_files += _file + ","

    data = {'new_files':new_files,'old_files':old_files}

    return data

def patch_server():
    parser = reqparse.RequestParser()
    parser.add_argument("uri")
    parser.add_argument('server_version')
    args = parser.parse_args()
    uri = args["uri"]
    ver = args["server_version"]
    print(ver)
    for path in Path(ROOT_DIR+uri).glob("*.*"):
        if 'server' in path.name:
            version = get_jar_version(ROOT_DIR+uri+"/"+path.name)
            break
    if ver == version:
        return SERVER + uri
    print("不需要更新")
    return ""

def get_jar_version(jar_file):
    """prints out .class files from jar_file"""
    zf = zipfile.ZipFile(jar_file, 'r')
    try:
        with zf.open('BOOT-INF/classes/version') as myfile:
            data = myfile.read()
            output = str(data, 'UTF-8')
            lines = output.split("\n")
            x = lines[1].strip()
            y = lines[3].strip()
            z = lines[5].strip()
            version = "%s.%s.%s"%(x,y,z)
            return version
    finally:
        zf.close()
    return None



def get_md5(filename):
    with open(filename,"rb") as f:
        bytes = f.read() # read file as bytes
        readable_hash = hashlib.md5(bytes).hexdigest();
        return readable_hash

def license():
    parser = reqparse.RequestParser()
    parser.add_argument("clientName")
    parser.add_argument("clientInfo")
    parser.add_argument('days')
    parser.add_argument('token')
    args = parser.parse_args()
    clientInfo = args["clientInfo"]
    clientName = args["clientName"]
    days = int(args["days"])
    token = args["token"]

    print(clientInfo,days,token)

    if token not in tokens:
        print("错误token!"+token)
        return "wrong token"
    # 为了避免给客户占便宜,长时间授权暂时只由华友操作
    #if token != "chy@2022": 
    #    if days > auth_days:
    #        days = auth_days

    msg,cpuId,end_date,start_date = RSA.parse(clientInfo,days)
    if connection is not None:
        try:
            record(clientName, cpuId, days, token, start_date, end_date) 
        except Exception as e:
            print(e)
            print("记录失败,那就不记录了")
    msg = msg.decode('utf-8')
    data = {'data':msg,'days':str(days),'user':clientName}
    return data


def license_2023():
    parser = reqparse.RequestParser()
    parser.add_argument("clientName")
    parser.add_argument("clientInfo")
    parser.add_argument('days')
    parser.add_argument('authorize_type')
    parser.add_argument('token')
    parser.add_argument('project_name')
    args = parser.parse_args()
    clientInfo = args["clientInfo"]
    clientName = args["clientName"]
    project_name = args["project_name"]
    authorize_type = args["authorize_type"]
    days = int(args["days"])
    token = args["token"]

    print(clientInfo,days,token)

    if token not in tokens:
        print("错误token!"+token)
        return "wrong token"

    # 为了避免给客户占便宜,长时间授权暂时只由华友操作
    #if token != "chy@2022": 
    #    if days > auth_days:
    #        days = auth_days

    msg,hard_fingerprint,start_date,end_date = RSA.parse_2023(clientInfo,authorize_type,days)
    if connection is not None:
        try:
            record_2023(clientName, hard_fingerprint, token, start_date, end_date, authorize_type, project_name) 
        except Exception as e:
            print(e)
            print("记录失败,那就不记录了")
    #msg = msg.decode('utf-8')
    data = {'data':msg,'days':str(days),'user':clientName,'project_name':project_name}
    return data

    

ALLOWED_EXTENSIONS = {'txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif', 'exe', 'jar'}

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/upload/<project>/<token>', methods=['GET', 'POST'])
def upload_file(project,token):

    if token != "cgm123456":
        return "token error"
    root_dir = ROOT_DIR + project

    if request.method == 'POST':
        # check if the post request has the file part
        if 'test_patch' in request.files:
            file = request.files['test_patch']
            # If the user does not select a file, the browser submits an
            # empty file without a filename.
            if file.filename == '':
                flash('No selected file')
                return redirect(request.url)
            if file and allowed_file(file.filename):
                filename = secure_filename(file.filename)
                file.save(os.path.join(root_dir + "/" + "installer/test/traceSys/app/", filename))
                return "upload patch file success"
        if 'patch' in request.files:
            file = request.files['patch']
            # If the user does not select a file, the browser submits an
            # empty file without a filename.
            if file.filename == '':
                flash('No selected file')
                return redirect(request.url)
            if file and allowed_file(file.filename):
                filename = secure_filename(file.filename)
                file.save(os.path.join(root_dir + "/" + "installer/traceSys/app/", filename))
                return "upload patch file success"

        if 'client' in request.files:
            file = request.files['client']
            # If the user does not select a file, the browser submits an
            # empty file without a filename.
            if file.filename == '':
                flash('No selected file')
                return redirect(request.url)
            if file and allowed_file(file.filename):
                filename = secure_filename(file.filename)
                file.save(os.path.join(root_dir + "/" + "installer/", filename))
                return "upload client file success"

    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>%s</h1>
    <h2>上传测试补丁</h2>
    <form method=post enctype=multipart/form-data>
      <input type=file name=test_patch multiple>
      <input type=submit value=Upload>
    </form>
    <h2>上传正式补丁</h2>
    <form method=post enctype=multipart/form-data>
      <input type=file name=patch multiple>
      <input type=submit value=Upload>
    </form>
    <h2>上传客户端</h2>
    <form method=post enctype=multipart/form-data>
      <input type=file name=client multiple>
      <input type=submit value=Upload>
    </form>

    '''%project


def base64_to_PIL(string):
    """
    base64 string to PIL
    """
    try:    
            base64_data = base64.b64decode(string)
            buf = six.BytesIO()
            buf.write(base64_data)
            buf.seek(0)
            img = Image.open(buf).convert('RGB')
            return img
    except Exception as e:
        print(e)
        return None


from table import extract
from table import NpEncoder
def table_to_json():
    parser = reqparse.RequestParser()
    parser.add_argument('data',required=True) 
    data = parser.parse_args()
     
    img = data['data']
     
    img = re.sub(' ','+',img)
    img = base64_to_PIL(img)
    print(img)
    im = img.convert("RGB")
    with tempfile.TemporaryDirectory() as tmpdirname:
        path = tmpdirname+"table.png";
        im.save(path)
        res = extract(path)
        #return jsonify(res,cls=NpEncoder)
        return res
        


todos = {
            'favicon.ico':'',
            'patch_list':patch_list,
            'patch_server':patch_server,
            'license':license,
            'license_2023':license_2023,
            'table_to_json':table_to_json,
        }

class TodoSimple(Resource):
        def get(self, todo_id):
            return {todo_id: todos[todo_id]}

        def post(self,todo_id):
            print('todo --> {}'.format(todo_id))
            return {todo_id: todos[todo_id]()}

api.add_resource(TodoSimple, '/<string:todo_id>')

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=10000)
    #app.run(host='0.0.0.0',port=10001)
