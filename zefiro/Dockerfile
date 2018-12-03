FROM tomcat:8.0.43-jre8
ADD target/zefiro.war /usr/local/tomcat/webapps/
VOLUME /usr/local/tomcat
EXPOSE 8080
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "run"]