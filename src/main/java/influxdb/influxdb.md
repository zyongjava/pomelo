## influxDB

#### 一、安装influxDB

安装环境： OS X Yosemite， 使用`brew`命令安装


```
brew install influxdb
```
执行结果:

```
➜  ~ brew install influxdb
==> Downloading https://homebrew.bintray.com/bottles/influxdb-0.13.0.yosemite.bo
######################################################################## 100.0%
==> Pouring influxdb-0.13.0.yosemite.bottle.tar.gz
==> Caveats
To have launchd start influxdb now and restart at login:
  brew services start influxdb
Or, if you don't want/need a background service you can just run:
  influxd -config /usr/local/etc/influxdb.conf
==> Summary
 /usr/local/Cellar/influxdb/0.13.0: 6 files, 48.7M
➜  ~
```

启动influxdb

```
influxd -config /usr/local/etc/influxdb.conf
```

执行结果ßß：

```
➜  ~ influxd -config /usr/local/etc/influxdb.conf

 8888888           .d888 888                   8888888b.  888888b.
   888            d88P"  888                   888  "Y88b 888  "88b
   888            888    888                   888    888 888  .88P
   888   88888b.  888888 888 888  888 888  888 888    888 8888888K.
   888   888 "88b 888    888 888  888  Y8bd8P' 888    888 888  "Y88b
   888   888  888 888    888 888  888   X88K   888    888 888    888
   888   888  888 888    888 Y88b 888 .d8""8b. 888  .d88P 888   d88P
 8888888 888  888 888    888  "Y88888 888  888 8888888P"  8888888P"

[run] 2017/03/03 11:31:41 InfluxDB starting, version 0.13.0, branch 0.13, commit e57fb88a051ee40fd9277094345fbd47bb4783ce
[run] 2017/03/03 11:31:41 Go version go1.6.2, GOMAXPROCS set to 4
[run] 2017/03/03 11:31:41 Using configuration at: /usr/local/etc/influxdb.conf
[store] 2017/03/03 11:31:41 Using data dir: /usr/local/var/influxdb/data
[subscriber] 2017/03/03 11:31:41 opened service
[monitor] 2017/03/03 11:31:41 Starting monitor system
[monitor] 2017/03/03 11:31:41 'build' registered for diagnostics monitoring
[monitor] 2017/03/03 11:31:41 'runtime' registered for diagnostics monitoring
[monitor] 2017/03/03 11:31:41 'network' registered for diagnostics monitoring
[monitor] 2017/03/03 11:31:41 'system' registered for diagnostics monitoring
[cluster] 2017/03/03 11:31:41 Starting cluster service
[shard-precreation] 2017/03/03 11:31:41 Starting precreation service with check interval of 10m0s, advance period of 30m0s
[snapshot] 2017/03/03 11:31:41 Starting snapshot service
[copier] 2017/03/03 11:31:41 Starting copier service
[admin] 2017/03/03 11:31:41 Starting admin service
[admin] 2017/03/03 11:31:41 Listening on HTTP: [::]:8083
[continuous_querier] 2017/03/03 11:31:41 Starting continuous query service
[httpd] 2017/03/03 11:31:41 Starting HTTP service
[httpd] 2017/03/03 11:31:41 Authentication enabled: false
[httpd] 2017/03/03 11:31:41 Listening on HTTP: [::]:8086
[retention] 2017/03/03 11:31:41 Starting retention policy enforcement service with check interval of 30m0s
[run] 2017/03/03 11:31:41 Listening for signals
[monitor] 2017/03/03 11:31:41 Storing statistics in database '_internal' retention policy 'monitor', at interval 10s
2017/03/03 11:31:41 Sending anonymous usage statistics to m.influxdb.com
[tsm1wal] 2017/03/03 11:31:51 tsm1 WAL starting with 10485760 segment size
[tsm1wal] 2017/03/03 11:31:51 tsm1 WAL writing to /usr/local/var/influxdb/wal/_internal/monitor/1
[shard] 2017/03/03 11:31:51 /usr/local/var/influxdb/data/_internal/monitor/1 database index loaded in 7.667µs

```

访问`http://127.0.0.1:8083` 查看是否安装成功

#### 二、Maven依赖

```
<dependency>
    <groupId>org.influxdb</groupId>
    <artifactId>influxdb-java</artifactId>
    <version>2.5</version>
</dependency>
```


#### 三、备注

端口说明：`8083`端口为界面显示端口，`8086`端口为数据库通讯端口

参考地址：https://docs.influxdata.com/influxdb/v1.2/introduction/getting_started/