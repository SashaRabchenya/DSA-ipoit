package by.it.group410971.rabchenya.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        int pisanoPeriod = getPisanoPeriodLength(m);
        int reducedN = (int)(n % pisanoPeriod);

        return fibMod(reducedN, m);
    }

    // Функция для поиска длины периода Пизано
    private int getPisanoPeriodLength(int m) {
        int prev = 0;
        int curr = 1;
        for (int i = 0; i < m * 6; i++) { // Длина периода максимум 6*m
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
            if (prev == 0 && curr == 1) {
                return i + 1;
            }
        }
        return -1; // не должно происходить
    }

    // Быстрое вычисление F(n) % m
    private int fibMod(int n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        int[] fib = new int[n + 1];
        fib[0] = 0;
        fib[1] = 1;

        for (int i = 2; i <= n; i++) {
            fib[i] = (fib[i - 1] + fib[i - 2]) % m;
        }

        return fib[n];
    }


}

