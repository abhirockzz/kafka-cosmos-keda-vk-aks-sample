export EVENTHUBS_BROKER=<replace with eventhubs namespace>.servicebus.windows.net:9093
export EVENTHUBS_TOPIC=<replace with eventhubs topic name>
export EVENTHUBS_CONNECTION_STRING="<replace with eventhubs connection string>"

go run eventhubs-kafka-producer/producer.go