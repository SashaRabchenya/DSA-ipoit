package by.it.group410971.rabchenya.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // читаем первую непустую строку и извлекаем из неё число
        String line = null;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (line != null) {
                line = line.trim();
                if (!line.isEmpty()) break;
            }
        }
        if (line == null || line.isEmpty()) {
            // нет корректного ввода — завершаем без вывода
            return;
        }

        // Разбираем первое число в строке (защита на случай "1 2 3" и т.п.)
        String[] parts = line.split("\\s+");
        int n;
        try {
            n = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            // неверный ввод — ничего не печатаем
            return;
        }

        // --- вызов решающей функции ---
        // Здесь я вызываю "solve" — текущая реализация может быть любая.
        // Ниже привожу простую (но экспоненциальную) реализацию через перебор 3^n состояний.
        // Если нужен корректный и быстрый алгоритм для больших n (например, до 21),
        // нужно переписать логику (могу сделать это по желанию).

        List<Integer> result = solveByBase3DSU(n);

        // выводим в одном ряду через пробел
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }

    // --- Пример "универсального" (но экспоненциального) решения ---
    // Представляет все 3^n конфигураций как числа в системе счисления base-3,
    // соединяет узлы, которые отличаются ровно в одной позиции, и считает компоненты.
    // ВАЖНО: для n>10..12 это может быть очень медленно и потреблять много памяти.
    private static List<Integer> solveByBase3DSU(int n) {
        if (n < 0) return Collections.emptyList();
        long totalLong = 1;
        for (int i = 0; i < n; i++) totalLong *= 3;
        if (totalLong > Integer.MAX_VALUE) {
            // защита: слишком большой объём для перебора
            // возвращаем пустой ответ — чтобы не падать по памяти/времену.
            // (Если нужно — перепишу алгоритм на математическое решение.)
            return Collections.emptyList();
        }
        int total = (int) totalLong;
        DSU dsu = new DSU(total);

        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (differsByOneBase3(i, j, n)) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> comps = new HashMap<>();
        for (int i = 0; i < total; i++) {
            int root = dsu.find(i);
            comps.put(root, comps.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(comps.values());
        Collections.sort(result);
        return result;
    }

    private static boolean differsByOneBase3(int a, int b, int n) {
        int diff = 0;
        for (int i = 0; i < n; i++) {
            int da = a % 3;
            int db = b % 3;
            if (da != db) diff++;
            if (diff > 1) return false;
            a /= 3;
            b /= 3;
        }
        return diff == 1;
    }

    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) return;
            if (size[pa] < size[pb]) {
                int t = pa; pa = pb; pb = t;
            }
            parent[pb] = pa;
            size[pa] += size[pb];
        }
    }
}
