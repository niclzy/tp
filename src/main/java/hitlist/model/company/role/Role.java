package hitlist.model.company.role;

import hitlist.commons.util.ToStringBuilder;

import java.util.Objects;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a Role in the hitlist.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Role {

    private final Name roleName;
    private final Description roleDescription;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     * @param roleDescription A valid role description.
     */
    public Role(Name roleName, Description roleDescription) {
        requireAllNonNull(roleName, roleDescription);
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    /* Returns the name of the role. */
    public Name getRoleName() {
        return roleName;
    }

    /* Returns the description of the role. */
    public Description getRoleDescription() {
        return roleDescription;
    }

    /**
    * Returns true if both Roles have the same role name
    * This defines a weaker notion of equality between two Roles.
    */
    public boolean isSameRole(Role otherRole) {
        if (otherRole == this) {
            return true;
        }

        return otherRole != null
                && roleName.equals(otherRole.roleName);
    }

    /**
     * Returns true if both Roles have the same role name and description.
     * This defines a stronger notion of equality between two Roles.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName)
                && roleDescription.equals(otherRole.roleDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, roleDescription);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("role", roleName)
                .add("description", roleDescription)
                .toString();
    }
}
