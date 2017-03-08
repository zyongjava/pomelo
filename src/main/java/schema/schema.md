## spring schema扩展

> Spring 2.5在2.0的基于Schema的Bean配置的基础之上，再增加了扩展XML配置的机制。通过该机制，我们可以编写自己的Schema，并根据自定义的Schema用自定的标签配置Bean。要使用的Spring的扩展XML配置机制，也比较简单，有以下4个步骤：
>
> 1. 编写自定义Schema文件；
> 2. 编写自定义NamespaceHandler；
> 3. 编写解析BeanDefinition的parser
> 4. 在Spring中注册上述组建


### Maven依赖

```
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
```

详见:[http://blog.csdn.net/zhengyong15984285623/article/details/60876418](http://blog.csdn.net/zhengyong15984285623/article/details/60876418)