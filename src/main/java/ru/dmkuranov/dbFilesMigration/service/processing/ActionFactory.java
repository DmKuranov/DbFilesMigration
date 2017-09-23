package ru.dmkuranov.dbFilesMigration.service.processing;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionFactory {
    @Autowired
    private BeanFactory beanFactory;

    public RowAction unknownRow(FileStoringRow row) {
        return beanFactory.getBean(RowAction.Unknown.class, row);
    }

    public FieldAction unknownField(FileStoringField field) {
        return beanFactory.getBean(FieldAction.Unknown.class, field);
    }

    public RowAction migrateRow(FileStoringRow entity, String reason) {
        List<FieldAction> fieldActions = new ArrayList<FieldAction>();
        return beanFactory.getBean(RowAction.MigrateFields.class, entity, reason, fieldActions);
    }

    public FieldAction migrateField(FileStoringField entity, String reason) {
        return beanFactory.getBean(FieldAction.Migrate.class, entity, reason);
    }

    public RowAction skipRow(FileStoringRow entity) {
        return beanFactory.getBean(RowAction.Skip.class, entity);
    }

    public FieldAction skipField(FileStoringField field, String reason) {
        return beanFactory.getBean(FieldAction.Skip.class, field, reason);
    }

}
