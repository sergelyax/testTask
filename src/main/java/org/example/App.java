package org.example;

import org.example.api.ApiClient;
import org.example.comparator.PackageComparator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

public class App {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <branch1> <branch2>");
            System.exit(1);
        }

        String branch1 = args[0];
        String branch2 = args[1];
        ApiClient apiClient = new ApiClient();
        PackageComparator comparator = new PackageComparator();
        ObjectMapper mapper = new ObjectMapper(); // Jackson's ObjectMapper

        try {
            List<Map<String, Object>> packagesBranch1 = apiClient.getPackagesForBranch(branch1);
            List<Map<String, Object>> packagesBranch2 = apiClient.getPackagesForBranch(branch2);

            System.out.println("Comparing packages...");
            Map<String, Object> comparisonResults = comparator.comparePackages(packagesBranch1, packagesBranch2);

            // Write the result directly as JSON to a file
            System.out.println("Writing comparison results to JSON file...");
            mapper.writeValue(new File("comparisonResults.json"), comparisonResults);

            System.out.println("Comparison results successfully written to 'comparisonResults.json'");
        } catch (Exception e) {
            System.err.println("An error occurred:");
            e.printStackTrace();
            System.exit(2);
        }
    }
}
