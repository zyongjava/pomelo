import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * <p>集合操作</p>
  * Created by zhengyong on 17/1/18.
  */
class Collections {

  /**
    * List
    */
  def list(): Unit = {
    // 字符串列表
    val site: List[String] = List("Runoob", "Google", "Baidu")

    // 整型列表
    val nums: List[Int] = List(1, 2, 3, 4)

    // 给集合nums 临时添加一个元素
    println("nums add:" + nums.::(8))

    // 创建空了列表(::集合添加元素)
    val nums2 = 5 :: 6 :: 7 :: Nil
    for (x <- nums2) {
      println("nums2:" + x)
    }

    // 空列表
    val empty: List[Nothing] = List()

    for (x <- site) {
      println("site:" + x)
    }

    // for loop execution with multiple filters
    for (x <- nums if x != 3) {
      println("nums:" + x)
    }

    // ::: 为两个集合合并,
    val mergeList = site ::: nums
    for (x <- mergeList) {
      println("mergeList:" + x)
    }


  }

  /**
    * Set 默认是不可变集合,需要可变集合要声明全路径scala.collection.mutalbe.Set
    */
  def set(): Unit = {
    val set = Set(1, 2, 3)
    println(set.getClass.getName) //

    println(set.exists(_ % 2 == 0)) //true
    println(set.drop(1)) //Set(2,3)

    for (x <- set) {
      println("set:" + x)
    }

    // 可变set
    val set1 = scala.collection.mutable.Set("hello", "world")
    set1 += "scala"
    println("mutable set list:")
    set1.foreach(println)

  }

  /**
    * Map
    */
  def map(): Unit = {
    val colors = Map("red" -> "#FF0000",
      "azure" -> "#F0FFFF",
      "peru" -> "#CD853F")

    val nums: Map[Int, Int] = Map()

    println("colors 中的键为 : " + colors.keys)
    println("colors 中的值为 : " + colors.values)
    println("检测 colors 是否为空 : " + colors.isEmpty)
    println("检测 nums 是否为空 : " + nums.isEmpty)

    colors.keys.foreach { i =>
      print("Key = " + i)
      println(", Value = " + colors(i))
    }

    val romanNumeral = Map(1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V")
    println("Map:" + romanNumeral(4))

  }

  /**
    * Tuple3(目前Scala支持的元组的最大长度为22)
    */
  def tuple(): Unit = {
    val tuple1 = (1, 3.14, "Fred")
    val tuple2 = new Tuple3(1, 3.14, "Fred")
    tuple1.productIterator.foreach { i => println("tuple1 Value = " + i) }
    tuple2.productIterator.foreach { i => println("tuple2 Value = " + i) }

    // 一旦定义了一个元组，可以使用._和索引来访问员组的元素（矢量的分量，注意和数组不同的是，元组的索引从1开始）
    val pair = (99, "hello")
    println("tuple value one=" + pair._1)
    println("tuple value two=" + pair._2)


  }

  /**
    * Option
    */
  def option(): Unit = {
    val myMap: Map[String, String] = Map("key1" -> "value")
    val value1: Option[String] = myMap.get("key1")
    val value2: Option[String] = myMap.get("key2")

    println("option:" + value1) // Some("value1")
    println("option:" + value2) // None
  }

  /**
    * foreach
    */
  def foreachMethod(): Unit = {

    // 指定数组类型
    val args = new Array[String](3)

    args(0) = "Hello"

    args(1) = "world"

    args(2) = "!"

    // 方式一
    args.foreach(arg => println(arg))

    // 方式二
    args.foreach(println)

    // 方式三
    for (arg <- args) println(arg)

    // 方式四(实际上Scala中表达式1+2，最终解释为(1).+(2)+也是Int的一个方法)
    for (i <- 0 to 2) println(args(i))

    // 方式五
    var i = 0
    while (i < args.length) {
      println(args(i))
      i += 1
    }
  }

}


object Main {

  def main(args: Array[String]) {

    val collections = new Collections

    println("\n------test list------")
    collections.list();

    println("\n------test set------")
    collections.set();

    println("\n------test map------")
    collections.map();

    println("\n------test tuple------")
    collections.tuple();

    println("\n------test option------")
    collections.option();

    println("\n------test foreach------")
    collections.foreachMethod();

    println("\n------test read file------")
    readFile();

    val conf = new SparkConf().setAppName("Simple Application").setMaster("local");
    val sc = SparkContext.getOrCreate(conf)
    val textFile = sc.textFile("file:///Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/scala.md")
    println(textFile.count());

  }

  /**
    * 读取文件
    */
  def readFile(): Unit = {
    val fileName = "/Users/zhengyong/spark-file/data.txt"
    for (line <- Source.fromFile(fileName).getLines())
      println(line.length + ":" + line)
  }
}

/**
  * Scala函数按名称调用
  */
object Test {
  def main(args: Array[String]) {


    /**
      * In delayed method
        Getting time in nano seconds
        Param: 81303808765843
        Getting time in nano seconds
      */
    delayed(time());

    /**
      * Getting time in nano seconds
        In delayed method
        Param: 15377476327963
      */
    val t  = time()
    delayed(t)
  }

  def time() = {
    println("Getting time in nano seconds")
    System.nanoTime
  }

  def delayed(t: => Long) = {
    println("In delayed method")
    println("Param: " + t)
    t
  }
}



