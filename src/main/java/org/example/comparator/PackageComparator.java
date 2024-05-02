package org.example.comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

public class PackageComparator {
  private final ObjectMapper objectMapper = new ObjectMapper();

  public Map<String, Object> comparePackages(
      List<Map<String, Object>> packagesBranch1,
      List<Map<String, Object>> packagesBranch2) throws Exception {

    // Собираем имена пакетов из каждой ветки
    Set<String> namesBranch1 = packagesBranch1.stream()
        .map(pkg -> (String) pkg.get("name"))
        .collect(Collectors.toSet());
    Set<String> namesBranch2 = packagesBranch2.stream()
        .map(pkg -> (String) pkg.get("name"))
        .collect(Collectors.toSet());

    // Определяем уникальные пакеты в каждой ветке
    Set<String> onlyInBranch1 = new HashSet<>(namesBranch1);
    onlyInBranch1.removeAll(namesBranch2);
    Set<String> onlyInBranch2 = new HashSet<>(namesBranch2);
    onlyInBranch2.removeAll(namesBranch1);

    // Сравниваем версии пакетов, которые есть в обеих ветках
    Map<String, String> higherVersionInBranch1 = packagesBranch1.stream()
        .filter(pkg -> namesBranch2.contains(pkg.get("name")))
        .collect(Collectors.toMap(
            pkg -> (String) pkg.get("name"),
            pkg -> (String) pkg.get("version") + "-" + (String) pkg.get("release"),
            (oldValue, newValue) -> compareVersionRelease(oldValue, newValue) > 0 ? oldValue : newValue
        ));

    Map<String, Object> results = new HashMap<>();
    results.put("onlyInBranch1", onlyInBranch1);
    results.put("onlyInBranch2", onlyInBranch2);
    results.put("higherVersionInBranch1", higherVersionInBranch1);

    return results; // Возвращаем объект вместо строки JSON
  }

  private int compareVersionRelease(String vr1, String vr2) {
    String[] parts1 = vr1.split("-");
    String[] parts2 = vr2.split("-");
    int compareVersion = parts1[0].compareTo(parts2[0]);
    if (compareVersion != 0) {
      return compareVersion;
    } else {
      return parts1[1].compareTo(parts2[1]);
    }
  }
}
