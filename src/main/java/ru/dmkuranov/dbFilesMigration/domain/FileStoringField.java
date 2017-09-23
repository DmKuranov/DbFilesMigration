package ru.dmkuranov.dbFilesMigration.domain;

public class FileStoringField {
    private FileStoringRow row;
    private String fieldName;
    private FileInfo fileInfo;

    public FileStoringField(FileStoringRow row, String fieldName) {
        this.row = row;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileStoringRow getRow() {
        return row;
    }

    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder();
        outp.append("field ").append(row.getTable().getTableName())
                .append(".").append(fieldName).append("=").append(fileInfo.getSourceStoredFilePath());
        return outp.toString();
    }
}
