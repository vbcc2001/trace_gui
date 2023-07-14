## 一、开发环境准备

### 1. 配置好JDK: 要求JDK17

```bash
java -version
```

### 2. 配置好Maven: 要求3.8以上

```bash
 mvn -v
```


## 二、外设准备

### 1. 标签打印机: Zebra品牌，最好300dpi，并安装好驱动。

官网：https://www.zebra.com/cn/zh/support-downloads.html


### 2. 称重机：要求串口，推荐青松柏品牌

http://www.wooleesmart.com/

### 3. 扫描枪：非必须，可通过键盘模拟


## 三、前端本地调试运行

```bash 
mvn clean compile exec:java
```

## 四、后端本地调试运行

```bash
# 运行素士版本
mvn spring-boot:run -Dport=8082
# 运行小米版本
mvn spring-boot:run -Pxiaomi -Dport=8081
```



## V3版本Windows客户端安装包打包说明

### 一、打包前准备

1、下载并安装 NSIS 程序

下载地址：https://prdownloads.sourceforge.net/nsis/nsis-3.08-setup.exe?download

2、编译客户端

```
  mvn clean package
```

3、拷贝 target/main-ui2-x.x.x.jar 到 traceSys_v3/app/

4、拷贝 target/lib/*的新依赖包到 traceSys_v3/app/ 

5、如果有新增jar, 修改 traceSys_v3/app/traceSys.cfg 的加载路径

6、修改版本号

修改打包脚本 `trace-deplop-v3-front.nsi` 中的 `PRODUCT_VERSION` 版本号信息。

### 二、打包

1、运行NSIS程序

2、选择 `Compiler` > `Compile NSI scripts` 菜单，并选择 `trace-deplop-v3-front.nsi` 文件

3、等待安装结果

4、成功后，`trace-deplop-v3-front.nsi` 文件夹下将生成打包后的 `新追溯系统-客户端-VX.X.X.exe` 文件


## V3版本Windows服务端安装包打包说明（以小米为例）

### 一、打包前准备

1、编译服务端

```bash
#打包素士版本:
mvn clean package
# 打包小米版本
mvn clean pacakge -Pxiaomi
```

2、 复制 target目录下 server-xiaomi-V3-SNAPSHOT.jar 到 `traceServer_v3_xiaomi/lib/`

3、修改版本号

修改打包脚本 `trace-deplop-v3-backend-xiaomi.nsi` 中的 `PRODUCT_VERSION` 版本号信息。

### 二、打包

1、运行NSIS程序

2、选择 `Compiler` > `Compile NSI scripts` 菜单，并选择 `trace-deplop-v3-backend.nsi` 文件

3、等待安装结果

4、成功后，`trace-deplop-v3-backend.nsi` 文件夹下将生成打包后的 `新追溯系统-服务端-VX.X.X.exe` 文件