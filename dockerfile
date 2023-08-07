FROM tomcat:9.0

MAINTAINER DTEAPI

COPY DTE_APIS/target/DTE_APIS-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/