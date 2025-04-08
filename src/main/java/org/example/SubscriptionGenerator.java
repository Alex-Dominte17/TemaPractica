package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SubscriptionGenerator {

    public static List<Subscription> generateSubscriptions(int count, Map<String, Double> fieldFrequencies, Map<String, Double> equalityMinFreq) {
        // Use CopyOnWriteArrayList for thread-safe list manipulation
        List<Subscription> subscriptions = new ArrayList<>(count);
        for (int i = 0; i < count; i++) subscriptions.add(new Subscription());

        // Compute required field counts
        Map<String, Integer> requiredCounts = computeExactCounts(count, fieldFrequencies);
        Map<String, Integer> equalityCounts = new ConcurrentHashMap<>(computeExactCounts(count, equalityMinFreq));

        System.out.println(requiredCounts);
        List<String> allFields = new ArrayList<>(fieldFrequencies.keySet());

        // Assign one random field to each subscription, and decrement counts
        for (Subscription sub : subscriptions) {
            String field;
            do {
                field = RandomUtils.randomElement(allFields);
            } while (requiredCounts.getOrDefault(field, 0) <= 0); // Ensure it's a field with quota left

            // Decide operator
            String operator;
            if (equalityCounts.getOrDefault(field, 0) > 0) {
                operator = "=";
                equalityCounts.computeIfPresent(field, (k, v) -> v - 1);
            } else if (field.equals("direction") || field.equals("date") || field.equals("city")) {
                operator = RandomUtils.randomElement(List.of("=", "!="));
            } else {
                operator = RandomUtils.randomElement(Subscription.OPERATORS);
            }

            // Assign the condition and decrease the required count
            String value = generateValueForField(field);
            sub.addCondition(field, operator, value);
            requiredCounts.computeIfPresent(field, (k, v) -> v - 1);
        }
//        System.out.println(requiredCounts);
        // Executor to process subscriptions in parallel
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        // Process each field in parallel
        for (String field : requiredCounts.keySet()) {
            int fieldCount = requiredCounts.get(field);
//            System.out.println("Field: " + field + "field count: " + fieldCount);
            futures.add(executor.submit(() -> {
                List<Integer> indices = getShuffledIndices(count, subscriptions);
                int assigned = 0;
                boolean ok = true;
                for (int idx = 0; idx < count; idx++) {
                    Subscription sub = subscriptions.get(indices.get(idx));

                    // Add condition only if the field hasn't been set already
                    if (!sub.hasField(field)) {
                        String operator;

                        if (equalityCounts.getOrDefault(field, 0) > 0) {
                            operator = "=";
                            equalityCounts.computeIfPresent(field, (k, v) -> v - 1);
                        } else if (field.equals("direction") || field.equals("date") || field.equals("city")) {
                            operator = RandomUtils.randomElement(List.of("=", "!="));
                        } else {
                            operator = RandomUtils.randomElement(Subscription.OPERATORS);
                        }

                        String value = generateValueForField(field);
                        sub.addCondition(field, operator, value);
                        assigned++;
                        if (assigned >= fieldCount) break;
                    }

                }

            }));
        }

        // Wait for all tasks to finish
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return subscriptions;
    }

    private static Map<String, Integer> computeExactCounts(int total, Map<String, Double> percentages) {
        Map<String, Integer> counts = new HashMap<>();
        percentages.forEach((key, percent) -> counts.put(key, (int) Math.round(total * percent)));
        return counts;
    }

    private static List<Integer> getShuffledIndices(int count, List<Subscription> subscriptions) {
        List<Integer> indices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);  // Single shuffle to randomize indices

        return indices;
    }

    private static String generateValueForField(String field) {
        return switch (field) {
            case "city" -> "\"" + RandomUtils.randomElement(List.of("Bucharest", "Cluj", "Iasi", "Timisoara")) + "\"";
            case "direction" ->
                    "\"" + RandomUtils.randomElement(List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW")) + "\"";
            case "stationid" -> String.valueOf(RandomUtils.randomInt(1, 100));
            case "temp" -> String.valueOf(RandomUtils.randomInt(-20, 40));
            case "rain" -> String.valueOf(RandomUtils.randomDouble(0.0, 10.0));
            case "wind" -> String.valueOf(RandomUtils.randomInt(0, 100));
            case "date" -> RandomUtils.randomElement(List.of("2.02.2023", "3.02.2023", "4.02.2023"));
            default -> "?";
        };
    }
}
