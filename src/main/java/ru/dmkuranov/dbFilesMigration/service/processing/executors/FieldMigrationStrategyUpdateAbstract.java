package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import org.springframework.beans.factory.annotation.Autowired;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.domain.FileInfo;
import ru.dmkuranov.dbFilesMigration.util.FileManipulator;

public abstract class FieldMigrationStrategyUpdateAbstract implements FieldMigrationStrategy {
    @Autowired(required = false)
    protected FileManipulator fileManipulator;

    protected abstract void fileManipulationExecute(FileInfo fileInfo);

    protected abstract void fileManipulationRollback(FileInfo fileInfo);

    @Override
    public void executeInternal(FieldAction entity) {
        FileInfo fileInfo = checkFileInfo(entity);
        FileStoringField field = entity.getEntity();
        fileManipulationExecute(fileInfo);
        entity.getRowAction().updateField(field.getFieldName(), fileInfo.getTargetStoredFilePath());
    }

    @Override
    public void rollback(FieldAction entity) {
        FileInfo fileInfo = checkFileInfo(entity);
        fileManipulationRollback(fileInfo);
    }

    @Override
    public String getDescription() {
        return "Update database field";
    }

    private FileInfo checkFileInfo(FieldAction entity) {
        FileInfo fileInfo = entity.getEntity().getFileInfo();
        if (fileInfo == null || fileInfo.getSourceFileSystemAbsolutePath() == null
                || fileInfo.getTargetFileSystemAbsolutePath() == null || fileInfo.getTargetStoredFilePath() == null
                || fileInfo.getSourceFileSystemAbsolutePath().trim().isEmpty()
                || fileInfo.getTargetFileSystemAbsolutePath().trim().isEmpty()
                || fileInfo.getTargetStoredFilePath().trim().isEmpty()) {
            throw new RuntimeException("insufficient info for migration");
        } else {
            return fileInfo;
        }
    }
}
