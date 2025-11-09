package by.it.group410971.rabchenya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    static class FileContent {
        String path;
        String content;

        FileContent(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileContent> files = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, src, files);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Находим копии
        Map<String, List<String>> copies = findCopies(files);

        // Сортируем и выводим результаты
        List<String> sortedPaths = new ArrayList<>(copies.keySet());
        Collections.sort(sortedPaths);

        for (String path : sortedPaths) {
            List<String> copyPaths = copies.get(path);
            if (!copyPaths.isEmpty()) {
                System.out.println(path);
                for (String copyPath : copyPaths) {
                    System.out.println(copyPath);
                }
            }
        }
    }

    private static void processJavaFile(Path file, String srcRoot, List<FileContent> files) {
        try {
            String content = readFileContent(file);

            // Пропускаем файлы с тестами
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Обрабатываем содержимое
            String processedContent = processContent(content);

            // Получаем относительный путь
            String relativePath = getRelativePath(file.toString(), srcRoot);

            files.add(new FileContent(relativePath, processedContent));

        } catch (Exception e) {
            // Игнорируем ошибки обработки файлов
        }
    }

    private static String readFileContent(Path file) throws IOException {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            byte[] bytes = Files.readAllBytes(file);
            return new String(bytes, StandardCharsets.ISO_8859_1);
        }
    }

    private static String processContent(String content) {
        // Удаляем комментарии
        String withoutComments = removeComments(content);

        // Удаляем package и import
        String withoutPackageImports = removePackageAndImports(withoutComments);

        // Заменяем последовательности символов с кодом <33 на пробел
        String normalized = normalizeWhitespace(withoutPackageImports);

        // Trim
        return normalized.trim();
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        char prevChar = 0;

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            if (inBlockComment) {
                if (prevChar == '*' && currentChar == '/') {
                    inBlockComment = false;
                    prevChar = 0;
                } else {
                    prevChar = currentChar;
                }
                continue;
            }

            if (inLineComment) {
                if (currentChar == '\n') {
                    inLineComment = false;
                    result.append(currentChar);
                }
                prevChar = currentChar;
                continue;
            }

            if (inString) {
                result.append(currentChar);
                if (prevChar != '\\' && currentChar == '"') {
                    inString = false;
                }
                prevChar = currentChar;
                continue;
            }

            if (inChar) {
                result.append(currentChar);
                if (prevChar != '\\' && currentChar == '\'') {
                    inChar = false;
                }
                prevChar = currentChar;
                continue;
            }

            if (prevChar == '/' && currentChar == '*') {
                result.deleteCharAt(result.length() - 1);
                inBlockComment = true;
                prevChar = 0;
                continue;
            }

            if (prevChar == '/' && currentChar == '/') {
                result.deleteCharAt(result.length() - 1);
                inLineComment = true;
                prevChar = 0;
                continue;
            }

            if (currentChar == '"') {
                inString = true;
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            if (currentChar == '\'') {
                inChar = true;
                result.append(currentChar);
                prevChar = currentChar;
                continue;
            }

            result.append(currentChar);
            prevChar = currentChar;
        }

        return result.toString();
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") &&
                    !trimmedLine.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String normalizeWhitespace(String content) {
        StringBuilder result = new StringBuilder();
        boolean inWhitespace = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    result.append(' ');
                    inWhitespace = true;
                }
            } else {
                result.append(c);
                inWhitespace = false;
            }
        }

        return result.toString();
    }

    private static String getRelativePath(String absolutePath, String srcRoot) {
        return absolutePath.substring(srcRoot.length());
    }

    private static Map<String, List<String>> findCopies(List<FileContent> files) {
        Map<String, List<String>> copies = new HashMap<>();
        int n = files.size();

        // Инициализируем карту
        for (FileContent file : files) {
            copies.put(file.path, new ArrayList<>());
        }

        // Оптимизация: предварительная фильтрация по длине
        for (int i = 0; i < n; i++) {
            FileContent file1 = files.get(i);
            String content1 = file1.content;

            for (int j = i + 1; j < n; j++) {
                FileContent file2 = files.get(j);
                String content2 = file2.content;

                // Быстрая проверка: если длины сильно отличаются, пропускаем
                if (Math.abs(content1.length() - content2.length()) > 10) {
                    continue;
                }

                // Вычисляем расстояние Левенштейна
                int distance = levenshteinDistance(content1, content2);

                if (distance < 10) {
                    copies.get(file1.path).add(file2.path);
                    copies.get(file2.path).add(file1.path);
                }
            }
        }

        return copies;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Используем оптимизацию: работаем с двумя строками
        if (len1 < len2) {
            return levenshteinDistance(s2, s1);
        }

        // Инициализация предыдущей строки
        int[] prev = new int[len2 + 1];
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        // Вычисление расстояния
        for (int i = 1; i <= len1; i++) {
            int[] curr = new int[len2 + 1];
            curr[0] = i;

            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
            }

            prev = curr;
        }

        return prev[len2];
    }
}