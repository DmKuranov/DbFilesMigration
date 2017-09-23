package ru.dmkuranov.dbFilesMigration.util;

public interface FileManipulator {
    String copyFile(String sourceFileName, String targetFileName);

    void deleteFile(String fileName);

    boolean isExist(String fileName);

    String moveFile(String sourceFileName, String targetFileName);
}
