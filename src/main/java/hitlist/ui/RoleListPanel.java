package hitlist.ui;

import java.util.logging.Logger;

import hitlist.commons.core.LogsCenter;
import hitlist.model.company.role.Role;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of roles.
 */
public class RoleListPanel extends UiPart<Region> {
    private static final String FXML = "RoleListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(RoleListPanel.class);
    private final ObservableList<Role> roleList;

    @javafx.fxml.FXML
    private ListView<Role> roleListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public RoleListPanel(ObservableList<Role> roleList) {
        super(FXML);
        this.roleList = roleList;
        setupListView();
    }

    /**
     * Sets up list bindings after FXML has been loaded.
     */
    private void setupListView() {
        if (roleListView == null) {
            // If this ever happens, FXML failed to inject fx:id="roleListView".
            throw new IllegalStateException("roleListView is not injected. Check RoleListPanel.fxml fx:id.");
        }
        roleListView.setItems(roleList);
        roleListView.setCellFactory(listView -> new RoleListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Role} using a {@code RoleCard}.
     */
    class RoleListViewCell extends ListCell<Role> {
        @Override
        protected void updateItem(Role role, boolean empty) {
            super.updateItem(role, empty);
            if (empty || role == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new RoleCard(role, getIndex() + 1).getRoot());
            }
        }
    }
}
