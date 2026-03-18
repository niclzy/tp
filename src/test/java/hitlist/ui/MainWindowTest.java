package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hitlist.logic.LogicStub;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Tests for {@link MainWindow}.
 */
public class MainWindowTest {

    @BeforeAll
    public static void setUpOnce() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping MainWindowTest in unsupported/headless JavaFX environment.");
    }

    @Test
    public void constructor_validDependencies_success() throws Exception {
        AtomicReference<MainWindow> windowRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            MainWindow window = new MainWindowStub(new Stage());
            windowRef.set(window);
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(windowRef.get());
        assertNotNull(windowRef.get().getPrimaryStage());
    }

    @Test
    public void showCompanyListPane_doesNotThrow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrownRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindowStub(new Stage());
                assertDoesNotThrow(window::showCompanyListPane);
            } catch (Throwable t) {
                thrownRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        if (thrownRef.get() != null) {
            throw new AssertionError(thrownRef.get());
        }
    }

    @Test
    public void hideCompanyListPane_doesNotThrow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrownRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindowStub(new Stage());
                assertDoesNotThrow(window::hideCompanyListPane);
            } catch (Throwable t) {
                thrownRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        if (thrownRef.get() != null) {
            throw new AssertionError(thrownRef.get());
        }
    }

    @Test
    public void showPersonListPane_doesNotThrow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrownRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindowStub(new Stage());
                assertDoesNotThrow(window::showPersonListPane);
            } catch (Throwable t) {
                thrownRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        if (thrownRef.get() != null) {
            throw new AssertionError(thrownRef.get());
        }
    }

    @Test
    public void hidePersonListPane_doesNotThrow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrownRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindowStub(new Stage());
                assertDoesNotThrow(window::hidePersonListPane);
            } catch (Throwable t) {
                thrownRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        if (thrownRef.get() != null) {
            throw new AssertionError(thrownRef.get());
        }
    }

    @Test
    public void handleHelp_doesNotThrow() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> thrownRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                MainWindow window = new MainWindowStub(new Stage());
                assertDoesNotThrow(window::handleHelp);
            } catch (Throwable t) {
                thrownRef.set(t);
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        if (thrownRef.get() != null) {
            throw new AssertionError(thrownRef.get());
        }
    }

    /**
     * Minimal MainWindow test double using LogicStub.
     */
    private static class MainWindowStub extends MainWindow {
        MainWindowStub(Stage stage) {
            super(stage, new LogicStub());
        }
    }
}
