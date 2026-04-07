package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyMatchesFindPredicate;

/**
 * Parses input arguments and creates a new FindCompanyCommand object
 */
public class FindCompanyCommandParser implements Parser<FindCompanyCommand> {

    @Override
    public FindCompanyCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmedArgs.split("\\s+");
        List<String> companyNameKeywords = new ArrayList<>();

        for (String companyNameKeyword : tokens) {
            companyNameKeywords.add(companyNameKeyword.trim());
        }

        return new FindCompanyCommand(new CompanyMatchesFindPredicate(companyNameKeywords));
    }
}
