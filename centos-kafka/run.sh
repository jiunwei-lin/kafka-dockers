sudo docker stop centos-kafka
sudo docker rm centos-kafka
sudo docker run -dit --name centos-kafka \
  --privileged=true \
  -p 2181:2181 \
  -p 9092:9092 \
  -v /etc/localtime:/etc/localtime:ro \
  -t "centos-kafka"