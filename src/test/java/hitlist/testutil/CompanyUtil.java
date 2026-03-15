package hitlist.testutil;

import hitlist.logic.commands.AddCompanyCommand;
import hitlist.model.company.Company;

import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY;
import static hitlist.logic.parser.CliSyntax.PREFIX_COMPANY_DESC;

public class CompanyUtil {

    public static String getAddCompanyCommand(Company company) {
        return AddCompanyCommand.COMMAND_WORD + " " + getCompanyDetails(company);
    }

    public static String getCompanyDetails(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_COMPANY)
                .append(company.getName())
                .append(" ")
                .append(PREFIX_COMPANY_DESC)
                .append(company.getDescription());
        return sb.toString();
    }
}
