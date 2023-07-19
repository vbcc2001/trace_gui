# 培训：第四节课

7，辅助程序helper
（1） 两个功能：生成授权码，自动升级后台
（2） helper部署用的Docker,运行在liunx。
（3） 打包命令：docker build -t helper:v1 . 
（4） 运行：docker run -p 10000:10000 -v /home/git/docs/devDoc/:/app/uploadfile --name helper -d helper:v1
（5）命令说明：开通端口10000，
（6）外部目录/home/git/docs/devDoc/挂载到docker容器/app/uploadfile，  /home/git/docs/devDoc ：要升级的jar文件夹 ，内部py程序读取目录 /app/uploadfile
（7）--name helper 运行容器起名 -d 自动启动  helper:v1  ：刚打包的容器
（8）容器复制，粘贴命令，用于不同电脑上容器传输。docker save -o helper.tar helper:v1   docker load < helper.tar
（9）Dockerfile :docker 打包配置文件  ，启动服务命令：python server.py
（10）不用dockers ，分2步运行：
```SHELL
# 安装依赖
pip install -r requirements.txt -i http://mirrors.aliyun.com/pypi/simple --trusted-host mirrors.aliyun.com
# 启动
python server.py
```
（11）herper/web/* web服务代码
（12）herper/web/keys 客户端授权数据加密RSA证书: 工厂提供序列号，服务端配置授权天数、客户名等信息后，进行加密，生成授权码。
（13）herper/web/digital_sign.py  数字签名
（14）herper/web/RSA.py  加密
（15）herper/web/server.py  web服务、授权、升级功能
（16）herper/requirements.txt 依赖配置信息
（17）herper/License_2023.sql 记录授权申请信息的数据表
（18）herper/example.ini 数据库配置
（19）herper/web/server.py  token_to_name方法： token 作为一个授权申请的唯一标识。不同的内部角色或人对应一个token，用来追溯谁申请的授权。
（20 ） herper/web/server.py   license_2023方法： 授权入口（http接口）
（21 ） herper/web/server.py   ？？？ ： 检查客户端是否更新入口
（22） herper/rsa.bat 请授权码批处理命令

--------------------------------------------------------------------------------

8.  新产品标签开发(假设新产品ID 888888)
  (1) 先收到产品标签图纸
 （2）客户端在com.dlsc.workbenchfx.demo.zplTemplate下创建模板类：TP888888.java
  (3) 修改标签名 @PrintTemplate 
  (4) 继承模板服务类 TPService ,实现了TPInterface 小米6个标签接口 （彩盒、栈板目的仓、栈板唯一码、中箱、中箱唯一码）
 （5） 检查新产品标签和 TPService 默认标签是否一样，一样就不改，不一样就覆写对应方法
 （6） 标签打印语言用的zpl格式，在线预览网站：http://labelary.com/viewer.html
  (7)  测试方法1：启动客户端，配置对应的打印机为斑马打印机，现在mock打印 。 控制台就会输出打印的zpl代码，复制zpl代码，到预览网站预览
 （8） 参考test/java/com/demo/lgh/zpl中的测试代码，编写自己的标签test代码，输出zpl代码,直接输出图片

```SHELL
package com.demo.lgh.zpl;

import com.dlsc.workbenchfx.demo.zplTemplate.layouts.ColorBox;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.PalletMark;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 江西乐丰标签打印Demo
 * @author LiangGuanHao
 *
 */
public class TP888888Test {

	public static void main(String[] args) {
		TP888888Test.xiaoMiColorBox();
	}
	public static void xiaoMiColorBox() {

		//彩盒
		var dpi = 600;
		var SNCode = "40088/SSAGBF2ZA00001";
		var sku = "BHR6046CN";
		var color =  "白色";
		var createDate = "20220606";
		var productName = "米家便携式冲牙器F300";
		var code69 = "6934177784828";
		var zpl = ColorBox.layout02_50_28(true, dpi,SNCode,sku,color,createDate,productName, code69);
		System.out.println(zpl.toZPL());

        try {
			ImageIO.write(zpl.toPNG(), "png", new File("target/layout03_117_60.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
```SHELL
