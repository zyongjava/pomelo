## jmeter使用指南

1. 在`Test Plan`下新建一个`Thread Group`
2. 选择`Thread Group`右键新建一个`Config Element`, 作为请求的参数
3. 选择	`Thread Group`右键 (Add ->Sampler -> HTTP Request)， 作为发送请求的http
4. 选中`Thread Group`右键(Add -> Listener -> Summary Report), 作为结果报告

#### Thread Group参数

1. Number of Threads(users):一个用户占一个线程，200个线程就是模拟200个用户

2. Ramp-Up Period(in seconds):设置线程需要多长时间全部启动。

3. Loop Count: 每个线程发送请求的次数


#### 参考

http://www.cnblogs.com/TankXiao/p/4059378.html?utm_source=tuicool