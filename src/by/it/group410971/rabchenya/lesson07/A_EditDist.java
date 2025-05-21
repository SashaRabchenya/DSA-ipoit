package by.it.group410971.rabchenya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Массив для мемоизации результатов подзадач
    private int[][] memo;

    // Основной метод, который запускает рекурсивный подсчет с мемоизацией
    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;  // -1 означает, что результат еще не вычислен
            }
        }
        return dp(one, two, n, m);
    }

    // Рекурсивная функция с мемоизацией, вычисляет расстояние редактирования
    private int dp(String one, String two, int i, int j) {
        // Если одна строка пустая, то расстояние — длина другой строки
        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            // Символы совпадают, идти дальше без добавления операций
            memo[i][j] = dp(one, two, i - 1, j - 1);
        } else {
            // Рассматриваем операции: вставка, удаление, замена
            int insert = dp(one, two, i, j - 1) + 1;
            int delete = dp(one, two, i - 1, j) + 1;
            int replace = dp(one, two, i - 1, j - 1) + 1;
            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }
        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
