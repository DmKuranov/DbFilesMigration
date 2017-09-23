package ru.dmkuranov.dbFilesMigration.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathFormatter {

    /**
     * Строит путь иерархии каталогов на основании числа.
     * Например, для number 771969 результат /0700000/0770000/0771000/0771900/0771960/0771969
     *
     * @param number число
     * @return путь иерархии каталогов
     */
    public static String buildDecimalHierarchyPath(Integer number) {
        StringBuilder numberedPath = new StringBuilder();
        int multiplier = 1;
        boolean firstIteration = true;
        do {
            if (!firstIteration) {
                numberedPath.insert(0, File.separator);
            } else {
                firstIteration = false;
            }
            numberedPath.insert(0, String.format("%07d", (number * multiplier)));
            number = number / 10;
            multiplier *= 10;
        } while (number > 0);
        return numberedPath.toString();
    }

    public static String getFileExtension(String fileName) {
        Matcher matcher = getExtensionAndWebOptions(fileName);
        if (matcher != null) {
            return matcher.group(1);
        }
        return "";
    }


    public static String getWebOptions(String storedFilePath) {
        Matcher matcher = getExtensionAndWebOptions(storedFilePath);
        if (matcher != null) {
            return matcher.group(2);
        }
        return "";
    }

    private static final Pattern extensionPattern = Pattern.compile("^([a-zA-Z0-9_]*)(.*)$");

    private static Matcher getExtensionAndWebOptions(String fileName) {
        if (fileName != null) {
            Integer lastDotPosition = fileName.lastIndexOf(".");
            if (lastDotPosition > 0) {
                String extension = fileName.substring(lastDotPosition + 1);
                Matcher matcher = extensionPattern.matcher(extension);
                if (matcher.matches()) {
                    return matcher;
                }
            }
        }
        return null;
    }
}
