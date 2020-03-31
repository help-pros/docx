# SQL打印设置

```xml
	<!--显示日志-->
	<logger name="org.springframework.jdbc.core" additivity="false" level="DEBUG" >
		<appender-ref ref="STDOUT" />
		<appender-ref ref="ERROR_LOG" />
	</logger>
```

```yaml
logging:
  config: classpath:log/logback-test.xml
```

