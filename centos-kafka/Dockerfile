FROM centos:latest

RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-* \
&& sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

RUN yum update -y \
&& yum install java-11-openjdk-devel vim wget git unzip -y \
&& cd /opt \
&& wget https://downloads.apache.org/kafka/3.1.0/kafka_2.13-3.1.0.tgz \
&& wget https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.4.1/confluentinc-kafka-connect-jdbc-10.4.1.zip \
&& wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.20/mysql-connector-java-8.0.20.jar \
&& mkdir -p /usr/local/kafka-server \
&& cd /usr/local/kafka-server \
&& tar -xvzf /opt/kafka_2.13-3.1.0.tgz --strip 1 \
&& unzip /opt/confluentinc-kafka-connect-jdbc-10.4.1.zip -d /usr/local \
&& mv /usr/local/confluentinc-kafka-connect-jdbc-10.4.1 /usr/local/kafka-connect \
&& mv /opt/mysql-connector-java-8.0.20.jar /usr/local/kafka-connect/lib

ENV JAVA_HOME=/usr/lib/jvm/jre-11-openjdk

COPY kafka_2.13-3.1.0/config/server.properties /usr/local/kafka-server/config/
COPY kafka_2.13-3.1.0/config/zookeeper.properties /usr/local/kafka-server/config/
COPY kafka_2.13-3.1.0/config/connect-distributed.properties /usr/local/kafka-server/config/

COPY zookeeper.service /etc/systemd/system/
COPY kafka.service /etc/systemd/system/
COPY kafka-connect.service /etc/systemd/system/

RUN systemctl enable zookeeper \
&& systemctl enable kafka \
&& systemctl enable kafka-connect

EXPOSE 2181
EXPOSE 8083
EXPOSE 9092

ENTRYPOINT ["/usr/sbin/init"] 
