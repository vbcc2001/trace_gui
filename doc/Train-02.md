# 培训：第二节课

3. 本地构建前端安装包

- (1) 准备环境：windows 10 以上操作系统
- (2) 准备环境：安装打包软件  NSIS， 下载地址：https://prdownloads.sourceforge.net/nsis/nsis-3.08-setup.exe?download
-（3） 打包客户端： 编译代码   mvn clean package
-（4） 打包客户端： 复制编译后jar包   target/main-ui2-11.3.1.jar 到 deploy/traceSys_v3/app 文件夹下
-（5） 打包客户端： 复制依赖jar包     target/libs/* 到 deploy/traceSys_v3/app 文件夹下 
-（6） 打包客户端： 修改客户端版本号   修改 deploy/trace-deplop-v3-front.nsi 脚本的 PRODUCT_VERSION 参数 ，版本对应：config.toml中的 number参数
-（7） 打包客户端： 版本号规则：自己定义，目前到了 V3.1.12版，客户端修改下版本号（config.toml中的 number 参数）
-（8） 打包客户端： 运行NSIS程序 ，选择 `Compiler` > `Compile NSI scripts` 菜单，并选择 `trace-deplop-v3-front.nsi` 文件 ，会自动编译
-（9） 打包客户端： 成功后，`trace-deplop-v3-front.nsi` 文件夹下将生成打包后的 `新追溯系统-客户端端-VX.X.X.exe` 文件
-（10）打包客户端： 编译安装本质进行压缩、解压事项，并注册到windows系统。
-（10）打包客户端： traceSys_v3目录下/traceSys.exe 主程序 是JavaFX生成的主运行程序。
-（11）打包客户端： traceSys_v3目录下/runtime Java的运行环境
-（12）打包客户端： traceSys_v3目录下/app 放置编译后的jar文件及依赖包。
-（13）打包客户端： traceSys_v3目录下/app/traceSys.cfg Java jar包引用配置路径
-（14）打包客户端： 直接在电脑升级：替换 C:\Users\用户名\AppData\Local\traceSys_v3\app 下的jar包
-（15）打包客户端： 在线升级连接到远程的http文件夹服务，有对应的/app文件夹的jar包信息，如果发现不一致，就下载远程的jar包更新到app文件夹下。
-（16）打包客户端： 在线升级代码在com.dlsc.workbenchfx.demo.Helper.java 中


4. 本地构建后端安装包

- (1) 准备环境：windows 10 以上操作系统
- (2) 准备环境：安装打包软件  NSIS， 下载地址：https://prdownloads.sourceforge.net/nsis/nsis-3.08-setup.exe?download
-（3） 打包服务户端： 编译代码（小米版） mvn clean package -Pxiaomi
-（4） 打包服务户端： 复制 target目录下 server-xiaomi-V3-SNAPSHOT.jar 到 `traceServer_v3_xiaomi/lib/`
-（5） 打包服务户端： 修改打包脚本 `trace-deplop-v3-backend-xiaomi.nsi` 中的 `PRODUCT_VERSION` 版本号信息。
-（6） 打包服务户端： 运行NSIS程序，选择 `Compiler` > `Compile NSI scripts` 菜单，并选择 `trace-deplop-v3-backend.nsi` 文件，等待安装结果
-（7） 打包服务户端： 成功后，`trace-deplop-v3-backend.nsi` 文件夹下将生成打包后的 `新追溯系统-服务端-VX.X.X.exe` 文件
-（7） 打包服务户端： 服务端默认安装在  C:\Users\用户名\AppData\Local\traceServer_v3_xiaomi 下
-（8） 打包服务户端： 服务端安装完成会创建一个 Apache Common Daemon 后台服务
-（9） 打包服务户端： 运行 Apache Common Daemon 后台服务脚本在traceServer_v3_xiaomi/bin/install.bat  ,卸载脚本uninstall.bat 
-（10） 打包服务户端： /bin 文件夹放置  Apache Common Daemon 程序及脚本
-（11） 打包服务户端： /jre 文件夹放置  java运行环境
-（12） 打包服务户端： /lib 文件夹放置  编译后的Jar包
-（13） 打包服务户端： 服务端版本配置在  src\main\resources\version 文件中配置：server 配置服务器版本  client 最低客户端版本，如果客户端低于这个版本，服务器不提供服务。
-（14） 服务端默认端口是9000, 验证是否安装成功，打开http://127.0.0.1:9000 有响应。
-（15） 服务端生成的数据库文件路径：C:\Users\用户名\AppData\Local\traceServer_v3_xiaomi\db_v3_xiaomi 文件夹下  
-（16） 数据库备份：先停掉服务端（强制关闭任务管理器 Apache Commons Daemon Service Runner  后台服务），在复制数据库文件。


