package com.abhirockzz;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author abhishekgupta
 */
public class App {

    private static final String EVENTHUBS_KAFKA_ENDPOINT_ENVVAR = "KAFKA_BOOTSTRAP_SERVER";
    private static final String EVENTHUBS_CONNECTION_STRING_ENVVAR = "EVENTHUBS_CONNECTION_STRING";
    private static final String EVENTHUBS_CONSUMER_GROUP_ENVVAR = "EVENTHUBS_CONSUMER_GROUP";
    private static final String EVENTHUBS_TOPIC_ENVVAR = "EVENTHUBS_TOPIC";

    private static final String COSMOSDB_ENDPOINT_ENVVAR = "COSMOSDB_ENDPOINT";
    private static final String COSMOSDB_KEY_ENVVAR = "COSMOSDB_KEY";
    private static final String COSMOSDB_DBNAME_ENVVAR = "COSMOSDB_DBNAME";
    private static final String COSMOSDB_COLLECTION_ENVVAR = "COSMOSDB_COLLECTION";

    public static void main(String[] args) throws Exception {
        String kafkaServer = System.getenv(EVENTHUBS_KAFKA_ENDPOINT_ENVVAR);

        if (kafkaServer == null) {
            throw new Exception("Missing environment variable " + EVENTHUBS_KAFKA_ENDPOINT_ENVVAR);
        }

        String eventHubsConnectionStr = System.getenv(EVENTHUBS_CONNECTION_STRING_ENVVAR);

        if (eventHubsConnectionStr == null) {
            throw new Exception("Missing environment variable " + EVENTHUBS_CONNECTION_STRING_ENVVAR);
        }

        String ehConsumerGroup = System.getenv(EVENTHUBS_CONSUMER_GROUP_ENVVAR);

        if (ehConsumerGroup == null) {
            throw new Exception("Missing environment variable " + EVENTHUBS_CONSUMER_GROUP_ENVVAR);
        }

        String ehTopic = System.getenv(EVENTHUBS_TOPIC_ENVVAR);

        if (ehTopic == null) {
            throw new Exception("Missing environment variable " + EVENTHUBS_TOPIC_ENVVAR);
        }

        String cosmosDBEndpoint = System.getenv(COSMOSDB_ENDPOINT_ENVVAR);

        if (cosmosDBEndpoint == null) {
            throw new Exception("Missing environment variable " + COSMOSDB_ENDPOINT_ENVVAR);
        }
        String cosmosDBKey = System.getenv(COSMOSDB_KEY_ENVVAR);

        if (cosmosDBKey == null) {
            throw new Exception("Missing environment variable " + cosmosDBKey);
        }

        String cosmosDBName = System.getenv(COSMOSDB_DBNAME_ENVVAR);

        if (cosmosDBName == null) {
            throw new Exception("Missing environment variable " + COSMOSDB_DBNAME_ENVVAR);
        }

        String cosmosCollName = System.getenv(COSMOSDB_COLLECTION_ENVVAR);

        if (cosmosCollName == null) {
            throw new Exception("Missing environment variable " + COSMOSDB_COLLECTION_ENVVAR);
        }

        /*
         * System.out.println("kafkaServer == " + kafkaServer);
         * System.out.println("eventHubsConnectionStr == " + eventHubsConnectionStr);
         * System.out.println("ehConsumerGroup == " + ehConsumerGroup);
         * System.out.println("ehTopic == " + ehTopic);
         * System.out.println("cosmosDBEndpoint == " + cosmosDBEndpoint);
         * System.out.println("cosmosDBKey == " + cosmosDBKey);
         * System.out.println("cosmosDBName == " + cosmosDBName);
         * System.out.println("cosmosCollName == " + cosmosCollName);
         */

        KafkaConsumer<String, OrderEvent> kc = createKafkaConsumer(kafkaServer, eventHubsConnectionStr, ehConsumerGroup,
                ehTopic);

        CosmosClient cosmosClient = connectToCosmosDB(cosmosDBEndpoint, cosmosDBKey);
        OrderProcessor orderProcessor = new OrderProcessor(kc, cosmosClient, cosmosDBName, cosmosCollName);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(orderProcessor);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                orderProcessor.shutdown();
                es.shutdownNow();
            }
        }));
    }

    public static KafkaConsumer<String, OrderEvent> createKafkaConsumer(String kafkaServer,
            String eventHubsConnectionStr, String ehConsumerGroup, String topic) {
        Properties props = new Properties();

        props.setProperty("bootstrap.servers", kafkaServer);
        props.setProperty("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\""
                        + eventHubsConnectionStr + "\";");
        props.setProperty("security.protocol", "SASL_SSL");
        props.setProperty("sasl.mechanism", "PLAIN");

        props.setProperty("group.id", ehConsumerGroup);
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", OrderEventDeserializer.class.getName());

        KafkaConsumer<String, OrderEvent> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

    public static CosmosClient connectToCosmosDB(String cosmosDBEndpoint, String cosmosDBKey) throws Exception {
        System.out.println("Connecting to CosmosDB");

        CosmosClient client = new CosmosClientBuilder().endpoint(cosmosDBEndpoint).key(cosmosDBKey)
                .consistencyLevel(ConsistencyLevel.EVENTUAL).buildClient();

        return client;
    }
}
