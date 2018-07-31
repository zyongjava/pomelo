package groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;


/**
 * java execute groovy
 *
 * @author: zhengyong Date: 2018/7/31 Time: 下午2:29
 */
public class ExecuteGroovy {

    private static GroovyClassLoader loader;

    private static final String GROOVY_SCRIPT = System.getProperty("user.dir") + "/src/main/java/groovy/test.groovy";

    static {
        ClassLoader parent = ExecuteGroovy.class.getClassLoader();
        loader = new GroovyClassLoader(parent);

    }

    public static void main(String[] args) throws Exception {

        long time1 = System.currentTimeMillis();
        executeScriptClass(GROOVY_SCRIPT, "myMethod", "['TopicName1':'Lists','TopicName2':'Maps']");
        long time2 = System.currentTimeMillis();
        System.out.println(String.format("execute groovy file cost:%sms", (time2 - time1)));

        executeScriptCode("println(\"groovy script execute success\")");
        long time3 = System.currentTimeMillis();
        System.out.println(String.format("execute groovy code cost:%sms", (time3 - time2)));

    }

    /**
     * 直接执行groovy文件
     *
     * @param path       文件地址
     * @param methodName 方法名称
     * @param args       入参
     * @return Object
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Object executeScriptClass(String path, String methodName, String args) throws IOException, IllegalAccessException, InstantiationException {
        File file = new File(path);
        Class groovyClass = loader.parseClass(file);
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();  // 缓存对象，编译一次
        return executeGroovy(groovyObject, "myMethod", "['TopicName1':'Lists','TopicName2':'Maps']");
    }

    /**
     * 直接执行groovy代码片段
     *
     * @param code groovy代码片段
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static void executeScriptCode(String code) throws IllegalAccessException, InstantiationException {
        Class groovyClass2 = loader.parseClass(code); //也可以直接使用String(脚本内容)
        GroovyObject groovyObject = (GroovyObject) groovyClass2.newInstance();  // 缓存对象，编译一次
        executeGroovy(groovyObject, "run", null);
    }

    private static Object executeGroovy(GroovyObject groovyObject, String methodName, String args) {
        return groovyObject.invokeMethod(methodName, args);
    }
}
