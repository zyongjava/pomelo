package java8.lambda;

/**
 * java8 java8.lambda 函数定义（Lambda 表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数化的编程能力。）
 *
 * @author: zhengyong Date: 2018/11/2 Time: 上午11:18
 */
public class OperationFunctionTest {

    public static void main(String[] args) {
        int sum = sum(1, 2);
        System.out.println("sum=" + sum);

        int sub = sub(1, 2);
        System.out.println("sub=" + sub);
    }

    //求和： lambda实现接口OperationFunction
    private static int sum(int s1, int s2) {
        OperationFunction sumFunction = (a, b) -> a + b;
        int sum = sumFunction.operate(s1, s2);
        return sum;
    }

    //求差集： lambda实现接口OperationFunction
    private static int sub(int s1, int s2) {
        OperationFunction subFunction = (a, b) -> a - b;
        int sub = subFunction.operate(s1, s2);
        return sub;
    }
}
