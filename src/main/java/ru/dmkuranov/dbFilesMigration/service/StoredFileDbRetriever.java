package ru.dmkuranov.dbFilesMigration.service;

import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;

public interface StoredFileDbRetriever {
    FileStoringRow next();
}
