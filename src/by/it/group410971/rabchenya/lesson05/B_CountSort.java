package by.it.group410971.rabchenya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)
*/

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Максимальное значение по условию задачи
        int maxValue = 10;

        // Создаем массив счетчиков для чисел от 1 до maxValue
        int[] counts = new int[maxValue + 1];

        // Подсчитываем количество каждого числа
        for (int value : points) {
            counts[value]++;
        }

        // Формируем отсортированный массив
        int index = 0;
        for (int value = 1; value <= maxValue; value++) {
            for (int count = 0; count < counts[value]; count++) {
                points[index++] = value;
            }
        }

        return points;
    }
}
