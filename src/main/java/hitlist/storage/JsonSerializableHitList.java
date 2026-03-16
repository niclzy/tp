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
import hitlist.model.group.Group;
import hitlist.model.person.Person;

/**
 * An Immutable HitList that is serializable to JSON format.
 */
@JsonRootName(value = "hitlist")
class JsonSerializableHitList {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Groups list contains duplicate group(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableHitList} with the given persons.
     */
    @JsonCreator
    public JsonSerializableHitList(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                   @JsonProperty("groups") List<JsonAdaptedGroup> groups) {
        this.persons.addAll(persons);
        this.groups.addAll(groups); // TODO check if a null check is necessary
    }

    /**
     * Converts a given {@code ReadOnlyHitList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableHitList}.
     */
    public JsonSerializableHitList(ReadOnlyHitList source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        groups.addAll(source.getGroupList().stream().map(JsonAdaptedGroup::new).collect(Collectors.toList()));
    }

    /**
     * Converts this hitlist into the model's {@code HitList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public HitList toModelType() throws IllegalValueException {
        HitList addressBook = new HitList();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            Group group = jsonAdaptedGroup.toModelType();
            if (addressBook.hasGroup(group)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
            }
            addressBook.addGroup(group);
        }

        return addressBook;
    }

}
