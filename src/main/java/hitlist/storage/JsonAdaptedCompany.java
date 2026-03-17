package hitlist.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.company.Company;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;
import hitlist.model.company.role.UniqueRoleList;

/**
 * Converts between {@code Company} objects and JSON format for storage.
 * This class acts as a bridge between our internal Company model and the JSON
 * file format. It exists because our Company class has validation logic and
 * custom types that Jackson (the JSON library) cannot automatically handle.
 *
 * When saving: Takes a Company object and extracts its data into simple Strings
 * that Jackson can write to the hitlist.json file.
 *
 * When loading: Reads String values from the JSON file, validates them,
 * and constructs proper Company objects with all their fields.
 */
class JsonAdaptedCompany {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Company's %s field is missing!";

    private final String name;
    private final String description;
    private final List<JsonAdaptedRole> roles = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedCompany} with the given company details.
     *
     * @param name The name of the company (required)
     * @param description The description of the company (required)
     * @param roles The list of roles associated with the company (optional, can be null or empty)
     */
    @JsonCreator
    public JsonAdaptedCompany(@JsonProperty("name") String name,
                              @JsonProperty("description") String description,
                              @JsonProperty("roles") List<JsonAdaptedRole> roles) {
        this.name = name;
        this.description = description;
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    /**
     * Converts a given {@code Company} into this class for Jackson use.
     * This constructor is used during the saving process to transform the
     * internal model into a JSON-serializable format.
     *
     * @param source The company object to convert (cannot be null)
     */
    public JsonAdaptedCompany(Company source) {
        // Fix: Use toString() method instead of directly accessing private field
        name = source.getName().toString();
        description = source.getDescription().toString();
        roles.addAll(source.getUniqueRoleList().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedRole::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted company object into the model's {@code Company} object.
     * This method is used during the loading process to reconstruct company objects from JSON data.
     * It performs thorough validation of all fields to ensure data integrity and throws appropriate
     * exceptions if any validation constraints are violated.
     *
     * @return A valid {@code Company} object reconstructed from the JSON data
     * @throws IllegalValueException if there were any data constraints violated in the adapted company,
     *         such as missing required fields, invalid field formats, or invalid role data
     */
    public Company toModelType() throws IllegalValueException {
        // Validate name
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CompanyName.class.getSimpleName()));
        }
        if (!CompanyName.isValidCompanyName(name)) {
            throw new IllegalValueException(CompanyName.MESSAGE_CONSTRAINTS);
        }
        final CompanyName modelName = new CompanyName(name);

        // Validate description
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CompanyDescription.class.getSimpleName()));
        }
        if (!CompanyDescription.isValidCompanyDescription(description)) {
            throw new IllegalValueException(CompanyDescription.MESSAGE_CONSTRAINTS);
        }
        final CompanyDescription modelDescription = new CompanyDescription(description);

        // Create company with empty role list first
        Company company = new Company(modelName, modelDescription);

        // Add roles if any
        UniqueRoleList modelRoles = company.getUniqueRoleList();
        for (JsonAdaptedRole role : roles) {
            modelRoles.add(role.toModelType());
        }

        return company;
    }
}
