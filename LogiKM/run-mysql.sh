sudo docker stop logikm-mysql
sudo docker rm logikm-mysql
sudo docker run --name logikm-mysql -d \
  -p 3306:3306 \
  -v data:/var/lib/mysql \
  -t logikm-mysql:5.7.37
