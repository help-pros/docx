# oracle优化 

https://blog.csdn.net/tonghui_tonghui/article/details/76696260

- 选择最有效率的表名顺序

  - oracle的解析器按照从右到左的顺序处理from子句中的表名，from子句中写在最后的表（基础表 driving table）将被最先处理；

    所以，在from子句中包含多个表的情况下，你必须选择记录条数最少的表作为基础表

- where子句中的连接顺序

  - oracle采用自下而上的顺序解析where子句，根据这个原理，表之间的连接必须写在其他where条件之前，那么可以过滤掉最大数量记录的条件必须写在where子句的末尾

- select子句中避免使用“*”

  - oracle在解析的过程中，会将“*”依次转换成所有的列名，这个工作是通过查询数据字典完成的，这意味着将消耗更多的时间

- 减少访问数据库的次数

  - 解析SQL语句，估算索引的利用率，绑定变量，读数据库块等

- 在SQL *plus，SQL*froms和pro*c中重新设置arraysize参数

  - 可增加每次数据库访问的检索数据量，建议值为200

- 使用decode函数来减少处理时间

  - 使用decode函数可避免重复扫描相同记录或重复连接相同的表

- 整合简单、无关联的数据库访问

  - 如果你有几个简单的数据库查询语句，你可整合到一个查询中

- 删除重复记录

  - 最高效的删除重复记录方法：delete from emp e where w.rowid >(select min(x.rowid) from emp x where x.emp_no = e.emp_no)

- 用truncate 替代delete

- 尽量多使用commit

- 用where子句替换having子句

- j减少对表的查询

- 通过内部函数提高SQL效率

- 使用表的别名（alias）

- 用exist代替in、用not exist代替not in

- 识别“低效执行”的SQL语句

- 用索引提高效率

- 用exist替换distinct

- SQL语句用大写的

- 在java代码中尽量少用连接符“+”连接字符串

- 避免在索引列上使用not

- 避免在索引列上使用计算

- 用>=替代>

- 用union替换or（使用于索引列）

- 用in 替换or

- 避免在索引列上使用is null和is not null

- 总是使用索引的第一个列

- 用union-all替换union

- 用where替代order by

- 避免改变索引列的类型

- !=，||，+停用了索引

- 如果检索数据量超过30%的表中记录数，使用索引将没有显著的效率提高

- 避免使用耗费资源的操作

  - distinct、UNicon、minus、intersect、order by会启动SQL引擎，执行耗费资源

- 优化group by

- 

# 锁表

```sql
select session_id from v$locked_object;
SELECT sid, serial#, username, osuser FROM v$session where sid =3768;
SELECT SID FROM V$MYSTAT WHERE ROWNUM =1;

SELECT object_name, machine, s.sid, s.serial# 
FROM gv$locked_object l, dba_objects o, gv$session s 
WHERE l.object_id　= o.object_id 
AND l.session_id = s.sid; 

--alter system kill session 'sid, serial#'; 
ALTER system kill session '3768, 10751'; 

SELECT uo.OBJECT_NAME, s."SQL_ID", s."SQL_TEXT", sess."SID", sess."SERIAL#",s.*
FROM v$locked_object lo-- ON lo."SESSION_ID" = sess."SID"
JOIN user_objects uo ON uo.OBJECT_ID = lo."OBJECT_ID" AND uo.OBJECT_NAME = UPPER('tb_shop_store')
JOIN v$session sess ON lo."SESSION_ID" = sess."SID"
JOIN v$sql s ON sess."SQL_ID" = s."SQL_ID";



```

# 行列转换

**(使用 case when进行行列转换)**

1）、 动态：https://blog.csdn.net/u012366626/article/details/40054129

```sql
SELECT 'SELECT city_name "cityName",community_name "communityName",' ||
       listagg('MAX(case site_name when ''' || SN ||
               ''' then PV else 0 end)"' || SN || 'PV"',
               ',') within group(order by SN) || ',' || listagg('MAX(case site_name when ''' || SN || ''' then UV else 0 end)"' || SN || 'UV"', ',') within group(order by SN) || 'from tb_behavior_city_week c where c.city_name is not null and statis_date =(select max(statis_date) from tb_behavior_city_week) and CITY_NAME = ''广州市'' group by community_name,city_name order by city_name'
  FROM (select site_name SN
          from tb_behavior_city_week c
         where statis_date =
               (select max(statis_date) from tb_behavior_city_week)
         group BY site_name)

```



2）、静态：https://blog.csdn.net/liqfyiyi/article/details/7087466

```sql
SELECT city_name,
MAX(case site_name when '冰神卡' then PV  else 0 end) AS "冰神卡PV",
MAX(case site_name when '场地预定' then PV  else 0 end) AS "场地预定PV",
MAX(case site_name when '访问' then PV  else 0 end) AS "访问PV",
MAX(case site_name when '集市' then PV  else 0 end) AS "集市PV",
MAX(case site_name when '宽带安装' then PV  else 0 end) AS "宽带安装PV",
MAX(case site_name when '手机开门' then PV  else 0 end) AS "手机开门PV",
MAX(case site_name when '首页' then PV  else 0 end) AS "首页PV",
MAX(case site_name when '腾讯网卡' then PV  else 0 end) AS "腾讯网卡PV",
MAX(case site_name when '我的房屋' then PV  else 0 end) AS "我的房屋PV",
MAX(case site_name when '物业缴费' then PV  else 0 end) AS "物业缴费PV",
MAX(case site_name when '小区公告' then PV  else 0 end) AS "小区公告PV"
from tb_behavior_city_day c where c.city_name is not null and  statis_date =(select max(statis_date) from tb_behavior_city_day)
group by city_name;
```

在mybatis上的写法：

```xml
<select id="findAllBehaviorCity" resultType="String" parameterType="java.util.Map">
		  <![CDATA[ SELECT 'SELECT city_name "cityName",community_name "communityName",'||
       
        listagg('MAX(case site_name when '''||SN||''' then PV  else 0 end)"' || SN || 'PV"',
                ',') within group(order by SN)
        ||',' ||
        listagg('MAX(case site_name when '''||SN||''' then UV  else 0 end)"' || SN || 'UV"',
                ',') within group(order by SN)
      ||'from TB_BEHAVIOR_WEEK c where c.city_name is not null and  statis_date =(select max(statis_date) from TB_BEHAVIOR_WEEK) 
]]>
<include refid="Conditions_Selective"></include>
<![CDATA[
group by community_name,city_name order by city_name'
  FROM (select site_name SN
          from TB_BEHAVIOR_WEEK c
         where statis_date =
               (select max(statis_date) from TB_BEHAVIOR_WEEK)
         group BY site_name)]]>
	</select>
```

# 连接字符串函数

http://terryjs.iteye.com/blog/2210863

oracle连接字符串函数，wmsys.wm_concat和LISTAGG

# insertOrUpdate 

merge into



# 复杂SQL  

```sql
select to_char(systimestamp, 'yyyymmddhh24misssss')||to_char(mod(abs(dbms_random.random), 1000000), 'FM000009') ID
, SMARTUSERID RESIDENT_ID, COMMUNITY_ID, null ACTIVITY_ID, 1 CHANNEL, 1 IS_PERMIT, min(CLICK_TIME) CREATE_TIME, ''UPDATE_TIME
from tb_rpt_log_base_d 
where resource_id in ('300017','300008')
group by COMMUNITY_ID, SMARTUSERID
order by smartuserid

```

```sql
merge into sh_old_user@dblink_178_129_2 a
using (
select to_char(systimestamp, 'yyyymmddhh24misssss')||to_char(mod(abs(dbms_random.random), 1000000), 'FM000009') ID
, SMARTUSERID RESIDENT_ID, COMMUNITY_ID, null ACTIVITY_ID, 1 CHANNEL, 1 IS_PERMIT, min(CLICK_TIME) CREATE_TIME, ''UPDATE_TIME
from tb_rpt_log_base_d 
where resource_id in ('300017','300008')
group by COMMUNITY_ID, SMARTUSERID) b
on a.RESIDENT_ID = b.RESIDENT_ID and a.COMMUNITY_ID = b.COMMUNITY_ID
when not matched then
    insert values (b.ID, b.RESIDENT_ID, b.COMMUNITY_ID, b.ACTIVITY_ID, b.CHANNEL, b.IS_PERMIT, b.CREATE_TIME, b.UPDATE_TIME);
    
    
merge into sh_old_user a
using (
select to_char(systimestamp, 'yyyymmddhh24misssss')||to_char(mod(abs(dbms_random.random), 1000000), 'FM000009') ID
, SMARTUSERID RESIDENT_ID, COMMUNITY_ID, null ACTIVITY_ID, 1 CHANNEL, 1 IS_PERMIT,min(cast(to_timestamp(click_time, 'yyyy-mm-dd hh24:mi:ss.ff') as date))CREATE_TIME, ''UPDATE_TIME
from tb_rpt_log_base_d@dblink_208_42 
where resource_id in ('300017','300008')
group by COMMUNITY_ID, SMARTUSERID) b
 on (a.RESIDENT_ID = b.RESIDENT_ID and a.COMMUNITY_ID = b.COMMUNITY_ID)
when not matched then
    insert values (b.ID, b.RESIDENT_ID, b.COMMUNITY_ID, b.ACTIVITY_ID, b.CHANNEL, b.IS_PERMIT, b.CREATE_TIME, b.UPDATE_TIME);    
```



# 时间函数：

https://www.cnblogs.com/huanghongbo/p/8109420.html

```sql
cast(to_timestamp(click_time, 'yyyy-mm-dd hh24:mi:ss.ff') as date)
---字符串转为timestamp类型，用to_timestamp
---将timestamp类型转换成date类型，用cast
```

# update 大表的操作 

先创新表，再更新

```sql
create table WO_USER_INFO_ne as 
select USERID, USERNAME, NICKNAME, REGTIME, REGIP, REGTYPE, LASTLOGINTIME, LOGINIP, MOBILE, EMAIL, SEX, USERTYPE, USERROLE, STATUS, TRUENAME, USERLOGO, REMARK, RUID, PASSMW, SHARENUM, DOWNNUM, BIRTHDAY, REG_RESOURCE, UCENTER_REG_SOURCE, UPDATE_TIME, PWD, ISWOUSER, 0 IS_LOG_OFF, LOG_OFF_TIME
from wo_user_info a
where a.userid in (
select WO17_USER_ID from tb_nethome_user_info
);

ALTER TABLE WO_USER_INFO_ne ADD CONSTRAINT "PK_USER_INFO_USERID_N" PRIMARY KEY ("USERID");

ALTER TABLE WO_USER_INFO_ne ADD CONSTRAINT "UQ_USER_INFO_MOBILE_N" UNIQUE ("MOBILE")
 
   COMMENT ON COLUMN WO_USER_INFO_ne."IS_LOG_OFF" IS '用户注销 0：正常，1：注销';
 
   COMMENT ON COLUMN WO_USER_INFO_ne."LOG_OFF_TIME" IS '用户注销时间';


alter table WO_USER_INFO_NE modify is_log_off default 0

select * from WO_USER_INFO_ne；

rename wo_user_info to wo_user_info_back;
rename WO_USER_INFO_NE to wo_user_info;

```



# oracle修改表字段默认值

alter table 表名 modify 字段名 default 默认值;