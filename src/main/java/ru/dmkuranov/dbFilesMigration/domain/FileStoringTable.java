package ru.dmkuranov.dbFilesMigration.domain;

import java.util.List;

public class FileStoringTable {
    private String tableName;
    private String idFieldName;

    /**
     * Префикс пути папки назначения
     */
    private String storagePathPrefix;
    /**
     * Префикс файлов назначения
     */
    private String filePrefix;
    /**
     * Наименования полей, содержащих ссылки на файлы
     */
    private List<String> fileFieldNames;

    public FileStoringTable(String tableName, String idFieldName, String storagePathPrefix, String filePrefix, List<String> fileFieldNames) {
        this.tableName = tableName;
        this.idFieldName = idFieldName;
        this.storagePathPrefix = storagePathPrefix;
        this.filePrefix = filePrefix;
        this.fileFieldNames = fileFieldNames;
    }

    public String getTableName() {
        return tableName;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public String getStoragePathPrefix() {
        return storagePathPrefix;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public List<String> getFileFieldNames() {
        return fileFieldNames;
    }

    @Override
    public String toString() {
        return "Table "+getTableName();
    }
}
