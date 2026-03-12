package hitlist.model.company;

/**
 * Represents a Company's description in the hitlist.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_CONSTRAINTS =
            "Descriptions must be between 2 and 1000 characters long, cannot start with a space, and cannot contain forward slashes (/) or line breaks.";

    /*
     * The first character of the description must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are banned to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,999}$";

    public final String companyDescription;

    /**
    * Constructs a {@code Description}.
    *
    * @param companyDescription A valid description.
    */
    public Description(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    /**
    * Returns true if a given string is a valid description.
    */
    public static boolean isValidDescription(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return companyDescription;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof hitlist.model.company.Description)) {
            return false;
        }

        hitlist.model.company.Description otherName = (hitlist.model.company.Description) other;
        return companyDescription.equals(otherName.companyDescription);
    }

    @Override
    public int hashCode() {
        return companyDescription.hashCode();
    }
}
