package org.example;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

public class Subscription {
    public static final List<String> OPERATORS = List.of("=", "!=", ">", ">=", "<", "<=");

    // Thread-safe map for storing conditions
    private final Map<String, String> conditions = new ConcurrentHashMap<>();

    // Atomic variable to track the number of conditions added to the subscription
    private final AtomicInteger fieldCount = new AtomicInteger(0);

    // Thread-safe method to add a condition
    public void addCondition(String field, String operator, String value) {
        // Use putIfAbsent to ensure atomic insertion or update
        String condition = String.format("(%s,%s,%s)", field, operator, value);
        if (conditions.putIfAbsent(field, condition) == null) {
            // Increment field count only when a new condition is added
            fieldCount.incrementAndGet();
        }
    }

    // Checks if a field is already present in the conditions
    public boolean hasField(String field) {
        return conditions.containsKey(field);
    }

    // Checks if the subscription is empty (no conditions)
    public boolean isEmpty() {
        return fieldCount.get() == 0;  // Use AtomicInteger for thread-safe check
    }

    @Override
    public String toString() {
        // Use String.join to concatenate the condition values
        return String.join(";", conditions.values());
    }

    // Get all conditions for this subscription
    public Map<String, String> getConditions() {
        return conditions;
    }
}




//package org.example;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Subscription {
//    public static final List<String> OPERATORS = List.of("=", "!=", ">", ">=", "<", "<=");
//
//    // Regular HashMap since we don't need thread safety anymore
//    private final Map<String, String> conditions = new HashMap<>();
//
//    // Simple integer instead of AtomicInteger
//    private int fieldCount = 0;
//
//    // Method to add a condition
//    public void addCondition(String field, String operator, String value) {
//        String condition = String.format("(%s,%s,%s)", field, operator, value);
//        if (!conditions.containsKey(field)) {
//            conditions.put(field, condition);
//            fieldCount++;
//        }
//    }
//
//    // Checks if a field is already present in the conditions
//    public boolean hasField(String field) {
//        return conditions.containsKey(field);
//    }
//
//    // Checks if the subscription is empty (no conditions)
//    public boolean isEmpty() {
//        return fieldCount == 0;
//    }
//
//    @Override
//    public String toString() {
//        return String.join(";", conditions.values());
//    }
//
//    // Get all conditions for this subscription
//    public Map<String, String> getConditions() {
//        return conditions;
//    }
//}
