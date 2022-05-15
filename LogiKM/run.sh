sudo docker stop logikm-mysql
sudo docker rm logikm-mysql
sudo docker run --name logikm-mysql -d \
  -p 3306:3306 \
  -v $PWD/data:/var/lib/mysql \
  -t logikm-mysql:5.7.37

sudo docker stop logikm
sudo docker rm logikm
sudo docker run --name logikm -d --link logikm-mysql \
  -e LOGI_MYSQL_HOST="192.168.2.102" \
  -e LOGI_MYSQL_PORT="3306" \
  -e LOGI_MYSQL_DATABASE="logi_kafka_manager" \
  -e LOGI_MYSQL_USER="root" \
  -e LOGI_MYSQL_PASSWORD="root" \
  -p 8080:8080 \
  -t logikm