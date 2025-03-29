kill -9 `ps -ef | grep java | grep api-server | awk '{print $2}'`
nohup ./mvnw clean -pl api-server spring-boot:run &> api-server.out&
kill -9 `ps -ef | grep java | grep oauth2-server | awk '{print $2}'`
nohup ./mvnw clean -pl oauth2-server spring-boot:run &> oauth2-server.out&

