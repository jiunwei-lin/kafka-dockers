curl -X DELETE 'http://127.0.0.1:8083/connectors/test-download-to-mysql'

curl -X POST -H 'Content-Type: application/json' -i 'http://172.21.42.7:8083/connectors' \
--data \
'{"name":"test-download-to-mysql","config":{
"connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
"connection.url":"jdbc:mysql://172.21.42.7:3306/target_database",
"connection.user":"root",
"connection.password":"root",
"topics":"test-mysql-source_users",
"auto.create":"false",
"insert.mode": "upsert",
"pk.mode":"record_value",
"pk.fields":"id",
"table.name.format": "target_users"}}'