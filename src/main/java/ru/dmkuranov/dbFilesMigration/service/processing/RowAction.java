package ru.dmkuranov.dbFilesMigration.service.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.service.processing.executors.RowMigrator;

import java.util.*;

public abstract class RowAction extends AbstractAction<FileStoringRow> {

    private Map<String, String> updatedFields = new LinkedHashMap<String, String>();
    private List<FieldAction> fieldActions = new ArrayList<FieldAction>();

    public RowAction(FileStoringRow entity, String reason) {
        super(entity, reason);
    }

    public RowAction(FileStoringRow entity, String reason, List<FieldAction> fieldActions) {
        super(entity, reason);
        this.fieldActions = fieldActions;
    }

    public List<FieldAction> getFieldActions() {
        return fieldActions;
    }

    public void updateField(String fieldName, String targetFieldValue) {
        updatedFields.put(fieldName, targetFieldValue);
    }

    public Map<String, String> getUpdatedFields() {
        return Collections.unmodifiableMap(updatedFields);
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Unknown extends RowAction {
        private static final ActionExecutor executor = AbstractAction.unexecutable;

        public Unknown(FileStoringRow entity) {
            super(entity, "");
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
    public static class Skip extends RowAction {
        private static final ActionExecutor executor = AbstractAction.skip;

        public Skip(FileStoringRow entity) {
            super(entity, "");
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
    public static class MigrateFields extends RowAction {
        @Autowired
        private RowMigrator rowMigrator;

        public MigrateFields(FileStoringRow entity, String reason, List<FieldAction> fieldActions) {
            super(entity, reason, fieldActions);
        }

        @Override
        protected void executeInternal() {
            rowMigrator.executeInternal(this);
        }

        @Override
        public void rollback() {
            rowMigrator.rollback(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder(super.toString());
        outp.append(":");
        for (FieldAction fieldAction : getFieldActions()) {
            outp.append("\n\t").append(fieldAction);
        }
        return outp.toString();
    }
}
