package dbFilesMigration.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.dmkuranov.dbFilesMigration.util.FileManipulator;

@Service
@Profile("test")
public class FileManipulatorTest implements FileManipulator {
    @Override
    public String copyFile(String sourceFileName, String targetFileName) {
        return null;
    }

    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public boolean isExist(String fileName) {
        return true;
    }

    @Override
    public String moveFile(String sourceFileName, String targetFileName) {
        return null;
    }
}
