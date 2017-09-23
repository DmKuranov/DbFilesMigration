package ru.dmkuranov.dbFilesMigration.service.processing;

public abstract class AbstractAction<T> {
    private String reason;
    private Boolean executedSuccessful;
    private Throwable failureReason;
    private T entity;

    public AbstractAction(T entity, String reason) {
        this.entity = entity;
        this.reason = reason;
    }

    public void execute() {
        try {
            executeInternal();
            setExecutedSuccessful(true);
        } catch (Exception e) {
            setFailureReason(e);
            setExecutedSuccessful(false);
        }
    }

    protected abstract void executeInternal();

    public abstract void rollback();

    private static class UnexecutableExecutor<T> implements ActionExecutor<T> {
        public void executeInternal(T entity) {
            throw new RuntimeException("Cannot execute unknown action for "+entity);
        }

        public void rollback(T entity) {
            throw new RuntimeException("Cannot rollback unknown action for "+entity);
        }
    }

    protected static ActionExecutor unexecutable = new UnexecutableExecutor();

    private static class SkipExecutor<T> implements ActionExecutor<T> {
        public void executeInternal(T entity) {
            // no op
        }

        public void rollback(T entity) {
            // no op
        }
    }

    protected static ActionExecutor skip = new SkipExecutor();

    public String getReason() {
        return reason;
    }

    public Boolean getExecutedSuccessful() {
        return executedSuccessful;
    }

    protected void setExecutedSuccessful(boolean executedSuccessful) {
        this.executedSuccessful = executedSuccessful;
    }

    public Throwable getFailureReason() {
        return failureReason;
    }

    protected void setFailureReason(Throwable failureReason) {
        this.failureReason = failureReason;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" on "+getEntity()+", "+getReason();
    }
}
