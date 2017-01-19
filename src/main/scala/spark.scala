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
