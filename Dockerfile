# build base image
FROM maven:3-openjdk-11-slim as maven

# copy pom.xml
COPY ./pom.xml ./pom.xml

# copy src files
COPY ./src ./src

# build
RUN mvn package

# final base image
FROM openjdk:11-jre-slim

# set deployment directory
WORKDIR /EIDER

# copy over the built artifact from the maven image
COPY --from=maven /target/eider*.jar ./EIDER.jar

#Settings
ENV LOGGING_LEVEL_TAMU='INFO'
ENV SERVER_PORT='9000'
ENV DATABASE_URL='jdbc:postgresql://host.docker.internal:5432/eider'
ENV DATABASE_DDL_AUTO='create-drop'
ENV DATABASE_PLATFORM='postgres'
ENV DATABASE_DRIVER_CLASS_NAME='org.postgresql.Driver'
ENV DATABASE_JPA_PLATFORM='org.hibernate.dialect.PostgreSQLDialect'
ENV DATABASE_USERNAME='spring'
ENV DATABASE_PASSWORD='spring'
ENV APP_USERNAME='admin'
ENV APP_PASSWORD='admin'

#expose port
EXPOSE ${SERVER_PORT}

#run java command
CMD java -jar ./EIDER.jar --logging.level.org.tamu=${LOGGING_LEVEL_TAMU} \
  --server.port=${SERVER_PORT} --spring.datasource.url=${DATABASE_URL} \
  --spring.jpa.hibernate.ddl-auto=${DATABASE_DDL_AUTO} --spring.sql.init.platform=${DATABASE_PLATFORM} \
  --spring.datasource.driver-class-name=${DATABASE_DRIVER_CLASS_NAME} --spring.jpa.database-platform=${DATABASE_JPA_PLATFORM} \
  --spring.datasource.username=${DATABASE_USERNAME} --spring.datasource.password=${DATABASE_PASSWORD} \
  --app.username=${APP_USERNAME} --app.password=${APP_PASSWORD}