package callback;

/**
 * java8 版本task
 */
public class TaskFunction8 {

    /**
     * 内置task
     *
     * @param <ParamType>
     * @param <ReturnType>
     */
    interface Task<ParamType, ReturnType> {

        ReturnType run(ParamType type);
    }

    /**
     * 执行任务
     *
     * @param callBack 回调函数
     * @return 返回结果
     */
    public static void runTask(Task<String, String> callBack) {
        for (int i = 0; i < 10; i++) {
            String returnMsg = callBack.run("value" + i);
            System.out.println("return message: " + returnMsg);
        }
    }


    /**
     * 调用任务
     */
    public static void invokeTask() {
        Task task = s -> {
            System.out.println("run task; received message: " + s);
            return "success: " + s;
        };
        runTask(task);
    }

    public static void main(String[] args) {
        invokeTask();
    }
}