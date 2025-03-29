./mvnw clean package
cp /Users/ethan.lee781/MyWorks/centerpoint/server/centerpoint/oauth2-server/target/oauth2-server-0.0.1-SNAPSHOT.war /Users/ethan.lee781/MyWorks/apache-tomcat-8.5.39/webapps/oauth2-server.war
cp /Users/ethan.lee781/MyWorks/centerpoint/server/centerpoint/api-server/target/api-server-0.0.1-SNAPSHOT.war  /Users/ethan.lee781/MyWorks/apache-tomcat-8.5.39/webapps/api-server.war

/Users/ethan.lee781/MyWorks/apache-tomcat-8.5.39/bin/shutdown.sh
/Users/ethan.lee781/MyWorks/apache-tomcat-8.5.39/bin/startup.sh

