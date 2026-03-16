package hitlist.storage;

import static hitlist.storage.JsonAdaptedGroup.MISSING_FIELD_MESSAGE_FORMAT;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalGroups.STUDENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;

public class JsonAdaptedGroupTest {
    private static final String INVALID_NAME = "A&B Group";

    private static final String VALID_NAME = STUDENTS.getName().toString();

    @Test
    public void toModelType_validGroupDetails_returnsGroup() throws Exception {
        JsonAdaptedGroup group = new JsonAdaptedGroup(STUDENTS);
        assertEquals(STUDENTS, group.toModelType());
    }

    @Test
    public void toModelType_nullGroupName_throwsIllegalValueException() {
        JsonAdaptedGroup group = new JsonAdaptedGroup(null, null); // TODO
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, GroupName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, group::toModelType);
    }
}
