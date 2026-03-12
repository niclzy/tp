package hitlist.model.company;

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
        assertFalse(Name.isValidName("Company/Name")); // contains forward slash
        assertFalse(Name.isValidName("Company\nName")); // contains line break

        // valid name
        assertTrue(Name.isValidName("Google")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("Google Inc.")); // alphanumeric characters and punctuation
        assertTrue(Name.isValidName("Google LLC")); // with capital letters
        assertTrue(Name.isValidName("Google")); // short name
        assertTrue(Name.isValidName("Google Google Google Google Go")); // long name (30
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Company Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Company Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Company Name")));
    }
}
