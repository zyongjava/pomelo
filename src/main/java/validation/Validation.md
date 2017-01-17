## Validation校验属性格式

基于javax.validation.Validation校验属性格式

#### 一、maven依赖

```xml
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.0.0.GA</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>4.3.1.Final</version>
</dependency>
```

```

#### 二、注意事项
异常：

```
Caused by: java.lang.NoClassDefFoundError: javax/validation/ParameterNameProvider
	at org.hibernate.validator.HibernateValidator.createGenericConfiguration(HibernateValidator.java:41)
	at javax.validation.Validation$GenericBootstrapImpl.configure(Validation.java:269)
	at javax.validation.Validation.buildDefaultValidatorFactory(Validation.java:111)
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.getValidatorFactory(TypeSafeActivator.java:445)
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.activate(TypeSafeActivator.java:96)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at org.hibernate.cfg.beanvalidation.BeanValidationIntegrator.integrate(BeanValidationIntegrator.java:150)
	... 50 more
```
解决参考：http://www.xuebuyuan.com/1955429.html
