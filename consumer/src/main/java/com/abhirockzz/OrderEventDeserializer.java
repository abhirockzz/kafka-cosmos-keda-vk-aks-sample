package com.abhirockzz;

import com.google.gson.Gson;
import java.io.IOException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * convert JSON from Kafka to an OrderEvent POJO
 * 
 * @author abhishekgupta
 */
public class OrderEventDeserializer implements Deserializer<OrderEvent> {

    static private Gson gson = new Gson();

    @Override
    public OrderEvent deserialize(String s, byte[] bytes) {
        OrderEvent oe = gson.fromJson(new String(bytes), OrderEvent.class);
        return oe;
    }

}
