# Use a base JDK image
FROM openjdk:17-jdk

# Add your built jar to the container
COPY target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]