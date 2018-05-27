mvn clean && mvn package -Pdev
rsync -avzP target/caishunpay-user.war root@hjdbuy.com:/home/tomcat/apache-tomcat-default/webapps
