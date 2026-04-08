package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_NAME;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;
import static java.util.Objects.requireNonNull;

import hitlist.commons.core.index.Index;
import hitlist.logic.commands.EditCommand;
import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Check for invalid format where prefix is concatenated with value (no space)
        // Look for patterns like "/eabc", "/a123", "/nJohn", "/p9123" that are not followed by space or end
        // But allow "/e" (clear), "/a" (clear), "/e " (space after), etc.
        String trimmedArgs = args.trim();

        // Check each token for invalid prefix-value concatenation
        String[] tokens = trimmedArgs.split("\\s+");
        for (int i = 1; i < tokens.length; i++) { // Skip first token (index)
            String token = tokens[i];
            // Check if token starts with a prefix and has more characters than just the prefix
            if ((token.startsWith("/n") || token.startsWith("/p") || token.startsWith("/e") || token.startsWith("/a"))
                    && token.length() > 2) {
                // The token has the prefix plus extra characters with no space
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditCommand.MESSAGE_USAGE));
            }
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }

        // Handle email: check if the prefix is present
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String emailValue = argMultimap.getValue(PREFIX_EMAIL).get();
            if (emailValue.isBlank()) {
                // Empty email value means clear the email field
                editPersonDescriptor.setClearEmail(true);
            } else {
                // Non-empty email value means set the email
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(emailValue));
            }
        }

        // Handle address: check if the prefix is present
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String addressValue = argMultimap.getValue(PREFIX_ADDRESS).get();
            if (addressValue.isBlank()) {
                // Empty address value means clear the address field
                editPersonDescriptor.setClearAddress(true);
            } else {
                // Non-empty address value means set the address
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(addressValue));
            }
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }
}
