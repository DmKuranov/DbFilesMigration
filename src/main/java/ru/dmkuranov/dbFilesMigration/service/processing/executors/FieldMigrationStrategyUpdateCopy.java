package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import ru.dmkuranov.dbFilesMigration.domain.FileInfo;

public class FieldMigrationStrategyUpdateCopy extends FieldMigrationStrategyUpdateAbstract {

    @Override
    protected void fileManipulationExecute(FileInfo fileInfo) {
        fileManipulator.copyFile(fileInfo.getSourceFileSystemAbsolutePath(), fileInfo.getTargetFileSystemAbsolutePath());
    }

    @Override
    protected void fileManipulationRollback(FileInfo fileInfo) {
        fileManipulator.deleteFile(fileInfo.getTargetFileSystemAbsolutePath());
    }

    @Override
    public String getDescription() {
        return super.getDescription()+", copy file";
    }

}
