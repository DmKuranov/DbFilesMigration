package ru.dmkuranov.dbFilesMigration.util;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dmkuranov.dbFilesMigration.service.processing.executors.FieldMigrationStrategy;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringTable;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class StartupDialog {
    public static void performInteraction() {
        System.out.println(prompt);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("data-config.xml");
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        String databaseUrl = beanFactory.resolveEmbeddedValue("${db.url}");
        String sourceFilesystemPath = beanFactory.resolveEmbeddedValue("${app.sourceFilesystemPath}");
        String targetFilesystemPath = beanFactory.resolveEmbeddedValue("${app.targetFilesystemPath}")
                + File.separator + beanFactory.resolveEmbeddedValue("${app.targetStoredPath}");
        System.out.println("database: "+databaseUrl);
        System.out.println("source path: "+sourceFilesystemPath);
        System.out.println("source path: "+targetFilesystemPath);
        List<FileStoringTable> tables = context.getBean("fileTablesList", List.class);
        for (FileStoringTable table : tables) {
            System.out.println("Table " + table.getTableName() + ", key field " + table.getIdFieldName());
            for(String fieldName : table.getFileFieldNames()) {
                System.out.println("\t"+fieldName);
            }
        }

        List<FieldMigrationStrategy> fieldMigrationStrategies = context.getBean("fieldMigrationStrategies", List.class);
        System.out.println("Choose migration strategy: ");
        for(FieldMigrationStrategy fieldMigrationStrategy : fieldMigrationStrategies) {
            System.out.println(String.valueOf(fieldMigrationStrategies.indexOf(fieldMigrationStrategy))
                    + ". " + ""+ fieldMigrationStrategy.getDescription()+" ("+ fieldMigrationStrategy.getClass().getSimpleName()+")");
        }
        System.out.println("Enter index number(default is first)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        FieldMigrationStrategy strategy = fieldMigrationStrategies.get(0);
        try {
            Integer index = Integer.parseInt(input);
            FieldMigrationStrategy chosen = fieldMigrationStrategies.get(index);
            strategy = chosen;
        } catch (NumberFormatException e) {
            // swallow it
        } catch (IndexOutOfBoundsException e) {
            // swallow it
        }
        System.out.println("Using strategy "+strategy.getClass().getSimpleName());
        System.setProperty("fieldMigrationStrategyClass", strategy.getClass().getCanonicalName());

        context.close();
    }

    private static String prompt = "This app can relocate files, links to which stored in database fields:";
}
