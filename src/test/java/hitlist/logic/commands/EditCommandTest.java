package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static hitlist.logic.commands.CommandTestUtil.assertCommandFailure;
import static hitlist.logic.commands.CommandTestUtil.assertCommandSuccess;
import static hitlist.logic.commands.CommandTestUtil.showPersonAtIndex;
import static hitlist.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static hitlist.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static hitlist.testutil.TypicalPersons.getTypicalHitList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import hitlist.commons.core.index.Index;
import hitlist.logic.Messages;
import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.model.HitList;
import hitlist.model.Model;
import hitlist.model.ModelManager;
import hitlist.model.UserPrefs;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.Phone;
import hitlist.testutil.EditPersonDescriptorBuilder;
import hitlist.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalHitList(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearEmailOnly_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create edited person with empty email using constructor
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                Optional.empty(),
                personToEdit.getAddress()
        );

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertTrue(editedPerson.getEmail().isEmpty());
    }

    @Test
    public void execute_clearAddressOnly_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create edited person with empty address using constructor
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                Optional.empty()
        );

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearAddress(true)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertTrue(editedPerson.getAddress().isEmpty());
    }

    @Test
    public void execute_clearBothEmailAndAddress_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create edited person with empty email and address using constructor
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                Optional.empty(),
                Optional.empty()
        );

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .withClearAddress(true)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertTrue(editedPerson.getEmail().isEmpty());
        assertTrue(editedPerson.getAddress().isEmpty());
    }

    @Test
    public void execute_updatePhoneAndClearEmail_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create edited person with updated phone and empty email
        Person editedPerson = new Person(
                personToEdit.getName(),
                new Phone(VALID_PHONE_BOB),
                Optional.empty(),
                personToEdit.getAddress()
        );

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withClearEmail(true)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertEquals(VALID_PHONE_BOB, editedPerson.getPhone().value);
        assertTrue(editedPerson.getEmail().isEmpty());
    }

    @Test
    public void execute_updateNameAndClearAddress_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Create edited person with updated name and empty address
        Person editedPerson = new Person(
                new Name(VALID_NAME_BOB),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                Optional.empty()
        );

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withClearAddress(true)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertEquals(VALID_NAME_BOB, editedPerson.getName().fullName);
        assertTrue(editedPerson.getAddress().isEmpty());
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new HitList(model.getHitList()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in HitList
        Person personInList = model.getHitList().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of HitList
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds HitList
        assertTrue(outOfBoundIndex.getZeroBased() < model.getHitList().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void equals_sameClearFlags_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .withClearAddress(true)
                .build();
        EditCommand editCommand1 = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        EditCommand editCommand2 = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertTrue(editCommand1.equals(editCommand2));
    }

    @Test
    public void equals_differentClearEmailFlags_returnsFalse() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .build();
        EditPersonDescriptor descriptor2 = new EditPersonDescriptorBuilder()
                .withClearEmail(false)
                .build();

        EditCommand editCommand1 = new EditCommand(INDEX_FIRST_PERSON, descriptor1);
        EditCommand editCommand2 = new EditCommand(INDEX_FIRST_PERSON, descriptor2);

        assertFalse(editCommand1.equals(editCommand2));
    }

    @Test
    public void equals_differentClearAddressFlags_returnsFalse() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptorBuilder()
                .withClearAddress(true)
                .build();
        EditPersonDescriptor descriptor2 = new EditPersonDescriptorBuilder()
                .withClearAddress(false)
                .build();

        EditCommand editCommand1 = new EditCommand(INDEX_FIRST_PERSON, descriptor1);
        EditCommand editCommand2 = new EditCommand(INDEX_FIRST_PERSON, descriptor2);

        assertFalse(editCommand1.equals(editCommand2));
    }

    @Test
    public void equals_sameFieldsDifferentClearFlags_returnsFalse() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .build();
        descriptor1.setClearEmail(true);

        EditPersonDescriptor descriptor2 = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .build();
        descriptor2.setClearEmail(false);

        EditCommand editCommand1 = new EditCommand(INDEX_FIRST_PERSON, descriptor1);
        EditCommand editCommand2 = new EditCommand(INDEX_FIRST_PERSON, descriptor2);

        assertFalse(editCommand1.equals(editCommand2));
    }
}
