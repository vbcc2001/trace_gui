# set publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB"
# rem $1:用户序列号, $2:授权期限,按天算 ,$3:token
# java src/main/java/com/dlsc/workbenchfx/demo/RSAUtil.java %publicKey% %1 %2 %3
rem $1:用户标识,$2:用户序列号, $3:授权期限,按天算 ,$4:token
java src/main/java/com/dlsc/workbenchfx/demo/RSAUtil.java %1 %2 %3 %4
