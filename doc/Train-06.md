# 培训：第四节课

9.  辅助程序helper 授权补充说明
（1） 不能在在window运行, 在腾讯云下  Ubuntu 22.04.2 LTS 下， 默认安装Python 3.10.6下运行成功。
（2） helper 不依赖linux ，可以引用了其他AI包导致Windows下不能运行
（3） 运行前，先在数据库运行license.sql，创建license表
（4） 运行
```SHELL
# 安装依赖
pip install -r requirements.txt -i http://mirrors.aliyun.com/pypi/simple --trusted-host mirrors.aliyun.com
# 启动
python server.py
```
（5） 申请授权接口：http的POST请求： http://主机地址:10000/license， 接口样例：
```SHELL
# POST请求 ，路径 http://主机地址:10000/license  请求header配置：Content-Type application/json
# 请求格式 clientName 客户名，clientInfo：客户端序列化， days： 授权时间（天），token：作为一个授权申请的唯一标识。不同的内部角色或人对应一个token，用来追溯谁申请的授权。
{
 "clientName":"111",
 "clientInfo":"nS+DjuF8eMTyJ2XR1H/yhO6o2/DKYGDOss5wEkcEEzJTlkOKK242TqzpyTwFgZdKcILToYYr/Mw+vsi4MmApxazQ3/3lhcJ2WpBRsY7v6rw=",
 "days":"15",
 "token":"drizzt@2022"
}

# curl 命令
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
 "clientName":"111",
 "clientInfo":"nS+DjuF8eMTyJ2XR1H/yhO6o2/DKYGDOss5wEkcEEzJTlkOKK242TqzpyTwFgZdKcILToYYr/Mw+vsi4MmApxazQ3/3lhcJ2WpBRsY7v6rw=",
 "days":"15",
 "token":"drizzt@2022"
}' \
 'http://106.55.177.197:10000/license'

 # 返回的数据： data :授权码 days： 天数，user：客户名

{"license": {"data": "miSSlLrPFXpRJx29yldzPAABBwji4df+n8zwx1N+C0T+8atfrkFvAza7pI8t/HnSMCNo9wFEjWz5ZCMsPeVldJRDwYqcL4jBwcRK8Be9OXirVEgNjqL6RocooaQXXnOo4XOASpjat5kcR6tA7pCWt20bcWdXfbmN51XXmylSdfk=", "days": "15", "user": "111"}}

```
（5）  数据库表 License 记录字段：clientName 客户名 ,cpuID: 解密后的客户端序列号，days 天数（累计天数） start_date：申请授权开始日期，end_date 授权到期结束日期

---
| clientName | cpuId | days | token | start_date | end_date | 
| 111 | BFEBFBFF000306C3 | 30 | 门 | 2023-07-22 | 2023-08-22 | 

（6） license_2023 接口和数据库都没用到（计划增加PDA时候用的，后面没完成）

10.  辅助程序helper 自动升级功能说明

（0） 运行前先检查server.py 的 ROOT_DIR值 和 SERVER的值 ROOT_DIR: 指的是helper能访问到的升级文件夹，用来检索和客户端发的数据进行比对。
      SERVER 升级文件下的服务器地址路径 ，对应返回信息要升级的文件new_files，old_files中的文件地址
（1） helper正确运行起来 
（2） patch_server 接口是服务端升级的升级接口，还没有做完，不能使用。
（3） 客户端升级接口patch_list： http://主机地址:10000/patch_list
（4） 客户端对应代码在 com.dlsc.workbenchfx.demo.Helper.java 中  主方法s是updatePatch()
（5） updateServer() 更新服务端的方法。目前还没有做完，不能使用。
（6） 判断升级是测试客户端，还是生产客户端:   boolean test
 (7)  升级思路：客户端将jar包文件名和MD5信息发送给 helper程序接口：  http://主机地址:10000/patch_list  ，判断MD5值和 服务器下指定文件夹下的JAR包MD5是否一致，不一致就升级
（8） 客户端点击升级，会发生HPPT情况，目前请求helper 的地址是写死的，要在代码124行中修改 String res 的内容改为：  http://106.55.177.197:10000/patch_list 
（9）客户端不分工厂版本，所有工厂都是一个版本。只是版本号有高有底，通过升级都会升级到最新版。
 (10) 客戶端隐藏用户用：super-admin(手动配置了这个用户才能用)，登录后可以切换先行版环境（区别就在于升级的uri地址不应用，用于部分客户测试、紧急问题处理用） 
（11） 请求样例数据格式

```SHELL
# POST请求 ，路径 http://主机地址:10000/patch_list   请求header配置：Content-Type application/json
# 请求格式  files 客户端文件名列表，checksums：文件的MD5值信息， uri： 服务端放置升级文件的地址。
# 目前客户端 有测试和生产版本2个标识。，升级服务器地址会变化：
# 如果是测试版：/trace/installer/test/V3/front/traceSys/app/
# 如果是生产版：/trace/installer/release/V3/front/traceSys/app/

{"files":"application.png...",
 "checksums":"f9451b9e762003928cabad1d6a2b06f4....",
 "uri":"/trace/installer/release/V3/front/traceSys/app/"}
# 返回参数：patch_list :需要升级的文件列表 
# new_files ：新增加文件
# old_files ：老的文件

 {
"patch_list":{
"new_files": "http://106.55.177.197:10000//trace/installer/release/V3/front/traceSys/app/1.txt,",
"old_files": "application.png...,"
}
```
（12） 客户端根据升级返回的信息，去更新下载对应文件。然后重启

（13） 需要用NG搭建一个文件服务器。提供文件的http地址。对应修改server.py 的  SERVER的值




