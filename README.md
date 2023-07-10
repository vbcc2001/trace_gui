### window系统调试

```bash
	# 启动项目:
	.\mvnw.cmd clean compile exec:java
	# 重新编译并执行单元测试(APITest为测试类,testGetLaser为测试函数名):	
	.\mvnw.cmd -Dtest=APITest#testGetLaser -D"maven.test.skip"=false test
	# 直接执行单元测试(不编译):
	.\mvnw.cmd -Dtest=APITest#testGetLaser -D"maven.test.skip"=false surefire:test
```

