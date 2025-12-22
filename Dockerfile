FROM amazoncorretto:17
MAINTAINER org.ilmi

COPY target/eposku-server-0.0.1-SNAPSHOT.jar eposku-server-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/eposku-server-0.0.1-SNAPSHOT.jar"]