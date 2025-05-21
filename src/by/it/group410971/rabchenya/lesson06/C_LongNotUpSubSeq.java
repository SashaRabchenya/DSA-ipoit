package by.it.group410971.rabchenya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

/*
Задача на программирование: наибольшая невозрастающая подпоследовательность

Дано:
    целое число 1<=n<=1E5
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу методами динамического программирования
*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int length = instance.getNotUpSeqSize(stream);
        System.out.println(length);
        instance.printSequence(stream); // Выводим индексы после подсчёта длины
    }

    // Метод для вычисления длины максимальной невозрастающей подпоследовательности
    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] dp = new int[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = -1;

        int maxLen = 0;
        int maxEndIndex = -1;

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxEndIndex = i;
            }
        }
        // Сохраняем состояние для вывода последовательности
        this.dp = dp;
        this.parent = parent;
        this.maxEndIndex = maxEndIndex;
        this.m = m;
        this.n = n;

        return maxLen;
    }

    // Переменные экземпляра для хранения результата
    private int[] dp;
    private int[] parent;
    private int maxEndIndex;
    private int[] m;
    private int n;

    // Метод для вывода самой подпоследовательности индексов
    void printSequence(InputStream stream) {
        if (maxEndIndex == -1) return; // Нет результата

        ArrayList<Integer> path = new ArrayList<>();
        int current = maxEndIndex;
        while (current != -1) {
            path.add(current + 1); // индексы с 1
            current = parent[current];
        }
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i != path.size() - 1) System.out.print(" ");
        }
    }
}
