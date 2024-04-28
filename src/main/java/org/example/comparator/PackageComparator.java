package org.example.comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

public class PackageComparator {
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String comparePackages(
      List<Map<String, Object>> packagesBranch1,
      List<Map<String, Object>> packagesBranch2) throws Exception {
    Set<String> branch1Names = packagesBranch1.stream()
        .map(pkg -> (String) pkg.get("name"))
        .collect(Collectors.toSet());

    Set<String> branch2Names = packagesBranch2.stream()
        .map(pkg -> (String) pkg.get("name"))
        .collect(Collectors.toSet());

    List<String> onlyInBranch1 = new ArrayList<>(branch1Names);
    onlyInBranch1.removeAll(branch2Names);

    List<String> onlyInBranch2 = new ArrayList<>(branch2Names);
    onlyInBranch2.removeAll(branch1Names);

    // Пакеты с более высокой версией в ветке 1
    Map<String, Map<String, String>> higherVersionInBranch1 = new HashMap<>();
    for (Map<String, Object> package1 : packagesBranch1) {
      String name = (String) package1.get("name");
      if (branch2Names.contains(name)) {
        String version1 = (String) package1.get("version");
        String release1 = (String) package1.get("release");
        Map<String, Object> package2 = packagesBranch2.stream()
            .filter(pkg -> name.equals(pkg.get("name")))
            .findFirst()
            .orElseThrow(() -> new Exception("Package not found in branch 2"));
        String version2 = (String) package2.get("version");
        String release2 = (String) package2.get("release");
        if (version1.compareTo(version2) > 0 ||
            (version1.equals(version2) && release1.compareTo(release2) > 0)) {
          higherVersionInBranch1.put(name, Map.of("version1", version1, "version2", version2));
        }
      }
    }

    Map<String, Object> results = new HashMap<>();
    results.put("onlyInBranch1", onlyInBranch1);
    results.put("onlyInBranch2", onlyInBranch2);
    results.put("higherVersionInBranch1", higherVersionInBranch1);

    return objectMapper.writeValueAsString(results);
  }
}
