package ru.dmkuranov.dbFilesMigration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringTable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Profile("!test")
public class StoredFileDbRetrieverImpl implements StoredFileDbRetriever {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FileInfoProvider fileInfoProvider;

    @Resource(name = "fileTablesList")
    private List<FileStoringTable> fileTablesList;

    private Iterator<FileStoringTable> tableIterator;
    private FileStoringTable currentTable;
    private SqlRowSet rowSet;


    @PostConstruct
    public void postConstruct() {
        tableIterator = fileTablesList.iterator();
    }

    @Override
    @Transactional(readOnly = true)
    public synchronized FileStoringRow next() {
        tableIterationLoop:
        if (rowSet == null || !rowSet.next()) {
            while (tableIterator.hasNext()) {
                currentTable = tableIterator.next();
                StringBuilder query = new StringBuilder("select [").append(currentTable.getIdFieldName()).append("]");
                for (String fieldName : currentTable.getFileFieldNames()) {
                    query.append(", [").append(fieldName).append("]");
                }
                query.append(" from [").append(currentTable.getTableName()).append("]");
                query.append(" order by [").append(currentTable.getIdFieldName()).append("]");
                rowSet = jdbcTemplate.queryForRowSet(query.toString());
                if (rowSet.next()) {
                    break tableIterationLoop;
                }
            }
            return null;
        }
        FileStoringRow row = new FileStoringRow(rowSet.getInt(currentTable.getIdFieldName()), currentTable);
        Set<FileStoringField> fields = new LinkedHashSet<FileStoringField>();
        for (String fieldName : currentTable.getFileFieldNames()) {
            FileStoringField fileStoringField = new FileStoringField(row, fieldName);
            String storedPath = rowSet.getString(fieldName);
            fileInfoProvider.provideFileInfo(fileStoringField, storedPath);
            fields.add(fileStoringField);
        }
        row.setFields(fields);
        return row;
    }
}
