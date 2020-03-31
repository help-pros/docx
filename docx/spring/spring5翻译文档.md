# 引言

请阅读[**综述**](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/overview.html#overview) ，快速介绍包括简要历史，设计原理，什从哪里去提问和技巧。有关信息，什么是新的，或者旧版本的迁移，可以去查看 [**Github Wiki**](https://github.com/spring-projects/spring-framework/wiki) 。

这个参考文档被分成几部分：

1. 核心：IOC容器、事件、资源、i18n国际标准、校验、数据绑定、类型转换、SpEL、AOP
2. 测试：模拟对象、TestContext框架、springmvc测试、WebTestClient
3. 数据访问：事务、DAO支持、jdbc、ORM、编组xml
4. Web Servlet：springmvc、websocket、sockJS、Stomp messaging
5. 网络反应：spring WebFlux、WebClient、WebSocket
6. 继承：远程、JMS、JCA、JMX、电子邮件、任务、定时器、缓存
7. 语言：芬兰湾的科特林、Groovy、动态语言



# 核心

这部分的参考文档涵盖了spring框架的不可或缺的所有技术。

其中最重要的是spring框架中的控制反转IOC容器。彻底领悟完spring框架中的IOC容器，紧接着介绍spring的面向切面编程AOP技术的全面覆盖。spring框架有自己独立的AOP框架，它从概念上容易理解，和成功解决了80%的java编程领域的AOP需求。

提供spring继承AspectJ（当前最富有、突出和在java企业领域中最成熟的AOP实现）

## IOC容器

### 介绍spring IOC容器和bean

这章包含spring框架的控制反转IOC的实现原理。IOC也叫依赖注入DI。这是一个通过对象定义自己依赖的过程，换句话说，它们使用其它对象时，只能通过构造器参数、工厂方法的参数、或在他们从一个工厂方法中被构造或返回时设置在一个对象实例中的属性。容器在创建bean时注入这些依赖。这个过程就是从根本上反转，因此命名为控制反转，bean本身通过使用类的直接构造或一个诸如服务定位模式的机制控制着依赖项的实例化或位置。

`org.springframework.beans`和`org.springframework.context` 这两个包是spring框架的IOC容器的基础。 [`BeanFactory`](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/beans/factory/BeanFactory.html) 接口提供了一种高级配置机制，能够管理任何类型的对象。[`ApplicationContext`](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/context/ApplicationContext.html) 是 `BeanFactory` 的子接口，它增加了更容易去集成springAOP特性；消息资源处理（用于国际化），事件发布，和应用层特定的上下文，例如用于web应用的`WebApplicationContext` 。

总的来说， `BeanFactory` 提供了配置框架和基础功能， `ApplicationContext` 增加了更多企业特性功能。 `ApplicationContext` 是 `BeanFactory` 的完整超集，并且在本章中专门使用于描述spring的IOC容器。关于更多信息去使用 `BeanFactory` 而不是`ApplicationContext` ，参考 [The BeanFactory](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-beanfactory).

在spring中，形成了你的应用程序的骨干，并且被spring的IOC容器管理的对象，称为bean。一个bean是由springIOC容器实例、组装和其他方式管理的对象。否则，一个bean是应用程序中许多对象之一。bean和它们之间的依赖关系反映在一个容器使用的配置元数据中。配置元数据可以表示为xml、java注解或java代码。

### 容器概述

 `org.springframework.context.ApplicationContext` 接口代表springIOC容器和负责实例化，配置和组装前面提及的bean。容器获得关于对象实例化的指令，配置和通过读取配置元数据去组装。它允许你去表达构成应用程序的对象和每一个对象之间的丰富的相互依赖关系。

`ApplicationContext` 接口的各自实现用于spring提供的开箱即用。在单体应用程序中，创建 [`ClassPathXmlApplicationContext`](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/context/support/ClassPathXmlApplicationContext.html)  或 [`FileSystemXmlApplicationContext`](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/context/support/FileSystemXmlApplicationContext.html) 的一个实例是普遍的。XML已经为定义配置元数据的传统格式，你可以通过提供一个少量xml配置去声明可支持这些额外元数据格式指导容器去使用java注解或代码作为元数据格式。

在大多数应用场景，不需要清楚用户代码来实例化springIOC容器的一个或多个实例。例如，在一个web应用场景中，一个引用web描述符xml的简单8行（大约）在应用的 `web.xml` 文件中通常就足够了（查看应用程序的便捷的[ApplicationContext](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#context-create)实例化）。如果你正在使用Eclipse配套的开发环境 [Spring Tool Suite](https://spring.io/tools/sts) ，那么可点击鼠标或键盘轻松容易创建一个样板配置。

下面图是一个描述spring如何工作的高级视图。你的应用classes文件与配置文件的元数据相结合，这样在 `ApplicationContext` 被创建和初始化后，你有一个完整的配置的和可执行的系统或应用。

![](images/container-magic.png)





#### 配置元数据

如上图所示，springIOC容器使用了一种配置元数据的方式，这些配置元数据表示在你的应用中，你作为一个应用开发者如何告诉spring容器去实例、配置和组装对象。

配置元数据传统上以一个简单直观的xml格式提供，这是这章大篇幅用来表达springIOC容器的关键概念和特征。

> 基于xml元数据不是唯一允许配置元数据的格式。springIOC容器本身已完全从这种实际编写配置元数据的格式中解耦。现在很多开发者为spring应用程序选择基于java的配置（[Java-based configuration](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-java)）。

关于使用spring容器的其它元数据格式的有关信息，请参阅：

基于注解配置（[Annotation-based configuration](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-annotation-config)） ：spring2.5引入了基于注解配置元数据的支持。

基于java配置（[Java-based configuration](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-java)） ：从spring3.0开始，spring javaConfig项目提供许多特点去成为核心spring框架的一部分。因此你可以通过使用java而不再是xml文件去定义bean来配置你的应用程序类。使用这些新特征，请参阅 `@Configuration`, `@Bean`, `@Import` 和 `@DependsOn` 注解。

spring配置由至少一个构成和容器必须管理通常不止一个bean定义。基于xml配置元数据在一个顶级标签 `<beans/>` 内配置`<bean/>` 标签来表示这些bean。java配置通常在一个 `@Configuration` 类中使用 `@Bean` 注解的方法。

这些bean定义与组成你的应用程序的实际对象一致。通常你定义service层对象，数据访问层对象（DAO）、展示层比如Struts `Action` 实例，基础结构对象比如Hibernate `SessionFactories`, JMS `Queues`等等 。通常在容器中不配置细粒度的实体对象，因为这通常是DAO和业务逻辑（Business）去创建和加载实体对象的职责。无论如何，你可以使用spring集成AspectJ 去配置IOC容器之外创建的对象。请参阅使用[AspectJ](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#aop-atconfigurable) 去依赖注入带有spring的实体对象 。

请看下面例子中XML基础配置元数据的基础结构：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions go here -->

</beans>
```

这个 `id` 属性是可用于识别单个bean属性的一个字符串。这个 `class` 属性可定义这个的bean的类型和使用完全限定的类名。这个id的值可引用其他对象（collaborating objects）。这个例子没有展示引用其他对象的xml，请参阅 [Dependencies](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-dependencies) 获得更多信息。

#### 实例化一个容器

实例化springIOC容器是很简单的。提供给 `ApplicationContext` 构造器的本地路径或位置实际上是资源字符串，允许容器从各种各样的外部资源例如从java类路径（ `CLASSPATH`）  ， 本地文件系统去加载配置元数据等等。

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```

在你了解springIOC容器后，你可能想知道更多关于spring资源的抽象知识，如   [Resources](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#resources) 的描述，它为读取一个从本地位置定义在一个URL语法中的输入流提供一种了方便机制。尤其， `Resource` 资源路径被用于去构造应用程序上下文如 [Application contexts and Resource paths](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#resources-app-ctx) 的描述。

下面例子介绍的是表现层对象（services.xml）的配置文件:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- services -->

    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
        <property name="accountDao" ref="accountDao"/>
        <property name="itemDao" ref="itemDao"/>
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for services go here -->

</beans>
```

下面例子介绍的是数据访问层对象（daos.xml）文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao"
        class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for data access objects go here -->

</beans>
```

在前面提到的例子中，表现层包括 `PetStoreServiceImpl` 类和`JpaAccountDao`  和 `JpaItemDao`  类型的两个数据访问层对象（基于JAP对象/关系映射标准）。 `property name` 标签引用的是javaBean配置的名字，`ref` 标签引用的是另一个bean定义的名称。`id` 和 `ref`  标签之间的结合表达的是协作其他对象之间的依赖关系。配置一个对象的依赖关系的详细信息，请查看 [Dependencies](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-dependencies) 。

##### 构成基于XML的配置元数据（容器支持的格式一）

bean的定义可跨越多个XML文件。通常每个个体XML配置文件在架构中代表一个逻辑层或模块。

你可以使用应用程序上下文构造函数从所有这些片段XMl中去加载bean定义。这些构造函数可以是多个资源`Resource` 位置，如前面章节解释。或者，使用一个或多个 `<import/>` 标签事件从另一个文件或多个文件去加载bean定义。例如：

```xml
<beans>
    <import resource="services.xml"/>
    <import resource="resources/messageSource.xml"/>
    <import resource="/resources/themeSource.xml"/>

    <bean id="bean1" class="..."/>
    <bean id="bean2" class="..."/>
</beans>
```

在前面的例子，从三个文件： `services.xml`, `messageSource.xml`, 和`themeSource.xml`  去加载外部定义的bean。所有的位置路径相对于导入的定义文件，因此 `services.xml` 作为要导入的文件必须要在同一个目录或类路径下，然而 `messageSource.xml` 和 `themeSource.xml` 必须在导入文件的一个资源`resources` 路径下。正如你看到的，前导斜杠是可以忽略的。但是考虑到这些路径是相对的，最好形式就是所有都不要使用斜杠。导入的文件的内容，包括顶级 `<beans/>` 标签，必须依据Spring Schema 有效XML bean定义。

> 也可以，但不推荐，在父目录下使用一个相对路径“../”去引入文件。这样做会对当前应用程序之外的一个文件创建依赖关系。尤其，这种 "classpath:"URLs 引入是不推荐的（例如："classpath:../services.xml"），当运行解析过程时选择最近的classpath 根路径，进而查看它的父目录。改变Classpath 配置可能导致一个不同的、不正确的目录的选择。
>
> 你可以使用完全限制的资源路径代替相对路径：例如，"file:C:/config/services.xml" 或 "classpath:/config/services.xml"。但是，请注意你正在耦合你的应用程序配置到特定的绝对路径上。通常保持使用相对路径会比使用绝对路径更好，例如，通过在运行时针对JVM系统属性解析的“$ {...}”占位符。

import指令是由beans命名空间本身提供的一个特性。除了普通bean定义之外的其他配置功能在Spring提供的一系列XML命名空间中可用，例如： “context”和“util”命名空间。

##### Groovy Bean 定义DSL（容器支持的格式二）

作为一个外部化配置元数据的更深一层的例子，bean定义也可以在spring的Groovy  Bean定义DSL中表示，从Grails 框架中可知道。通常，此配置将在一个“.groovy”文件中，其结构如下：

```java
beans {
    dataSource(BasicDataSource) {
        driverClassName = "org.hsqldb.jdbcDriver"
        url = "jdbc:hsqldb:mem:grailsDB"
        username = "sa"
        password = ""
        settings = [mynew:"setting"]
    }
    sessionFactory(SessionFactory) {
        dataSource = dataSource
    }
    myService(MyService) {
        nestedBean = { AnotherBean bean ->
            dataSource = dataSource
        }
    }
}
```

这种配置风格很大程度上等同于XML bean定义，甚至支持spring的XML配置命名空间。它允许通过一个“importBeans”指令导入xml bean定义文件。

#### 容器的使用

 `ApplicationContext` 是一个高级工厂的接口，能维持不同beans和它们的依赖关系的注册。你可以检索bean的实例化通过使用 `T getBean(String name, Class<T> requiredType)` 方法。

 `ApplicationContext` 能使你读取bean定义和访问它们，如下：

```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml","daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore",PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

使用Groovy配置，bootstrapping看起来非常相似，只是一个不同的上下文实现类，它是Groovy感知的（但也理解XML bean定义）：

```java
ApplicationContext context = new GenericGroovyApplicationContext("services.groovy", "daos.groovy");
```

最灵活的转化是 `GenericApplicationContext`  与reader delegates的组合，例如， 使用XML文件的`XmlBeanDefinitionReader` ：

```java
GenericApplicationContext context = new GenericApplicationContext();
new XmlBeanDefinitionReader(context).loadBeanDefinitions("services.xml", "daos.xml");
context.refresh();
```

或使用 Groovy 文件的 `GroovyBeanDefinitionReader` ：

```java
GenericApplicationContext context = new GenericApplicationContext();
new GroovyBeanDefinitionReader(context).loadBeanDefinitions("services.groovy", "daos.groovy");
context.refresh();
```

此种 reader delegates 可以在同样的 `ApplicationContext` 上混合和相匹配，如果需要，可从多个配置资源上读取bean定义。

你可以使用`getBean` 来检索bean的实例。`ApplicationContext`  接口提供少些其它方法可以检索bean，但理想情况下，你的应用程序代码不应该使用它们。实际上，你的应用程序代码不应该调用`getBean()` 方法，因此不依赖Spring API。例如spring集成Web框架，为各种web框架组件提供依赖注入，例如控制器和JSF-managed bean，允许你通过元数据（例如自动装配注解）对一个特定bean声明依赖关系。

### Bean概述

spring IOC容器管理一个或多个bean。这些bean是通过你提供给容器的配置元数据创建的。例如，以XMl  `<bean/>` 格式定义。

在容器本身内，这些bean定义表示为 `BeanDefinition` 对象，其中包含（以及其他信息）以下元数据：

- 包限定的类名：通常是正在定义的bean的实际实现类。
- bean行为配置元素，说明bean在容器中行为状况（范围、生命周期回调函数等等）
- 引用bean执行其工作所需的其它bean，这些引用通常被称为协作者或依赖项。
- 在新创建的对象中设置其它配置设置，例如，在一个管理连接池的bean中使用连接数，或池的大小限制。


这些元数据转换为组成每一个bean定义的一组属性。

表格1：bean定义

| Property                 | Explained in…                            |
| ------------------------ | ---------------------------------------- |
| class                    | [Instantiating beans](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-class) |
| name                     | [Naming beans](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-beanname) |
| scope                    | [Bean scopes](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-scopes) |
| constructor arguments    | [Dependency Injection](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators) |
| properties               | [Dependency Injection](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators) |
| autowiring mode          | [Autowiring collaborators](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-autowire) |
| lazy-initialization mode | [Lazy-initialized beans](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-lazy-init) |
| initialization method    | [Initialization callbacks](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) |
| destruction method       | [Destruction callbacks](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean) |

除了包含如何创建一个特定bean的信息的bean定义之外，`ApplicationContext` 实现类允许现有对象的注册，那些由用户在容器外部创建的。这些操作通过 `getBeanFactory()` 方法访问 ApplicationContext’s BeanFactory ，该方法返回BeanFactory 实现 `DefaultListableBeanFactory` 。 `DefaultListableBeanFactory` 通过`registerSingleton(..)` and `registerBeanDefinition(..)`方法支持这种注册。然而，典型的应用程序仅适用于通过元数据bean定义定义的bean。

> bean元数据和手动提供单例实例需要提前注册，以便容器在自动装配和其它自省步骤期间正确推理它们。虽然在某种程度上支持覆盖现有元数据和现有单例实例，在运行期间新bean的注册（与工厂的实时访问同步）是不受官方支持，并且可能在bean容器中导致并发访问异常和/或不一致状态。

#### 命名bean

每一个bean有一个或多个标识符。这些标识符在承载bean的容器内必须是唯一的。每个bean通常仅有一个标识符，但如果需要多个，额外的标识符可认为是别名。

在基于XML配置元数据，你可使用 `id`  和/或`name`  属性去指定bean标识符。 `id` 属性允许你指定一个id。按照惯例，这些命名是字母数字（'myBean', 'fooService',等等），但也可能包含特殊字符。如果你想给beanc引入其它别名，你可以在 `name` 属性上指定它们，通过一个逗号（,），分号（;）或空格隔开。作为历史记录，在Spring 3.1之前的版本中， `id` 属性被定义为`xsd:ID` 类型，它约束可能的字符。从3.1开始，它被定义为 `xsd:string` 类型。请注意，bean `id` 唯一性仍由容器强制执行，虽然不再由XML解析器强制执行。

你不再需要为bean提供一个名字或id。如果没有明确提供名字或id，容器会为bean生成一个唯一的名称。然而，如果你需要通过名称引入bean，通过 `ref` 标签或 [Service Locator](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-servicelocator) 样式查找的使用，你必须提供一个名字。不提供名称的动机与使用内部bean [inner beans](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-inner-beans) 和自动配置协作者 [autowiring collaborators](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-autowire) 是有关联的。

> bean 命名约定
>
> 这个约定是在命名bean时使用标准java约定作为实例字段名称。就是说，bean命名以小写字母开头，从那时起就是驼峰式的。这些名称的示例是（没有引号）`'accountManager'`, `'accountService'`, `'userDao'`, `'loginController'`, 等等。
>
> 命名bean始终使您的配置更易于阅读和理解，如果你使用Spring AOP ，那么在建议应用于与名称相关的一组bean时，它会有很大帮助。

> 通过classpath类路径中的组件扫描，Spring为没有命名的组件生产bean名称，遵循上述规则：基本上，采用简单的类名并将其首字母小写。然而，在（异常）特殊情况下，有一个或多个字符时，并且第一个和第二个字符都是大写，将保留原始的大小写。这些规则与`java.beans.Introspector.decapitalize` （Spring在这里使用）定义的规则相同。

##### 在bean定义之外别名bean

在一个bean定义自身，你可为bean提供多个名称，通过 使用 `id` 属性指定一个名称，和在 `name` 属性中指定多个其它名称的组合。这些名称可以认为是同一个bean的别名，在一些情景下是很有用的，比如允许应用程序下每一个组件通过容器本身指定的bean名称引用公共依赖项。

指定bean实际定义的所有别名并不是总是足够的，然而，有时需要为其它地方定义的bean引入别名。在大型系统中这种情况常见，配置在每个子系统直接分配，每个子系统具有其直接的一组对象定义。在基于XML配置元数据，你可以使用 `<alias/>` 标签去实现。

```xml
<alias name="fromName" alias="toName"/>
```

在这种情况下，在同一个容器中名称为 `fromName` 的bean，也可能，在使用别名定义之后被称为 `toName` 

例如，为系统A配置元数据可以通过名称为`subsystemA-dataSource` 引入数据源DataSource 。系统B配置元数据可以通过名称为 `subsystemB-dataSource` 引入数据源DataSource 。在组合使用两个系统的主应用程序时，主应用程序通过名称为 `myApp-dataSource` 引入数据源DataSource 。要让所有三个名称都指向你添加到MyApp配置元数据的同一对象，请使用以下别名定义：

```xml
<alias name="subsystemA-dataSource" alias="subsystemB-dataSource"/>
<alias name="subsystemA-dataSource" alias="myApp-dataSource" />
```

现在，每个组件和主应用程序可以引入数据源DataSource 通过唯一的，并保证不会与任何其它定义冲突（有效地创建一个命名空间），但它们引入的是同一个bean。

> java 配置
>
> 如果你使用java配置，可使用@Bean注解提供别名，详细信息请查阅 [Using the @Bean annotation](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-java-bean-annotation) 。

#### 实例化bean

实例化bean本质上是用于创建一个或多个对象的配方。容器在请求时查看命名bean的配方，并使用该bean定义封装的配置元数据去创建（或获得）实际对象。

如果你使用基于XML配置元数据，则要在 `<bean/>` 标签的 `class`属性上指定实例化对象的类型（或class）。 这个`class` 属性通常是必须的，它在内部是`BeanDefinition`  实例上的一个 `Class` 属性。（异常情况，请看 [Instantiation using an instance factory method](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-factory-class-instance-factory-method)  和 [Bean definition inheritance](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions) ）。你可通过两个方式使用 `Class`属性：

- 通常，容器自身直接通过反射调用其构造函数创建bean，在这种情况下指定构造的bean类，稍微等同于用使用`new` 操作符的java代码。
- 去指定包含将被调用去创建对象的`static`静态 工厂方法的实际类，在不太常见的情况下，容器在类上调用 `static` 静态工厂方法来创建bean。对象类型是从`static`静态工厂方法中返回的，可能是相同类或另一个完全不同的类。

> 内部类命名
>
> 如果你想为一个 `static` 静态嵌套类配置一个bean定义，则必须使用嵌套类的二进制名称。
>
> 例如，你有一个名为 `Foo` 类在`com.example` 包下，并且这个 `Foo` 类有一个命名为 `Bar` 的静态嵌套类，bean定义中`'class' ` 属性值将是。。。
>
> `com.example.Foo$Bar` 
>
> 注意$字符的使用是将外部类名和嵌套类名分隔开来。

#####  用构造函数实例化

当你通过构造函数的方法创建一个bean时，所有的普通类都可以使用，并且与spring兼容。也就是说，自主开发的类不需要去实现任何指定的接口或在用指定的格式去编码。简单地指定bean类就足够了。然而，用于那个指定bean依赖于IOC的类型，你可能需要一个默认（空）构造函数。Spring IOC容器实际上会管理任何你需要去管理的类；它不限于去管理真正的JavaBeans。更多spring使用者更喜欢仅有一个默认（无参数）构造函数的真正的JavaBeans ，并且在容器内属性之后建模了适当的setters 和getters 。你也可以在容器中使用更多独特的non-bean-style classes（没有bean样式的类)。例如，你需要使用一个绝对没有遵守javaBean规范的旧连接池，Spring也可以对其进行管理。

基于XML配置元数据，你可以指定bean类，如下：

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>

<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```

详细关于为构造函数提供参数的机制（如果需要），以及在对象被构造后设置对象实例属性，请查看 [Injecting Dependencies](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators).

##### 通过静态工厂方法实例化

当创建一个静态工厂方法定义bean时，你使用 `class` 属性指定包含 `static`静态工厂方法的类 和一个命名为`factory-method`  属性，去指定工厂方法本身的名称。你应该能够调用这些方法（使用可选参数，如后面描述），并且返回一个存在的对象，随后将其视为通过构造函数创建的对象。这种bean定义的一个用途是在旧代码里调用 `static` 工厂。

以下bean定义指定通过调用工厂方法创建bean。这种定义不需要指定返回对象的类型（类），仅包含工厂方法的类。在这个例子里，`createInstance()` 方法必须是一个静态方法。

```xml
<bean id ="clientService" class="examples.ClientService" factory-method="createInstance" />
```

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```

关于向工厂方法提供（可选的）参数以及在从工厂返回对象后设置对象实例属性的机制的详情内容，请查阅[Dependencies and configuration in detail](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-properties-detailed).

##### 使用实例工厂方法实例化

类似于通过静态工厂方法去实例化，用实例工厂方法进行实例化从容器中调用现有bean的非静态方法以创建新bean。要使用这样的机制，将class属性保留为空，并保留在 `factory-bean` 属性中，在当前（或父/祖先）容器中指定bean的名称，该容器中包含被调用去创建对象的实例方法。用 `factory-method` 属性设置工厂方法自身的名称。

```xml
<!-- the factory bean, which contains a method called createInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<!-- the bean to be created via the factory bean -->
<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>
```

```java
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

一个工厂类可以拥有更多工厂方法，如下：

```xml
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>

<bean id="accountService"
    factory-bean="serviceLocator"
    factory-method="createAccountServiceInstance"/>
```

```java
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    private static AccountService accountService = new AccountServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }

    public AccountService createAccountServiceInstance() {
        return accountService;
    }
}
```

这些方法表明的是可以通过依赖注入（DI）管理和配置工厂bean自身。请查阅[Dependencies and configuration in detail](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-properties-detailed).

> 在spring文档中，工厂bean指的是配置在spring容器中的bean，它将通过实例[instance](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-class-instance-factory-method) or静态 [static](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-class-static-factory-method) 工厂方法创建对象。相比之下，`FactoryBean` （注意大写）指的是spring指定的 [`FactoryBean` ](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-factory-extension-factorybean) 

### 依赖性

一个典型的企业应用程序不包含单个对象（或在spring 说法中的bean）。甚至最简单的应用程序有少些对象，这些对象工作一起,以呈现最终用户所看到的互相耦合的应用程序。下一节解释的是如何从定义大量独立的bean定义到一个完全实现的应用程序，这些对象互相协作去完成这个目标。

#### 依赖注入

依赖注入（DI）是一个对象定义其依赖性的过程，也就是，使用其它对象，只能通过构造函数参数，工厂方法的参数，对象实例在构造器或从工厂方法返回后设置的属性。在创建bean的时候容器会注入其依赖关系。这个过程就是从根本上反转，因此命名为控制反转（IOC），关于bean本身控制其依赖关系的实例化或位置可通过使用类的直接构造函数，或*Service Locator* pattern。

通过DI原则，代码更加清晰，并且当对象提供它们的依赖关系时，更有效的去耦。对象不会查找其依赖关系，也不知道依赖关系的位置或类。例如，你的类变得更容易去测试，尤其当依赖关系都是接口或抽象基类时，它允许在单元测试中使用根或模拟实现。

DI存在于两种变体中， [Constructor-based dependency injection](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) 和 [Setter-based dependency injection](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/core.html#beans-setter-injection)。 

##### 基于构造函数的依赖注入

基于构造函数的依赖注入是由容器调用一个有多个参数的构造函数完成的，每一个参数都代表一个依赖关系。调用一个指定参数的 `static`静态工厂方法去构造bean几乎是等效的。本讨论同样处理构造函数和静态工厂方法的参数。下面的例子呈现一个仅可通过构造函数注入进行依赖注入的类。注意这是一个没有任何特殊的类，它是一个POJO，它不依赖容器指定接口、基类或注解。

```java
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on a MovieFinder
    private MovieFinder movieFinder;

    // a constructor so that the Spring container can inject a MovieFinder
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

###### 构造函数参数解析

使用参数的类型进行构造函数参数解析匹配。如果bean定义的构造函数参数中不存在潜在的歧义，那么在bean定义中定义构造函数参数的顺序是在实例化bean时将这些参数提供给适当的构造函数的顺序。考虑下面的类：

```java
package x.y;

public class Foo {

    public Foo(Bar bar, Baz baz) {
        // ...
    }
}
```

没有潜在的歧义，假设 `Bar` and `Baz` 类没有继承关系。因此，下面的配置可以很好的工作，你也不需要在`<constructor-arg/>` 标签上指定构造函数参数索引和or明确的类型。

```xml
<beans>
    <bean id="foo" class="x.y.Foo">
        <constructor-arg ref="bar"/>
        <constructor-arg ref="baz"/>
    </bean>

    <bean id="bar" class="x.y.Bar"/>

    <bean id="baz" class="x.y.Baz"/>
</beans>
```

当引用另外的bean是，类型是知道的，并且可以进行匹配（就像前面的例子一样）。当使用一个简单类型，例如 `<value>true</value>`， Spring不指定值的类型，因此在没有帮助的情况下无法按类型匹配。考虑下面的类：

```java
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private int years;

    // The Answer to Life, the Universe, and Everything
    private String ultimateAnswer;

    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

###### 构造函数参数类型匹配

在前面的场景中，容器可以使用与简单类型匹配的类型，如果使用 `type` 属性明确指定构造函数参数的类型。例如：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```

###### 构造函数参数索引

使用`index` 属性指定明确的构造函数参数的索引。例如：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```

###### 构造函数参数名称

你可以使用构造参数参数名称为值消除歧义：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```

请记住，要使这个功能发挥作用，您的代码必须在启用调试标志的情况下进行编译，以便Spring可以从构造函数中查找参数名称。如果你的代码不能再调试标志下完成编译，你可以使用[@ConstructorProperties](https://download.oracle.com/javase/6/docs/api/java/beans/ConstructorProperties.html) JDK注解去明确你的构造函数参数名称。然后，示例类必须如下所示：

```java
package examples;

public class ExampleBean {

    // Fields omitted

    @ConstructorProperties({"years", "ultimateAnswer"})
    public ExampleBean(int years, String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}
```

##### 基于Setter的依赖注入



