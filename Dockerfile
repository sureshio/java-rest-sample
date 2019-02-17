FROM tomcat

#RUN mkdir /usr/local/tomcat/webapps/studentapp

COPY ./target/java_mongo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/studentapp.war
