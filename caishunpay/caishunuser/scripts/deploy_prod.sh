mvn clean && mvn package -Pprod
rsync -avzP target/caishunpay-user.war root@gateway.ykbpay.com:/home/tomcat/apache-tomcat-default/webapps
