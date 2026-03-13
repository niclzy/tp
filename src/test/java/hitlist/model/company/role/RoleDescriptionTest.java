package hitlist.model.company.role;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RoleDescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoleDescription(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new RoleDescription(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> RoleDescription.isValidDescription(null));

        // invalid description
        assertFalse(RoleDescription.isValidDescription("")); // empty string
        assertFalse(RoleDescription.isValidDescription(" ")); // spaces only
        assertFalse(RoleDescription.isValidDescription("/")); // forward slash only
        assertFalse(RoleDescription.isValidDescription("Role/Description")); // contains forward slash
        assertFalse(RoleDescription.isValidDescription("Role\nDescription")); // contains line break

        // valid description
        assertTrue(RoleDescription.isValidDescription("Software Engineer")); // alphabets only
        assertTrue(RoleDescription.isValidDescription("12345")); // numbers only
        assertTrue(RoleDescription.isValidDescription("An entry-level software engineering role responsible for "
                + "developing and maintaining software applications.")); // long description
    }

    @Test
    public void toString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RoleDescription(null).toString());
    }

    @Test
    public void toString_validDescription_returnsDescription() {
        RoleDescription description = new RoleDescription("Valid Role Description");
        assertEquals("Valid Role Description", description.toString());
    }

    @Test
    public void equals() {
        RoleDescription description = new RoleDescription("Valid Role Description");

        // same values -> returns true
        assertTrue(description.equals(new RoleDescription("Valid Role Description")));

        // same object -> returns true
        assertTrue(description.equals(description));

        // null -> returns false
        assertFalse(description.equals(null));

        // different types -> returns false
        assertFalse(description.equals(5.0f));

        // different values -> returns false
        assertFalse(description.equals(new RoleDescription("Other Valid Role Description")));
    }

    @Test
    public void hashCodeTest() {
        RoleDescription description1 = new RoleDescription("Valid Role Description");
        RoleDescription description2 = new RoleDescription("Valid Role Description");
        RoleDescription description3 = new RoleDescription("Other Valid Role Description");

        // same values -> same hash code
        assertEquals(description1.hashCode(), description2.hashCode());

        // different values -> different hash code
        assertNotEquals(description1.hashCode(), description3.hashCode());
    }
}
