package ru.dmkuranov.dbFilesMigration.domain;

import java.util.Set;

public class FileStoringRow {
    private Integer id;
    private FileStoringTable table;
    private Set<FileStoringField> fields;

    public FileStoringRow(Integer id, FileStoringTable table) {
        this.id = id;
        this.table = table;
    }

    public Integer getId() {
        return id;
    }

    public FileStoringTable getTable() {
        return table;
    }

    public void setFields(Set<FileStoringField> fields) {
        this.fields = fields;
    }

    public Set<FileStoringField> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder();
        outp.append(" table ").append(table.getTableName()).append(", ");
        outp.append(table.getIdFieldName()).append("=").append(id);
        return outp.toString();
    }
}
