package hitlist.model.company;

import static hitlist.commons.util.CollectionUtil.requireAllNonNull;

import hitlist.commons.util.ToStringBuilder;
import hitlist.model.company.role.UniqueRoleList;

import java.util.Objects;

/**
 * Represents a Company in the hitlist.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Company {
    private final Name name;
    private final Description description;
    private final UniqueRoleList uniqueRoleList;

     /**
     * Every field must be present and not null.
     */
    public Company(Name name, Description description, UniqueRoleList uniqueRoleList) {
        requireAllNonNull(name, description, uniqueRoleList);
        this.name = name;
        this.description = description;
        this.uniqueRoleList = uniqueRoleList;
    }

    /* Returns the name of the company. */
    public Name getName() {
        return this.name;
    }

    /* Returns the description of the company. */
    public Description getDescription() {
        return this.description;
    }

    /* Returns the list of roles in the company. */
    public UniqueRoleList getUniqueRoleList() {
        return this.uniqueRoleList;
    }

    /**
     * Returns true if both companies have the same name and description.
     * This defines a weaker notion of equality between two companies.
     */
    public boolean isSameCompany(Company otherCompany) {
        if (otherCompany == this) {
            return true;
        }

        return otherCompany != null
                && this.name.equals(otherCompany.name)
                && this.description.equals(otherCompany.description);
    }

    /**
     * Returns true if both companies have the same fields and data.
     * This defines a stronger notion of equality between two companies.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Company)) {
            return false;
        }

        Company otherCompany = (Company) other;
        return this.name.equals(otherCompany.name)
                && this.description.equals(otherCompany.description)
                && this.uniqueRoleList.equals(otherCompany.uniqueRoleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, uniqueRoleList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("companyName", name)
                .add("description", description)
                .add("roles", uniqueRoleList)
                .toString();
    }
}
