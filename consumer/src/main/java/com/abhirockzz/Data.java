package com.abhirockzz;

import java.util.HashMap;
import java.util.Map;

/**
 * static data for simulation
 * @author abhishekgupta
 */
public class Data {
    public static final Map<String, String> CUSTOMER_DATA = new HashMap<>();
    public static final Map<String, String> PRODUCT_DATA = new HashMap<>();

    static {
        CUSTOMER_DATA.put("customer-1", "Abhishek");
        CUSTOMER_DATA.put("customer-2", "Brad");
        CUSTOMER_DATA.put("customer-3", "Naomi");
        CUSTOMER_DATA.put("customer-4", "Kramer");
        CUSTOMER_DATA.put("customer-5", "Joey");
        CUSTOMER_DATA.put("customer-6", "Monica");
        CUSTOMER_DATA.put("customer-7", "Rachel");
        CUSTOMER_DATA.put("customer-8", "Chandler");
        CUSTOMER_DATA.put("customer-9", "Ross");
        CUSTOMER_DATA.put("customer-10", "Phoebe");

        PRODUCT_DATA.put("product-1", "iPhone");
        PRODUCT_DATA.put("product-2", "iPad");
        PRODUCT_DATA.put("product-3", "Apple Watch");
        PRODUCT_DATA.put("product-4", "Umbrella");
        PRODUCT_DATA.put("product-5", "Macbook Pro");
        PRODUCT_DATA.put("product-6", "Samsung Galaxy");
        PRODUCT_DATA.put("product-7", "Apples");
        PRODUCT_DATA.put("product-8", "T-shirt");
        PRODUCT_DATA.put("product-9", "Gatorade");
        PRODUCT_DATA.put("product-10", "Cornflakes");
    }
}