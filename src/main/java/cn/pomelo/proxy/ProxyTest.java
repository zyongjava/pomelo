/**
 * 
 */
package cn.pomelo.proxy;

import java.lang.reflect.Proxy;

/**
 * @author pomelo
 *
 */
public class ProxyTest {
	
	public static void main(String[] args) {
		// 实例化目标对象
		OriginInterface target = new OriginImpl();
		// 根据目标对象创建代理对象
		OriginInterface proxy = (OriginInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new OriginProxy(target));
		// 调用代理对象方法
		proxy.testProxy();
	}

}
