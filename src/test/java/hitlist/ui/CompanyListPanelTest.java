package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.model.company.Company;
import hitlist.testutil.CompanyBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Focused tests for CompanyListPanel.
 */
public class CompanyListPanelTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping CompanyListPanelTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void display_nonEmptyList() throws Exception {
        ObservableList<Company> companies = FXCollections.observableArrayList(
                new CompanyBuilder().withName("Google Inc.").build(),
                new CompanyBuilder().withName("Meta Platforms").build()
        );

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.runLater(() -> {
            Stage stage = null;
            try {
                CompanyListPanel panel = new CompanyListPanel(companies);

                stage = new Stage();
                Scene scene = new Scene(panel.getRoot());
                stage.setScene(scene);
                stage.show();

                panel.getRoot().applyCss();
                panel.getRoot().layout();

                // Basic sanity that panel is
                assertTrue(panel.getRoot().isVisible());
            } catch (Throwable t) {
                failure.set(t);
            } finally {
                if (stage != null) {
                    stage.hide();
                }
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for JavaFX thread");
        if (failure.get() != null) {
            throw new AssertionError("JavaFX task failed", failure.get());
        }
    }

    @Test
    public void display_emptyList() throws Exception {
        ObservableList<Company> emptyList = FXCollections.observableArrayList();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.runLater(() -> {
            Stage stage = null;
            try {
                CompanyListPanel panel = new CompanyListPanel(emptyList);

                stage = new Stage();
                Scene scene = new Scene(panel.getRoot(), 200, 500);
                stage.setScene(scene);
                stage.show();

                panel.getRoot().applyCss();
                panel.getRoot().layout();

                assertTrue(panel.getRoot().isVisible());
            } catch (Throwable t) {
                failure.set(t);
            } finally {
                if (stage != null) {
                    stage.hide();
                }
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for JavaFX thread");
        if (failure.get() != null) {
            throw new AssertionError("JavaFX task failed", failure.get());
        }
    }

    @Test
    public void companyListViewCell_emptyItem_clearsCellState() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                CompanyListPanel panel = new CompanyListPanel(FXCollections.observableArrayList());
                CompanyListPanel.CompanyListViewCell cell = panel.new CompanyListViewCell();

                cell.updateItem(null, true);

                assertNull(cell.getGraphic());
                assertNull(cell.getText());
            } catch (Throwable t) {
                failure.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for JavaFX thread");
        if (failure.get() != null) {
            throw new AssertionError("JavaFX task failed", failure.get());
        }
    }

    @Test
    public void companyListViewCell_nonEmptyItem_setsGraphic() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Company company = new CompanyBuilder().withName("OpenAI").build();
                CompanyListPanel panel = new CompanyListPanel(FXCollections.observableArrayList());
                CompanyListPanel.CompanyListViewCell cell = panel.new CompanyListViewCell();

                // Set index context by attaching to ListView.
                ListView<Company> lv = new ListView<>();
                lv.getItems().add(company);
                cell.updateListView(lv);
                cell.updateIndex(0);

                cell.updateItem(company, false);

                assertTrue(cell.getGraphic() != null);
            } catch (Throwable t) {
                failure.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for JavaFX thread");
        if (failure.get() != null) {
            throw new AssertionError("JavaFX task failed", failure.get());
        }
    }
}
