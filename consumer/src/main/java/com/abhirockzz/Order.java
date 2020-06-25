package com.abhirockzz;

import java.util.UUID;

/**
 * POJO to be persisted in CosmosDB
 * 
 * @author abhishekgupta
 */
public class Order {

    // cosmos db needs "id"
    private String id;
    private String orderId;
    private String customerName;
    private String customerId;
    private String productName;

    private String partition;
    private String offset;

    public Order(OrderEvent oe, String partition, String offset) {
        this.id = UUID.randomUUID().toString();
        this.orderId = oe.getOrderId();

        this.customerName = Data.CUSTOMER_DATA.get(oe.getCustomerId());
        this.customerId = oe.getCustomerId();

        this.productName = Data.PRODUCT_DATA.get(oe.getProduct());

        // kafka metadata added, just for demonstration
        this.partition = partition;
        this.offset = offset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderId=" + orderId + ", customerName=" + customerName + ", customerId="
                + customerId + ", productName=" + productName + '}';
    }
}
