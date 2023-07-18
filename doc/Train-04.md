# 培训：第四节课

5，服务端代码功能讲解
（1） 服务端jdk17  ,springboot 2.7, 数据库组件JPA，数据库表不存在，会自动创建表
（2） pom.xml 中build节点配置打包参数：版本号、端口、小米版素士版参数
（3） src/main/resources 配置文件  client-type 对应：小米版素士版参数，对应代码com.kwanhor.trace.server.TraceConfig 
（4） src/main/version  配置系统版本号和客户端最低可连接版本号
（5） src/com.kwanhor.trace.server.advice/RestResponseAdvice: 切面处理(请求前后增加附加操作，统一处理系统错误后返回给前端错误信息)
（6） src/com.kwanhor.trace.server.api/*  提供给客户端接口API
（7） src/com.kwanhor.trace.server.api/CounterAPI  统一流水计数器API: 前端镭射、装箱、装栈预生成SN代码流水号生成。防止多个客户端生成SN流水码重复
（7） src/com.kwanhor.trace.server.error/* 错误信息封装
（7） src/com.kwanhor.trace.server.init/DictionaryManager 系统启动初始化: 数据字典初始化，
      注意：（V2版客户端的数据字典也存在了数据库，导致A客户端修改了参数，会影响B客户端，V3版本客户端数据字典放到客户端缓存中了。）
（8） src/com.kwanhor.trace.server.init/H2BackupManager 数据库自动备份定时任务初始化
（9） src/com.kwanhor.trace.server.model/* 数据建模，部分对应数据库表，，DataRecycle：数据调整的记录信息
（10）src/com.kwanhor.trace.server.model/* 比较重要的表：ProductInfo 产品信息表 ，ProductLaser ：产品镭射表/彩盒表（SN码入口表） ProductDetailInfo： 产品查询中明细信息 CartoonBox：中箱表 Pallet： 对应栈板表  UserInfo：用户信息表
（11） src/com.kwanhor.trace.server.query/* 通用分页查询的封装
（12） src/com.kwanhor.trace.server.repo/* 数据库对象查询方法,类似mybeais 的配置的xml文件
（13） src/com.kwanhor.trace.server.util/* 公用方法
（14） src/com.kwanhor.trace.server/TraceConfig 配置参数
（15） src/com.kwanhor.trace.server/TraceServerApplication 系统启动入口

6. 数据库表说明：（后续提供数据说明文件）

   CARTOON_BOX : 中箱表
   DATA_RECYCLE ：数据操作记录回收站
   DICTIONARY : 数据字典表（可能没用了）
   LOGIN_USER： ？？
   PALLET： 栈板
   PRODUCT_CODE： ？
   PRODUCT_DETAIL_INFO ：产品明细（视图）
   PRODUCT_INFO ： 产品设置信息表
   PRODUCT_LASER： 产品SN码表（核心表，数据从彩盒扫描录入）
   USER_INFO： 用户信息表