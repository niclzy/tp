package hitlist.testutil;

import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.model.person.Address;
import hitlist.model.person.Email;
import hitlist.model.person.Name;
import hitlist.model.person.Person;
import hitlist.model.person.Phone;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        if (person.getEmail().isPresent()) {
            descriptor.setEmail(person.getEmail().get());
        }
        if (person.getAddress().isPresent()) {
            descriptor.setAddress(person.getAddress().get());
        }
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the clear email flag of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withClearEmail(boolean clearEmail) {
        descriptor.setClearEmail(clearEmail);
        return this;
    }

    /**
     * Sets the clear address flag of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withClearAddress(boolean clearAddress) {
        descriptor.setClearAddress(clearAddress);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
