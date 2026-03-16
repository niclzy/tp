package hitlist.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import hitlist.commons.exceptions.IllegalValueException;
import hitlist.model.group.Group;
import hitlist.model.group.GroupName;
import hitlist.model.person.Person;
import hitlist.model.person.UniquePersonList;

/**
 * Jackson-friendly version of {@link Group}.
 */
class JsonAdaptedGroup {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Group's %s field is missing!";
    public static final String MESSAGE_DUPLICATE_PERSON_IN_GROUP = "Members list in group contains duplicate person(s).";

    private final String groupName;
    private final List<JsonAdaptedPerson> members = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given group details.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("groupName") String groupName, 
                            @JsonProperty("members") List<JsonAdaptedPerson> members) {
        this.groupName = groupName;
        if (members != null) {
            this.members.addAll(members);
        }
    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        // Assuming GroupName stores its string value in a field called 'value' or 'fullName'
        // like Address/Phone/Name. Adjust this based on your actual GroupName implementation.
        this.groupName = source.getName().toString(); 
        
        this.members.addAll(source.getMembers().asUnmodifiableObservableList().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted group object into the model's {@code Group} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted group.
     */
    public Group toModelType() throws IllegalValueException {
        if (groupName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, GroupName.class.getSimpleName()));
        }
        if (!GroupName.isValidGroupName(groupName)) {
            throw new IllegalValueException(GroupName.MESSAGE_CONSTRAINTS);
        }
        final GroupName modelGroupName = new GroupName(groupName);

        Group group = new Group(modelGroupName);

        // 2. Validate and convert the UniquePersonList (Members)
        final UniquePersonList modelMembers = new UniquePersonList();
        
        for (JsonAdaptedPerson jsonAdaptedMember : members) {
            Person person = jsonAdaptedMember.toModelType();
            
            if (modelMembers.contains(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON_IN_GROUP);
            }
            group.addMember(person);
        }

        return group;
    }
}
