# FROM openjdk:16-alpine3.13
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
# CMD ["java","-jar","/app.jar"]

FROM openjdk:16-alpine3.13
ARG JAR_FILE_DEMO=demo/target/*.jar
ARG JAR_FILE_AUTH=lib_auth/target/*.jar
ARG JAR_FILE_EMAIL=lib_email/target/*.jar
COPY ${JAR_FILE_DEMO} appDemo.jar
COPY ${JAR_FILE_AUTH} appAuth.jar
COPY ${JAR_FILE_EMAIL} appEmail.jar
CMD ["java","-jar","/appDemo.jar"]
CMD ["java","-jar","/appAuth.jar"]
CMD ["java","-jar","/appEmail.jar"]
