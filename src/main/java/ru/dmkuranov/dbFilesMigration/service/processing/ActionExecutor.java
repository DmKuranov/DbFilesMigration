package ru.dmkuranov.dbFilesMigration.service.processing;

public interface ActionExecutor<T> {
    void executeInternal(T entity);
    void rollback(T entity);
}
