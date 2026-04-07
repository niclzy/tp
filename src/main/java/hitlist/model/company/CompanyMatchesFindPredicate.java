package hitlist.model.company;

import java.util.List;
import java.util.function.Predicate;

import hitlist.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Company}'s {@code CompanyName} matches any of the keywords given.
 */
public class CompanyMatchesFindPredicate implements Predicate<Company> {

    private final List<String> companyNameKeywords;

    public CompanyMatchesFindPredicate(List<String> companyNameKeywords) {
        this.companyNameKeywords = companyNameKeywords;
    }

    @Override
    public boolean test(Company company) {
        return companyNameKeywords.isEmpty()
                || companyNameKeywords.stream()
                .anyMatch(keyword ->
                        containsIgnoreCase(company.getCompanyName().companyName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CompanyMatchesFindPredicate)) {
            return false;
        }
        CompanyMatchesFindPredicate otherPredicate = (CompanyMatchesFindPredicate) other;
        return companyNameKeywords.equals(otherPredicate.companyNameKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("companyNameKeywords", companyNameKeywords)
                .toString();
    }

    /**
     * Checks if the given string contains the search string, ignoring case.
     *
     * @param str The string to check.
     * @param searchStr The string to search for.
     * @return true if str contains searchStr (case-insensitive), false otherwise.
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

}
