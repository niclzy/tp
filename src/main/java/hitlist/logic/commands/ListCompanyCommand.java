package hitlist.logic.commands;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static hitlist.ui.UiPaneVisibility.SHOW_COMPANY_LIST;
import static hitlist.ui.UiPaneVisibility.SHOW_ROLE_LIST;
import static java.util.Objects.requireNonNull;

import hitlist.logic.commands.exceptions.CommandException;
import hitlist.model.Model;
import hitlist.model.company.CompanyName;

/**
 * Lists all companies in the HitList to the user.
 */
public class ListCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmplist";

    public static final String MESSAGE_SUCCESS = "Listed company: %1$s";
    public static final String DEFAULT_MESSAGE_SUCCESS = "Listed all companies";
    public static final String MESSAGE_NO_COMPANY_FOUND = "No company found with the name: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all companies, or lists the roles of a specified company.\n"
            + "Parameters: [" + PREFIX_COMPANY + " COMPANY_NAME]\n"
            + "Examples:\n"
            + COMMAND_WORD + "\n"
            + COMMAND_WORD + " " + PREFIX_COMPANY + " Google";

    private final CompanyName name;

    /**
     * Creates a ListCompanyCommand to list all companies.
     */
    public ListCompanyCommand() {
        this.name = null;
    }

    /**
     * Creates a ListCompanyCommand to list the specified company.
     *
     * @param name The company name to list roles for.
     */
    public ListCompanyCommand(CompanyName name) {
        requireNonNull(name);
        this.name = name;
    }

    private boolean isListAllCompanies() {
        return name == null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (isListAllCompanies()) {
            model.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
            return new CommandResult(DEFAULT_MESSAGE_SUCCESS, SHOW_COMPANY_LIST);
        } else if (model.hasCompanyByName(name)) {
            model.updateRoleList(name);
            return new CommandResult(String.format(MESSAGE_SUCCESS, name), SHOW_ROLE_LIST);
        } else {
            throw new CommandException(String.format(MESSAGE_NO_COMPANY_FOUND, name));
        }
    }
}
