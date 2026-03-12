package hitlist.model.company.role;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("/")); // forward slash only
        assertFalse(Name.isValidName("Role/Name")); // contains forward slash
        assertFalse(Name.isValidName("Role\nName")); // contains line break

        // valid name
        assertTrue(Name.isValidName("Software Engineer")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("Software Engineer I")); // alphanumeric characters and punctuation
        assertTrue(Name.isValidName("Software Engineer II")); // with capital letters
        assertTrue(Name.isValidName("SE I")); // short name
        assertTrue(Name.isValidName("Software Engineer I Software E")); // long name (30 characters)
    }
}
