package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import hitlist.logic.commands.FindCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.PersonMatchesFindPredicate;

/**
 * Parses input arguments and creates a new {@code FindCommand} object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    @Override
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();
        List<String> nameKeywords = preamble.isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(preamble.split("\\s+"));

        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG).stream()
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());

        if (nameKeywords.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonMatchesFindPredicate(nameKeywords, tagKeywords));
    }
}
