/**
 * 
 */
package cn.pomelo.proxy;

/**
 * @author pomelo
 */
public class ProxyTest {

    public static void main(String[] args) {
        // 实例化目标对象
        OriginInterface target = new OriginImpl();

        // 根据目标对象创建代理对象
        OriginProxy proxyObject = new OriginProxy(target);
        OriginInterface proxy = proxyObject.build();

        // 调用代理对象方法
        proxy.testProxy();
    }

}
