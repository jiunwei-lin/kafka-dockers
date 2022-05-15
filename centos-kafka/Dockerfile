FROM centos:latest

RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-* \
&& sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

RUN yum update -y \
&& yum install java-11-openjdk-devel vim wget git unzip -y \
&& cd /opt \
&& wget https://downloads.apache.org/kafka/3.1.0/kafka_2.13-3.1.0.tgz \
&& mkdir /usr/local/kafka-server \
&& cd /usr/local/kafka-server \
&& tar -xvzf /opt/kafka_2.13-3.1.0.tgz --strip 1

ENV JAVA_HOME=/usr/lib/jvm/jre-11-openjdk

COPY kafka_2.13-3.1.0/config/server.properties /usr/local/kafka-server/config/
COPY kafka_2.13-3.1.0/config/zookeeper.properties /usr/local/kafka-server/config/
COPY kafka.service /etc/systemd/system/
COPY zookeeper.service /etc/systemd/system/

RUN systemctl enable zookeeper \
&& systemctl enable kafka

EXPOSE 2181
EXPOSE 9092

CMD /usr/sbin/init