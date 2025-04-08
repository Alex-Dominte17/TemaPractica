package org.example;

import java.util.List;
import java.util.Map;

public class SubscriptionGeneratorThread extends Thread {
    private final int numSubscriptions;
    private final Map<String, Double> fieldFrequencies;
    private final Map<String, Double> equalityMinFreq;
    private List<Subscription> result;

    public SubscriptionGeneratorThread(int numSubscriptions, Map<String, Double> fieldFrequencies, Map<String, Double> equalityMinFreq) {
        this.numSubscriptions = numSubscriptions;
        this.fieldFrequencies = fieldFrequencies;
        this.equalityMinFreq = equalityMinFreq;
    }

    @Override
    public void run() {
        result = Generator.generateSubscriptions(numSubscriptions, fieldFrequencies, equalityMinFreq);
    }

    public List<Subscription> getResult() {
        return result;
    }
}
