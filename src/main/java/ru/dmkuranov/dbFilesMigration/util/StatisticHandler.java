package ru.dmkuranov.dbFilesMigration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.service.processing.AbstractAction;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class StatisticHandler {
    private static final Logger log = LoggerFactory.getLogger(StatisticHandler.class);
    private int iterationsCounter = 0;
    private int iterationsInterval = 99;
    StatsMap statsMap = new StatsMap();

    public synchronized void handle(RowAction action) {
        if (log.isInfoEnabled()) {
            statsMap.addEntry(action);
            iterationsCounter++;
            if (iterationsCounter >= iterationsInterval) {
                log.info(statsMap.toString());
                iterationsCounter = 0;
            }
        }
    }

    private static class StatsMap {
        private Map<String, Long> countByDescr = new HashMap<String, Long>();
        private Comparator<Comparable> reverseUnsafeComparator = new Comparator<Comparable>() {
            @Override
            public int compare(Comparable o1, Comparable o2) {
                return -o1.compareTo(o2);
            }
        };

        void addEntry(AbstractAction action) {
            addEntryInternal(action);
            if (action instanceof RowAction) {
                RowAction rowAction = (RowAction) action;
                for (FieldAction fieldAction : rowAction.getFieldActions()) {
                    addEntryInternal(fieldAction);
                }
            }
        }

        private void addEntryInternal(AbstractAction action) {
            String key = getKey(action);
            Long count = countByDescr.get(key);
            if (count == null) {
                countByDescr.put(key, 1L);
            } else {
                countByDescr.put(key, count + 1);
            }
        }

        private String getKey(AbstractAction action) {
            return ((action instanceof RowAction ? "Row " : "Field ")) +
                    action.getClass().getSimpleName() + "(" + action.getReason() + ")";
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder("Action stats: ");
            Map<Long, String> byCountMap = new TreeMap<Long, String>(reverseUnsafeComparator);
            for (Map.Entry<String, Long> entry : countByDescr.entrySet()) {
                byCountMap.put(entry.getValue(), entry.getKey());
            }
            for (Map.Entry<Long, String> entry : byCountMap.entrySet()) {
                output.append(entry.getValue()).append(":").append(entry.getKey()).append(", ");
            }
            return output.toString();
        }
    }
}
