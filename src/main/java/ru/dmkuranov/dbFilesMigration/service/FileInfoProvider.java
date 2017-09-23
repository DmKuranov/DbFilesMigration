package ru.dmkuranov.dbFilesMigration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.domain.FileInfo;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringTable;
import ru.dmkuranov.dbFilesMigration.util.FileManipulator;
import ru.dmkuranov.dbFilesMigration.util.PathFormatter;
import ru.dmkuranov.dbFilesMigration.util.StoredPathConverter;

import java.io.File;

@Service
public class FileInfoProvider {
    @Value("${app.targetFilesystemPath}")
    private String targetFilesystemPath;
    @Value("${app.targetStoredPath}")
    private String targetStoredPath;
    @Value("${app.sourceFilesystemPath}")
    private String sourceFilesystemPathRoot;
    @Autowired
    private FileManipulator fileManipulator;


    public void provideFileInfo(FileStoringField fileStoringField, String storedFilePath) {
        FileInfo fileInfo;
        if (storedFilePath != null && storedFilePath.trim().length() > 0) {
            FileStoringRow fileStoringRow = fileStoringField.getRow();
            FileStoringTable table = fileStoringField.getRow().getTable();
            String generatedTargetFilesystemPath = File.separator + table.getStoragePathPrefix() + File.separator
                    + PathFormatter.buildDecimalHierarchyPath(fileStoringRow.getId()) + File.separator
                    + table.getFilePrefix() + "_" + fileStoringField.getFieldName()
                    + "." + PathFormatter.getFileExtension(storedFilePath);
            String sourceFilesystemPath = sourceFilesystemPathRoot + StoredPathConverter.toFileSystemPath(storedFilePath);
            fileInfo = new FileInfo(
                    storedFilePath
                    , sourceFilesystemPath
                    , StoredPathConverter.toStoredPath(targetStoredPath + generatedTargetFilesystemPath) + PathFormatter.getWebOptions(storedFilePath)
                    , targetFilesystemPath + generatedTargetFilesystemPath
                    , fileManipulator.isExist(sourceFilesystemPath)
            );
        } else {
            fileInfo = emptyFileInfo;
        }
        fileStoringField.setFileInfo(fileInfo);
    }

    private static final FileInfo emptyFileInfo = new FileInfo(null, null, null, null, false);
}
