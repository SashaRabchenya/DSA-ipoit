package by.it.group410971.rabchenya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

По сравнению с задачей A доработайте алгоритм так, чтобы
1) он оптимально использовал время и память:
    - за стек отвечает элиминация хвостовой рекурсии
    - за сам массив отрезков - сортировка на месте
    - рекурсивные вызовы должны проводиться на основе 3-разбиения

2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
для первого отрезка решения, а затем найдите оставшуюся часть решения
(т.е. отрезков, подходящих для точки, может быть много)

Sample Input:
2 3
0 5
7 10
1 6 11
Sample Output:
1 0 0
*/

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort3Way(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countCoveringSegments(segments, points[i]);
        }

        return result;
    }

    // Быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt++, i++);
                } else if (cmp > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }
            // Рекурсия на меньшую часть, хвост — через цикл
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Поиск первого индекса отрезка с start > point
    private int binarySearchFirstGreaterStart(Segment[] segments, int point) {
        int low = 0, high = segments.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (segments[mid].start > point) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // Подсчет отрезков, покрывающих точку
    private int countCoveringSegments(Segment[] segments, int point) {
        int pos = binarySearchFirstGreaterStart(segments, point);
        int count = 0;
        for (int i = pos - 1; i >= 0; i--) {
            if (segments[i].stop >= point) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }
}
