package ru.dmkuranov.dbFilesMigration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.domain.FileInfo;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.service.processing.ActionFactory;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessingDecider {
    @Autowired
    private ActionFactory actionFactory;

    public RowAction decide(FileStoringRow fileStoringRow) {
        RowAction unknownAction = actionFactory.unknownRow(fileStoringRow);
        RowAction rowAction = unknownAction;
        List<FieldAction> fieldActions = new ArrayList<FieldAction>();
        boolean rowProcessingRequired = false;
        for (FileStoringField field : fileStoringRow.getFields()) {
            FieldAction fieldAction = actionFactory.unknownField(field);
            FileInfo fileInfo = field.getFileInfo();
            String sourcePath = fileInfo.getSourceStoredFilePath();
            if (sourcePath == null || sourcePath.trim().length() == 0) {
                fieldAction = actionFactory.skipField(field, "path empty");
            } else {
                String storedPath = fileInfo.getTargetStoredFilePath();
                if (sourcePath.equals(storedPath)) {
                    fieldAction = actionFactory.skipField(field, "no correction required");
                } else {
                    if(!fileInfo.getExist()) {
                        fieldAction = actionFactory.skipField(field, "file not exist");
                    } else {
                        rowProcessingRequired = true;
                        if (rowAction == unknownAction) {
                            rowAction = actionFactory.migrateRow(fileStoringRow, "child field requires migration");
                        }
                        fieldAction = actionFactory.migrateField(field, "source != target");
                    }
                }
            }

            fieldActions.add(fieldAction);
        }
        if (!rowProcessingRequired) {
            rowAction = actionFactory.skipRow(fileStoringRow);
        }
        rowAction.getFieldActions().addAll(fieldActions);
        for (FieldAction fieldAction : fieldActions) {
            fieldAction.setRowAction(rowAction);
        }
        return rowAction;
    }
}
