/**
  * <p>spark 基本操作</p>
  * http://spark.apache.org/docs/latest/programming-guide.html
  *
  * Created by zhengyong on 17/1/18.
  */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

class Spark {

  // spark 上下文
  var sc: SparkContext = getInstance()

  // 本地任意文件地址
  val file = "file:///Users/zhengyong/spark-file/data.txt"

  /**
    * 创建spark实例
    *
    * @return SparkContext
    */
  private def getInstance(): SparkContext = {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
    val sc = new SparkContext(conf)
    return sc;
  }

  /**
    * 处理文件(Spark can create distributed datasets from any storage source supported by Hadoop, including your local file system, HDFS, Cassandra, HBase, Amazon S3, etc. )
    */
  def dealFile(): Unit = {
    // Should be some file on your system
    val logData = sc.textFile(file, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

  }

  /**
    * rdd 操作
    */
  def rdd(): Unit = {

    // 定义RDD, 这时并没有把文件加载到内存中或其他操作, lines是指向文件的指针
    val lines = sc.textFile(file)
    // lineLengths是map转换结果, 它不会立刻开始计算
    val lineLengths = lines.map(s => s.length)
    // 保存在内存中
    lineLengths.persist()
    // 我们运行reduce，这是一个动作。 在这一点上，Spark将计算分解为在单独的机器上运行的任务，每个机器运行它的一部分map和本地reduce，然后返回它的结果
    val totalLength = lineLengths.reduce((a, b) => a + b)
    println("rdd total Length = " + totalLength)
  }

  /**
    * 键值(key-value)对 RDD操作, 统计每个单词出现次数
    */
  def reduceByKey(): Unit = {
    val lines = sc.textFile(file)
    val pairs = lines.flatMap(line => line.split(" ")).map(word => (word, 1))
    val counts = pairs.reduceByKey((a, b) => a + b)
    counts.sortByKey()
    println("reduceByKey collect first = " + counts.first())
  }

  /**
    * 广播变量(多节点共享)
    */
  def broadcast(): Unit = {
    // 为了保证所有节点获取的广播变量具有相同值, value在broadcast后不能修改
    val value = Array(1,2,3,4);
    val broad = sc.broadcast(value)
    println("broadcast value[0] = " + broad.value.apply(0))
  }

  /**
    * 累加器(多节点共享)
    */
  def accumulator(): Unit ={
    val accum = sc.accumulator(0, "My Accumulator")
    sc.parallelize(Array(1, 2, 3, 4)).foreach(x => accum += x)
    println("accumulator value = " + accum.value)
  }

  /**
    * 并行化累加
    */
  def parallelCollections(): Unit = {
    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)
    val result = distData.reduce((a, b) => a + b)
    println("parallel data = " + result)
  }

}


object SparkTest {

  def main(args: Array[String]) {
    val spark = new Spark;
    spark.dealFile()
    spark.parallelCollections()
    spark.rdd()
    spark.reduceByKey()
    spark.broadcast()
    spark.accumulator()
  }

}
