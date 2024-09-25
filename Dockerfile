# COMPOSE
# BASE IMAGE
FROM maven:3.9.8-eclipse-temurin-21 AS build

# CONFIGURE TEMPORARY VOLUME
WORKDIR /opt/app
COPY . /opt/app
RUN mvn clean package -DskipTests

# BASE IMAGE
FROM eclipse-temurin:21-jre-alpine

# CONFIGURE FINAL IMAGE
WORKDIR /opt/app
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar

# SET TIMEZONE
ENV TZ=America/Sao_Paulo
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo "$TZ" > /etc/timezone

# ENVIRONMENT VARIABLES
ENV PROFILE=prd

# PORT
EXPOSE 8081

# INIT
ENTRYPOINT ["sh", "-c", "java -Duser.timezone=America/Sao_Paulo -Dspring.profiles.active=$PROFILE -jar /opt/app/app.jar"]



# ---
# # PROD
# # BASE IMAGE
# FROM eclipse-temurin:21-alpine
#
# # CONFIGURE TEMPORARY VOLUME
# VOLUME /tmp
#
# # SET TIMEZONE
# ENV TZ=America/Sao_Paulo
# RUN apk add --no-cache tzdata && \
#     cp /usr/share/zoneinfo/$TZ /etc/localtime && \
#     echo "$TZ" > /etc/timezone
#
# # ENVIRONMENT
# # ENV JWT_SECRET="123"
# # ENV ROLE_SECRET="321"
#
# # PORT
# EXPOSE 8080
#
# # JAR FILE
# ARG JAR_FILE=target/heimd4ll_backend-0.0.1-SNAPSHOT.jar
#
# # ADD JAR TO CONTAINER
# ADD ${JAR_FILE} app.jar
#
# # INIT
# ENTRYPOINT ["java","-Duser.timezone=America/Sao_Paulo","-jar","/app.jar"]