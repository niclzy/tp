package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import hitlist.logic.commands.FindCompanyCommand;
import hitlist.model.company.CompanyMatchesFindPredicate;

public class FindCompanyCommandParserTest {

    private final FindCompanyCommandParser parser = new FindCompanyCommandParser();

    /**
     * Helper method to get the exact expected error message for an invalid keyword.
     */
    private String getExpectedErrorMessage(String invalidKeyword) {
        return String.format(FindCompanyCommand.MESSAGE_INVALID_KEYWORD, invalidKeyword,
                FindCompanyCommand.SEARCH_KEYWORD_CONSTRAINTS);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validCompanyKeywords_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "Google Meta", expectedCommand);
        assertParseSuccess(parser, " \n Google \n \t Meta \t", expectedCommand);
    }

    @Test
    public void parse_validCompanyWithHyphen_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("AT-T", "T-Mobile")));

        assertParseSuccess(parser, "AT-T T-Mobile", expectedCommand);
    }

    @Test
    public void parse_validCompanyWithAmpersand_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("AT&T", "Johnson&Johnson")));

        assertParseSuccess(parser, "AT&T Johnson&Johnson", expectedCommand);
    }

    @Test
    public void parse_validCompanyWithAtSymbol_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google@Meta")));

        assertParseSuccess(parser, "Google@Meta", expectedCommand);
    }

    @Test
    public void parse_validCompanyWithHashSymbol_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("C#Corp")));

        assertParseSuccess(parser, "C#Corp", expectedCommand);
    }

    @Test
    public void parse_validCompanyWithNumbers_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("7-Eleven", "24x7")));

        assertParseSuccess(parser, "7-Eleven 24x7", expectedCommand);
    }

    @Test
    public void parse_singleCharacterAlphanumericKeyword_returnsFindCompanyCommand() {
        // Single character alphanumeric searches are now allowed
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X")));
        assertParseSuccess(parser, "X", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("a")));
        assertParseSuccess(parser, "a", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("1")));
        assertParseSuccess(parser, "1", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Z")));
        assertParseSuccess(parser, "Z", expectedCommand);
    }

    @Test
    public void parse_invalidSingleCharacterNonAlphanumeric_throwsParseException() {
        // Single character non-alphanumeric (symbols) should fail
        assertParseFailure(parser, "=", getExpectedErrorMessage("="));
        assertParseFailure(parser, "{", getExpectedErrorMessage("{"));
        assertParseFailure(parser, "[", getExpectedErrorMessage("["));
        assertParseFailure(parser, "@", getExpectedErrorMessage("@"));
        assertParseFailure(parser, "&", getExpectedErrorMessage("&"));
        assertParseFailure(parser, "!", getExpectedErrorMessage("!"));
        assertParseFailure(parser, "?", getExpectedErrorMessage("?"));
        assertParseFailure(parser, "*", getExpectedErrorMessage("*"));
        assertParseFailure(parser, "(", getExpectedErrorMessage("("));
        assertParseFailure(parser, ")", getExpectedErrorMessage(")"));
        assertParseFailure(parser, "+", getExpectedErrorMessage("+"));
        assertParseFailure(parser, "=", getExpectedErrorMessage("="));
    }

    @Test
    public void parse_invalidKeywordWithForwardSlash_throwsParseException() {
        // Contains forward slash - NOT allowed
        assertParseFailure(parser, "/n", getExpectedErrorMessage("/n"));
        assertParseFailure(parser, "n/", getExpectedErrorMessage("n/"));
        assertParseFailure(parser, "/c", getExpectedErrorMessage("/c"));
        assertParseFailure(parser, "Google/Meta", getExpectedErrorMessage("Google/Meta"));
    }

    @Test
    public void parse_mixedValidAndInvalidKeywords_throwsParseException() {
        // When there are multiple keywords, the first invalid one causes the error
        // Single character non-alphanumeric
        assertParseFailure(parser, "Google = Meta", getExpectedErrorMessage("="));
        assertParseFailure(parser, "= Google Meta", getExpectedErrorMessage("="));
        assertParseFailure(parser, "Google Meta =", getExpectedErrorMessage("="));

        // Contains forward slash
        assertParseFailure(parser, "Google /n Meta", getExpectedErrorMessage("/n"));

        // Single character alphanumeric is now valid, so these should pass
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "X", "Meta")));
        assertParseSuccess(parser, "Google X Meta", expectedCommand);
    }

    @Test
    public void parse_keywordWithOnlySpaces_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleSpacesBetweenKeywords_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "Google   Meta", expectedCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google")));

        assertParseSuccess(parser, "Google", expectedCommand);
    }

    @Test
    public void parse_keywordWithLeadingAndTrailingSpaces_returnsFindCompanyCommand() {
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google", "Meta")));

        assertParseSuccess(parser, "  Google   Meta   ", expectedCommand);
    }

    @Test
    public void parse_validKeywordWithSpecialCharacters_returnsFindCompanyCommand() {
        // For 2+ characters, special characters are allowed (except /)
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google$Meta")));
        assertParseSuccess(parser, "Google$Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google%Meta")));
        assertParseSuccess(parser, "Google%Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google*Meta")));
        assertParseSuccess(parser, "Google*Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google+Meta")));
        assertParseSuccess(parser, "Google+Meta", expectedCommand);

        expectedCommand = new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Google=Meta")));
        assertParseSuccess(parser, "Google=Meta", expectedCommand);
    }

    @Test
    public void parse_mixedLengthKeywords_returnsFindCompanyCommand() {
        // Mix of single character and multi-character keywords
        FindCompanyCommand expectedCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X", "Google", "Y", "Meta")));

        assertParseSuccess(parser, "X Google Y Meta", expectedCommand);
    }

    @Test
    public void parse_caseInsensitiveSingleCharacter_returnsFindCompanyCommand() {
        // Case shouldn't matter for single characters
        FindCompanyCommand expectedCommandLower =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("x")));
        FindCompanyCommand expectedCommandUpper =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("X")));

        assertParseSuccess(parser, "x", expectedCommandLower);
        assertParseSuccess(parser, "X", expectedCommandUpper);
    }
}
