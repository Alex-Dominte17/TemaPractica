package org.example;

import java.util.List;

public class PublicationGeneratorThread extends Thread {
    private final int numPublications;
    private List<Publication> result;

    public PublicationGeneratorThread(int numPublications) {
        this.numPublications = numPublications;
    }

    @Override
    public void run() {
        result = Generator.generatePublications(numPublications);
    }

    public List<Publication> getResult() {
        return result;
    }
}

