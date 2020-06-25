package com.abhirockzz;

/**
 * POJO received from Kafka
 * 
 * @author abhishekgupta
 */
public class OrderEvent {
    private String orderId;
    private String customerId;
    private String product;

    public OrderEvent(String orderId, String customerName, String product) {
        this.orderId = orderId;
        this.customerId = customerName;
        this.product = product;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerId = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderEvent [customerId=" + customerId + ", orderId=" + orderId + ", product=" + product + "]";
    }

}