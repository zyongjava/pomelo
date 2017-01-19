import org.apache.spark.SparkContext

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

    // 空列表
    val empty: List[Nothing] = List()

    for (x <- site) {
      println("site:" + x)
    }

    for (x <- nums) {
      println("nums:" + x)
    }

  }

  /**
    * Set
    */
  def set(): Unit = {
    val set = Set(1, 2, 3)
    println(set.getClass.getName) //

    println(set.exists(_ % 2 == 0)) //true
    println(set.drop(1)) //Set(2,3)

    for (x <- set) {
      println("set:" + x)
    }
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

  }

  /**
    * Tuple3
    */
  def tuple(): Unit = {
    val tuple1 = (1, 3.14, "Fred")
    val tuple2 = new Tuple3(1, 3.14, "Fred")
    tuple1.productIterator.foreach { i => println("tuple1 Value = " + i) }
    tuple2.productIterator.foreach { i => println("tuple2 Value = " + i) }
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

    val sc = SparkContext.getOrCreate()
    val textFile = sc.textFile("file:///Users/zhengyong/Development/spark-2.1.0-bin-hadoop2.7/scala.md")
    println(textFile.count());

  }
}



