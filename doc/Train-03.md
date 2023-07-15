# 培训：第二节课

5，代码功能讲解
（1） 代码位置src/main
（2） 引用库位置src/libs   core-gui-1.2-SNAPSHOT.jar  公共方法库 提供下源码jar
（3） 测试 src/test  , 主要标签打印时候用
（4） 前端框架：开源 workbenchfx  地址： https://github.com/dlsc-software-consulting-gmbh/WorkbenchFX
（5） src/main/resources 下 log4j2.xml  日志配置文件
（6） src/main/style.css 框架样式
（7） src/main/demo-locale_de_CH.properties 国际化，没用到
（8） resources/config.toml  moduleNames： 配置首页功能菜单名称配置
（9） resources/config.toml [version] number = "V3.1.12" 对应的客户端版本号  server ="V3.2.6" 服务端最低版本号， 手动修改
（10） resources/colorBoxSN.css 彩盒样式
（11） resources/application.ico 、application.ico  没用到
（12） resources/application.ico 、application.ico  没用到
（13） resources/zpl/xiaomi.png 标签打印中小米Logo图
（14） resources/zpl/其他 ：參考資料，都沒用
（15） resources/fonts/* ：基本不调整这里文件，估计没用
（16） resources/css/* ：框架自己的样式
（17） resources/com.dlsc.workbenchfx.deom/* ：框架自己的样式和图片
（18） src/logo/windows/* 项目Logo，workbenchfx打包成客户端用。（workbenchfx 本身自己支持打包成安装程序）
（19） src/logo/linux/*、 macosx/* 下文件没用
（20） src/java/module-info.java 打包用生成对应traceSys.cfg
（21） src/com.dlsc.workbenchfx.deom 源代码主目录
（22） src/com.dlsc.workbenchfx.deom/ExtendedDemo.java 项目的入口文件，public static void main(String[] args) 是入口方法 (参考 workbenchfx 框架)
（23） ExtendedDemo.java 包含项目授权，登录，业务参数缓存功能
（24） 全局缓存文件放在 C:\Users\用户目录\cache.bin    缓存业务源码：，com.dlsc.workbenchfx.demo.global.GlobalConfig, 缓存文件处理源码：com.dlsc.workbenchfx.demo.LocalCache
（25） 登录对应代码ExtendedDemo.java login()方法
（26） 证书验收在ExtendedDemo.java  start()方法  ，证书授权弹框Certificate()
（27） ExtendedDemo.java建议重构下
（28） src/java/modules/放置的是具体功能模块代码
（29） src/java/modules/colrBoxSN 对应的是彩盒功能
（30） src/java/modules/colrBoxSN/ColorBoxSNModule.java 彩盒功能入口文件
（31） src/java/modules/colrBoxSN/view/ColorBoxSNView.java 界面样式
（32） src/java/modules/colrBoxSN/model/ColorBoxSN.java 彩盒对象模型
（33） src/java/modules/其他 都和彩盒类似
（34） src/java/api/* 和服务端交互的接口API
（35） src/java/global/GlobalConfig  系统全局参数配置类
（36） src/java/global/ProductInfoForm  系统公用产品参数表单样式
 (37) src/java/zplTemplate  标签打印对应的模板源代码
 (38) src/java/zplTemplate/layouts 标签通用模板样式：中箱物流标签、中箱唯一码.... 对应小米给的标签图纸
 (39) src/java/zplTemplate/TP.... 对应的具体小米产品的标签样式
 (40) @PrintTemplate(value = {"TP40088","TP40089"}) 配置的小米对应产品的标签模板名称，和界面中产品设置中新增产品的打印模板对应
 (41)  添加新打印标签，要新建TP... 文件，自己写标签样式
 (42)  src/java/其他文件，都是对应功能类。

