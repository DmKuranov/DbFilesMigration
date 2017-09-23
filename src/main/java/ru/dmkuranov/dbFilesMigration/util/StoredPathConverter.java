package ru.dmkuranov.dbFilesMigration.util;

import java.io.File;

public class StoredPathConverter {

    public static String toFileSystemPath(String storedPath) {
        String tmpPath = new String(storedPath);
        String extension = PathFormatter.getFileExtension(storedPath);
        if(extension.length()>0) {
            tmpPath = tmpPath.substring(0, storedPath.lastIndexOf("."))+"."+extension;
        }
        return tmpPath.replace('/', File.separatorChar);
    }

    public static String toStoredPath(String fileSystemPath) {
        return fileSystemPath.replace(File.separatorChar, '/');
    }
}
