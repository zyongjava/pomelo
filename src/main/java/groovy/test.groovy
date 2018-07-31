package groovy

class test {

    static myMethod(def sMap) {
        int aInt = 1
        long bLong = 1L
        double cDouble = 1d
        float dFloat = 1f
        println("int value: ${aInt}")
        println("long value: ${bLong}")
        println("double value: ${cDouble}")
        println("float value: ${dFloat}")
        println(sMap)
        // String to Map
        def fMap = Eval.me(sMap)
        println("map element TopicName1 value: " + fMap.TopicName1)
    }

}
