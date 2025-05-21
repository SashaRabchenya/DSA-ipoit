package by.it.group410971.rabchenya.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        // Считываем количество символов и длину закодированной строки
        int k = scanner.nextInt(); // количество различных букв
        int l = scanner.nextInt(); // длина закодированной строки
        scanner.nextLine(); // переход на следующую строку

        // Считываем коды символов
        Map<String, Character> codeMap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            codeMap.put(code, letter);
        }

        // Считываем закодированную строку
        String encoded = scanner.nextLine();

        // Декодирование строки
        StringBuilder currentCode = new StringBuilder();
        for (char c : encoded.toCharArray()) {
            currentCode.append(c);
            if (codeMap.containsKey(currentCode.toString())) {
                result.append(codeMap.get(currentCode.toString()));
                currentCode.setLength(0); // очищаем буфер
            }
        }

        return result.toString();
    }
}
