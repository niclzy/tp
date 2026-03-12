package hitlist.testutil;

import hitlist.model.company.role.Description;
import hitlist.model.company.role.Name;
import hitlist.model.company.role.Role;

/**
 * A utility class to help with building Role objects.
 */
public class RoleBuilder {

    public static final String DEFAULT_NAME = "Default Role Name";
    public static final String DEFAULT_DESCRIPTION = "Default Role Description";

    private Name name;
    private Description description;

    /**
     * Creates a {@code RoleBuilder} with the default details.
     */
    public RoleBuilder() {
        this.name = new Name(DEFAULT_NAME);
        this.description = new Description(DEFAULT_DESCRIPTION);
    }

    /**
    * Initializes the RoleBuilder with the data of {@code roleToCopy}.
    */
    public RoleBuilder(Role roleToCopy) {
        this.name = roleToCopy.getRoleName();
        this.description = roleToCopy.getRoleDescription();
    }

    /**
    * Sets the {@code Name} of the {@code Role} that we are building.
    */
    public RoleBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Role} that we are building.
     */
    public RoleBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
    * Builds a Role object with the given details.
    */
    public hitlist.model.company.role.Role build() {
        return new Role(name, description);
    }
}
