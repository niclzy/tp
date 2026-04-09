package hitlist.testutil;

import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;

import hitlist.logic.commands.AddCommand;
import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.model.person.Person;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME).append(" ").append(person.getName().fullName).append(" ");
        sb.append(PREFIX_PHONE).append(" ").append(person.getPhone().value).append(" ");
        if (person.getEmail().isPresent()) {
            sb.append(PREFIX_EMAIL).append(" ").append(person.getEmail().get().value).append(" ");
        }
        if (person.getAddress().isPresent()) {
            sb.append(PREFIX_ADDRESS).append(" ").append(person.getAddress().get().value).append(" ");
        }
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME)
                .append(" ")
                .append(name.fullName)
                .append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE)
                .append(" ")
                .append(phone.value)
                .append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL)
                .append(" ")
                .append(email.value)
                .append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS)
                .append(" ")
                .append(address.value)
                .append(" "));
        return sb.toString();
    }
}
