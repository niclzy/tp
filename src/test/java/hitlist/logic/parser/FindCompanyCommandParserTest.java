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

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCompanyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_companyNameKeywords_returnsFindCommand() {
        FindCompanyCommand expectedCompanyFindCommand =
                new FindCompanyCommand(new CompanyMatchesFindPredicate(Arrays.asList("Goo", "flix")));

        assertParseSuccess(parser, "Goo flix", expectedCompanyFindCommand);
        assertParseSuccess(parser, " \n Goo \n \t flix \t", expectedCompanyFindCommand);
    }
}
