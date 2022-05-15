# Run ZK with user 12345 and volumes mapped to host volumes
sudo docker stop zk-vols
sudo docker rm zk-vols
sudo docker run -d \
  --name=zk-vols \
  --privileged=true \
  --net=host \
  --user=12345 \
  -e ZOOKEEPER_TICK_TIME=2000 \
  -e ZOOKEEPER_CLIENT_PORT=32181 \
  -v $PWD/vol1/zk-data:/var/lib/zookeeper/data \
  -v $PWD/vol2/zk-txn-logs:/var/lib/zookeeper/log \
  confluentinc/cp-zookeeper:7.1.1

sudo docker stop kafka-vols
sudo docker rm kafka-vols
sudo docker run -d \
  --name=kafka-vols \
  --privileged=true \
  --net=host \
  --user=12345 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=localhost:32181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:39092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -v $PWD/vol3/kafka-data:/var/lib/kafka/data \
  confluentinc/cp-kafka:7.1.1