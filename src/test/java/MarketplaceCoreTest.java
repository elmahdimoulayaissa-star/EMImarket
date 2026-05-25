import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MarketplaceCoreTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void cleanup() {
        Authentification.currentUser = null;
        BD.users = new java.util.ArrayList<>();
        BD.annonces = new java.util.ArrayList<>();
        BD.transactions = new java.util.ArrayList<>();
        System.clearProperty("marketplace.data.dir");
    }

    @Test
    void shouldPersistOwnershipAfterReload() throws Exception {
        System.setProperty("marketplace.data.dir", tempDir.toString());

        User seller = new User("seller1", "Password1@", "seller@test.com", 500);
        BD.users.add(seller);
        Authentification.currentUser = seller;
        assertTrue(Poste.publier(
                "MacBook Pro",
                "Ordinateur portable en très bon état avec batterie neuve et chargeur d'origine.",
                "https://example.com/macbook.jpg",
                1200,
                Categorie.Electronique
        ));
        BD.update();

        BD.users = new java.util.ArrayList<>();
        BD.annonces = new java.util.ArrayList<>();
        BD.transactions = new java.util.ArrayList<>();
        Authentification.currentUser = null;

        BD.load();
        assertTrue(Authentification.login("seller1", "Password1@"));
        assertEquals(1, BD.annonces.size());
        assertEquals(Authentification.currentUser, BD.annonces.get(0).getVendeur());
    }

    @Test
    void shouldModifyAndDeleteAnnonce() throws Exception {
        System.setProperty("marketplace.data.dir", tempDir.toString());

        User seller = new User("seller2", "Password1@", "seller2@test.com", 500);
        BD.users.add(seller);
        Authentification.currentUser = seller;
        assertTrue(Poste.publier(
                "Livre Java",
                "Livre complet pour apprendre Java avec de nombreux exemples pratiques et exercices corrigés.",
                "https://example.com/book.png",
                100,
                Categorie.Livre
        ));

        Annonce annonce = BD.annonces.get(0);
        assertTrue(Poste.modifier(
                annonce,
                "Livre Java 21",
                "Livre complet mis à jour pour Java 21 avec exemples pratiques, exercices et mini projets.",
                "https://example.com/book-21.png",
                120,
                Categorie.Livre
        ));
        assertEquals("Livre Java 21", annonce.getTitre());
        assertEquals(120, annonce.getPrix());

        assertTrue(Poste.supprimer(annonce));
        assertTrue(BD.annonces.isEmpty());
    }

    @Test
    void shouldImportLocalImageIntoAppFolder() throws Exception {
        System.setProperty("marketplace.data.dir", tempDir.toString());

        Path source = tempDir.resolve("photo-test.jpg");
        Files.writeString(source, "fake-image-content");

        String stored = ImageStorage.importLocalImage(source.toFile());
        assertTrue(stored.contains("images"));
        assertTrue(Files.exists(Path.of(stored)));
    }
}
