package hitlist.logic.commands;

import static hitlist.model.Model.PREDICATE_SHOW_ALL_COMPANIES;
import static java.util.Objects.requireNonNull;

import hitlist.model.Model;

/**
 * Lists all companies in the hit list to the user.
 */
public class ListCompanyCommand extends Command {

    public static final String COMMAND_WORD = "cmplist";

    public static final String MESSAGE_SUCCESS = "Listed all Companies";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }
}
