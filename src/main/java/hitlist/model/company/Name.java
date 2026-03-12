package hitlist.model.company;

import static hitlist.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Company's name in the hit list.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Company names must be between 2 and 30 characters long, cannot start with a space, and cannot contain forward slashes (/), or line breaks.";

    /*
     * The first character of the company name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are not allowed to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,29}$";

    private final String companyName;

    /**
     * Constructs a {@code Name}.
     *
     * @param companyName A valid company name.
     */
    public Name(String companyName) {
        requireNonNull(companyName);
        checkArgument(isValidName(companyName), MESSAGE_CONSTRAINTS);
        this.companyName = companyName;
    }

    /**
     * Returns true if a given string is a valid company name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return companyName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof hitlist.model.company.Name)) {
            return false;
        }

        hitlist.model.company.Name otherName = (hitlist.model.company.Name) other;
        return companyName.equals(otherName.companyName);
    }

    @Override
    public int hashCode() {
        return companyName.hashCode();
    }
}
