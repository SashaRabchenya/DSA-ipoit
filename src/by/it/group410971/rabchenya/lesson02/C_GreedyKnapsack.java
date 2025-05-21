package by.it.group410971.rabchenya.lesson02;

/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
*/

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)\n", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // сколько предметов
        int W = input.nextInt();      // вместимость рюкзака
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        for (Item item : items) {
            System.out.println(item);
        }

        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // Сортируем по удельной стоимости (стоимость на 1 кг)
        Arrays.sort(items);

        double result = 0;
        int weightLeft = W;

        for (Item item : items) {
            if (weightLeft == 0) break;

            if (item.weight <= weightLeft) {
                // Берем весь предмет
                result += item.cost;
                weightLeft -= item.weight;
            } else {
                // Берем часть предмета
                double fraction = (double) weightLeft / item.weight;
                result += item.cost * fraction;
                weightLeft = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        // Удельная стоимость: cost / weight
        @Override
        public int compareTo(Item o) {
            double thisRatio = (double) this.cost / this.weight;
            double otherRatio = (double) o.cost / o.weight;
            return Double.compare(otherRatio, thisRatio); // сортировка по убыванию
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", valuePerKg=" + String.format("%.2f", (double) cost / weight) +
                    '}';
        }
    }
}
