package hitlist.model.company.role;

import static hitlist.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Role's description in the hitlist.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_CONSTRAINTS =
            "Role names must be between 2 and 1000 characters long, cannot start with a space, and cannot contain forward slashes (/) or line breaks.";

    /*
     * The first character of the role name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are not allowed to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,999}$";

    private String roleDescription;

    /**
     * Constructs a {@code Description}.
     *
     * @param roleDescription A valid company name.
     */
    public Description(String roleDescription) {
        requireNonNull(roleDescription);
        checkArgument(isValidDescription(roleDescription), MESSAGE_CONSTRAINTS);
        this.roleDescription = roleDescription.trim();
    }

    /**
     * Returns true if a given string is a valid company name.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return roleDescription;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof hitlist.model.company.role.Description)) {
            return false;
        }

        hitlist.model.company.role.Description otherDescription = (hitlist.model.company.role.Description) other;
        return roleDescription.equals(otherDescription.roleDescription);
    }

    @Override
    public int hashCode() {
        return roleDescription.hashCode();
    }
}
