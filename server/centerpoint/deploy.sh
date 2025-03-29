./mvnw clean package
scp -i ~/.ssh/mhb8436.pem /Users/ethan.lee781/MyWorks/centerpoint/server/centerpoint/oauth2-server/target/oauth2-server-0.0.1-SNAPSHOT.war ubuntu@api.pizzastudio.app:~/oauth2-server.war
scp -i ~/.ssh/mhb8436.pem /Users/ethan.lee781/MyWorks/centerpoint/server/centerpoint/api-server/target/api-server-0.0.1-SNAPSHOT.war ubuntu@api.pizzastudio.app:~/api-server.war


