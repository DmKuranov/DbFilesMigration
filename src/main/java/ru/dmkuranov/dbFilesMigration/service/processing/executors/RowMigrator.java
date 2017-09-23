package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import ru.dmkuranov.dbFilesMigration.service.processing.ActionExecutor;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;

public interface RowMigrator extends ActionExecutor<RowAction.MigrateFields> {
}
