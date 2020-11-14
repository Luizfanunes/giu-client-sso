call mvn package -U
call mvn install:install-file -Dfile=./target/giu-client-sso-0.1.jar -DpomFile=./pom.xml