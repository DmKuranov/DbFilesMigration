package dbFilesMigration.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringTable;
import ru.dmkuranov.dbFilesMigration.service.FileInfoProvider;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Profile("test")
public class InstanceCreator {
    @Autowired
    private FileInfoProvider fileInfoProvider;

    public FileStoringRow createStoredRow(FileStoringTable table, Integer id, String... fieldValues) {
        FileStoringRow row = new FileStoringRow(id, table);
        Iterator<String> fieldNameIterator = table.getFileFieldNames().iterator();
        Iterator<String> fieldValueIterator = Arrays.asList(fieldValues).iterator();
        Set<FileStoringField> fieldSet = new LinkedHashSet<FileStoringField>();
        while(fieldNameIterator.hasNext() && fieldValueIterator.hasNext()) {
            String storedPath = fieldValueIterator.next();
            FileStoringField field = new FileStoringField(row, fieldNameIterator.next());
            fileInfoProvider.provideFileInfo(field, storedPath);
            fieldSet.add(field);
        }
        row.setFields(fieldSet);
        return row;
    }

}
