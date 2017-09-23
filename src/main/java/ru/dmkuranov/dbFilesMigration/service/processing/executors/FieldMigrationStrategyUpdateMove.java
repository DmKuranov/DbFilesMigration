package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import ru.dmkuranov.dbFilesMigration.domain.FileInfo;

public class FieldMigrationStrategyUpdateMove extends FieldMigrationStrategyUpdateAbstract {
    @Override
    protected void fileManipulationExecute(FileInfo fileInfo) {
        fileManipulator.copyFile(fileInfo.getSourceFileSystemAbsolutePath(), fileInfo.getTargetFileSystemAbsolutePath());
        fileManipulator.deleteFile(fileInfo.getSourceFileSystemAbsolutePath());
    }

    @Override
    protected void fileManipulationRollback(FileInfo fileInfo) {
        fileManipulator.copyFile(fileInfo.getTargetFileSystemAbsolutePath(), fileInfo.getSourceFileSystemAbsolutePath());
        fileManipulator.deleteFile(fileInfo.getTargetFileSystemAbsolutePath());
    }

    @Override
    public String getDescription() {
        return super.getDescription()+", move file";
    }
}
