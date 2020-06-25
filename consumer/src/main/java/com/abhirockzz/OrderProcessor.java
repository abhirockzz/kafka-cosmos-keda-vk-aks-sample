package com.abhirockzz;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.models.CosmosItemResponse;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

/**
 * Process order event from Kafka and persist them to cosmosdb. it commits
 * offsets manually only after a record is persisted successfully
 * 
 * @author abhishekgupta
 */
public class OrderProcessor implements Runnable {

    private KafkaConsumer<String, OrderEvent> consumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private CosmosClient cosmosClient;
    String databaseName = null;
    String collectionName = null;

    public OrderProcessor(KafkaConsumer<String, OrderEvent> consumer, CosmosClient cosmosClient, String databaseName,
            String collectionName) {
        this.consumer = consumer;
        this.cosmosClient = cosmosClient;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    public void run() {
        System.out.println("Started Order Processor");

        try {

            while (!closed.get()) {
                System.out.println("Polling Kafka for Orders");

                ConsumerRecords<String, OrderEvent> records = consumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, OrderEvent> record : records) {
                    System.out.printf("Got order: partition = %d, offset = %d, value = %s%n", record.partition(),
                            record.offset(), record.value());
                    Order order = new Order(record.value(), String.valueOf(record.partition()),
                            String.valueOf(record.offset()));

                    try {
                        System.out.println("Saving order ID " + record.key());

                        cosmosClient.getDatabase(this.databaseName).getContainer(this.collectionName).createItem(order);
                        System.out.println("Saved order successfully");

                    } catch (Exception e) {
                        Logger.getLogger(OrderProcessor.class.getName()).log(Level.SEVERE,
                                "Failed to save order " + record.key(), e);
                    }

                    /*
                     * Observable<ResourceResponse<Document>> ob =
                     * cosmos.createDocument(collectionLink, order, new RequestOptions(), true);
                     * ob.single().subscribe(rr -> { System.out.println("Saved order ID " +
                     * record.key()); this.consumer.commitSync();
                     * System.out.println("Committed offset.."); done.countDown(); }, error -> {
                     * System.err.println("Failed to save Order info to Cosmos DB: " +
                     * error.getMessage()); done.countDown(); });
                     * 
                     * try { // we wait on purpose to allow offset to be committed after record is
                     * saved to // cosmosdb done.await(); } catch (InterruptedException ex) {
                     * Logger.getLogger(OrderProcessor.class.getName()).log(Level.SEVERE, null, ex);
                     * }
                     */
                }
            }

        } catch (WakeupException e) {
            if (!closed.get()) {
                throw e;
            }
        } catch (Throwable e) {
            Logger.getLogger(OrderProcessor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            consumer.close();
        }

    }

    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
        cosmosClient.close();
        System.out.println("shutdown...");
    }

}
