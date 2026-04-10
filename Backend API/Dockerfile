# 1. Build Main.java with JDK
FROM eclipse-temurin:21-jdk-alpine AS builder
COPY Main.java /usr/src/Main.java
WORKDIR /usr/src/
RUN javac Main.java

# 2. Run java code with the lean JRE
FROM eclipse-temurin:21-jre-alpine
# Copy resource from previous stage
COPY --from=builder /usr/src/Main.class /usr/src/Main.class
WORKDIR /usr/src/
# Run java code with the JRE
ENTRYPOINT ["java", "Main"]
