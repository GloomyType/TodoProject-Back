# 서버를 구동 Java 이미지 선택
FROM openjdk:11-jdk

# JAR_FIL 이름으로 빌드된 jar 파일 지정
ARG JAR_FILE=./build/libs/*.jar

# 지정한 jar 파일을 app.jar라는 이름으로 Docker Container에 추가
ADD ${JAR_FILE} /app.jar

# app.jar 파일 실행
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]