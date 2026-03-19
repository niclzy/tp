package hitlist.ui;

import java.util.Objects;

import hitlist.model.company.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * A UI component that displays information of a {@code Company}.
 */
public class CompanyCard extends UiPart<Region> {

    private static final String FXML = "CompanyListCard.fxml";
    private final int displayedIndex;
    private final Company company;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label description;

    /**
     * Creates a {@code CompanyCard} with the given {@code Company} and index to display.
     */
    public CompanyCard(Company company, int displayedIndex) {
        super(FXML);
        this.company = company;
        this.displayedIndex = displayedIndex;

        id.setText(displayedIndex + ". ");
        name.setText(company.getName().toString());
        description.setText(company.getDescription().toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CompanyCard)) {
            return false;
        }

        CompanyCard card = (CompanyCard) other;
        return displayedIndex == card.displayedIndex
                && company.equals(card.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayedIndex, company);
    }
}
