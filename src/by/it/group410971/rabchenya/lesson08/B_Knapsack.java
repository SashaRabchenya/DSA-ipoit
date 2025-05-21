package by.it.group410971.rabchenya.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt(); // Вес каждого слитка
        }

        // dp[i] — максимальный вес золота, который можно унести при вместимости рюкзака i
        int[] dp = new int[w + 1];

        for (int i = 0; i < n; i++) {
            // Перебираем веса рюкзака в обратном порядке,
            // чтобы не использовать один слиток более одного раза
            for (int j = w; j >= gold[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - gold[i]] + gold[i]);
            }
        }

        return dp[w]; // Максимальный вес, который можно унести
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
