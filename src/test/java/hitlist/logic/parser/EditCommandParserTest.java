package hitlist.logic.parser;

import static hitlist.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hitlist.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static hitlist.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static hitlist.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static hitlist.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static hitlist.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static hitlist.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static hitlist.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static hitlist.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static hitlist.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static hitlist.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static hitlist.logic.parser.CliSyntax.PREFIX_EMAIL;
import static hitlist.logic.parser.CliSyntax.PREFIX_PHONE;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hitlist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static hitlist.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static hitlist.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static hitlist.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import hitlist.commons.core.index.Index;
import hitlist.logic.Messages;
import hitlist.logic.commands.EditCommand;
import hitlist.logic.commands.EditCommand.EditPersonDescriptor;
import hitlist.model.person.Email;
import hitlist.model.person.Name;
import hitlist.model.person.Phone;
import hitlist.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC,
                String.format(Phone.MESSAGE_INVALID_PHONE, "911a"));

        // invalid email
        assertParseFailure(parser, "1 /e invalidemail", Email.MESSAGE_CONSTRAINTS);

        // Address validation only rejects empty string or whitespace-starting strings.
        // Empty string is now valid for clearing, so skip invalid address test.

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                String.format(Phone.MESSAGE_INVALID_PHONE, "911a"));

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + " /e invalidemail"
                + VALID_ADDRESS_AMY + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + NAME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + " /e " + VALID_EMAIL_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + " /a " + VALID_ADDRESS_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearEmailOnly_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " /e";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearAddressOnly_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " /a";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearAddress(true)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearBothEmailAndAddress_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " /e /a";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withClearEmail(true)
                .withClearAddress(true)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_updatePhoneAndClearEmail_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + " /e";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withClearEmail(true)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_updateNameAndClearAddress_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + " /a";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withClearAddress(true)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB;
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC
                + INVALID_EMAIL_DESC + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }
}
