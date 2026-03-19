package hitlist.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import javafx.scene.control.Label;

public class CompanyCardTest {

    @BeforeAll
    public static void setupFxGuard() {
        assumeTrue(JavaFxTestSupport.isFxAvailable(),
                "Skipping CompanyCardTest: JavaFX unavailable in this environment.");
    }

    @Test
    public void constructor_validCompany_displaysCorrectDetails() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Google Inc.")
                .withDescription("A multinational technology company")
                .build();

        AtomicReference<CompanyCard> cardRef = new AtomicReference<>();
        AtomicReference<String> idText = new AtomicReference<>();
        AtomicReference<String> nameText = new AtomicReference<>();
        AtomicReference<String> descText = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 1);
            cardRef.set(card);

            Label id = (Label) card.getRoot().lookup("#id");
            Label name = (Label) card.getRoot().lookup("#name");
            Label description = (Label) card.getRoot().lookup("#description");

            idText.set(id == null ? null : id.getText());
            nameText.set(name == null ? null : name.getText());
            descText.set(description == null ? null : description.getText());
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(cardRef.get());
        assertEquals("1. ", idText.get());
        assertEquals("Google Inc.", nameText.get());
        assertEquals("A multinational technology company", descText.get());
    }

    @Test
    public void constructor_differentIndex_displaysCorrectIndex() throws Exception {
        Company company = new CompanyBuilder()
                .withName("Meta Platforms, Inc.")
                .withDescription("A technology conglomerate")
                .build();

        AtomicReference<String> idText = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            CompanyCard card = new CompanyCard(company, 7);
            Label id = (Label) card.getRoot().lookup("#id");
            idText.set(id == null ? null : id.getText());
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertEquals("7. ", idText.get());
    }

    @Test
    public void equals() throws Exception {
        Company company = new CompanyBuilder().withName("Google Inc.").build();

        AtomicReference<CompanyCard> card1Ref = new AtomicReference<>();
        AtomicReference<CompanyCard> card2Ref = new AtomicReference<>();
        AtomicReference<CompanyCard> card3Ref = new AtomicReference<>();
        AtomicReference<CompanyCard> card4Ref = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            card1Ref.set(new CompanyCard(company, 1));
            card2Ref.set(new CompanyCard(company, 1)); // same company, same index
            card3Ref.set(new CompanyCard(company, 2)); // same company, different index

            Company differentCompany = new CompanyBuilder().withName("Meta Platforms, Inc.").build();
            card4Ref.set(new CompanyCard(differentCompany, 1)); // different company, same index

            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);

        CompanyCard card1 = card1Ref.get();
        CompanyCard card2 = card2Ref.get();
        CompanyCard card3 = card3Ref.get();
        CompanyCard card4 = card4Ref.get();

        // same object -> returns true
        assertTrue(card1.equals(card1));

        // same values -> returns true
        assertTrue(card1.equals(card2));

        // null -> returns false
        assertFalse(card1.equals(null));

        // different types -> returns false
        assertFalse(card1.equals(1));

        // different index -> returns false
        assertFalse(card1.equals(card3));

        // different company -> returns false
        assertFalse(card1.equals(card4));
    }
}
