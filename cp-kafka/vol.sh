# Create dirs for Kafka / ZK data.
sudo mkdir -p $PWD/vol1/zk-data
sudo mkdir -p $PWD/vol2/zk-txn-logs
sudo mkdir -p $PWD/vol3/kafka-data

# Make sure the user has the read and write permissions.
sudo chown -R 1000:1000 $PWD/vol1/zk-data
sudo chown -R 1000:1000 $PWD/vol2/zk-txn-logs
sudo chown -R 1000:1000 $PWD/vol3/kafka-data