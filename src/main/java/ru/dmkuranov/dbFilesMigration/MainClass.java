package ru.dmkuranov.dbFilesMigration;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dmkuranov.dbFilesMigration.util.StartupDialog;
import ru.dmkuranov.dbFilesMigration.service.ProcessingPipeline;

public class MainClass {
    public static void main(String[] args) {
        new MainClass();
    }

    public MainClass() {
        StartupDialog.performInteraction();
        ClassPathXmlApplicationContext fullContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ProcessingPipeline pipeline = fullContext.getBean(ProcessingPipeline.class);
        pipeline.migrateAll();
    }
}
