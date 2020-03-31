# 安装教程 

 https://naver.github.io/pinpoint/installation.html 

 https://www.cnblogs.com/qiumingcheng/p/6899822.html 

- 安装JDK1.8

- 安装HBASE

- 安装pinpoint-collector

- 安装pinpoint-web

- 部署pp-agent采集监控数据

  - >  pinpoint.config 
    >
    > profiler.collector.ip=192.168.245.136  

  - >  //tomcat
    >
    >  vi catalina.sh  
    >
    >  CATALINA_OPTS="$CATALINA_OPTS -javaagent:/data/pp-agent/pinpoint-bootstrap-1.5.2.jar"
    >
    >  CATALINA_OPTS="$CATALINA_OPTS -Dpinpoint.agentId=pp20161122"
    >
    >  CATALINA_OPTS="$CATALINA_OPTS -Dpinpoint.applicationName=MyTestPP
>
    >  //jar
>
    >  java -javaagent:F:\Download\pinpoint-agent-1.8.5\pinpoint-bootstrap-1.8.5.jar -Dpinpoint.applicationName=test01 -Dpinpoint.agentId=test01 -jar F:\Download\demo\target\demo-0.0.1-SNAPSHOT.jar --server.port=9080
    >
    >  
    >
    >  pinpoint-collector-1.7.3.war(用于接收agent端数据)   
    >
    >  pinpoint的web展示端，逻辑控制机，以及Hbase存储
    >  pinpoint-web-1.7.3.war（用于数据可视化）	
    >  xxx.xxx.xxx.249	    Centos7	          
    >
    >  pinpoint-agent(采集信息端，作为探针)	
    >  
    
    
    
    
    
    