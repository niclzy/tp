package hitlist.model.company.role;

import static hitlist.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Role's name in the hitlist.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Role names must be between 2 and 50 characters long, cannot start with a space, and cannot contain forward slashes (/) or line breaks.";

    /*
     * The first character of the role name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Forward slashes (/) are not allowed to prevent command parser conflicts.
     */
    public static final String VALIDATION_REGEX = "^[^\\s/][^/\\v]{1,49}$";

    private final String roleName;

    /**
     * Constructs a {@code Name}.
     *
     * @param roleName A valid company name.
     */
    public Name(String roleName) {
        requireNonNull(roleName);
        checkArgument(isValidName(roleName), MESSAGE_CONSTRAINTS);
        this.roleName = roleName;
    }

    /**
     * Returns true if a given string is a valid company name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return roleName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof hitlist.model.company.role.Name)) {
            return false;
        }

        hitlist.model.company.role.Name otherName = (hitlist.model.company.role.Name) other;
        return roleName.equals(otherName.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }
}
