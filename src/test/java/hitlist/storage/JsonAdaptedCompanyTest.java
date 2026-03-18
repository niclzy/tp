package hitlist.storage;

import static hitlist.storage.JsonAdaptedCompany.MISSING_FIELD_MESSAGE_FORMAT;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalCompanies.GOOGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;

public class JsonAdaptedCompanyTest {

    private static final String INVALID_NAME = " Google"; // starts with space - INVALID
    private static final String INVALID_NAME_SLASH = "Google/Maps"; // contains slash - INVALID
    private static final String INVALID_NAME_EMPTY = ""; // empty - INVALID
    private static final String INVALID_DESCRIPTION = " "; // blank - INVALID
    private static final String INVALID_DESCRIPTION_SLASH = "Description with / slash"; // contains slash - INVALID

    private static final String VALID_NAME = GOOGLE.getName().toString();
    private static final String VALID_DESCRIPTION = GOOGLE.getDescription().toString();
    private static final List<JsonAdaptedRole> VALID_ROLES = GOOGLE.getUniqueRoleList()
            .asUnmodifiableObservableList().stream()
            .map(JsonAdaptedRole::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validCompanyDetails_returnsCompany() throws Exception {
        JsonAdaptedCompany company = new JsonAdaptedCompany(GOOGLE);
        assertEquals(GOOGLE, company.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(INVALID_NAME, VALID_DESCRIPTION, VALID_ROLES);
        String expectedMessage = CompanyName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_invalidNameWithSlash_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(INVALID_NAME_SLASH, VALID_DESCRIPTION, VALID_ROLES);
        String expectedMessage = CompanyName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(null, VALID_DESCRIPTION, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, CompanyName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(VALID_NAME, INVALID_DESCRIPTION, VALID_ROLES);
        String expectedMessage = CompanyDescription.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_invalidDescriptionWithSlash_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(VALID_NAME, INVALID_DESCRIPTION_SLASH, VALID_ROLES);
        String expectedMessage = CompanyDescription.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedCompany company = new JsonAdaptedCompany(VALID_NAME, null, VALID_ROLES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, CompanyDescription.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, company::toModelType);
    }

    @Test
    public void toModelType_invalidRoles_throwsIllegalValueException() {
        List<JsonAdaptedRole> invalidRoles = new ArrayList<>(VALID_ROLES);
        invalidRoles.add(new JsonAdaptedRole(" Role", "Valid Description")); // starts with space - INVALID
        JsonAdaptedCompany company = new JsonAdaptedCompany(VALID_NAME, VALID_DESCRIPTION, invalidRoles);
        assertThrows(IllegalValueException.class, company::toModelType);
    }
}
