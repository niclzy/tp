package hitlist.storage;

import static hitlist.storage.JsonAdaptedRole.MISSING_FIELD_MESSAGE_FORMAT;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalRoles.SOFTWARE_ENGINEER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;

public class JsonAdaptedRoleTest {

    private static final String INVALID_NAME = " Engineer"; // starts with space - INVALID
    private static final String INVALID_NAME_SLASH = "Software/Engineer"; // contains slash - INVALID
    private static final String INVALID_DESCRIPTION = " "; // blank - INVALID
    private static final String INVALID_DESCRIPTION_SLASH = "Description with / slash"; // contains slash - INVALID

    private static final String VALID_NAME = SOFTWARE_ENGINEER.getRoleName().toString();
    private static final String VALID_DESCRIPTION = SOFTWARE_ENGINEER.getRoleDescription().toString();

    @Test
    public void toModelType_validRoleDetails_returnsRole() throws Exception {
        JsonAdaptedRole role = new JsonAdaptedRole(SOFTWARE_ENGINEER);
        assertEquals(SOFTWARE_ENGINEER, role.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(INVALID_NAME, VALID_DESCRIPTION);
        String expectedMessage = RoleName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }

    @Test
    public void toModelType_invalidNameWithSlash_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(INVALID_NAME_SLASH, VALID_DESCRIPTION);
        String expectedMessage = RoleName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(null, VALID_DESCRIPTION);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, RoleName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(VALID_NAME, INVALID_DESCRIPTION);
        String expectedMessage = RoleDescription.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }

    @Test
    public void toModelType_invalidDescriptionWithSlash_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(VALID_NAME, INVALID_DESCRIPTION_SLASH);
        String expectedMessage = RoleDescription.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedRole role = new JsonAdaptedRole(VALID_NAME, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, RoleDescription.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, role::toModelType);
    }
}
