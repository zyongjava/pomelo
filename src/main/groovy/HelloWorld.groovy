import com.alibaba.fastjson.JSON

/**
 *  Using a simple println statement to print output to the console
 *  <pre>
 *            <dependency>
                     <groupId>org.codehaus.groovy</groupId>
                     <artifactId>groovy</artifactId>
                     <version>2.3.1</version>
              </dependency>
 *  </pre>
 */
class HelloWorld {

    static void main(String[] args) {
        int aInt = 1
        long bLong = 1L
        double cDouble = 1d
        float dFloat = 1f
        def fMap = ['TopicName1':'Lists','TopicName2':'Maps']
        Date date = new Date()

        println("int value: ${aInt}")
        println("long value: ${bLong}")
        println("double value: ${cDouble}")
        println("float value: ${dFloat}")
        println("map element TopicName1 value: " + fMap.TopicName1)
        println("sum： 5 + 2 = " + sum(2))

        System.out.println("date is: " + date.toString())
        parseJson()
    }

    /**
     * calculate sum
     * @param a
     * @param b default 5
     * @return sum
     */
    static int sum(int a,int b = 5) {
        int c = a+b
        return c
    }

    /**
     * parse jason
     */
    static void parseJson() {
        def object = JSON.parseObject('{ "name": "John", "ID" : "1"}')
        println("json name: " + object.name)
        println("json ID: " + object.ID)
    }
}
