set GRAALVM_HOME=C:\Users\Administrator\.jdks\graalvm-jdk-21.0.3
set JAVA_HOME=%GRAALVM_HOME%
set Path=%GRAALVM_HOME%\bin;%Path%
mvn native:compile -DskipTests "-Dmaven.repo.local=F:\Program Files\apache-maven-3.9.3\repo3" -Pnative
