package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numPublications = args.length > 0 ? Integer.parseInt(args[0]) : 100;
        int numSubscriptions = args.length > 1 ? Integer.parseInt(args[1]) : 100;


        Map<String, Double> fieldFrequencies = Map.of(
                "city", 1.0,
                "date", 0.5,
                "temp", 0.7,
                "wind", 0.7,
                "direction", 0.2,
                "stationid",0.1,
                "rain",0.7

        );

        Map<String, Double> equalityMinFreq = Map.of("city", 0.7);


        List<Publication> publications = Generator.generatePublications(numPublications);


        long startTime1 = System.nanoTime();
        List<Subscription> subscriptions = SubscriptionGenerator.generateSubscriptions(numSubscriptions, fieldFrequencies, equalityMinFreq);
        long endTime1 = System.nanoTime();

        long startTime2 = System.nanoTime();

        List<Subscription> subscriptions2 = Generator.generateSubscriptions(numSubscriptions, fieldFrequencies, equalityMinFreq);
        long endTime2 = System.nanoTime();

        long durationInMillis1 = (endTime1 - startTime1) / 1_000_000;
        long durationInMillis2 = (endTime2 - startTime2) / 1_000_000;


//        System.out.println("Generated Publications:");
//        publications.forEach(System.out::println);
//
//        System.out.println("\nGenerated Subscriptions:");
//        subscriptions.forEach(System.out::println);

        System.out.println("\n(Multithreading) Field counts in Subscriptions:");
        Map<String, Integer> fieldCounts = countFields(subscriptions);
        fieldCounts.forEach((field, count) -> System.out.println(field + ": " + count));

        System.out.println("\n(Single threaded) Field counts in Subscriptions:");
        Map<String, Integer> fieldCounts2 = countFields(subscriptions2);
        fieldCounts2.forEach((field, count) -> System.out.println(field + ": " + count));

        int cores = Runtime.getRuntime().availableProcessors();
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        String version = System.getProperty("os.version");

        System.out.println("Sistem de operare: " + os + " " + version);
        System.out.println("Arhitectură: " + arch);
        System.out.println("Număr de nuclee: " + cores);

        System.out.println("\n(Multithreading) Time taken for generation: " + durationInMillis1 + " ms");
        System.out.println("\n(Single threaded) Time taken for generation: " + durationInMillis2 + " ms");
        FileUtils.writeListToFile(publications, "publications.txt");
        FileUtils.writeListToFile(subscriptions, "subscriptions.txt");


    }

    public static Map<String, Integer> countFields(List<Subscription> subscriptions) {
        Map<String, Integer> totalFieldCounts = new HashMap<>(); // field -> total appearances
        Map<Integer, Integer> subscriptionFieldCountStats = new TreeMap<>(); // numFields -> how many subscriptions have this many

        for (Subscription subscription : subscriptions) {
            int fieldCount = subscription.getConditions().size();

            subscriptionFieldCountStats.put(fieldCount, subscriptionFieldCountStats.getOrDefault(fieldCount, 0) + 1);

            for (String field : subscription.getConditions().keySet()) {
                totalFieldCounts.put(field, totalFieldCounts.getOrDefault(field, 0) + 1);
            }
        }

        System.out.println("\nSubscription Field Count Distribution:");
        subscriptionFieldCountStats.forEach((numFields, count) ->
                System.out.println(numFields + " field(s): " + count + " subscriptions"));

        return totalFieldCounts;
    }


}
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////
//package org.example;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        int numPublications = 1_000_000;
//        int numSubscriptions = 1_000_000;
//
//        Map<String, Double> fieldFrequencies = Map.of(
//                "city", 0.7,
//                "date", 0.5,
//
//                "temp", 0.6,
//                "wind", 0.5,
//                "direction", 0.8
//        );
//
//        Map<String, Double> equalityMinFreq = Map.of("city", 0.7);
//
//
//
//        SubscriptionGeneratorThread subThread = new SubscriptionGeneratorThread(
//                numSubscriptions, fieldFrequencies, equalityMinFreq);
//        PublicationGeneratorThread pubThread = new PublicationGeneratorThread(numPublications);
//
//        subThread.start();
//        pubThread.start();
//        long startTime1 = System.nanoTime();
//
//        subThread.join();
//        pubThread.join();
//        long endTime1 = System.nanoTime();
//
//        List<Subscription> subscriptions = subThread.getResult();
//        List<Publication> publications = pubThread.getResult();
//
//        long startTime2 = System.nanoTime();
//        List<Publication> publications2 = Generator.generatePublications(numPublications);
//
//        List<Subscription> subscriptions2 = Generator.generateSubscriptions(numSubscriptions, fieldFrequencies, equalityMinFreq);
//        long endTime2 = System.nanoTime();
//
//        long durationInMillis1 = (endTime1 - startTime1) / 1_000_000;
//        long durationInMillis2 = (endTime2 - startTime2) / 1_000_000;
//
//
////        System.out.println("Generated Publications:");
////        publications.forEach(System.out::println);
////
////        System.out.println("\nGenerated Subscriptions:");
////        subscriptions.forEach(System.out::println);
//
////        System.out.println("\n(Multithreading) Field counts in Subscriptions:");
////        Map<String, Integer> fieldCounts = countFields(subscriptions);
////        fieldCounts.forEach((field, count) -> System.out.println(field + ": " + count));
////
////        System.out.println("\n(Single threaded) Field counts in Subscriptions:");
////        Map<String, Integer> fieldCounts2 = countFields(subscriptions2);
////        fieldCounts2.forEach((field, count) -> System.out.println(field + ": " + count));
//
//        System.out.println("\n(Multithreading) Time taken for generation: " + durationInMillis1 + " ms");
//        System.out.println("\n(Single threaded) Time taken for generation: " + durationInMillis2 + " ms");
//
//    }
//
//    public static Map<String, Integer> countFields(List<Subscription> subscriptions) {
//        Map<String, Integer> totalFieldCounts = new HashMap<>(); // field -> total appearances
//        Map<Integer, Integer> subscriptionFieldCountStats = new TreeMap<>(); // numFields -> how many subscriptions have this many
//
//        for (Subscription subscription : subscriptions) {
//            int fieldCount = subscription.getConditions().size();
//
//            subscriptionFieldCountStats.put(fieldCount, subscriptionFieldCountStats.getOrDefault(fieldCount, 0) + 1);
//
//            for (String field : subscription.getConditions().keySet()) {
//                totalFieldCounts.put(field, totalFieldCounts.getOrDefault(field, 0) + 1);
//            }
//        }
//
//        System.out.println("\nSubscription Field Count Distribution:");
//        subscriptionFieldCountStats.forEach((numFields, count) ->
//                System.out.println(numFields + " field(s): " + count + " subscriptions"));
//
//        return totalFieldCounts;
//    }
//
//
//}
