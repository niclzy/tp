package hitlist.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.company.role.Role;
import hitlist.model.company.role.RoleDescription;
import hitlist.model.company.role.RoleName;

/**
 * Jackson-friendly version of {@link Role} for JSON serialization/deserialization.
 */
class JsonAdaptedRole {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Role's %s field is missing!";

    private final String name;
    private final String description;

    /**
     * Constructs a {@code JsonAdaptedRole} with the given role details.
     */
    @JsonCreator
    public JsonAdaptedRole(@JsonProperty("name") String name,
                           @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Converts a given {@code Role} into this class for Jackson use.
     */
    public JsonAdaptedRole(Role source) {
        // Fix: Use toString() method instead of directly accessing private fields
        this.name = source.getRoleName().toString();
        this.description = source.getRoleDescription().toString();
    }

    /**
     * Converts this Jackson-friendly adapted role object into the model's {@code Role} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted role.
     */
    public Role toModelType() throws IllegalValueException {
        // Validate name
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    RoleName.class.getSimpleName()));
        }
        if (!RoleName.isValidRoleName(name)) {
            throw new IllegalValueException(RoleName.MESSAGE_CONSTRAINTS);
        }
        final RoleName modelName = new RoleName(name);

        // Validate description
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    RoleDescription.class.getSimpleName()));
        }
        if (!RoleDescription.isValidRoleDescription(description)) {
            throw new IllegalValueException(RoleDescription.MESSAGE_CONSTRAINTS);
        }
        final RoleDescription modelDescription = new RoleDescription(description);

        return new Role(modelName, modelDescription);
    }
}
