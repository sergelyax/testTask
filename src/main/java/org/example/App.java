package org.example;

import org.example.api.ApiClient;
import org.example.comparator.PackageComparator;
import java.util.*;

public class App {
    public static void main(String[] args) {
        // Проверка входных аргументов
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <branch1> <branch2>");
            System.exit(1);
        }

        // Получение имен веток
        String branch1 = args[0];
        String branch2 = args[1];
        System.out.println("Branches to compare: " + branch1 + " vs " + branch2);

        // Создание экземпляров ApiClient и PackageComparator
        ApiClient apiClient = new ApiClient();
        PackageComparator comparator = new PackageComparator();

        try {
            // Получение пакетов для первой ветки
            System.out.println("Retrieving packages for branch " + branch1);
            List<Map<String, Object>> packagesBranch1 = apiClient.getPackagesForBranch(branch1);

            // Получение пакетов для второй ветки
            System.out.println("Retrieving packages for branch " + branch2);
            List<Map<String, Object>> packagesBranch2 = apiClient.getPackagesForBranch(branch2);

            // Сравнение пакетов и получение результата
            System.out.println("Comparing packages...");
            String result = comparator.comparePackages(packagesBranch1, packagesBranch2);

            // Вывод результатов сравнения
            System.out.println("Comparison Results:");
            System.out.println(result);

        } catch (Exception e) {
            // Логирование исключения, если что-то пошло не так
            System.err.println("An error occurred during comparison:");
            e.printStackTrace();
            System.exit(2);
        }
    }
}
