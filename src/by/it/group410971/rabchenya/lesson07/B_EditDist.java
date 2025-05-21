package by.it.group410971.rabchenya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // dp[i][j] — расстояние редактирования между подстроками one[0..i-1] и two[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация: расстояние до пустой строки — длина другой строки
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // удаление всех символов
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // вставка всех символов
        }

        // Заполняем таблицу снизу вверх
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают, стоимость не увеличивается
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Берём минимум из трёх операций + 1
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
