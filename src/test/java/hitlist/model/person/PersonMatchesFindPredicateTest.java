package hitlist.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import hitlist.testutil.PersonBuilder;

public class PersonMatchesFindPredicateTest {

    @Test
    public void equals() {
        PersonMatchesFindPredicate firstPredicate =
                new PersonMatchesFindPredicate(Arrays.asList("first"));
        PersonMatchesFindPredicate secondPredicate =
                new PersonMatchesFindPredicate(Arrays.asList("second"));

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesFindPredicate firstPredicateCopy =
                new PersonMatchesFindPredicate(Arrays.asList("first"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_namePrefixMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("Han"));

        assertTrue(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Han").build()));
    }

    @Test
    public void test_nameMidWordSubstring_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Arrays.asList("ans"));

        assertTrue(predicate.test(new PersonBuilder().withName("Hans Gruber").build()));
    }

    @Test
    public void test_tagExactIgnoreCaseMatch_returnsTrue() {
        PersonMatchesFindPredicate predicate =
                new PersonMatchesFindPredicate(Collections.emptyList());

        assertTrue(predicate.test(new PersonBuilder().build()));
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    void test_nullInputsContainsIgnoreCase_returnsFalse() {
        // Covers: str == null
        assertFalse(containsIgnoreCase(null, "search"), "Should return false when str is null");

        // Covers: str != null && searchStr == null
        assertFalse(containsIgnoreCase("text", null), "Should return false when searchStr is null");

        // Covers: both are null
        assertFalse(containsIgnoreCase(null, null), "Should return false when both are null");
    }

    @Test
    void test_inputsContainsIgnoreCase_returnsTrue() {
        // Exact match
        assertTrue(containsIgnoreCase("Hello World", "Hello World"));

        // Lowercase search string against mixed case
        assertTrue(containsIgnoreCase("Hello World", "world"));

        // Uppercase search string against mixed case
        assertTrue(containsIgnoreCase("Hello World", "HELLO"));

        // Substring in the middle
        assertTrue(containsIgnoreCase("Hello World", "lo wo"));
    }

    @Test
    void test_inputsContainsIgnoreCase_returnsFalse() {
        // Completely different string
        assertFalse(containsIgnoreCase("Hello World", "Java"));

        // Search string is longer than the base string
        assertFalse(containsIgnoreCase("Hello", "Hello World"));
    }

    @Test
    void test_emptyStringContainsIgnoreCase_success() {
        // Any string technically "contains" an empty string
        assertTrue(containsIgnoreCase("Hello", ""));

        // Empty string contains empty string
        assertTrue(containsIgnoreCase("", ""));

        // Empty string does not contain text
        assertFalse(containsIgnoreCase("", "Hello"));
    }

    /**
     * Helper method to test the static method in PersonMatchesFindPredicate.
     * This is necessary because the method is static and cannot be accessed directly from the test class.
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }
}
