package hitlist.ui;

import java.util.logging.Logger;

import hitlist.commons.core.GuiSettings;
import hitlist.commons.core.LogsCenter;
import hitlist.logic.Logic;
import hitlist.logic.commands.CommandResult;
import hitlist.logic.commands.exceptions.CommandException;
import hitlist.logic.parser.exceptions.ParseException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final double MIN_VISIBLE_WIDTH = 100;
    private static final double TITLE_BAR_HEIGHT = 50;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private CompanyListPanel companyListPanel;
    private GroupListPanel groupListPanel;
    private RoleListPanel roleListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private javafx.scene.layout.VBox personList;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane companyListPanelPlaceholder;

    @FXML
    private javafx.scene.layout.VBox companyListPane;

    @FXML
    private javafx.scene.layout.VBox groupListPane;

    @FXML
    private StackPane groupListPanelPlaceholder;

    @FXML
    private javafx.scene.layout.VBox roleListPane;

    @FXML
    private StackPane roleListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic.getGroupList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        companyListPanel = new CompanyListPanel(logic.getFilteredCompanyList());
        companyListPanelPlaceholder.getChildren().add(companyListPanel.getRoot());

        groupListPanel = new GroupListPanel(logic.getGroupList());
        groupListPanelPlaceholder.getChildren().add(groupListPanel.getRoot());

        roleListPanel = new RoleListPanel(logic.getRoleList());
        roleListPanelPlaceholder.getChildren().add(roleListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());

        if (guiSettings.getWindowCoordinates() != null) {
            double x = guiSettings.getWindowCoordinates().getX();
            double y = guiSettings.getWindowCoordinates().getY();
            if (isWindowOnAnyVisibleScreen(x, y, primaryStage.getWidth(), primaryStage.getHeight())) {
                primaryStage.setX(x);
                primaryStage.setY(y);
            } else {
                // Saved coordinates can become invalid after monitor layout changes.
                centerWindowOnPrimaryScreen();
            }
        }
    }

    /**
     * Returns true if any visible part of the window intersects a currently available screen.
     */
    private boolean isWindowOnAnyVisibleScreen(double x, double y, double width, double height) {
        return Screen.getScreens().stream()
                .map(Screen::getVisualBounds)
                .anyMatch(bounds -> intersects(bounds, x, y, width, height));
    }

    /**
     * Move the window to the center of the primary screen as a safe fallback.
     */
    private void centerWindowOnPrimaryScreen() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double centeredX = bounds.getMinX() + Math.max(0, (bounds.getWidth() - primaryStage.getWidth()) / 2);
        double centeredY = bounds.getMinY() + Math.max(0, (bounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.setX(centeredX);
        primaryStage.setY(centeredY);
    }

    private boolean intersects(Rectangle2D bounds, double x, double y, double width, double height) {
        // The title bar is at the very top of the window (y)
        double titleBarBottom = y + TITLE_BAR_HEIGHT;
        double windowRight = x + width;

        // Check if the title bar's Y-coordinates are within the screen's vertical bounds
        boolean isYOnScreen = y >= bounds.getMinY() && titleBarBottom <= bounds.getMaxY();

        // Calculate how much of the width is actually on screen
        double overlapWidth = Math.min(windowRight, bounds.getMaxX()) - Math.max(x, bounds.getMinX());
        boolean isXOnScreen = overlapWidth >= MIN_VISIBLE_WIDTH;

        return isYOnScreen && isXOnScreen;
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Returns the person list panel.
     */
    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Returns the company list panel.
     */
    public CompanyListPanel getCompanyListPanel() {
        return companyListPanel;
    }

    /**
     * Returns the role list panel.
     */
    public RoleListPanel getRoleListPanel() {
        return roleListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see hitlist.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isShowCompanyList()) {
                hidePersonListPane();
                hideGroupListPane();
                showCompanyListPane();
                hideRoleListPane();

                // Force refresh the company list
                companyListPanel = new CompanyListPanel(logic.getFilteredCompanyList());
                companyListPanelPlaceholder.getChildren().clear();
                companyListPanelPlaceholder.getChildren().add(companyListPanel.getRoot());
            } else if (commandResult.isShowGroupList()) {
                hidePersonListPane();
                showGroupListPane();
                hideCompanyListPane();
                hideRoleListPane();

                // Force refresh the group list
                groupListPanel = new GroupListPanel(logic.getGroupList());
                groupListPanelPlaceholder.getChildren().clear();
                groupListPanelPlaceholder.getChildren().add(groupListPanel.getRoot());
            } else if (commandResult.isShowRoleList()) {
                hideCompanyListPane();
                hideGroupListPane();
                hidePersonListPane();
                showRoleListPane();

                // Force refresh the role list
                roleListPanel = new RoleListPanel(logic.getRoleList());
                roleListPanelPlaceholder.getChildren().clear();
                roleListPanelPlaceholder.getChildren().add(roleListPanel.getRoot());
            } else { // person list
                showPersonListPane();
                hideGroupListPane();
                hideCompanyListPane();
                hideRoleListPane();

                // Force refresh the person list
                personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic.getGroupList());
                personListPanelPlaceholder.getChildren().clear();
                personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Shows the person list pane.
     */
    public void showPersonListPane() {
        personList.setVisible(true);
        personList.setManaged(true);
    }

    /**
     * Hides the person list pane.
     */
    public void hidePersonListPane() {
        personList.setVisible(false);
        personList.setManaged(false);
    }

    /**
     * Shows the group list pane.
     */
    public void showGroupListPane() {
        groupListPane.setVisible(true);
        groupListPane.setManaged(true);
    }

    /**
     * Hides the group list pane.
     */
    public void hideGroupListPane() {
        groupListPane.setVisible(false);
        groupListPane.setManaged(false);
    }

    /**
     * Shows the company list pane.
     */
    public void showCompanyListPane() {
        companyListPane.setVisible(true);
        companyListPane.setManaged(true);
    }

    /**
     * Hides the company list pane.
     */
    public void hideCompanyListPane() {
        companyListPane.setVisible(false);
        companyListPane.setManaged(false);
    }

    /**
     * Shows the role list pane.
     */
    private void showRoleListPane() {
        roleListPane.setVisible(true);
        roleListPane.setManaged(true);
    }

    /**
     * Hides the role list pane.
     */
    private void hideRoleListPane() {
        roleListPane.setVisible(false);
        roleListPane.setManaged(false);
    }
}
