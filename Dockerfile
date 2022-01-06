FROM openjdk:11
WORKDIR /
ADD target/identityservice.jar //
EXPOSE 8080
ENTRYPOINT [ "java", "-Dspring.profiles.active=default", "-jar", "/identityservice.jar"]
