curl -X DELETE 'http://127.0.0.1:8083/connectors/test-upload-source-mysql'

curl -X POST -H 'Content-Type: application/json' -i 'http://172.21.42.7:8083/connectors' \
--data \
'{
"name":"test-upload-source-mysql",
"config":{
    "connector.class":"io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url":"jdbc:mysql://172.21.42.7:3306/source_database?user=root&password=root",
    "table.whitelist":"source_users",
    "incrementing.column.name": "id",
    "mode":"incrementing",
    "topic.prefix": "test-mysql-"}
}'