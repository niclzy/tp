package hitlist.model.company;

import static hitlist.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * A test class for {@code Description}.
 */
public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid description
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only
        assertFalse(Description.isValidDescription("/")); // forward slash only
        assertFalse(Description.isValidDescription("Company/Name")); // contains forward slash
        assertFalse(Description.isValidDescription("Company\nDescription")); // contains line break

        // valid description
        assertTrue(Description.isValidDescription("Google")); // alphabets only
        assertTrue(Description.isValidDescription("12345")); // numbers only
        assertTrue(Description.isValidDescription("An American multinational technology company with headquarters "
                + "in Redmond, Washington. It develops, manufactures, licenses, supports and sells computer software, "
                + "consumer electronics, personal computers, and related services")); // long description
    }
}
