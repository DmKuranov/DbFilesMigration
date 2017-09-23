package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.service.processing.ActionExecutor;

public interface FieldMigrationStrategy extends ActionExecutor<FieldAction> {
    String getDescription();
}
