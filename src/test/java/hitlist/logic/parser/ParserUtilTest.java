package hitlist.logic.parser;

import static hitlist.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static hitlist.testutil.Assert.assertThrows;
import static hitlist.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import hitlist.logic.parser.exceptions.ParseException;
import hitlist.model.company.CompanyDescription;
import hitlist.model.company.CompanyName;
import hitlist.model.person.Address;
import hitlist.model.person.Email;
import hitlist.model.person.Name;
import hitlist.model.person.Phone;

public class ParserUtilTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_NAME_1 = "Alan Walker";
    private static final String VALID_NAME_2 = "Bryan Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";

    private static final String INVALID_COMPANY_DESCRIPTION =
            "We specialize in B2B/B2C marketing.";
    private static final String INVALID_COMPANY_NAME = " A/B Testing Ltd";

    private static final String VALID_COMPANY_DESCRIPTION =
            "A multinational technology company that specializes in Internet-related services and products";
    private static final String VALID_COMPANY_NAME = "Google Inc.";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseNames_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseNames(null));
    }

    @Test
    public void parseNames_collectionWithInvalidNames_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseNames(Arrays.asList(VALID_NAME, INVALID_NAME)));
    }

    @Test
    public void parseNames_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseNames(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseNames_collectionWithValidNames_returnsTagSet() throws Exception {
        Set<Name> actualNameSet = ParserUtil.parseNames(Arrays.asList(VALID_NAME_1, VALID_NAME_2));
        Set<Name> expectedNameSet = new HashSet<Name>(Arrays.asList(new Name(VALID_NAME_1), new Name(VALID_NAME_2)));

        assertEquals(expectedNameSet, actualNameSet);
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseCompanyName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCompanyName(null));
    }

    @Test
    public void parseCompanyName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCompanyName(INVALID_COMPANY_NAME));
    }

    @Test
    public void parseCompanyName_validValueWithoutWhitespace_returnsCompanyName() throws Exception {
        CompanyName expectedCompanyName = new CompanyName(VALID_COMPANY_NAME);
        assertEquals(expectedCompanyName, ParserUtil.parseCompanyName(VALID_COMPANY_NAME));
    }

    @Test
    public void parseCompanyName_validValueWithWhitespace_returnsTrimmedCompanyName() throws Exception {
        String companyNameWithWhitespace = WHITESPACE + VALID_COMPANY_NAME + WHITESPACE;
        CompanyName expectedCompanyName = new CompanyName(VALID_COMPANY_NAME);
        assertEquals(expectedCompanyName, ParserUtil.parseCompanyName(companyNameWithWhitespace));
    }

    @Test
    public void parseCompanyDescription_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCompanyDescription(null));
    }

    @Test
    public void parseCompanyDescription_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCompanyDescription(INVALID_COMPANY_DESCRIPTION));
    }

    @Test
    public void parseCompanyDescription_validValueWithoutWhitespace_returnsCompanyDescription() throws Exception {
        CompanyDescription expectedCompanyDescription = new CompanyDescription(VALID_COMPANY_DESCRIPTION);
        assertEquals(expectedCompanyDescription, ParserUtil.parseCompanyDescription(VALID_COMPANY_DESCRIPTION));
    }

    @Test
    public void parseCompanyDescription_validValueWithWhitespace_returnsTrimmedCompanyDescription() throws Exception {
        String companyDescriptionWithWhitespace = WHITESPACE + VALID_COMPANY_DESCRIPTION + WHITESPACE;
        CompanyDescription expectedCompanyDescription = new CompanyDescription(VALID_COMPANY_DESCRIPTION);
        assertEquals(expectedCompanyDescription, ParserUtil.parseCompanyDescription(companyDescriptionWithWhitespace));
    }
}
