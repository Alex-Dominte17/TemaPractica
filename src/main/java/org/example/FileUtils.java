package org.example;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtils {

    public static <T> void writeListToFile(List<T> list, String fileName) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (T item : list) {
                String json = gson.toJson(item);   // convertește fiecare item în JSON
                writer.write(json);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + fileName);
            e.printStackTrace();
        }

        System.out.println("Successfully written to file " + fileName);
    }

}