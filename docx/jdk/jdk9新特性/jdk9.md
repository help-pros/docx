# 模块化系统

- 用模块来管理各个package，不声明默认就是隐藏，可指定哪部分暴露，哪部分隐藏；即根据模块的需要加载程序运行需要的class，而不是以前的不管其中的类是否被classloader加载而整个rt.jar都会被jvm加载到内存当中去

- 例如：jdk中的Junit，不声明就不能用了；

  ```java
  //声明引用Junit
  requires junit
  ```

  

# shell命令

- REPL工具

```java
jshell
/help
/imports
/exit    
```



# 语法改变

## try语句 

- java8 中，可以实现资源的自动关闭，但是要求执行后必须关闭所有资源必须在try子句中初始化，否则编译不通过。

  - ```java
    try(InputStreamReader reader = new InputStreamReader(System.in)){
        
    }catch(IOException e){
        e.printStackTrace();
    }
    ```

- java9 中资源关闭操作：需要自动关闭的资源的实例化可以放在try的一对小括号外。此时的资源属性是常量，声明为final，不可修改

  - ```java
    InputStreamReader reader = new InputStreamReader(System.in);
    OutputStreamWriter writer = new OutputStreamReader(System.out);
    try(reader;writer){
        
    }catch(IOException e){
        e.printStackTrace();
    }
    ```

## String 存储结构变更 

- 结论：String再也不用char[]来存储啦，改成了byte[]加上编码标记，节约了一些空间

## 集合工厂方法：快速创建只读集合 

- List.of(1,2,3,4,5);

- Collections.unmodifiableList(list);

- 如果修改就抛异常

## InputStream加强 

- transferTo：可以用来将数据直接传输到OutputStream

- ```java
  ClassLoader classLoader = this.getClass().getClassLoader();
  
          try (InputStream resourceAsStream = classLoader.getResourceAsStream("7t.sql");OutputStream outputStream = new FileOutputStream("src\\test.sql")){
              resourceAsStream.transferTo(outputStream);
          } catch (IOException e) {
              e.printStackTrace();
          }
  ```

- 

# API改变

## 增强的Stream API

### takeWhile

- 在有序的Stream中，takeWhile返回从开头开始的尽量多的元素

- ```java
  List<Integer> list = Arrays.asList(23,33,32,44,55,23);
  list.stream().takeWhile(x->x<40).forEach(System.out::println);
  //23,33,32
  ```

- 

### dropWhile

- 与takeWhile 相反，返回剩余的元素

- ```java
  List<Integer> list = Arrays.asList(23,33,32,44,55,25);
  list.stream().takeWhile(x->x<40).forEach(System.out::println);
  //44 55 25
  ```

- 

### ofNullable

- java8 中Stream不能完全为null，否则会报空指针异常。
- 而java9中的ofNullable方法允许我们创建一个单元素Stream，可以包含一个非空元素，也可以创建一个空Stream

### iterate方法的新重载方法

- ```java
  //java 8
  Stream.iterate(0,x->x+2).limit(10).forEach(System.out::println)
      //java 9
  Stream.iterate(0,x->x<100,x->x+2).forEach(System.out::println)  
  ```

##  

### Optional获取Stream的方法

- ```java
  Optional<List<String>> optional = Optional.ofNullable(list)
  optional.stream();
  stream.flatMap(x->x.stream()).forEach(System.out::println) 
  ```

- 

# JavaScript引擎升级：Nashorn

- jdk8 为java提供一个JavaScript引擎
- jdk9 包含一个用来解析Nashorn的ECMAScript

