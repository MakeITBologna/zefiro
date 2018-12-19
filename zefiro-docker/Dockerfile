FROM maven:3.5-jdk-8-alpine

COPY ./zefiro-sdk/src /opt/workspace/zefiro-sdk/src
COPY ./zefiro-sdk/pom.xml /opt/workspace/zefiro-sdk/pom.xml

COPY ./zefiro/src /opt/workspace/zefiro/src
COPY ./zefiro/pom.xml /opt/workspace/zefiro/pom.xml
WORKDIR /opt/workspace/zefiro-sdk
RUN mvn install
WORKDIR /opt/workspace/zefiro
RUN mvn clean package -Pdocker


FROM tomcat:8.0.43-jre8
COPY --from=0 /opt/workspace/zefiro/target/zefiro.war /usr/local/tomcat/webapps/

EXPOSE 8081
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
RUN sed -i -e 's/8080/8081/' /usr/local/tomcat/conf/server.xml
CMD ["catalina.sh", "run"]