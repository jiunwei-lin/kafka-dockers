sudo docker stop centos-kafka
sudo docker rm centos-kafka
sudo docker run -dit --name centos-kafka \
  --privileged=true \
  -v $PWD/data:/tmp \
  -p 2181:2181 \
  -p 8083:8083 \
  -p 9092:9092 \
  -v /etc/localtime:/etc/localtime:ro \
  -t "centos-kafka"