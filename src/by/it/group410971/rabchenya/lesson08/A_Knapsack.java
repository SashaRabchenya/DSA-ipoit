package by.it.group410971.rabchenya.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Массив для хранения максимального веса, который можно набрать для каждого веса от 0 до w
        int[] dp = new int[w + 1];

        // Перебираем все веса от 1 до w
        for (int i = 1; i <= w; i++) {
            for (int j = 0; j < n; j++) {
                if (gold[j] <= i) {
                    dp[i] = Math.max(dp[i], dp[i - gold[j]] + gold[j]);
                }
            }
        }

        return dp[w]; // максимальный вес, который можно унести
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
