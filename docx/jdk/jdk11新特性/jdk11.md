# 新增了一系列字符串处理方法 

- 判断字符串是否为空白
  - isBlank();
- 去除首尾空白
  - .strip();
- 去除尾部空格
  - .stripTrailing();
- 去除首部空格
  - stripLeading();
- 复制字符串
  - "Java".repeat(3);//JavaJavaJava 
- 行数统计
  - "A\nB\nC".lines().count();//3

# Optional加强 

- isEmpty()
  - 判断value是否为空
- ifPresentOrElse(Consumer<?super T> action,Runnable emptyAction)
  - value非空，执行参数1功能；如果value为空，执行参数2功能
- Optional<T> or(Supplier<? extends Optional Optional<? extends T>> supplier)
  - value非空，返回对应的Optional；value为空，返回形参封装的Optional
- Stream<T> stream()
  - value为空，返回仅包含此value的Stream；否则，返回一个空的Stream
- T orElseThrow()
  - value非空，返回value；否则抛异常NoSuchElementException

# 局部变量类型推断升级 

- ```java 
  //错误的形式:必须要有类型，可以加上var
  //Consumer<String> con1 = (@Deprecated t)->System.out.println(t.toUpperCase());
  //jdk11正确的形式
  //使用var的好处是在使用lamdba表达式时给参数加上注解
  //Consumer<String> con1 = (@Deprecated var t)->System.out.println(t.toUpperCase());
  ```



# 全新的HTTP客户端API

- HttpClient 替换原有的HttpURLConnection

# 更简化的编译运行程序 

- ```java
  //编译
  javac javastack.java
  //运行
  java javastack
  //jdk11:
  java javastack.java    
  ```



# 废弃Nashorn引擎 

# ZGC 

- 垃圾回收器





 