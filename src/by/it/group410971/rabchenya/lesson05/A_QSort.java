package by.it.group410971.rabchenya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            // если нужно, гарантируем start <= end
            if (a <= b) {
                starts[i] = a;
                ends[i] = b;
            } else {
                starts[i] = b;
                ends[i] = a;
            }
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы
        Arrays.sort(starts);
        Arrays.sort(ends);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int startsCount = upperBound(starts, point);   // сколько отрезков start <= point
            int endsCount = lowerBound(ends, point);       // сколько отрезков end < point
            result[i] = startsCount - endsCount;
        }

        return result;
    }

    // Находит индекс первого элемента > value (т.е. количество элементов <= value)
    private int upperBound(int[] arr, int value) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Находит индекс первого элемента >= value (т.е. количество элементов < value)
    private int lowerBound(int[] arr, int value) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

}
