## spring初始化bean循环依赖解决

```
2017-04-14 10:01:05,511 - org.springframework.core.env.StandardEnvironment -0    [main] DEBUG  - Initializing new StandardEnvironment
2017-04-14 10:01:05,542 - org.springframework.core.env.StandardEnvironment -31   [main] DEBUG  - Adding [systemProperties] PropertySource with lowest search precedence
2017-04-14 10:01:05,544 - org.springframework.core.env.StandardEnvironment -33   [main] DEBUG  - Adding [systemEnvironment] PropertySource with lowest search precedence
2017-04-14 10:01:05,545 - org.springframework.core.env.StandardEnvironment -34   [main] DEBUG  - Initialized StandardEnvironment with PropertySources [systemProperties,systemEnvironment]
2017-04-14 10:01:05,551 - org.springframework.context.support.ClassPathXmlApplicationContext -40   [main] INFO   - Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@90f6bfd: startup date [Fri Apr 14 10:01:05 CST 2017]; root of context hierarchy
2017-04-14 10:01:05,775 - org.springframework.core.env.StandardEnvironment -264  [main] DEBUG  - Initializing new StandardEnvironment
2017-04-14 10:01:05,776 - org.springframework.core.env.StandardEnvironment -265  [main] DEBUG  - Adding [systemProperties] PropertySource with lowest search precedence
2017-04-14 10:01:05,780 - org.springframework.core.env.StandardEnvironment -269  [main] DEBUG  - Adding [systemEnvironment] PropertySource with lowest search precedence
2017-04-14 10:01:05,783 - org.springframework.core.env.StandardEnvironment -272  [main] DEBUG  - Initialized StandardEnvironment with PropertySources [systemProperties,systemEnvironment]
2017-04-14 10:01:05,852 - org.springframework.beans.factory.xml.XmlBeanDefinitionReader -341  [main] INFO   - Loading XML bean definitions from class path resource [applicationContext.xml]
2017-04-14 10:01:05,861 - org.springframework.beans.factory.xml.DefaultDocumentLoader -350  [main] DEBUG  - Using JAXP provider [com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl]
2017-04-14 10:01:06,105 - org.springframework.beans.factory.xml.PluggableSchemaResolver -594  [main] DEBUG  - Loading schema mappings from [META-INF/spring.schemas]
2017-04-14 10:01:06,136 - org.springframework.beans.factory.xml.PluggableSchemaResolver -625  [main] DEBUG  - Loaded schema mappings: {http://www.springframework.org/schema/util/spring-util.xsd=org/springframework/beans/factory/xml/spring-util-3.2.xsd, http://www.springframework.org/schema/jee/spring-jee-3.2.xsd=org/springframework/ejb/config/spring-jee-3.2.xsd}
2017-04-14 10:01:06,139 - org.springframework.beans.factory.xml.PluggableSchemaResolver -628  [main] DEBUG  - Found XML schema [http://www.springframework.org/schema/beans/spring-beans.xsd] in classpath: org/springframework/beans/factory/xml/spring-beans-3.2.xsd
2017-04-14 10:01:06,252 - org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader -741  [main] DEBUG  - Loading bean definitions
2017-04-14 10:01:06,309 - org.springframework.beans.factory.xml.XmlBeanDefinitionReader -798  [main] DEBUG  - Loaded 2 bean definitions from location pattern [/applicationContext.xml]
2017-04-14 10:01:06,309 - org.springframework.context.support.ClassPathXmlApplicationContext -798  [main] DEBUG  - Bean factory for org.springframework.context.support.ClassPathXmlApplicationContext@90f6bfd: org.springframework.beans.factory.support.DefaultListableBeanFactory@7e2d773b: defining beans [studentId,teacherId]; root of factory hierarchy
2017-04-14 10:01:06,372 - org.springframework.context.support.ClassPathXmlApplicationContext -861  [main] DEBUG  - Unable to locate MessageSource with name 'messageSource': using default [org.springframework.context.support.DelegatingMessageSource@6950e31]
2017-04-14 10:01:06,380 - org.springframework.context.support.ClassPathXmlApplicationContext -869  [main] DEBUG  - Unable to locate ApplicationEventMulticaster with name 'applicationEventMulticaster': using default [org.springframework.context.event.SimpleApplicationEventMulticaster@5891e32e]
2017-04-14 10:01:06,386 - org.springframework.beans.factory.support.DefaultListableBeanFactory -875  [main] INFO   - Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@7e2d773b: defining beans [studentId,teacherId]; root of factory hierarchy
2017-04-14 10:01:06,387 - org.springframework.beans.factory.support.DefaultListableBeanFactory -876  [main] DEBUG  - Creating shared instance of singleton bean 'studentId'
2017-04-14 10:01:06,387 - org.springframework.beans.factory.support.DefaultListableBeanFactory -876  [main] DEBUG  - Creating instance of bean 'studentId'
2017-04-14 10:01:06,573 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1062 [main] DEBUG  - Eagerly caching bean 'studentId' to allow for resolving potential circular references
2017-04-14 10:01:06,682 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1171 [main] DEBUG  - Creating shared instance of singleton bean 'teacherId'
2017-04-14 10:01:06,682 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1171 [main] DEBUG  - Creating instance of bean 'teacherId'
2017-04-14 10:01:06,683 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1172 [main] DEBUG  - Eagerly caching bean 'teacherId' to allow for resolving potential circular references
2017-04-14 10:01:06,689 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1178 [main] DEBUG  - Returning eagerly cached instance of singleton bean 'studentId' that is not fully initialized yet - a consequence of a circular reference
2017-04-14 10:01:06,698 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1187 [main] DEBUG  - Finished creating instance of bean 'teacherId'
2017-04-14 10:01:06,699 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1188 [main] DEBUG  - Finished creating instance of bean 'studentId'
2017-04-14 10:01:06,699 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1188 [main] DEBUG  - Returning cached instance of singleton bean 'teacherId'
2017-04-14 10:01:06,701 - org.springframework.context.support.ClassPathXmlApplicationContext -1190 [main] DEBUG  - Unable to locate LifecycleProcessor with name 'lifecycleProcessor': using default [org.springframework.context.support.DefaultLifecycleProcessor@69a10787]
2017-04-14 10:01:06,702 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1191 [main] DEBUG  - Returning cached instance of singleton bean 'lifecycleProcessor'
2017-04-14 10:01:06,707 - org.springframework.core.env.PropertySourcesPropertyResolver -1196 [main] DEBUG  - Searching for key 'spring.liveBeansView.mbeanDomain' in [systemProperties]
2017-04-14 10:01:06,707 - org.springframework.core.env.PropertySourcesPropertyResolver -1196 [main] DEBUG  - Searching for key 'spring.liveBeansView.mbeanDomain' in [systemEnvironment]
2017-04-14 10:01:06,708 - org.springframework.core.env.PropertySourcesPropertyResolver -1197 [main] DEBUG  - Could not find key 'spring.liveBeansView.mbeanDomain' in any property source. Returning [null]
2017-04-14 10:01:06,709 - org.springframework.beans.factory.support.DefaultListableBeanFactory -1198 [main] DEBUG  - Returning cached instance of singleton bean 'studentId'
name: student3 age :23

```


1）创建bean是先检查缓存是否有或利用this.singletonsCurrentlyInCreation.containsKey(beanName)条件过滤正在创建中的bean(singletonObject = this.earlySingletonObjects.get(beanName);)，没有就创建bean

```
DefaultSingletonBeanRegistry: 180行
```


2）创建bean时先标明这个bean正在创建singletonsCurrentlyInCreation.put(beanName, Boolean.TRUE)， getEarlyBeanReference（）

```
DefaultSingletonBeanRegistry: 217行 标记bean正在创建

AbstractAutowireCapableBeanFactory : 510行 获取正在创建引用bean

```

```
/**
 * Add the given singleton factory for building the specified singleton
 * if necessary.
 * <p>To be called for eager registration of singletons, e.g. to be able to
 * resolve circular references.
 * @param beanName the name of the bean
 * @param singletonFactory the factory for the singleton object
 */
protected void addSingletonFactory(String beanName, ObjectFactory singletonFactory) {
	Assert.notNull(singletonFactory, "Singleton factory must not be null");
	synchronized (this.singletonObjects) {
		if (!this.singletonObjects.containsKey(beanName)) {
			this.singletonFactories.put(beanName, singletonFactory);
			this.earlySingletonObjects.remove(beanName);
			this.registeredSingletons.add(beanName);
		}
	}
}
```

3） 利用缓存bean。检测改bean是否是正在创建的bean，然后利用getObjectForBeanInstance
（）方法获取真正bean,


