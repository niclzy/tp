package hitlist.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.HitList;
import hitlist.model.ReadOnlyHitList;
import hitlist.model.company.Company;
import hitlist.model.group.Group;
import hitlist.model.person.Person;

/**
 * An Immutable HitList that is serializable to JSON format.
 */
@JsonRootName(value = "hitlist")
class JsonSerializableHitList {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_COMPANY = "Companies list contains duplicate company(ies).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Groups list contains duplicate group(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedCompany> companies = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableHitList} with the given persons, companies and groups.
     */
    @JsonCreator
    public JsonSerializableHitList(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                   @JsonProperty("companies") List<JsonAdaptedCompany> companies,
                                   @JsonProperty("groups") List<JsonAdaptedGroup> groups) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (companies != null) {
            this.companies.addAll(companies);
        }
        if (groups != null) {
            this.groups.addAll(groups);
        }
    }

    /**
     * Converts a given {@code ReadOnlyHitList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableHitList}.
     */
    public JsonSerializableHitList(ReadOnlyHitList source) {
        persons.addAll(source.getPersonList().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
        companies.addAll(source.getCompanyList().stream()
                .map(JsonAdaptedCompany::new)
                .collect(Collectors.toList()));
        groups.addAll(source.getGroupList().stream()
                .map(JsonAdaptedGroup::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this hitlist into the model's {@code HitList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public HitList toModelType() throws IllegalValueException {
        HitList hitList = new HitList();

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (hitList.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            hitList.addPerson(person);
        }

        for (JsonAdaptedCompany jsonAdaptedCompany : companies) {
            Company company = jsonAdaptedCompany.toModelType();
            if (hitList.hasCompany(company)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_COMPANY);
            }
            hitList.addCompany(company);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            Group group = jsonAdaptedGroup.toModelType();
            if (hitList.hasGroup(group)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
            }
            hitList.addGroup(group);
        }

        return hitList;
    }
}
