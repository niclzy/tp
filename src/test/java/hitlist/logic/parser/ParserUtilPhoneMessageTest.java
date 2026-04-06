package hitlist.logic.parser;

import static hitlist.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.person.Phone;

public class ParserUtilPhoneMessageTest {

    @Test
    public void parsePhone_blankValue_throwsParseExceptionWithConstraintMessage() {
        assertThrows(ParseException.class, Phone.MESSAGE_CONSTRAINTS,
                () -> ParserUtil.parsePhone(""));
    }

    @Test
    public void parsePhone_whitespaceValue_throwsParseExceptionWithConstraintMessage() {
        assertThrows(ParseException.class, Phone.MESSAGE_CONSTRAINTS,
                () -> ParserUtil.parsePhone("   "));
    }
}
