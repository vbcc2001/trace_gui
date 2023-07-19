from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5
from base64 import b64decode
from base64 import b64encode
import datetime
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from Crypto.Cipher import AES
from Crypto import Random
import hashlib
import base64
import json
import os





publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB"


def unpad(byte_array):
    last_byte = byte_array[-1]
    return byte_array[0:-last_byte]

# AES 'pad' byte array to multiple of BLOCK_SIZE bytes
def pad(byte_array):
    BLOCK_SIZE = 16
    pad_len = BLOCK_SIZE - len(byte_array) % BLOCK_SIZE
    return byte_array + (bytes([pad_len]) * pad_len)

def aes_decrypt(key, message):
    """
    Input encrypted bytes, return decrypted bytes, using iv and key
    """
    byte_array = base64.b64decode(message)
    iv = byte_array[0:16] # extract the 16-byte initialization vector
    messagebytes = byte_array[16:] # encrypted message is the bit after the iv
    cipher = AES.new(key.encode("UTF-8"), AES.MODE_CBC, iv )
    decrypted_padded = cipher.decrypt(messagebytes)
    decrypted = unpad(decrypted_padded)
    return decrypted.decode("UTF-8");


def aes_encrypt(key, message):
    """
    Input String, return base64 encoded encrypted String
    """

    byte_array = message.encode("UTF-8")

    padded = pad(byte_array)

    # generate a random iv and prepend that to the encrypted result.
    # The recipient then needs to unpack the iv and use it.
    iv = os.urandom(AES.block_size)
    cipher = AES.new( key.encode("UTF-8"), AES.MODE_CBC, iv )
    encrypted = cipher.encrypt(padded)
    # Note we PREPEND the unencrypted iv to the encrypted message
    return base64.b64encode(iv+encrypted).decode("UTF-8")


def rsa_encrypt(public_key,s):
    key = b64decode(public_key)
    key = RSA.importKey(key)

    cipher = PKCS1_v1_5.new(key)
    text = cipher.encrypt(bytes(s, "utf-8"))
    #print(text)
    ciphertext = b64encode(text)

    return ciphertext


def parse(clientInfo,days):

    #先用aes解密
    try:
        clientInfo = aes_decrypt("2022@upupcat.com", clientInfo)
    except:
        pass

    randomUUID = clientInfo[0:32]
    cpuId = clientInfo[32:]
    start_date = datetime.datetime.now()
    end_date = start_date+datetime.timedelta(days)
    expiredDateStr = datetime.datetime.strftime(end_date, "%d%m%Y")
    originalString = randomUUID  + expiredDateStr + cpuId
    msg = rsa_encrypt(publicKey,originalString)

    start_date_str = datetime.datetime.strftime(start_date,"%Y-%m-%d")
    end_date_str = datetime.datetime.strftime(end_date,"%Y-%m-%d")
    return msg,cpuId,end_date_str,start_date_str


def parse_2023(clientInfo,authorize_type,days):
    #这个函数兼容pda以及所有新项目
    #先用aes解密
    clientInfo = aes_decrypt("2022@upupcat.com", clientInfo)
    clientInfo = json.loads(clientInfo)

    hard_fingerprint = clientInfo['hard_fingerprint']
    soft_fingerprint = clientInfo['soft_fingerprint']

    start_date = datetime.datetime.now()
    expired_date = datetime.datetime.strftime(start_date+datetime.timedelta(days), "%Y-%m-%d")

    authorizedInfo = {"hard_fingerprint":hard_fingerprint,"soft_fingerprint":soft_fingerprint,
            "authorize_type":authorize_type,"expired_date":expired_date}

    authorizedInfo = json.dumps(authorizedInfo)
    print(authorizedInfo)
    msg = aes_encrypt("2022@upupcat.com", authorizedInfo)

    return msg,hard_fingerprint,start_date,expired_date



if __name__ == '__main__':
    clientInfo = "tVB18WtMTvXvu6qM+PBBr5QPVfDUtpOTmjYRI4qBLOei1mekE5fBJ6LgVIMGWIDMO2Z/MNPyQU6u4ylK9RCN/AdoYjJYIx+/xxh2GbrHxWsN/9p7lNOnje1sZeeuwPDVMMcqc6ZfSp/7xngDG1ZWCqspnLaOdj3Kq8/ssTujVpCx4E1/iW7UJy7JOv/ZzMCv+0sc251pkjKzPTA6W+oqpA=="
    clientInfo = "rMCx2COhPPHHCxmyPP/oDAt6ApdKBmBm2x/zI4wTrXFAQeeVjpqIBL+qLKwJNlmmN0TrbDkHAnTVTO+cSrRC86ghUaaD0xaDOCESec6tFDC+CorvdGFqJNtNc+QrN/T34dDOFRQ9Eh7qnjfbEOQxIig9PQmzmJiDuIb8XyTveU4XxdtFkok+lyQqsjhLA9x4q1s77kcVh9qydVffmHUoBw=="
    msg = parse_pda(clientInfo,"",0)
    print(msg)




