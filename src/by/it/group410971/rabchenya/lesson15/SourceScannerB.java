package by.it.group410971.rabchenya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerB {

    static class FileInfo {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;

        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        processJavaFile(file, src, fileInfos);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Игнорируем ошибки чтения файлов
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка: по размеру, затем по пути
        fileInfos.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(a.size, b.size);
            }
            return a.path.compareTo(b.path);
        });

        // Вывод результатов
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static void processJavaFile(Path file, String srcRoot, List<FileInfo> fileInfos) {
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

            // Вычисляем размер в байтах (в кодировке UTF-8)
            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

            fileInfos.add(new FileInfo(relativePath, size));

        } catch (Exception e) {
            // Игнорируем ошибки обработки файлов
        }
    }

    private static String readFileContent(Path file) throws IOException {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Пробуем прочитать файл с другой кодировкой
            byte[] bytes = Files.readAllBytes(file);
            return new String(bytes, StandardCharsets.ISO_8859_1);
        }
    }

    private static String processContent(String content) {
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
                result.deleteCharAt(result.length() - 1); // удаляем предыдущий '/'
                inBlockComment = true;
                prevChar = 0;
                continue;
            }

            if (prevChar == '/' && currentChar == '/') {
                result.deleteCharAt(result.length() - 1); // удаляем предыдущий '/'
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

        // Удаляем package и import строки
        String withoutPackageImports = removePackageAndImports(result.toString());

        // Удаляем символы с кодом <33 в начале и конце
        String trimmed = removeControlCharsFromStart(withoutPackageImports);
        trimmed = removeControlCharsFromEnd(trimmed);

        // Удаляем пустые строки
        return removeEmptyLines(trimmed);
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

    private static String removeEmptyLines(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeControlCharsFromStart(String str) {
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }
        return str.substring(start);
    }

    private static String removeControlCharsFromEnd(String str) {
        int end = str.length() - 1;
        while (end >= 0 && str.charAt(end) < 33) {
            end--;
        }
        return str.substring(0, end + 1);
    }

    private static String getRelativePath(String absolutePath, String srcRoot) {
        return absolutePath.substring(srcRoot.length());
    }
}