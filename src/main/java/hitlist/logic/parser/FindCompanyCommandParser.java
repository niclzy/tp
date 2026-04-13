package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyMatchesFindPredicate;
import hitlist.model.company.CompanyName;

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

        for (String token : tokens) {
            String trimmedToken = token.trim();

            if (trimmedToken.isEmpty()) {
                continue;
            }

            // Validate search keyword
            if (!isValidSearchKeyword(trimmedToken)) {
                throw new ParseException(
                        String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD,
                                trimmedToken,
                                FindCompanyCommand.SEARCH_KEYWORD_CONSTRAINTS));
            }
            companyNameKeywords.add(trimmedToken);
        }

        if (companyNameKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
        }

        return new FindCompanyCommand(new CompanyMatchesFindPredicate(companyNameKeywords));
    }

    /**
     * Validates a search keyword.
     * Single characters are allowed (unlike CompanyName which requires min 2 characters).
     * For 2+ characters, must follow CompanyName validation rules.
     */
    private boolean isValidSearchKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        String trimmed = keyword.trim();

        // SPECIAL EXCEPTION: Allow single character searches
        if (trimmed.length() == 1) {
            // Single character must be alphanumeric (consistent with company name rules)
            return trimmed.matches("\\p{Alnum}");
        }

        // For 2+ characters, use the standard CompanyName validation
        return CompanyName.isValidCompanyName(trimmed);
    }
}
