package by.it.group410971.rabchenya.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки
        Map<String, List<String>> graph = new HashMap<>();

        // Разделяем по запятым для получения пар вершин
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            // Разделяем каждую пару по "->"
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Убедимся, что все вершины присутствуют в графе
            graph.putIfAbsent(to, new ArrayList<>());
        }

        // Проверяем наличие циклов
        boolean hasCycle = hasCycle(graph);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String node, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Если вершина уже в текущем пути рекурсии - найден цикл
        if (recursionStack.contains(node)) {
            return true;
        }

        // Если вершина уже полностью обработана - циклов нет
        if (visited.contains(node)) {
            return false;
        }

        // Добавляем вершину в посещенные и в текущий стек вызовов
        visited.add(node);
        recursionStack.add(node);

        // Рекурсивно проверяем всех соседей
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (dfs(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Убираем вершину из текущего стека вызовов (завершаем обработку)
        recursionStack.remove(node);
        return false;
    }
}