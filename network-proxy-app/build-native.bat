set GRAALVM_HOME=H:\Program Files\graalvm-jdk-17.0.9+11.1
set JAVA_HOME=%GRAALVM_HOME%
set Path=%GRAALVM_HOME%\bin;%Path%
mvn native:compile -DskipTests "-Dmaven.repo.local=F:\Program Files\apache-maven-3.9.3\repo3" -Pnative
