package dbFilesMigration;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.dmkuranov.dbFilesMigration.domain.FileInfo;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringField;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringRow;
import ru.dmkuranov.dbFilesMigration.domain.FileStoringTable;
import ru.dmkuranov.dbFilesMigration.service.FileInfoProvider;
import ru.dmkuranov.dbFilesMigration.service.ProcessingDecider;
import ru.dmkuranov.dbFilesMigration.service.processing.FieldAction;
import ru.dmkuranov.dbFilesMigration.service.processing.RowAction;
import ru.dmkuranov.dbFilesMigration.util.StoredPathConverter;
import dbFilesMigration.util.InstanceCreator;

import java.io.File;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ProcessingDeciderTest extends SpringContextConfiguration {
    @Autowired
    private ProcessingDecider decider;
    @Autowired
    private FileInfoProvider fileInfoProvider;
    @Autowired
    private InstanceCreator instanceCreator;
    @Value("${app.targetStoredPath}")
    private String targetStoredPath;

    @Test
    public void deciderMustSkipNullsOrEmptyTest() {
        FileStoringRow row = instanceCreator.createStoredRow(table1, 2, null, "", " ");
        RowAction action = decider.decide(row);
        for (FieldAction fieldAction : action.getFieldActions()) {
            assertThat(fieldAction, instanceOf(FieldAction.Skip.class));
        }
        assertThat(action, instanceOf(RowAction.Skip.class));
    }

    @Test
    public void rowActionMigrate() {
        FileStoringRow row = instanceCreator.createStoredRow(table1, 3
                , /* 0 */ null
                , /* 1 */ "sdfsg23"
                , /* 2 */ ""
                , /* 3 */ " "
        );
        RowAction action = decider.decide(row);
        assertThat(action, instanceOf(RowAction.MigrateFields.class));
        int index = 0;
        for (FieldAction fieldAction : action.getFieldActions()) {
            Assert.assertEquals(fieldAction.getRowAction(), action);
            if (index == 1) {
                assertThat(fieldAction, instanceOf(FieldAction.Migrate.class));
            } else {
                assertThat(fieldAction, instanceOf(FieldAction.Skip.class));
            }
            index++;
        }
    }

    @Test
    public void pathFormatting() {
        String fileSystemPath = targetStoredPath + File.separator
                + table1PathPrefix + File.separator
                + "0000100" + File.separator + "0000120" + "/0000123\\"
                + table1FilePrefix + "_" + table1Field2FieldName + ".tst";
        ;
        String storedPath = StoredPathConverter.toStoredPath(fileSystemPath);
        FileStoringRow row = instanceCreator.createStoredRow(table1, 123,
                "", storedPath);
        RowAction action = decider.decide(row);
        assertThat(action, instanceOf(RowAction.Skip.class));
    }

    @Test
    public void storedPathWebOptionsPreservation() {
        String extension = "j_2";
        String webOption = "?123,31243";
        String storedPath = "/dir/path/filename."+extension+webOption;
        FileStoringRow row = instanceCreator.createStoredRow(table1, 123,
                "", storedPath);
        RowAction action = decider.decide(row);

        assertThat(action, instanceOf(RowAction.MigrateFields.class));
        FileStoringField firstField = action.getFieldActions().get(0).getEntity();
        fileInfoProvider.provideFileInfo(firstField, storedPath);
        FileInfo fileInfo = firstField.getFileInfo();
        Assert.assertNotNull(fileInfo);
        String targetStoredPath = fileInfo.getTargetStoredFilePath();
        Assert.assertNotEquals(storedPath, targetStoredPath);
        int lastDotIndex = targetStoredPath.lastIndexOf(".");
        String afterLastDotStoredPart = targetStoredPath.substring(lastDotIndex + 1);
        Assert.assertEquals(afterLastDotStoredPart, extension+webOption);
    }

    private static final String table1Name = "testTable1";
    private static final String table1IdFieldName = "testTable1Id";
    private static final String table1PathPrefix = "testTable1Files";
    private static final String table1FilePrefix = "testTable1File";
    private static final String table1Field1FieldName = "testTable1File1";
    private static final String table1Field2FieldName = "testTable1File2";

    private static final FileStoringTable table1 = new FileStoringTable(table1Name, table1IdFieldName, table1PathPrefix, table1FilePrefix, new ArrayList<String>() {{
        add(table1Field1FieldName);
        add(table1Field2FieldName);
        add("appendix");
    }});


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

}