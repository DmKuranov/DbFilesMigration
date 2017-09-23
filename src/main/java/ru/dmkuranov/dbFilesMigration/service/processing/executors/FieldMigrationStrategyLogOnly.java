package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.domain.FileInfo;

public class FieldMigrationStrategyLogOnly implements FieldMigrationStrategy {
    private static final Logger log = LoggerFactory.getLogger(FieldMigrationStrategyLogOnly.class);

    @Override
    public String getDescription() {
        return "Log only";
    }

    @Override
    public void executeInternal(FieldAction entity) {
        FileInfo fileInfo = entity.getEntity().getFileInfo();
        log.info("Migrating " + fileInfo.getSourceStoredFilePath() + ": "
                + fileInfo.getSourceFileSystemAbsolutePath() + " => " + fileInfo.getTargetFileSystemAbsolutePath());
    }

    @Override
    public void rollback(FieldAction entity) {
    }
}
