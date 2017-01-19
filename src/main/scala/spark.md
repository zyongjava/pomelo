## spark(2.1.0)

简介：该文档没有安装hadoop进行关联，简单学习spark文件读取，仅安装spark即可，安装包自带scala环境。

#### 环境安装

下载地址：http://spark.apache.org/downloads.html

1. 下载spark-2.1.0-bin-hadoop2.7.tgz
2. 解压spark-2.1.0-bin-hadoop2.7.tgz
3. 拷贝生成配置文件,执行`cp ./conf/spark-env.sh.template ./conf/spark-env.sh`命令
4. 执行`./bin/run-example SparkPi` 查看spark是否启动成功
5. spark(2.1.0), 安装包里面自带scala(2.11.8), 使用启动`./spark-shell`可以看见scala版本信息如下：

```
➜  bin ./spark-shell
Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
17/01/19 01:03:47 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
17/01/19 01:04:03 WARN ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
Spark context Web UI available at http://192.168.0.102:4040
Spark context available as 'sc' (master = local[*], app id = local-1484759031036).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.1.0
      /_/

Using Scala version 2.11.8 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_45)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```


#### 示列代码

#####一、maven依赖

```
<!-- scala-->
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-library</artifactId>
    <version>2.11.8</version>
</dependency>
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-compiler</artifactId>
    <version>2.11.8</version>
</dependency>
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-reflect</artifactId>
    <version>2.11.8</version>
</dependency>
<!-- spark-->
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-core_2.11</artifactId>
    <version>2.1.0</version>
</dependency>
```

#####二、scala代码

```
/**
  * Created by zhengyong on 17/1/18.
  */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object spark {

  def main(args: Array[String]) {
    val logFile = "file:///Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/README.md" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }

}

```
#####三、运行结果

```
log4j:WARN Continuable parsing error 31 and column 12
log4j:WARN 元素类型为 "root" 的内容必须匹配 "(param*,(priority|level)?,appender-ref*)"。
2017-01-19 00:53:23,295 - org.apache.spark.SparkContext -0    [main] INFO   - Running Spark version 2.1.0
2017-01-19 00:53:24,165 - org.apache.hadoop.util.NativeCodeLoader -870  [main] WARN   - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
2017-01-19 00:53:24,462 - org.apache.spark.SecurityManager -1167 [main] INFO   - Changing view acls to: zhengyong
2017-01-19 00:53:24,463 - org.apache.spark.SecurityManager -1168 [main] INFO   - Changing modify acls to: zhengyong
2017-01-19 00:53:24,464 - org.apache.spark.SecurityManager -1169 [main] INFO   - Changing view acls groups to:
2017-01-19 00:53:24,464 - org.apache.spark.SecurityManager -1169 [main] INFO   - Changing modify acls groups to:
2017-01-19 00:53:24,466 - org.apache.spark.SecurityManager -1171 [main] INFO   - SecurityManager: authentication disabled; ui acls disabled; users  with view permissions: Set(zhengyong); groups with view permissions: Set(); users  with modify permissions: Set(zhengyong); groups with modify permissions: Set()
2017-01-19 00:53:25,698 - org.apache.spark.util.Utils -2403 [main] INFO   - Successfully started service 'sparkDriver' on port 58878.
2017-01-19 00:53:25,741 - org.apache.spark.SparkEnv -2446 [main] INFO   - Registering MapOutputTracker
2017-01-19 00:53:25,778 - org.apache.spark.SparkEnv -2483 [main] INFO   - Registering BlockManagerMaster
2017-01-19 00:53:25,784 - org.apache.spark.storage.BlockManagerMasterEndpoint -2489 [main] INFO   - Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
2017-01-19 00:53:25,784 - org.apache.spark.storage.BlockManagerMasterEndpoint -2489 [main] INFO   - BlockManagerMasterEndpoint up
2017-01-19 00:53:25,808 - org.apache.spark.storage.DiskBlockManager -2513 [main] INFO   - Created local directory at /private/var/folders/0s/0jc79csj47x_cnhh6hn6t60c0000gn/T/blockmgr-d8891d37-20d7-4be0-bbef-bac4d6b79c1c
2017-01-19 00:53:25,854 - org.apache.spark.storage.memory.MemoryStore -2559 [main] INFO   - MemoryStore started with capacity 366.3 MB
2017-01-19 00:53:26,104 - org.apache.spark.SparkEnv -2809 [main] INFO   - Registering OutputCommitCoordinator
2017-01-19 00:53:26,317 - org.spark_project.jetty.util.log -3022 [main] INFO   - Logging initialized @4760ms
2017-01-19 00:53:26,569 - org.spark_project.jetty.server.Server -3274 [main] INFO   - jetty-9.2.z-SNAPSHOT
2017-01-19 00:53:26,626 - org.spark_project.jetty.server.handler.ContextHandler -3331 [main] INFO   - Started o.s.j.s.ServletContextHandler@1021f6c9{/jobs,null,AVAILABLE}
2017-01-19 00:53:26,626 - org.spark_project.jetty.server.handler.ContextHandler -3331 [main] INFO   - Started o.s.j.s.ServletContextHandler@7516e4e5{/jobs/json,null,AVAILABLE}
2017-01-19 00:53:26,629 - org.spark_project.jetty.server.handler.ContextHandler -3334 [main] INFO   - Started o.s.j.s.ServletContextHandler@488eb7f2{/jobs/job,null,AVAILABLE}
2017-01-19 00:53:26,632 - org.spark_project.jetty.server.handler.ContextHandler -3337 [main] INFO   - Started o.s.j.s.ServletContextHandler@5e81e5ac{/jobs/job/json,null,AVAILABLE}
2017-01-19 00:53:26,633 - org.spark_project.jetty.server.handler.ContextHandler -3338 [main] INFO   - Started o.s.j.s.ServletContextHandler@4189d70b{/stages,null,AVAILABLE}
2017-01-19 00:53:26,635 - org.spark_project.jetty.server.handler.ContextHandler -3340 [main] INFO   - Started o.s.j.s.ServletContextHandler@3fa2213{/stages/json,null,AVAILABLE}
2017-01-19 00:53:26,636 - org.spark_project.jetty.server.handler.ContextHandler -3341 [main] INFO   - Started o.s.j.s.ServletContextHandler@3e7634b9{/stages/stage,null,AVAILABLE}
2017-01-19 00:53:26,661 - org.spark_project.jetty.server.handler.ContextHandler -3366 [main] INFO   - Started o.s.j.s.ServletContextHandler@6f0b0a5e{/stages/stage/json,null,AVAILABLE}
2017-01-19 00:53:26,662 - org.spark_project.jetty.server.handler.ContextHandler -3367 [main] INFO   - Started o.s.j.s.ServletContextHandler@6035b93b{/stages/pool,null,AVAILABLE}
2017-01-19 00:53:26,662 - org.spark_project.jetty.server.handler.ContextHandler -3367 [main] INFO   - Started o.s.j.s.ServletContextHandler@320de594{/stages/pool/json,null,AVAILABLE}
2017-01-19 00:53:26,663 - org.spark_project.jetty.server.handler.ContextHandler -3368 [main] INFO   - Started o.s.j.s.ServletContextHandler@3dd1dc90{/storage,null,AVAILABLE}
2017-01-19 00:53:26,663 - org.spark_project.jetty.server.handler.ContextHandler -3368 [main] INFO   - Started o.s.j.s.ServletContextHandler@abf688e{/storage/json,null,AVAILABLE}
2017-01-19 00:53:26,663 - org.spark_project.jetty.server.handler.ContextHandler -3368 [main] INFO   - Started o.s.j.s.ServletContextHandler@478ee483{/storage/rdd,null,AVAILABLE}
2017-01-19 00:53:26,665 - org.spark_project.jetty.server.handler.ContextHandler -3370 [main] INFO   - Started o.s.j.s.ServletContextHandler@1a7288a3{/storage/rdd/json,null,AVAILABLE}
2017-01-19 00:53:26,665 - org.spark_project.jetty.server.handler.ContextHandler -3370 [main] INFO   - Started o.s.j.s.ServletContextHandler@2974f221{/environment,null,AVAILABLE}
2017-01-19 00:53:26,665 - org.spark_project.jetty.server.handler.ContextHandler -3370 [main] INFO   - Started o.s.j.s.ServletContextHandler@58fe0499{/environment/json,null,AVAILABLE}
2017-01-19 00:53:26,666 - org.spark_project.jetty.server.handler.ContextHandler -3371 [main] INFO   - Started o.s.j.s.ServletContextHandler@686449f9{/executors,null,AVAILABLE}
2017-01-19 00:53:26,666 - org.spark_project.jetty.server.handler.ContextHandler -3371 [main] INFO   - Started o.s.j.s.ServletContextHandler@665df3c6{/executors/json,null,AVAILABLE}
2017-01-19 00:53:26,666 - org.spark_project.jetty.server.handler.ContextHandler -3371 [main] INFO   - Started o.s.j.s.ServletContextHandler@68b6f0d6{/executors/threadDump,null,AVAILABLE}
2017-01-19 00:53:26,667 - org.spark_project.jetty.server.handler.ContextHandler -3372 [main] INFO   - Started o.s.j.s.ServletContextHandler@4044fb95{/executors/threadDump/json,null,AVAILABLE}
2017-01-19 00:53:26,678 - org.spark_project.jetty.server.handler.ContextHandler -3383 [main] INFO   - Started o.s.j.s.ServletContextHandler@aa549e5{/static,null,AVAILABLE}
2017-01-19 00:53:26,678 - org.spark_project.jetty.server.handler.ContextHandler -3383 [main] INFO   - Started o.s.j.s.ServletContextHandler@36f48b4{/,null,AVAILABLE}
2017-01-19 00:53:26,679 - org.spark_project.jetty.server.handler.ContextHandler -3384 [main] INFO   - Started o.s.j.s.ServletContextHandler@5c00384f{/api,null,AVAILABLE}
2017-01-19 00:53:26,680 - org.spark_project.jetty.server.handler.ContextHandler -3385 [main] INFO   - Started o.s.j.s.ServletContextHandler@3b7ff809{/jobs/job/kill,null,AVAILABLE}
2017-01-19 00:53:26,680 - org.spark_project.jetty.server.handler.ContextHandler -3385 [main] INFO   - Started o.s.j.s.ServletContextHandler@1bb564e2{/stages/stage/kill,null,AVAILABLE}
2017-01-19 00:53:26,692 - org.spark_project.jetty.server.ServerConnector -3397 [main] INFO   - Started ServerConnector@129b4fe2{HTTP/1.1}{0.0.0.0:4040}
2017-01-19 00:53:26,693 - org.spark_project.jetty.server.Server -3398 [main] INFO   - Started @5139ms
2017-01-19 00:53:26,693 - org.apache.spark.util.Utils -3398 [main] INFO   - Successfully started service 'SparkUI' on port 4040.
2017-01-19 00:53:26,697 - org.apache.spark.ui.SparkUI -3402 [main] INFO   - Bound SparkUI to 0.0.0.0, and started at http://192.168.0.102:4040
2017-01-19 00:53:26,978 - org.apache.spark.executor.Executor -3683 [main] INFO   - Starting executor ID driver on host localhost
2017-01-19 00:53:27,069 - org.apache.spark.util.Utils -3774 [main] INFO   - Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 58879.
2017-01-19 00:53:27,074 - org.apache.spark.network.netty.NettyBlockTransferService -3779 [main] INFO   - Server created on 192.168.0.102:58879
2017-01-19 00:53:27,077 - org.apache.spark.storage.BlockManager -3782 [main] INFO   - Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
2017-01-19 00:53:27,081 - org.apache.spark.storage.BlockManagerMaster -3786 [main] INFO   - Registering BlockManager BlockManagerId(driver, 192.168.0.102, 58879, None)
2017-01-19 00:53:27,088 - org.apache.spark.storage.BlockManagerMasterEndpoint -3793 [dispatcher-event-loop-2] INFO   - Registering block manager 192.168.0.102:58879 with 366.3 MB RAM, BlockManagerId(driver, 192.168.0.102, 58879, None)
2017-01-19 00:53:27,097 - org.apache.spark.storage.BlockManagerMaster -3802 [main] INFO   - Registered BlockManager BlockManagerId(driver, 192.168.0.102, 58879, None)
2017-01-19 00:53:27,098 - org.apache.spark.storage.BlockManager -3803 [main] INFO   - Initialized BlockManager: BlockManagerId(driver, 192.168.0.102, 58879, None)
2017-01-19 00:53:27,459 - org.spark_project.jetty.server.handler.ContextHandler -4164 [main] INFO   - Started o.s.j.s.ServletContextHandler@525d79f0{/metrics/json,null,AVAILABLE}
2017-01-19 00:53:28,251 - org.apache.spark.storage.memory.MemoryStore -4956 [main] INFO   - Block broadcast_0 stored as values in memory (estimated size 127.1 KB, free 366.2 MB)
2017-01-19 00:53:28,337 - org.apache.spark.storage.memory.MemoryStore -5042 [main] INFO   - Block broadcast_0_piece0 stored as bytes in memory (estimated size 14.3 KB, free 366.2 MB)
2017-01-19 00:53:28,340 - org.apache.spark.storage.BlockManagerInfo -5045 [dispatcher-event-loop-0] INFO   - Added broadcast_0_piece0 in memory on 192.168.0.102:58879 (size: 14.3 KB, free: 366.3 MB)
2017-01-19 00:53:28,348 - org.apache.spark.SparkContext -5053 [main] INFO   - Created broadcast 0 from textFile at spark.scala:15
2017-01-19 00:53:28,611 - org.apache.hadoop.mapred.FileInputFormat -5316 [main] INFO   - Total input paths to process : 1
2017-01-19 00:53:28,633 - org.apache.spark.SparkContext -5338 [main] INFO   - Starting job: count at spark.scala:16
2017-01-19 00:53:28,676 - org.apache.spark.scheduler.DAGScheduler -5381 [dag-scheduler-event-loop] INFO   - Got job 0 (count at spark.scala:16) with 2 output partitions
2017-01-19 00:53:28,679 - org.apache.spark.scheduler.DAGScheduler -5384 [dag-scheduler-event-loop] INFO   - Final stage: ResultStage 0 (count at spark.scala:16)
2017-01-19 00:53:28,680 - org.apache.spark.scheduler.DAGScheduler -5385 [dag-scheduler-event-loop] INFO   - Parents of final stage: List()
2017-01-19 00:53:28,687 - org.apache.spark.scheduler.DAGScheduler -5392 [dag-scheduler-event-loop] INFO   - Missing parents: List()
2017-01-19 00:53:28,703 - org.apache.spark.scheduler.DAGScheduler -5408 [dag-scheduler-event-loop] INFO   - Submitting ResultStage 0 (MapPartitionsRDD[2] at filter at spark.scala:16), which has no missing parents
2017-01-19 00:53:28,807 - org.apache.spark.storage.memory.MemoryStore -5512 [dag-scheduler-event-loop] INFO   - Block broadcast_1 stored as values in memory (estimated size 3.3 KB, free 366.2 MB)
2017-01-19 00:53:28,810 - org.apache.spark.storage.memory.MemoryStore -5515 [dag-scheduler-event-loop] INFO   - Block broadcast_1_piece0 stored as bytes in memory (estimated size 2023.0 B, free 366.2 MB)
2017-01-19 00:53:28,811 - org.apache.spark.storage.BlockManagerInfo -5516 [dispatcher-event-loop-2] INFO   - Added broadcast_1_piece0 in memory on 192.168.0.102:58879 (size: 2023.0 B, free: 366.3 MB)
2017-01-19 00:53:28,813 - org.apache.spark.SparkContext -5518 [dag-scheduler-event-loop] INFO   - Created broadcast 1 from broadcast at DAGScheduler.scala:996
2017-01-19 00:53:28,819 - org.apache.spark.scheduler.DAGScheduler -5524 [dag-scheduler-event-loop] INFO   - Submitting 2 missing tasks from ResultStage 0 (MapPartitionsRDD[2] at filter at spark.scala:16)
2017-01-19 00:53:28,826 - org.apache.spark.scheduler.TaskSchedulerImpl -5531 [dag-scheduler-event-loop] INFO   - Adding task set 0.0 with 2 tasks
2017-01-19 00:53:28,925 - org.apache.spark.scheduler.TaskSetManager -5630 [dispatcher-event-loop-3] INFO   - Starting task 0.0 in stage 0.0 (TID 0, localhost, executor driver, partition 0, PROCESS_LOCAL, 5933 bytes)
2017-01-19 00:53:28,942 - org.apache.spark.executor.Executor -5647 [Executor task launch worker-0] INFO   - Running task 0.0 in stage 0.0 (TID 0)
2017-01-19 00:53:29,138 - org.apache.spark.rdd.HadoopRDD -5843 [Executor task launch worker-0] INFO   - Input split: file:/Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/README.md:0+1909
2017-01-19 00:53:29,165 - org.apache.hadoop.conf.Configuration.deprecation -5870 [Executor task launch worker-0] INFO   - mapred.tip.id is deprecated. Instead, use mapreduce.task.id
2017-01-19 00:53:29,165 - org.apache.hadoop.conf.Configuration.deprecation -5870 [Executor task launch worker-0] INFO   - mapred.task.id is deprecated. Instead, use mapreduce.task.attempt.id
2017-01-19 00:53:29,165 - org.apache.hadoop.conf.Configuration.deprecation -5870 [Executor task launch worker-0] INFO   - mapred.task.is.map is deprecated. Instead, use mapreduce.task.ismap
2017-01-19 00:53:29,166 - org.apache.hadoop.conf.Configuration.deprecation -5871 [Executor task launch worker-0] INFO   - mapred.task.partition is deprecated. Instead, use mapreduce.task.partition
2017-01-19 00:53:29,166 - org.apache.hadoop.conf.Configuration.deprecation -5871 [Executor task launch worker-0] INFO   - mapred.job.id is deprecated. Instead, use mapreduce.job.id
2017-01-19 00:53:29,190 - org.apache.spark.storage.memory.MemoryStore -5895 [Executor task launch worker-0] INFO   - Block rdd_1_0 stored as values in memory (estimated size 6.3 KB, free 366.2 MB)
2017-01-19 00:53:29,191 - org.apache.spark.storage.BlockManagerInfo -5896 [dispatcher-event-loop-2] INFO   - Added rdd_1_0 in memory on 192.168.0.102:58879 (size: 6.3 KB, free: 366.3 MB)
2017-01-19 00:53:29,221 - org.apache.spark.executor.Executor -5926 [Executor task launch worker-0] INFO   - Finished task 0.0 in stage 0.0 (TID 0). 2005 bytes result sent to driver
2017-01-19 00:53:29,227 - org.apache.spark.scheduler.TaskSetManager -5932 [dispatcher-event-loop-3] INFO   - Starting task 1.0 in stage 0.0 (TID 1, localhost, executor driver, partition 1, PROCESS_LOCAL, 5933 bytes)
2017-01-19 00:53:29,228 - org.apache.spark.executor.Executor -5933 [Executor task launch worker-0] INFO   - Running task 1.0 in stage 0.0 (TID 1)
2017-01-19 00:53:29,236 - org.apache.spark.scheduler.TaskSetManager -5941 [task-result-getter-0] INFO   - Finished task 0.0 in stage 0.0 (TID 0) in 364 ms on localhost (executor driver) (1/2)
2017-01-19 00:53:29,237 - org.apache.spark.rdd.HadoopRDD -5942 [Executor task launch worker-0] INFO   - Input split: file:/Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/README.md:1909+1909
2017-01-19 00:53:29,250 - org.apache.spark.storage.memory.MemoryStore -5955 [Executor task launch worker-0] INFO   - Block rdd_1_1 stored as values in memory (estimated size 5.7 KB, free 366.1 MB)
2017-01-19 00:53:29,251 - org.apache.spark.storage.BlockManagerInfo -5956 [dispatcher-event-loop-2] INFO   - Added rdd_1_1 in memory on 192.168.0.102:58879 (size: 5.7 KB, free: 366.3 MB)
2017-01-19 00:53:29,254 - org.apache.spark.executor.Executor -5959 [Executor task launch worker-0] INFO   - Finished task 1.0 in stage 0.0 (TID 1). 1845 bytes result sent to driver
2017-01-19 00:53:29,259 - org.apache.spark.scheduler.TaskSetManager -5964 [task-result-getter-1] INFO   - Finished task 1.0 in stage 0.0 (TID 1) in 35 ms on localhost (executor driver) (2/2)
2017-01-19 00:53:29,262 - org.apache.spark.scheduler.TaskSchedulerImpl -5967 [task-result-getter-1] INFO   - Removed TaskSet 0.0, whose tasks have all completed, from pool
2017-01-19 00:53:29,262 - org.apache.spark.scheduler.DAGScheduler -5967 [dag-scheduler-event-loop] INFO   - ResultStage 0 (count at spark.scala:16) finished in 0.416 s
2017-01-19 00:53:29,271 - org.apache.spark.scheduler.DAGScheduler -5976 [main] INFO   - Job 0 finished: count at spark.scala:16, took 0.638347 s
2017-01-19 00:53:29,285 - org.apache.spark.SparkContext -5990 [main] INFO   - Starting job: count at spark.scala:17
2017-01-19 00:53:29,285 - org.apache.spark.scheduler.DAGScheduler -5990 [dag-scheduler-event-loop] INFO   - Got job 1 (count at spark.scala:17) with 2 output partitions
2017-01-19 00:53:29,286 - org.apache.spark.scheduler.DAGScheduler -5991 [dag-scheduler-event-loop] INFO   - Final stage: ResultStage 1 (count at spark.scala:17)
2017-01-19 00:53:29,286 - org.apache.spark.scheduler.DAGScheduler -5991 [dag-scheduler-event-loop] INFO   - Parents of final stage: List()
2017-01-19 00:53:29,288 - org.apache.spark.scheduler.DAGScheduler -5993 [dag-scheduler-event-loop] INFO   - Missing parents: List()
2017-01-19 00:53:29,288 - org.apache.spark.scheduler.DAGScheduler -5993 [dag-scheduler-event-loop] INFO   - Submitting ResultStage 1 (MapPartitionsRDD[3] at filter at spark.scala:17), which has no missing parents
2017-01-19 00:53:29,291 - org.apache.spark.storage.memory.MemoryStore -5996 [dag-scheduler-event-loop] INFO   - Block broadcast_2 stored as values in memory (estimated size 3.3 KB, free 366.1 MB)
2017-01-19 00:53:29,293 - org.apache.spark.storage.memory.MemoryStore -5998 [dag-scheduler-event-loop] INFO   - Block broadcast_2_piece0 stored as bytes in memory (estimated size 2022.0 B, free 366.1 MB)
2017-01-19 00:53:29,293 - org.apache.spark.storage.BlockManagerInfo -5998 [dispatcher-event-loop-1] INFO   - Added broadcast_2_piece0 in memory on 192.168.0.102:58879 (size: 2022.0 B, free: 366.3 MB)
2017-01-19 00:53:29,294 - org.apache.spark.SparkContext -5999 [dag-scheduler-event-loop] INFO   - Created broadcast 2 from broadcast at DAGScheduler.scala:996
2017-01-19 00:53:29,294 - org.apache.spark.scheduler.DAGScheduler -5999 [dag-scheduler-event-loop] INFO   - Submitting 2 missing tasks from ResultStage 1 (MapPartitionsRDD[3] at filter at spark.scala:17)
2017-01-19 00:53:29,295 - org.apache.spark.scheduler.TaskSchedulerImpl -6000 [dag-scheduler-event-loop] INFO   - Adding task set 1.0 with 2 tasks
2017-01-19 00:53:29,301 - org.apache.spark.scheduler.TaskSetManager -6006 [dispatcher-event-loop-2] INFO   - Starting task 0.0 in stage 1.0 (TID 2, localhost, executor driver, partition 0, PROCESS_LOCAL, 5933 bytes)
2017-01-19 00:53:29,302 - org.apache.spark.executor.Executor -6007 [Executor task launch worker-0] INFO   - Running task 0.0 in stage 1.0 (TID 2)
2017-01-19 00:53:29,311 - org.apache.spark.storage.BlockManager -6016 [Executor task launch worker-0] INFO   - Found block rdd_1_0 locally
2017-01-19 00:53:29,313 - org.apache.spark.executor.Executor -6018 [Executor task launch worker-0] INFO   - Finished task 0.0 in stage 1.0 (TID 2). 1123 bytes result sent to driver
2017-01-19 00:53:29,315 - org.apache.spark.scheduler.TaskSetManager -6020 [dispatcher-event-loop-0] INFO   - Starting task 1.0 in stage 1.0 (TID 3, localhost, executor driver, partition 1, PROCESS_LOCAL, 5933 bytes)
2017-01-19 00:53:29,316 - org.apache.spark.executor.Executor -6021 [Executor task launch worker-0] INFO   - Running task 1.0 in stage 1.0 (TID 3)
2017-01-19 00:53:29,316 - org.apache.spark.scheduler.TaskSetManager -6021 [task-result-getter-2] INFO   - Finished task 0.0 in stage 1.0 (TID 2) in 18 ms on localhost (executor driver) (1/2)
2017-01-19 00:53:29,322 - org.apache.spark.storage.BlockManager -6027 [Executor task launch worker-0] INFO   - Found block rdd_1_1 locally
2017-01-19 00:53:29,324 - org.apache.spark.executor.Executor -6029 [Executor task launch worker-0] INFO   - Finished task 1.0 in stage 1.0 (TID 3). 1123 bytes result sent to driver
2017-01-19 00:53:29,326 - org.apache.spark.scheduler.TaskSetManager -6031 [task-result-getter-3] INFO   - Finished task 1.0 in stage 1.0 (TID 3) in 12 ms on localhost (executor driver) (2/2)
2017-01-19 00:53:29,326 - org.apache.spark.scheduler.TaskSchedulerImpl -6031 [task-result-getter-3] INFO   - Removed TaskSet 1.0, whose tasks have all completed, from pool
2017-01-19 00:53:29,327 - org.apache.spark.scheduler.DAGScheduler -6032 [dag-scheduler-event-loop] INFO   - ResultStage 1 (count at spark.scala:17) finished in 0.029 s
2017-01-19 00:53:29,327 - org.apache.spark.scheduler.DAGScheduler -6032 [main] INFO   - Job 1 finished: count at spark.scala:17, took 0.042488 s
Lines with a: 62, Lines with b: 30
2017-01-19 00:53:29,332 - org.apache.spark.SparkContext -6037 [Thread-1] INFO   - Invoking stop() from shutdown hook
2017-01-19 00:53:29,340 - org.spark_project.jetty.server.ServerConnector -6045 [Thread-1] INFO   - Stopped ServerConnector@129b4fe2{HTTP/1.1}{0.0.0.0:4040}
2017-01-19 00:53:29,342 - org.spark_project.jetty.server.handler.ContextHandler -6047 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@1bb564e2{/stages/stage/kill,null,UNAVAILABLE}
2017-01-19 00:53:29,343 - org.spark_project.jetty.server.handler.ContextHandler -6048 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@3b7ff809{/jobs/job/kill,null,UNAVAILABLE}
2017-01-19 00:53:29,343 - org.spark_project.jetty.server.handler.ContextHandler -6048 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@5c00384f{/api,null,UNAVAILABLE}
2017-01-19 00:53:29,343 - org.spark_project.jetty.server.handler.ContextHandler -6048 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@36f48b4{/,null,UNAVAILABLE}
2017-01-19 00:53:29,344 - org.spark_project.jetty.server.handler.ContextHandler -6049 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@aa549e5{/static,null,UNAVAILABLE}
2017-01-19 00:53:29,344 - org.spark_project.jetty.server.handler.ContextHandler -6049 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@4044fb95{/executors/threadDump/json,null,UNAVAILABLE}
2017-01-19 00:53:29,345 - org.spark_project.jetty.server.handler.ContextHandler -6050 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@68b6f0d6{/executors/threadDump,null,UNAVAILABLE}
2017-01-19 00:53:29,345 - org.spark_project.jetty.server.handler.ContextHandler -6050 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@665df3c6{/executors/json,null,UNAVAILABLE}
2017-01-19 00:53:29,346 - org.spark_project.jetty.server.handler.ContextHandler -6051 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@686449f9{/executors,null,UNAVAILABLE}
2017-01-19 00:53:29,346 - org.spark_project.jetty.server.handler.ContextHandler -6051 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@58fe0499{/environment/json,null,UNAVAILABLE}
2017-01-19 00:53:29,346 - org.spark_project.jetty.server.handler.ContextHandler -6051 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@2974f221{/environment,null,UNAVAILABLE}
2017-01-19 00:53:29,347 - org.spark_project.jetty.server.handler.ContextHandler -6052 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@1a7288a3{/storage/rdd/json,null,UNAVAILABLE}
2017-01-19 00:53:29,347 - org.spark_project.jetty.server.handler.ContextHandler -6052 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@478ee483{/storage/rdd,null,UNAVAILABLE}
2017-01-19 00:53:29,348 - org.spark_project.jetty.server.handler.ContextHandler -6053 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@abf688e{/storage/json,null,UNAVAILABLE}
2017-01-19 00:53:29,348 - org.spark_project.jetty.server.handler.ContextHandler -6053 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@3dd1dc90{/storage,null,UNAVAILABLE}
2017-01-19 00:53:29,348 - org.spark_project.jetty.server.handler.ContextHandler -6053 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@320de594{/stages/pool/json,null,UNAVAILABLE}
2017-01-19 00:53:29,348 - org.spark_project.jetty.server.handler.ContextHandler -6053 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@6035b93b{/stages/pool,null,UNAVAILABLE}
2017-01-19 00:53:29,349 - org.spark_project.jetty.server.handler.ContextHandler -6054 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@6f0b0a5e{/stages/stage/json,null,UNAVAILABLE}
2017-01-19 00:53:29,349 - org.spark_project.jetty.server.handler.ContextHandler -6054 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@3e7634b9{/stages/stage,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@3fa2213{/stages/json,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@4189d70b{/stages,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@5e81e5ac{/jobs/job/json,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@488eb7f2{/jobs/job,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@7516e4e5{/jobs/json,null,UNAVAILABLE}
2017-01-19 00:53:29,350 - org.spark_project.jetty.server.handler.ContextHandler -6055 [Thread-1] INFO   - Stopped o.s.j.s.ServletContextHandler@1021f6c9{/jobs,null,UNAVAILABLE}
2017-01-19 00:53:29,355 - org.apache.spark.ui.SparkUI -6060 [Thread-1] INFO   - Stopped Spark web UI at http://192.168.0.102:4040
2017-01-19 00:53:29,369 - org.apache.spark.MapOutputTrackerMasterEndpoint -6074 [dispatcher-event-loop-1] INFO   - MapOutputTrackerMasterEndpoint stopped!
2017-01-19 00:53:29,385 - org.apache.spark.storage.memory.MemoryStore -6090 [Thread-1] INFO   - MemoryStore cleared
2017-01-19 00:53:29,386 - org.apache.spark.storage.BlockManager -6091 [Thread-1] INFO   - BlockManager stopped
2017-01-19 00:53:29,390 - org.apache.spark.storage.BlockManagerMaster -6095 [Thread-1] INFO   - BlockManagerMaster stopped
2017-01-19 00:53:29,396 - org.apache.spark.scheduler.OutputCommitCoordinator$OutputCommitCoordinatorEndpoint -6101 [dispatcher-event-loop-2] INFO   - OutputCommitCoordinator stopped!
2017-01-19 00:53:29,398 - org.apache.spark.SparkContext -6103 [Thread-1] INFO   - Successfully stopped SparkContext
2017-01-19 00:53:29,399 - org.apache.spark.util.ShutdownHookManager -6104 [Thread-1] INFO   - Shutdown hook called
2017-01-19 00:53:29,400 - org.apache.spark.util.ShutdownHookManager -6105 [Thread-1] INFO   - Deleting directory /private/var/folders/0s/0jc79csj47x_cnhh6hn6t60c0000gn/T/spark-7ea5fe19-03e1-4b19-9992-624b1d3b6224

Process finished with exit code 0
```

#### IntelliJ IDEA构建

1. 首先按住`command + ;` 进入“Project Structure”
2. 选择左边标签“Global Libraries” 新建一个"Scala SDK", 然后browser选择刚刚安装`~/spark-2.1.0-bin-hadoop2.7/jars`这个目录，后面就可以正常运行了。

#### 参考

1. 官方文档：http://spark.apache.org/docs/latest/programming-guide.html
2. 构建： http://www.powerxing.com/spark-quick-start-guide/

#### 搭建过程遇到问题

1. 版本不一致：http://blog.csdn.net/u012102306/article/details/51103560（最终解决，使用spark自带的scala关联idea进行启动）
2. master url： http://www.mamicode.com/info-detail-946376.html （设置VM参数`-Dspark.master=local`）

```
Exception in thread "main" org.apache.spark.SparkException: A master URL must be set in your configuration
	at org.apache.spark.SparkContext.<init>(SparkContext.scala:379)
	at spark$.main(spark.scala:14)
	at spark.main(spark.scala)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)
```