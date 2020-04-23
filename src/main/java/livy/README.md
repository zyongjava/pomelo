### LIVY使用


### 前言

	本次部署是在单台机器10.57.17.215上部署，只部署了spark和livy；通过配置livy加载本地文件jar；然后通过livy rest api提交spark任务；

####一、前置条件

	1.安装spark: 
	2.安装hadoop(可选，没有使用本地文件测试)

####二、安装部署

1）下载安装包

	http://livy.incubator.apache.org/download/
	
2）	创建配置文件`livy.conf`

```
cd apache-livy-0.7.0-incubating-bin/conf
cp livy.conf.template livy.conf
vi livy.conf
```
`livy.conf`中修改内容如下：

```
# What host address to start the server on. By default, Livy will bind to all network interfaces.
livy.server.host = 10.57.17.215

# What spark master Livy sessions should use.
livy.spark.master = spark://10.57.17.215:7077

# List of local directories from where files are allowed to be added to user sessions. By
# default it's empty, meaning users can only reference remote URIs when starting their
# sessions.
livy.file.local-dir-whitelist = /home/admin/apps/jars/
```

3）	创建配置文件`livy-env.sh`

```
cd apache-livy-0.7.0-incubating-bin/conf
cp livy-env.sh.template livy-env.sh
vi livy-env.sh
```
`livy-env.sh`中新增内容如下：

```
export SPARK_HOME=/usr/lib/spark
export HADOOP_CONF_DIR=/etc/hadoop/conf
```

4）	创建配置文件`spark-blacklist.conf`

```
cd apache-livy-0.7.0-incubating-bin/conf
cp spark-blacklist.conf.template spark-blacklist.conf
vi spark-blacklist.conf
```
`spark-blacklist.conf`中修改内容如下（注释掉下面2行内容），让客户端可以配置这2个参数：

```
# Disallow overriding the master and the deploy mode.
#spark.master
#spark.submit.deployMode
```

5）启动程序

```
cd apache-livy-0.7.0-incubating-bin

./bin/livy-server start
```

6）验证测试

```
http://10.57.17.215:8998/ui
```

####三、REST调用

API文档地址：http://livy.incubator.apache.org/docs/latest/rest-api.html


###四、异常问题
问题：requirement failed: Local path /home/admin/apps/jars/etl.jar cannot be added to user sessions.

解决办法：修改`livy.conf`配置

```
livy.file.local-dir-whitelist = /home/admin/apps/jars/
```
