import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BD {

    private static final String STATE_FILE = "marketplace-state.ser";
    private static final String USERS = "users.ser";
    private static final String ANNONCES = "annonces.ser";
    private static final String TRANSACTIONS = "transactions.ser";

    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Annonce> annonces = new ArrayList<>();
    public static ArrayList<Transaction> transactions = new ArrayList<>();

    private BD() {}

    public static Path getAppDir() {
        String overridden = System.getProperty("marketplace.data.dir");
        if (overridden != null && !overridden.isBlank()) {
            return Paths.get(overridden);
        }
        return Paths.get(System.getProperty("user.home"), ".marketplace-fx");
    }

    public static Path getImagesDir() {
        Path path = getAppDir().resolve("images");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Impossible de créer le dossier images : " + e.getMessage());
        }
        return path;
    }

    public static void load() {
        users = new ArrayList<>();
        annonces = new ArrayList<>();
        transactions = new ArrayList<>();

        try {
            Files.createDirectories(getAppDir());
            Files.createDirectories(getImagesDir());
        } catch (IOException e) {
            System.err.println("Impossible d'initialiser le dossier de données : " + e.getMessage());
        }

        if (!loadUnifiedState()) {
            loadLegacyState();
        }

        relinkAndSanitize();
        update();
    }

    public static void update() {
        try {
            Files.createDirectories(getAppDir());
            DatabaseState state = new DatabaseState(users, annonces, transactions);
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(getAppDir().resolve(STATE_FILE)))) {
                oos.writeObject(state);
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    private static boolean loadUnifiedState() {
        Path statePath = getAppDir().resolve(STATE_FILE);
        if (!Files.exists(statePath)) return false;

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(statePath))) {
            Object obj = ois.readObject();
            if (obj instanceof DatabaseState state) {
                users = state.users != null ? state.users : new ArrayList<>();
                annonces = state.annonces != null ? state.annonces : new ArrayList<>();
                transactions = state.transactions != null ? state.transactions : new ArrayList<>();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Lecture du nouvel état impossible : " + e.getMessage());
        }
        return false;
    }

    private static void loadLegacyState() {
        users = tryLoadList(USERS);
        annonces = tryLoadList(ANNONCES);
        transactions = tryLoadList(TRANSACTIONS);
    }

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> tryLoadList(String fileName) {
        for (Path candidate : legacyCandidates(fileName)) {
            if (!Files.exists(candidate)) continue;
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(candidate))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    return (ArrayList<T>) obj;
                }
            } catch (Exception e) {
                System.err.println("Impossible de lire " + candidate + " : " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private static List<Path> legacyCandidates(String fileName) {
        return List.of(
                getAppDir().resolve(fileName),
                Paths.get(System.getProperty("user.dir")).resolve(fileName)
        );
    }

    private static void relinkAndSanitize() {
        if (users == null) users = new ArrayList<>();
        if (annonces == null) annonces = new ArrayList<>();
        if (transactions == null) transactions = new ArrayList<>();

        Map<String, User> usersByName = new LinkedHashMap<>();
        ArrayList<User> canonicalUsers = new ArrayList<>();
        for (User user : users) {
            if (user == null || user.getUsername() == null) continue;
            String key = user.getUsername().toLowerCase(Locale.ROOT);
            if (!usersByName.containsKey(key)) {
                usersByName.put(key, user);
                canonicalUsers.add(user);
            }
        }
        users = canonicalUsers;

        Map<String, Annonce> annoncesByKey = new LinkedHashMap<>();
        ArrayList<Annonce> canonicalAnnonces = new ArrayList<>();
        for (Annonce annonce : annonces) {
            if (annonce == null || annonce.getTitre() == null || annonce.getVendeur() == null) continue;

            User seller = canonicalUser(annonce.getVendeur(), usersByName);
            if (seller == null) {
                seller = annonce.getVendeur();
                usersByName.put(seller.getUsername().toLowerCase(Locale.ROOT), seller);
                users.add(seller);
            }
            annonce.setVendeur(seller);

            if (annonce.getAcheteurEnAttente() != null) {
                annonce.setAcheteurEnAttente(canonicalUser(annonce.getAcheteurEnAttente(), usersByName));
            }

            String key = annonceKey(annonce);
            if (!annoncesByKey.containsKey(key)) {
                annoncesByKey.put(key, annonce);
                canonicalAnnonces.add(annonce);
            }
        }
        annonces = canonicalAnnonces;

        for (User user : users) {
            ArrayList<Annonce> fixedFavoris = new ArrayList<>();
            for (Annonce favori : new ArrayList<>(user.favoris)) {
                Annonce canonical = canonicalAnnonce(favori, annoncesByKey);
                if (canonical != null && !fixedFavoris.contains(canonical)) {
                    fixedFavoris.add(canonical);
                }
            }
            user.favoris = fixedFavoris;
            user.achatsEnCours.clear();
            user.ventesEnCours.clear();
        }

        for (Annonce annonce : annonces) {
            if (annonce.getStatue() == Statue.EN_COURS && annonce.getAcheteurEnAttente() != null) {
                User buyer = canonicalUser(annonce.getAcheteurEnAttente(), usersByName);
                annonce.setAcheteurEnAttente(buyer);
                if (buyer != null && !buyer.achatsEnCours.contains(annonce)) buyer.achatsEnCours.add(annonce);
                if (annonce.getVendeur() != null && !annonce.getVendeur().ventesEnCours.contains(annonce)) {
                    annonce.getVendeur().ventesEnCours.add(annonce);
                }
            } else if (annonce.getStatue() != Statue.EN_COURS) {
                annonce.setAcheteurEnAttente(null);
            }
        }

        ArrayList<Transaction> fixedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction == null) continue;
            transaction.acheteur = canonicalUser(transaction.acheteur, usersByName);
            transaction.vendeur = canonicalUser(transaction.vendeur, usersByName);
            fixedTransactions.add(transaction);
        }
        transactions = fixedTransactions;
    }

    private static User canonicalUser(User user, Map<String, User> usersByName) {
        if (user == null || user.getUsername() == null) return null;
        return usersByName.get(user.getUsername().toLowerCase(Locale.ROOT));
    }

    private static Annonce canonicalAnnonce(Annonce annonce, Map<String, Annonce> annoncesByKey) {
        if (annonce == null) return null;
        return annoncesByKey.get(annonceKey(annonce));
    }

    private static String annonceKey(Annonce annonce) {
        if (annonce.getId() != null && !annonce.getId().isBlank()) return annonce.getId();
        return annonce.getTitre().trim().toLowerCase(Locale.ROOT);
    }

    private static class DatabaseState implements Serializable {
        private static final long serialVersionUID = 1L;

        private final ArrayList<User> users;
        private final ArrayList<Annonce> annonces;
        private final ArrayList<Transaction> transactions;

        private DatabaseState(ArrayList<User> users, ArrayList<Annonce> annonces, ArrayList<Transaction> transactions) {
            this.users = users;
            this.annonces = annonces;
            this.transactions = transactions;
        }
    }
}
