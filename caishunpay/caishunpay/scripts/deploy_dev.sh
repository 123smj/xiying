mvn clean && mvn package -Pdev
rsync -avzP target/caishunpay-web.war root@hjdbuy.com:/home/tomcat/apache-tomcat-default/webapps
