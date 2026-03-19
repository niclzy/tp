package hitlist.storage;

import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static hitlist.testutil.TypicalCompanies.MICROSOFT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.commons.util.JsonUtil;
import hitlist.model.HitList;
import hitlist.testutil.TypicalGroups;
import hitlist.testutil.TypicalPersons;

public class JsonSerializableHitListTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableHitListTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsHitList.json");
    private static final Path TYPICAL_GROUPS_FILE = TEST_DATA_FOLDER.resolve("typicalGroupsHitList.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonHitList.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonHitList.json");
    private static final Path DUPLICATE_GROUP_FILE = TEST_DATA_FOLDER.resolve("duplicateGroupHitList.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableHitList dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableHitList.class).get();
        HitList hitListFromFile = dataFromFile.toModelType();
        HitList typicalPersonsHitList = TypicalPersons.getTypicalHitList();
        assertEquals(hitListFromFile, typicalPersonsHitList);
    }

    @Test
    public void toModelType_typicalGroupsFile_success() throws Exception {
        JsonSerializableHitList dataFromFile = JsonUtil.readJsonFile(TYPICAL_GROUPS_FILE,
                JsonSerializableHitList.class).get();
        HitList hitListFromFile = dataFromFile.toModelType();
        HitList typicalGroupsHitList = TypicalGroups.getTypicalHitList();
        assertEquals(hitListFromFile, typicalGroupsHitList);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableHitList dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableHitList.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableHitList dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableHitList.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableHitList.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateCompanies_throwsIllegalValueException() throws Exception {
        List<JsonAdaptedCompany> duplicateCompanies = new ArrayList<>();
        duplicateCompanies.add(new JsonAdaptedCompany(GOOGLE));
        duplicateCompanies.add(new JsonAdaptedCompany(GOOGLE));

        JsonSerializableHitList dataFromFile = new JsonSerializableHitList(
                new ArrayList<>(),
                duplicateCompanies,
                new ArrayList<>());

        assertThrows(IllegalValueException.class,
                JsonSerializableHitList.MESSAGE_DUPLICATE_COMPANY,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_validCompanies_success() throws Exception {
        List<JsonAdaptedCompany> validCompanies = new ArrayList<>();
        validCompanies.add(new JsonAdaptedCompany(GOOGLE));
        validCompanies.add(new JsonAdaptedCompany(MICROSOFT));

        JsonSerializableHitList dataFromFile = new JsonSerializableHitList(
                new ArrayList<>(),
                validCompanies,
                new ArrayList<>());

        HitList hitList = dataFromFile.toModelType();
        assertEquals(2, hitList.getCompanyList().size());
        assertEquals(GOOGLE, hitList.getCompanyList().get(0));
        assertEquals(MICROSOFT, hitList.getCompanyList().get(1));
    }

    @Test
    public void toModelType_duplicateGroups_throwsIllegalValueException() throws Exception {
        JsonSerializableHitList dataFromFile = JsonUtil.readJsonFile(DUPLICATE_GROUP_FILE,
                JsonSerializableHitList.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableHitList.MESSAGE_DUPLICATE_GROUP,
                dataFromFile::toModelType);
    }
}
