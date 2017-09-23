package ru.dmkuranov.dbFilesMigration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;
import ru.dmkuranov.dbFilesMigration.util.StatisticHandler;

@Service
@Profile("!test")
public class ProcessingPipeline {
    private static final Logger log = LoggerFactory.getLogger(ProcessingPipeline.class);

    @Autowired
    private StoredFileDbRetriever storedFileDbRetriever;
    @Autowired
    private ProcessingDecider processingDecider;
    @Autowired
    private StatisticHandler statisticHandler;

    public void migrateAll() {
        FileStoringRow row = storedFileDbRetriever.next();
        int i = 1;
        while (row != null) {
            RowAction rowAction = processingDecider.decide(row);
            statisticHandler.handle(rowAction);
            if (log.isTraceEnabled()) {
                log.trace(rowAction.toString());
            }
            rowAction.execute();
            if (!rowAction.getExecutedSuccessful()) {
                log.error("Processing failure: " + rowAction.getFailureReason());
                log.info("Rows processed: " + i);
                rowAction.rollback();
                break;
            }
            row = storedFileDbRetriever.next();
            i++;
        }
        log.info("Successfully processed rows: " + i);
    }
}
