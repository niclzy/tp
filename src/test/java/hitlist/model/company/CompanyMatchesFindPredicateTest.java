package hitlist.model.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import hitlist.testutil.CompanyBuilder;

public class CompanyMatchesFindPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        CompanyMatchesFindPredicate firstPredicate = new CompanyMatchesFindPredicate(firstPredicateKeywordList);
        CompanyMatchesFindPredicate secondPredicate = new CompanyMatchesFindPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        CompanyMatchesFindPredicate firstPredicateCopy = new CompanyMatchesFindPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different company parameters -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emptyKeywordList_returnsTrue() {
        // Based on your implementation, an empty list returns true
        CompanyMatchesFindPredicate predicate = new CompanyMatchesFindPredicate(Collections.emptyList());

        Company company = new CompanyBuilder().withName("Google").build();
        assertTrue(predicate.test(company));
    }

    @Test
    public void test_companyNameContainsKeywords_returnsTrue() {
        // One keyword
        CompanyMatchesFindPredicate predicate = new CompanyMatchesFindPredicate(Collections.singletonList("Apple"));
        assertTrue(predicate.test(new CompanyBuilder().withName("Apple Inc").build()));

        // Multiple keywords (only one needs to match based on anyMatch)
        predicate = new CompanyMatchesFindPredicate(Arrays.asList("Apple", "Microsoft"));
        assertTrue(predicate.test(new CompanyBuilder().withName("Microsoft Corporation").build()));

        // Mixed-case keywords
        predicate = new CompanyMatchesFindPredicate(Arrays.asList("aPpLe", "mIcRoSoFt"));
        assertTrue(predicate.test(new CompanyBuilder().withName("Apple Inc").build()));

        // Partial word match (e.g., searching "Goo" matches "Google")
        predicate = new CompanyMatchesFindPredicate(Collections.singletonList("Goo"));
        assertTrue(predicate.test(new CompanyBuilder().withName("Google").build()));
    }

    @Test
    public void test_companyNameDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        CompanyMatchesFindPredicate predicate = new CompanyMatchesFindPredicate(Arrays.asList("Amazon"));
        assertFalse(predicate.test(new CompanyBuilder().withName("Netflix").build()));

        // Keywords match other fields but not company name (assuming Company has other fields)
        predicate = new CompanyMatchesFindPredicate(Arrays.asList("12345", "alice@email.com"));
        assertFalse(predicate.test(new CompanyBuilder().withName("Meta").build()));
    }

    @Test
    public void containsIgnoreCase_validStrings_correctResult() {
        // Exact match
        assertTrue(CompanyMatchesFindPredicate.containsIgnoreCase("Google", "Google"));

        // Lowercase search
        assertTrue(CompanyMatchesFindPredicate.containsIgnoreCase("Google", "google"));

        // Uppercase search
        assertTrue(CompanyMatchesFindPredicate.containsIgnoreCase("Google", "GOOGLE"));

        // Partial match
        assertTrue(CompanyMatchesFindPredicate.containsIgnoreCase("Google Inc", "gle"));

        // No match
        assertFalse(CompanyMatchesFindPredicate.containsIgnoreCase("Google", "Apple"));
    }

    @Test
    public void containsIgnoreCase_nullStrings_returnsFalse() {
        assertFalse(CompanyMatchesFindPredicate.containsIgnoreCase(null, "Google"));
        assertFalse(CompanyMatchesFindPredicate.containsIgnoreCase("Google", null));
        assertFalse(CompanyMatchesFindPredicate.containsIgnoreCase(null, null));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        CompanyMatchesFindPredicate predicate = new CompanyMatchesFindPredicate(keywords);

        // This expects the standard AddressBook-style ToStringBuilder format
        String expected = CompanyMatchesFindPredicate.class.getCanonicalName()
                + "{companyNameKeywords="
                + keywords
                + "}";
        assertEquals(expected, predicate.toString());
    }
}
