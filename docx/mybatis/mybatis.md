# 使用注解配置SQL映射器 

## 映射语句 

- @Insert
- @Update
- @Delete
- @SelectStatements

## 动态SQL

- @SelectProvider
- @InsertProvider
- @UpdateProvider
- @DeleteProvider

## 结果映射 

- @Result

- @ResultMap

- ```java
  @Select({"select id, name, class_id from my_student"})
  @Results(id="studentMap", value={
      @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
      @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
      @Result(column="class_id ", property="classId", jdbcType=JdbcType.INTEGER)
  })
  List<Student> selectAll();
   
  @Select({"select id, name, class_id from my_student where id = #{id}"})
  @ResultMap(value="studentMap")
  Student selectById(integer id);
  ```


## 分页查询 

```java
   /**
     * 分页查询
     * @param lastname
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_shop_brand b WHERE isdeleted = 0 and audit_status=?1  ORDER BY update_time desc nulls last,audit_time desc NULLS LAST",
            countQuery = "SELECT count(*) FROM  tb_shop_brand b WHERE isdeleted = 0 and audit_status=?1", nativeQuery = true)
    Page<TbShopBrand> findByPage(int auditStatus,Pageable pageable);
```



# mybatis-plus 

- > 特性：
  >
  > - 支持lambda
  > - 支持注解自动生成：支持多达4种主键策略（内含分布式唯一ID生成器-Sequence）
  > - 内置代码生成器：采用代码或maven插件可快速生成Mapper、Model、service、controller层代码，支持模板引擎，更有超多自定义配置
  > - 内置分页插件：写分页等同于普通List查询
  > - 分页插件支持多种数据库：支持mysql、oracle等
  > - 内置性能分析插件：可输出SQL语句及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
  > - 内置全局拦截插件：提供全部delete、update操作智能分析阻断，也可自定义拦截规则，预防误操作

# 坑

## mybatis-plus使用jdk8的LocalDateTime 查询时报错

- **mybatis-plus版本降至3.1.0或以下即可**





# mybatis中的#和$的区别

- >  将传入的数据都当成一个字符串，会对自动传入的数据加一个双引号。如：order by #user_id#，如果传入的值是111,那么解析成sql时的值为order by "111", 如果传入的值是id，则解析成的sql为order by "id"

- > $将传入的数据直接显示生成在sql中。如：order by $user_id$，如果传入的值是111,那么解析成sql时的值为order by user_id, 如果传入的值是id，则解析成的sql为order by id