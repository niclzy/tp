package hitlist.model.company;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CompanyNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new CompanyName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> CompanyName.isValidName(null));

        // invalid name
        assertFalse(CompanyName.isValidName("")); // empty string
        assertFalse(CompanyName.isValidName(" ")); // spaces only
        assertFalse(CompanyName.isValidName("/")); // forward slash only
        assertFalse(CompanyName.isValidName("Company/Name")); // contains forward slash
        assertFalse(CompanyName.isValidName("Company\nName")); // contains line break

        // valid name
        assertTrue(CompanyName.isValidName("Google")); // alphabets only
        assertTrue(CompanyName.isValidName("12345")); // numbers only
        assertTrue(CompanyName.isValidName("Google Inc.")); // alphanumeric characters and punctuation
        assertTrue(CompanyName.isValidName("Google LLC")); // with capital letters
        assertTrue(CompanyName.isValidName("Google")); // short name
        assertTrue(CompanyName.isValidName("Google Google Google Google Go")); // long name (30
    }

    @Test
    public void toString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CompanyName(null).toString());
    }

    @Test
    public void toString_validName_returnsName() {
        CompanyName name = new CompanyName("Valid Company Name");
        assertTrue(name.toString().equals("Valid Company Name"));
    }

    @Test
    public void equals() {
        CompanyName name = new CompanyName("Valid Company Name");

        // same values -> returns true
        assertTrue(name.equals(new CompanyName("Valid Company Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new CompanyName("Other Valid Company Name")));
    }

    @Test
    public void hashCodeTest() {
        CompanyName name1 = new CompanyName("Valid Company Name");
        CompanyName name2 = new CompanyName("Valid Company Name");
        CompanyName name3 = new CompanyName("Other Valid Company Name");

        // same values -> same hash code
        assertEquals(name1.hashCode(), name2.hashCode());

        // different values -> different hash code
        assertNotEquals(name1.hashCode(), name3.hashCode());
    }
}
