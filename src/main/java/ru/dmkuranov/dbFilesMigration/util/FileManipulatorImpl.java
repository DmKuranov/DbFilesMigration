package ru.dmkuranov.dbFilesMigration.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Profile("!test")
public class FileManipulatorImpl implements FileManipulator {
    private static final Logger log = LoggerFactory.getLogger(FileManipulatorImpl.class);

    @Override
    public String copyFile(String sourceFileName, String targetFileName) {
        log.debug("Copy "+sourceFileName+" -> "+targetFileName);
        try {
            File target = new File(targetFileName);
            File source = new File(sourceFileName);
            FileUtils.copyFile(source, target);
            return targetFileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String moveFile(String sourceFileName, String targetFileName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteFile(String fileName) {
        log.debug("Remove "+fileName);
        new File(fileName).delete();
    }

    @Override
    public boolean isExist(String fileName) {
        return new File(fileName).exists();
    }
}
