/**
  * <p>spark 基本操作</p>
  * http://spark.apache.org/docs/latest/programming-guide.html
  *
  * Created by zhengyong on 17/1/18.
  */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

class Spark {

  var sc: SparkContext = getInstance()

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
    val logFile = "file:///Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/README.md"
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

  }

  /**
    * rdd 操作
    */
  def rdd(): Unit = {

    val logFile = "file:///Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/README.md"

    // 定义RDD, 这时并没有把文件加载到内存中或其他操作, lines是指向文件的指针
    val lines = sc.textFile(logFile)
    // lineLengths是map转换结果, 它不会立刻开始计算
    val lineLengths = lines.map(s => s.length)
    // 保存在内存中
    lineLengths.persist()
    // 我们运行reduce，这是一个动作。 在这一点上，Spark将计算分解为在单独的机器上运行的任务，每个机器运行它的一部分map和本地reduce，然后返回它的结果
    val totalLength = lineLengths.reduce((a, b) => a + b)
    println("rdd total Length = " + totalLength)
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
  }

}
