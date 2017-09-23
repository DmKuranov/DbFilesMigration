package ru.dmkuranov.dbFilesMigration.service.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.service.processing.executors.FieldMigrationStrategy;

public abstract class FieldAction extends AbstractAction<FileStoringField> {
    private RowAction rowAction;

    public FieldAction(FileStoringField entity, String reason, RowAction rowAction) {
        super(entity, reason);
        this.rowAction = rowAction;
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Unknown extends FieldAction {
        private static ActionExecutor executor = unexecutable;

        public Unknown(FileStoringField entity, RowAction rowAction) {
            super(entity, "", rowAction);
        }

        public Unknown(FileStoringField entity) {
            this(entity, null);
        }

        @Override
        protected void executeInternal() {
            executor.executeInternal(getEntity());
        }

        @Override
        public void rollback() {
            executor.rollback(getEntity());
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Skip extends FieldAction {
        private static ActionExecutor executor = skip;

        public Skip(FileStoringField entity, String reason, RowAction rowAction) {
            super(entity, reason, rowAction);
        }

        public Skip(FileStoringField entity, String reason) {
            this(entity, reason, null);
        }

        public Skip(FileStoringField entity, RowAction rowAction) {
            this(entity, "", rowAction);
        }

        public Skip(FileStoringField entity) {
            this(entity, "", null);
        }

        @Override
        protected void executeInternal() {
            executor.executeInternal(getEntity());
        }

        @Override
        public void rollback() {
            executor.rollback(getEntity());
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Migrate extends FieldAction {
        @Autowired
        @Qualifier("fieldMigrationStrategy")
        private FieldMigrationStrategy fieldMigrationStrategy;

        public Migrate(FileStoringField entity, String reason, RowAction rowAction) {
            super(entity, reason, rowAction);
        }

        public Migrate(FileStoringField entity, String reason) {
            this(entity, reason, null);
        }

        protected void executeInternal() {
            fieldMigrationStrategy.executeInternal(this);
        }

        public void rollback() {
            fieldMigrationStrategy.rollback(this);
        }
    }

    public RowAction getRowAction() {
        return rowAction;
    }

    public void setRowAction(RowAction rowAction) {
        this.rowAction = rowAction;
    }

}
