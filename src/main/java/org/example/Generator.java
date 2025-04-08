package org.example;

import java.util.*;

public class Generator {
    public static List<Publication> generatePublications(int count) {
        List<Publication> publications = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            publications.add(new Publication());
        }
        return publications;
    }


    public static List<Subscription> generateSubscriptions(int count, Map<String, Double> fieldFrequencies, Map<String, Double> equalityMinFreq) {
        List<Subscription> subscriptions = new ArrayList<>();

        // Calculează exact câte subscripții trebuie să conțină fiecare câmp
        Map<String, Integer> requiredCounts = computeExactCounts(count, fieldFrequencies);
        Map<String, Integer> equalityCounts = computeExactCounts(count, equalityMinFreq);


        // Initialize the subscriptions list
        for (int i = 0; i < count; i++) {
            subscriptions.add(new Subscription());
        }

        int fieldSize = requiredCounts.size();
        Iterator<String> it = requiredCounts.keySet().iterator();

        // Loop through the fields and assign conditions to subscriptions
        while (fieldSize > 0) {
            String field = it.next();

            // Shuffle and sort subscriptions so empty ones are first
            Collections.shuffle(subscriptions);
            subscriptions.sort(Comparator.comparingInt(sub -> sub.getConditions().isEmpty() ? 0 : 1));

            fieldSize--;
            for (int i = 0; i < requiredCounts.get(field); i++) {

                String operator;

                // Use "=" if it must appear according to the frequency
                if (equalityCounts.getOrDefault(field, 0) > 0) {
                    operator = "=";
                    equalityCounts.put(field, equalityCounts.get(field) - 1);
                } else {
                    // For categorical fields, only "=" or "!=" should be used
                    if (field.equals("direction") || field.equals("date") || field.equals("city")) {
                        operator = RandomUtils.randomElement(List.of("=", "!="));  // Only "=" or "!=" for categorical fields
                    } else {
                        // For numeric fields, use the full set of operators
                        operator = RandomUtils.randomElement(Subscription.OPERATORS);
                    }
                }

                // Generate a value based on the field
                String value = generateValueForField(field);
                subscriptions.get(i).addCondition(field, operator, value);
            }

        }

        return subscriptions;
    }

    private static Map<String, Integer> computeExactCounts(int total, Map<String, Double> percentages) {
        Map<String, Integer> counts = new HashMap<>();
        percentages.forEach((key, percent) -> counts.put(key, (int) Math.round(total * percent)));
        return counts;
    }


    private static String generateValueForField(String field) {
        return switch (field) {
            case "city" -> "\"" + RandomUtils.randomElement(List.of("Bucharest", "Cluj", "Iasi", "Timisoara")) + "\"";
            case "direction" -> "\"" + RandomUtils.randomElement(List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW")) + "\"";
            case "stationid" -> String.valueOf(RandomUtils.randomInt(1, 100));
            case "temp" -> String.valueOf(RandomUtils.randomInt(-20, 40));
            case "rain" -> String.valueOf(RandomUtils.randomDouble(0.0, 10.0));
            case "wind" -> String.valueOf(RandomUtils.randomInt(0, 100));
            case "date" -> RandomUtils.randomElement(List.of("2.02.2023", "3.02.2023", "4.02.2023"));
            default -> "?";
        };
    }
}
