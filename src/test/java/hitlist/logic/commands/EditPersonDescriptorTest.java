package hitlist.logic.commands;

import static hitlist.logic.commands.CommandTestUtil.DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different clearEmail flag -> returns false
        EditPersonDescriptor withClearEmail = new EditPersonDescriptor();
        withClearEmail.setClearEmail(true);
        assertFalse(DESC_AMY.equals(withClearEmail));

        // different clearAddress flag -> returns false
        EditPersonDescriptor withClearAddress = new EditPersonDescriptor();
        withClearAddress.setClearAddress(true);
        assertFalse(DESC_AMY.equals(withClearAddress));
    }

    @Test
    public void equals_sameClearFlags_returnsTrue() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptor();
        descriptor1.setClearEmail(true);
        descriptor1.setClearAddress(true);

        EditPersonDescriptor descriptor2 = new EditPersonDescriptor();
        descriptor2.setClearEmail(true);
        descriptor2.setClearAddress(true);

        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void equals_differentClearEmailFlag_returnsFalse() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptor();
        descriptor1.setClearEmail(true);

        EditPersonDescriptor descriptor2 = new EditPersonDescriptor();
        descriptor2.setClearEmail(false);

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void equals_differentClearAddressFlag_returnsFalse() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptor();
        descriptor1.setClearAddress(true);

        EditPersonDescriptor descriptor2 = new EditPersonDescriptor();
        descriptor2.setClearAddress(false);

        assertFalse(descriptor1.equals(descriptor2));
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

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void equals_bothHaveSameClearFlagsAndSameFields_returnsTrue() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .build();
        descriptor1.setClearEmail(true);
        descriptor1.setClearAddress(true);

        EditPersonDescriptor descriptor2 = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .build();
        descriptor2.setClearEmail(true);
        descriptor2.setClearAddress(true);

        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void isAnyFieldEdited_withClearEmailOnly_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClearEmail(true);
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_withClearAddressOnly_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClearAddress(true);
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_withClearEmailAndClearAddress_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClearEmail(true);
        descriptor.setClearAddress(true);
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_withNoFieldsAndNoClearFlags_returnsFalse() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_withNameAndClearEmail_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .build();
        descriptor.setClearEmail(true);
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_withPhoneAndClearAddress_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .build();
        descriptor.setClearAddress(true);
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void getClearEmail_defaultValue_returnsFalse() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isClearEmail());
    }

    @Test
    public void getClearAddress_defaultValue_returnsFalse() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isClearAddress());
    }

    @Test
    public void setClearEmail_thenIsClearEmail_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClearEmail(true);
        assertTrue(descriptor.isClearEmail());

        descriptor.setClearEmail(false);
        assertFalse(descriptor.isClearEmail());
    }

    @Test
    public void setClearAddress_thenIsClearAddress_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClearAddress(true);
        assertTrue(descriptor.isClearAddress());

        descriptor.setClearAddress(false);
        assertFalse(descriptor.isClearAddress());
    }

    @Test
    public void copyConstructor_withClearFlags_copiesCorrectly() {
        EditPersonDescriptor original = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .build();
        original.setClearEmail(true);
        original.setClearAddress(true);

        EditPersonDescriptor copy = new EditPersonDescriptor(original);

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getPhone(), copy.getPhone());
        assertEquals(original.getEmail(), copy.getEmail());
        assertEquals(original.getAddress(), copy.getAddress());
        assertEquals(original.isClearEmail(), copy.isClearEmail());
        assertEquals(original.isClearAddress(), copy.isClearAddress());
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + ", clearEmail="
                + editPersonDescriptor.isClearEmail() + ", clearAddress="
                + editPersonDescriptor.isClearAddress() + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }

    @Test
    public void toStringMethod_withClearFlagsSet_showsCorrectValues() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .build();
        descriptor.setClearEmail(true);

        String result = descriptor.toString();
        assertTrue(result.contains("clearEmail=true"));
        assertTrue(result.contains("clearAddress=false"));
        assertTrue(result.contains("name=" + VALID_NAME_BOB));
    }
}
