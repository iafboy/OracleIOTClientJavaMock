# Oracle IOT Client Java版模拟程序
## API使用流程
1.连接并判断设备激活
2.发送与云中约定的结构化数据
## 主要需要注意SSL问题
JDK1.8可以直接使用如下直接信任证书
HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("XXX.XXX.XXX.XXX"));
