### **目的**
Kafka 作为一个实时的消息中间件，其高可用的重要性毋庸置疑。然而在我们之前的线上环境中，三台 kafka 服务器共同存在于一台物理机上。一旦这台物理机由于意外情况（如断电）而挂机。整个 kafka 服务就会中断。因此，我们的目标是将位于同一台物理机上的三台虚拟机分别迁移至不同的物理节点上。

### **原理简述**
要实现 kafka 的平滑迁移，需要了解一下其中的原理。官方文档：http://kafka.apache.org/documentation.html

用最简单的语言来描述，在kafka 消息系统中，存在多个producer，这些 producer 会在 kafka 中发布 topic，并不断往相应的 topic 中发送消息。同时，也存在多个 consumer，这些 consumer 会定时拉取相应 topic 中的消息。

一个 topic 可以设定由多个 partition 来存储消息，其中每个 partition 位于不同的节点（broker）上。一个 topic 对应的消息到来之后，kafka 随机选取一个 partition 写入消息。与此同时，每个 partition 同时存在多个 replicas 以实现容错。其中一个 replicas 作为 leader 承担来自 producer 的写请求和来自 consumer 的读请求。其他的 replicas 则不断从 leader 同步最新的数据。

### **风险所在**
由于在迁移的过程中，需要将其中每一台虚拟机上的 kafka 服务一一重启。所以必然会存在其中的某台 broker 所管理的 partition的消息落后于其他 broker 的情况发生。 所以，我们需要注意如何在 broker 重启之后恢复其 partition 中落后于其他节点的那部分消息。

### **操作步骤**
在开始迁移之前，首先查看一下 kafka 系统内部的 topic 状态。

```shell
sudo /usr/install/kafka/bin/kafka-topics.sh --zookeeper 10.57.47.83,10.57.47.84,10.57.47.86:2181 –describe
```

在 10.57.47.83， 10.57.47.84， 10.57.47.86 每台机器上，分别执行如下的操作

停止 broker

```shell
sudo /usr/install/kafka/bin/kafka-server-stop.sh /usr/install/kafka/config/server.properties
```

停止 zookeeper

```shell
sudo /usr/install/zookeeper/bin/zkServer.sh stop
```

注意，这里最好不要使用 kill -9 pid 的方式来关掉 kafka server 进程，而尽量使用上述命令。因为使用后面这种方式来关闭的时候会触发一个称为 controlled shutdown 的过程。在这个过程中，正在关闭的 broker 会自动把其所管理的所有的 partition 的leadership转移到其他的 broker 上。从而避免 zookeeper 再次对这些 partition 执行一个选举的操作。

这时，再使用 describe 命令来查看 kafka 内部 topic 的状态。注意检查刚才已经停掉的 kafka server 中每个 partition 的 leadership 有没有自动转移到其他的 kafka server 对应的副本上以便继续对外提供数据的读写请求。

将已经停掉的 kafka 虚拟机迁移到其他的物理机上，虚拟机重新启动以后，首先需要检查并确保新迁移的虚拟机的防火墙已经关闭。否则后面的操作步骤都会出问题。

启动 zookeeper

```shell
sudo /usr/install/zookeeper/bin/zkServer.sh start
```

启动 broker

```shell
nohup sudo /usr/install/kafka/bin/kafka-server-start.sh /usr/install/kafka/config/server.properties &
```

一旦 kafka server 重新启动起来后， 会主动去其他的 kafka server 拉取在停机后落后于其他机器的那部分消息。

查看日志 /usr/install/kafka/logs/server.log ，注意信息

```shell
2016-06-30 12:24:36,047 [ReplicaFetcherThread-0-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-0-1], Starting
2016-06-30 12:24:36,053 [ReplicaFetcherThread-1-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-1-1], Starting
2016-06-30 12:24:36,060 [ReplicaFetcherThread-0-2] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-0-2], Starting
2016-06-30 12:24:36,062 [ReplicaFetcherThread-2-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-2-1], Starting
2016-06-30 12:24:36,067 [ReplicaFetcherThread-3-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-3-1], Starting
2016-06-30 12:24:36,071 [ReplicaFetcherThread-3-2] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-3-2], Starting
```

这时新启动的 kafka server 会启动线程去其他机器上主动拉取每个 partition 中已经落后的消息。这时需要等待一段时间。直到日志中出现以下信息。

```shell
2016-06-30 12:27:38,956 [kafka-request-handler-6] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-2-1], Shutdown completed
2016-06-30 12:27:38,956 [ReplicaFetcherThread-2-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-2-1], Stopped
2016-06-30 12:27:38,967 [kafka-request-handler-3] INFO  kafka.server.ReplicaFetcherManager  - [ReplicaFetcherManager on broker 0] Removed fetcher for partitions [pomelo,1]
2016-06-30 12:27:38,969 [kafka-request-handler-3] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-1-1], Shutting down
2016-06-30 12:27:39,017 [kafka-processor-9092-7] INFO  kafka.network.Processor  - Closing socket connection to /172.31.238.29.
2016-06-30 12:27:39,022 [kafka-processor-9092-0] INFO  kafka.network.Processor  - Closing socket connection to /172.31.238.29.
2016-06-30 12:27:39,120 [ReplicaFetcherThread-1-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-1-1], Stopped
2016-06-30 12:27:39,120 [kafka-request-handler-3] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-1-1], Shutdown completed
2016-06-30 12:27:39,128 [kafka-request-handler-2] INFO  kafka.server.ReplicaFetcherManager  - [ReplicaFetcherManager on broker 0] Removed fetcher for partitions [velocity,1]
2016-06-30 12:27:39,130 [kafka-request-handler-2] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-0-1], Shutting down
2016-06-30 12:27:39,258 [kafka-processor-9092-1] INFO  kafka.network.Processor  - Closing socket connection to /172.31.238.29.
2016-06-30 12:27:39,388 [ReplicaFetcherThread-0-1] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-0-1], Stopped
2016-06-30 12:27:39,389 [kafka-request-handler-2] INFO  kafka.server.ReplicaFetcherThread  - [ReplicaFetcherThread-0-1], Shutdown completed
```

日志中继续显示同步线程已经关闭，说明该 kafka server 上的 partition 消息已经从其他的节点上同步完成。

再使用命令查看 kafka topic 的状态。

```shell
sudo /usr/install/kafka/bin/kafka-topics.sh --zookeeper 10.57.47.83,10.57.47.84,10.57.47.86:2181 –describe
```

这时会发现 kafka topic 的leadership已经自动进行了切换。

当我们分别对上述的三台机器进行同样的操作后，整个 kafka 集群就已经迁移完成了。

### **注意事项**

在 kafka 启动的过程中，日志中可能会出现以下的警告信息

```shell
2016-06-30 12:27:38,870 [kafka-request-handler-1] WARN  kafka.server.KafkaApis  - [KafkaApi-0] Fetch request with correlation id 0 from client ReplicaFetcherThread-1-0 on partition [pomelo,1] failed due to Leader not local for partition [pomelo,1] on broker 0
2016-06-30 12:27:38,897 [kafka-request-handler-4] WARN  kafka.server.KafkaApis  - [KafkaApi-0] Fetch request with correlation id 0 from client ReplicaFetcherThread-0-0 on partition [velocity,1] failed due to Leader not local for partition [velocity,1] on broker 0
```

出现这种信息的原因在于在新启动的 kafka server 顺利同步完落后的消息以后，会自动触发 partition leadership 的 rebalance。这时，会有部分 partition 的 leadership 进行切换。在切换的瞬间，可能还存在 producer 和 consumer 正在对以前处于 leader 状态的 partition 进行读写，从而导致这次的读写请求会失败。




