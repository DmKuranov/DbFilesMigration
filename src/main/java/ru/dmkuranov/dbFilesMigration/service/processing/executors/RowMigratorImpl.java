package ru.dmkuranov.dbFilesMigration.service.processing.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RowMigratorImpl implements RowMigrator {
    private static final Logger log = LoggerFactory.getLogger(RowMigratorImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeInternal(RowAction.MigrateFields rowAction) {
        FileStoringRow row = rowAction.getEntity();
        List<FieldAction> fieldActions = rowAction.getFieldActions();
        for (FieldAction fieldAction : fieldActions) {
            fieldAction.execute();
            if (!fieldAction.getExecutedSuccessful()) {
                throw new RuntimeException("Nested action execution failure: "+fieldAction+". Reason "+fieldAction.getFailureReason());
            }
        }
        Map<String, String> updatedFields = rowAction.getUpdatedFields();
        if(!updatedFields.isEmpty()) {
            boolean commaRequired = false;
            StringBuilder updateSb = new StringBuilder("update [").append(row.getTable().getTableName()).append("] set ");
            List bindVars = new ArrayList();
            for(Map.Entry<String, String> updatedEntry : updatedFields.entrySet()) {
                if(commaRequired) {
                    updateSb.append(", ");
                }
                updateSb.append("[").append(updatedEntry.getKey()).append("]=?");
                bindVars.add(updatedEntry.getValue());
                commaRequired = true;
            }
            updateSb.append(" where [").append(row.getTable().getIdFieldName()).append("]=?");
            bindVars.add(row.getId());
            String update = updateSb.toString();
            jdbcTemplate.update(update, bindVars.toArray());
            log.debug("Executing update: "+update+", using: "+bindVars);
        }
    }

    @Override
    public void rollback(RowAction.MigrateFields entity) {
        List<FieldAction> fieldsToRevert = new ArrayList<FieldAction>();
        for(FieldAction fieldAction : entity.getFieldActions()) {
            if(Boolean.TRUE.equals(fieldAction.getExecutedSuccessful())) {
                fieldsToRevert.add(fieldAction);
            }
        }
        log.debug("Rolling back fields: "+fieldsToRevert);
        for(FieldAction fieldAction : fieldsToRevert) {
            try {
                fieldAction.rollback();
            } catch (Exception e) {
                log.error("Exception rolling back field "+fieldAction);
            }
        }
    }
}
