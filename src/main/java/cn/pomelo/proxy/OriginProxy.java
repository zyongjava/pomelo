/**
 * 
 */
package cn.pomelo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk代理 代理<br/>
 *
 * 类实现InvocationHandler
 * 
 * @author pomelo
 */
public class OriginProxy implements InvocationHandler {

    private Object target;

    public OriginProxy(OriginInterface target){
        super();
        this.target = target;

    }

    /**
     * 构建代理对象
     * 
     * @return OriginInterface
     */
    public OriginInterface build() {
        return (OriginInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                                                        target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("begin invoke method.");

        Object result = method.invoke(target, args);

        System.out.println("end invoke method.");

        return result;
    }

}
