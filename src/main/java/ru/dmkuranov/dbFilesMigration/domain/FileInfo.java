package ru.dmkuranov.dbFilesMigration.domain;

public class FileInfo {
    private String sourceStoredFilePath;
    private String sourceFileSystemAbsolutePath;
    private String targetStoredFilePath;
    private String targetFileSystemAbsolutePath;
    private Boolean exist;

    public FileInfo(String sourceStoredFilePath, String sourceFileSystemAbsolutePath, String targetStoredFilePath, String targetFileSystemAbsolutePath, Boolean exist) {
        this.sourceStoredFilePath = sourceStoredFilePath;
        this.sourceFileSystemAbsolutePath = sourceFileSystemAbsolutePath;
        this.targetStoredFilePath = targetStoredFilePath;
        this.targetFileSystemAbsolutePath = targetFileSystemAbsolutePath;
        this.exist = exist;
    }

    public String getSourceStoredFilePath() {
        return sourceStoredFilePath;
    }

    public String getSourceFileSystemAbsolutePath() {
        return sourceFileSystemAbsolutePath;
    }

    public void setSourceFileSystemAbsolutePath(String sourceFileSystemAbsolutePath) {
        this.sourceFileSystemAbsolutePath = sourceFileSystemAbsolutePath;
    }

    public String getTargetStoredFilePath() {
        return targetStoredFilePath;
    }

    public void setTargetStoredFilePath(String targetStoredFilePath) {
        this.targetStoredFilePath = targetStoredFilePath;
    }

    public String getTargetFileSystemAbsolutePath() {
        return targetFileSystemAbsolutePath;
    }

    public void setTargetFileSystemAbsolutePath(String targetFileSystemAbsolutePath) {
        this.targetFileSystemAbsolutePath = targetFileSystemAbsolutePath;
    }

    public Boolean getExist() {
        return exist;
    }
}
