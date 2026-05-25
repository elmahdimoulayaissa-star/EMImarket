import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MarketplaceApp extends Application {

    private Stage primaryStage;
    private StackPane contentArea;
    private Label sidebarSoldeLabel;

    private static final String C_BG = "#0f0f14";
    private static final String C_SURFACE = "#1a1a24";
    private static final String C_CARD = "#22222f";
    private static final String C_BORDER = "#2e2e40";
    private static final String C_ACCENT = "#7c5cfc";
    private static final String C_ACCENT2 = "#c084fc";
    private static final String C_TEXT = "#e8e8f0";
    private static final String C_MUTED = "#7070a0";
    private static final String C_SUCCESS = "#34d399";
    private static final String C_WARNING = "#fbbf24";
    private static final String C_DANGER = "#f87171";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        BD.load();
        stage.setTitle("Marketplace FX");
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        showLogin();
        stage.show();
    }

    private Scene buildScene(Parent root) {
        return new Scene(root, 1240, 780);
    }

    private Label label(String text, String color, int size, boolean bold) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + color + ";-fx-font-size:" + size + "px;" + (bold ? "-fx-font-weight:bold;" : ""));
        l.setWrapText(true);
        return l;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(fieldStyle());
        tf.setPrefHeight(42);
        return tf;
    }

    private PasswordField styledPwd(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(fieldStyle());
        pf.setPrefHeight(42);
        return pf;
    }

    private String fieldStyle() {
        return "-fx-background-color:" + C_SURFACE + ";-fx-text-fill:" + C_TEXT + ";"
                + "-fx-prompt-text-fill:" + C_MUTED + ";-fx-border-color:" + C_BORDER + ";"
                + "-fx-border-radius:10;-fx-background-radius:10;-fx-font-size:14px;-fx-padding:9 12;";
    }

    private VBox card(double width) {
        VBox box = new VBox(14);
        box.setMaxWidth(width);
        box.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:18;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:18;-fx-padding:24;");
        return box;
    }

    private Button accentBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + C_ACCENT + ";-fx-text-fill:white;-fx-font-size:14px;"
                + "-fx-font-weight:bold;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:10 22;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:#6b4fe0;-fx-text-fill:white;-fx-font-size:14px;"
                + "-fx-font-weight:bold;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:10 22;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:" + C_ACCENT + ";-fx-text-fill:white;-fx-font-size:14px;"
                + "-fx-font-weight:bold;-fx-background-radius:10;-fx-cursor:hand;-fx-padding:10 22;"));
        return b;
    }

    private Button ghostBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + C_ACCENT2 + ";-fx-font-size:13px;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:10;-fx-background-radius:10;"
                + "-fx-cursor:hand;-fx-padding:8 18;");
        return b;
    }

    private Button successBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#0d2e22;-fx-text-fill:" + C_SUCCESS + ";-fx-font-size:13px;"
                + "-fx-background-radius:10;-fx-cursor:hand;-fx-padding:8 18;");
        return b;
    }

    private Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#3d1a1a;-fx-text-fill:" + C_DANGER + ";-fx-font-size:13px;"
                + "-fx-background-radius:10;-fx-cursor:hand;-fx-padding:8 18;");
        return b;
    }

    private Separator sep() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + C_BORDER + ";");
        return s;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color:" + C_SURFACE + ";");
        alert.showAndWait();
    }

    private boolean showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("-fx-background-color:" + C_SURFACE + ";");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private StackPane badge(String text, String background, String foreground) {
        Label l = label(text, foreground, 11, true);
        StackPane sp = new StackPane(l);
        sp.setStyle("-fx-background-color:" + background + ";-fx-background-radius:18;-fx-padding:4 10;");
        return sp;
    }

    private Region spacer() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    private void setContent(Node node) {
        contentArea.getChildren().setAll(node);
        FadeTransition ft = new FadeTransition(Duration.millis(180), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(60), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }

    private String categoryEmoji(Categorie categorie) {
        return switch (categorie) {
            case Electronique -> "💻";
            case Livre -> "📚";
            case MODE -> "👗";
            case Autre -> "📦";
        };
    }

    private StackPane buildImageBox(String photoRef, Categorie categorie, double width, double height) {
        StackPane box = new StackPane();
        box.setMinSize(width, height);
        box.setPrefSize(width, height);
        box.setStyle("-fx-background-color:" + C_SURFACE + ";-fx-background-radius:14;");

        try {
            String uri = ImageStorage.toLoadableUri(photoRef);
            if (uri != null && !uri.isBlank()) {
                Image image = new Image(uri, width, height, false, true, false);
                if (!image.isError()) {
                    ImageView view = new ImageView(image);
                    view.setFitWidth(width);
                    view.setFitHeight(height);
                    view.setPreserveRatio(true);
                    Rectangle clip = new Rectangle(width, height);
                    clip.setArcWidth(18);
                    clip.setArcHeight(18);
                    view.setClip(clip);
                    box.getChildren().add(view);
                    return box;
                }
            }
        } catch (Exception ignored) {
        }

        Label fallbackEmoji = label(categoryEmoji(categorie == null ? Categorie.Autre : categorie), C_MUTED, 44, false);
        Label fallbackText = label("Aperçu image", C_MUTED, 12, false);
        VBox fallback = new VBox(6, fallbackEmoji, fallbackText);
        fallback.setAlignment(Pos.CENTER);
        box.getChildren().add(fallback);
        return box;
    }

    private void refreshSoldeLabel() {
        if (sidebarSoldeLabel != null && Authentification.currentUser != null) {
            sidebarSoldeLabel.setText(String.format("%.2f DH", Authentification.currentUser.getSolde()));
        }
    }

    private void showLogin() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:" + C_BG + ";");

        for (int i = 0; i < 4; i++) {
            Circle c = new Circle(80 + i * 60);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.web(C_ACCENT, 0.06 + i * 0.02));
            c.setStrokeWidth(1.5);
            StackPane.setAlignment(c, i % 2 == 0 ? Pos.TOP_LEFT : Pos.BOTTOM_RIGHT);
            root.getChildren().add(c);
        }

        VBox center = new VBox(28);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(420);

        Label logo = label("🛒 Marketplace FX", C_ACCENT2, 28, true);
        Label sub = label("Votre place de marché en ligne", C_MUTED, 14, false);
        VBox logoBox = new VBox(4, logo, sub);
        logoBox.setAlignment(Pos.CENTER);

        VBox formCard = card(400);
        formCard.setAlignment(Pos.CENTER_LEFT);

        TextField tfUser = styledField("Nom d'utilisateur");
        PasswordField pfPass = styledPwd("Mot de passe");
        Label errLabel = label("", C_DANGER, 13, false);

        Button btnLogin = accentBtn("Se connecter");
        Button btnRegister = ghostBtn("Créer un compte");
        btnLogin.setPrefWidth(360);
        btnRegister.setPrefWidth(360);

        btnLogin.setOnAction(e -> {
            if (Authentification.login(tfUser.getText().trim(), pfPass.getText())) {
                showMain();
            } else {
                errLabel.setText("❌ Identifiants incorrects.");
                shake(formCard);
            }
        });
        pfPass.setOnAction(e -> btnLogin.fire());
        btnRegister.setOnAction(e -> showRegister());

        formCard.getChildren().addAll(
                label("Connexion", C_TEXT, 22, true),
                sep(),
                tfUser,
                pfPass,
                errLabel,
                btnLogin,
                btnRegister
        );

        center.getChildren().addAll(logoBox, formCard);
        root.getChildren().add(center);
        primaryStage.setScene(buildScene(root));
    }

    private void showRegister() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:" + C_BG + ";");

        VBox center = new VBox(24);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(460);

        VBox formCard = card(440);
        TextField tfUser = styledField("Nom d'utilisateur (3-15 caractères)");
        PasswordField pfPass = styledPwd("Mot de passe");
        TextField tfEmail = styledField("Email");
        Label hint = label("Mot de passe : majuscule, minuscule, chiffre, caractère spécial, minimum 8 caractères.", C_MUTED, 11, false);
        Label errLabel = label("", C_DANGER, 13, false);

        Button btnRegister = accentBtn("S'inscrire");
        Button btnBack = ghostBtn("← Retour");
        btnRegister.setPrefWidth(400);
        btnBack.setPrefWidth(400);

        btnRegister.setOnAction(e -> {
            try {
                boolean created = Authentification.inscrire(tfUser.getText().trim(), pfPass.getText(), tfEmail.getText().trim());
                if (created) {
                    showAlert("Succès", "Compte créé. Vous pouvez maintenant vous connecter.", Alert.AlertType.INFORMATION);
                    showLogin();
                } else {
                    errLabel.setText("❌ Ce nom d'utilisateur existe déjà.");
                }
            } catch (Exception ex) {
                errLabel.setText("❌ " + ex.getMessage());
                shake(formCard);
            }
        });
        btnBack.setOnAction(e -> showLogin());

        formCard.getChildren().addAll(
                label("Créer un compte", C_TEXT, 22, true),
                sep(),
                tfUser,
                pfPass,
                hint,
                tfEmail,
                errLabel,
                btnRegister,
                btnBack
        );

        center.getChildren().addAll(label("🛒 Marketplace FX", C_ACCENT2, 24, true), formCard);
        root.getChildren().add(center);
        primaryStage.setScene(buildScene(root));
    }

    private void showMain() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + C_BG + ";");

        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setPadding(new Insets(24, 12, 24, 12));
        sidebar.setStyle("-fx-background-color:" + C_SURFACE + ";-fx-border-color:" + C_BORDER + ";-fx-border-width:0 1 0 0;");

        Label brand = label("🛒 Marketplace FX", C_ACCENT2, 18, true);
        brand.setPadding(new Insets(0, 0, 12, 8));

        VBox navBox = new VBox(4,
                navBtn("🏠 Marché", this::showMarche),
                navBtn("❤ Favoris", this::showFavoris),
                navBtn("🛍 Mes achats", this::showMesAchats),
                navBtn("📦 Mes ventes", this::showMesVentes),
                navBtn("➕ Publier", this::showPublier),
                navBtn("👤 Profil", this::showProfil)
        );
        if (Authentification.currentUser.isAdmin()) {
            navBox.getChildren().add(sep());
            navBox.getChildren().add(navBtn("⚙ Administration", this::showAdmin));
        }

        Label userLabel = label("@" + Authentification.currentUser.getUsername(), C_TEXT, 13, true);
        sidebarSoldeLabel = label(String.format("%.2f DH", Authentification.currentUser.getSolde()), C_SUCCESS, 13, true);
        Label dataDirLabel = label("Données : " + BD.getAppDir(), C_MUTED, 10, false);
        Button logout = dangerBtn("Se déconnecter");
        logout.setPrefWidth(200);
        logout.setOnAction(e -> {
            Authentification.logout();
            showLogin();
        });

        sidebar.getChildren().addAll(brand, navBox, spacer(), userLabel, sidebarSoldeLabel, dataDirLabel, logout);

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:" + C_BG + ";");
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        primaryStage.setScene(buildScene(root));
        showMarche();
    }

    private Button navBtn(String text, Runnable action) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + C_TEXT + ";-fx-font-size:13px;"
                + "-fx-background-radius:8;-fx-cursor:hand;-fx-padding:9 12;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + C_CARD + ";-fx-text-fill:" + C_ACCENT2 + ";-fx-font-size:13px;"
                + "-fx-background-radius:8;-fx-cursor:hand;-fx-padding:9 12;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + C_TEXT + ";-fx-font-size:13px;"
                + "-fx-background-radius:8;-fx-cursor:hand;-fx-padding:9 12;"));
        b.setOnAction(e -> action.run());
        return b;
    }

    private void showMarche() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");

        Label title = label("Le Marché", C_TEXT, 24, true);
        Label subtitle = label("Annonces sauvegardées automatiquement. Les images acceptent les URL et les fichiers locaux.", C_MUTED, 12, false);

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        TextField search = styledField("🔍 Rechercher...");
        search.setPrefWidth(280);

        ComboBox<String> catBox = new ComboBox<>();
        catBox.setStyle(fieldStyle());
        catBox.getItems().add("Toutes catégories");
        for (Categorie c : Categorie.values()) catBox.getItems().add(c.getDescription());
        catBox.setValue("Toutes catégories");

        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.setStyle(fieldStyle());
        sortBox.getItems().addAll("Plus récentes", "Prix ↑", "Prix ↓");
        sortBox.setValue("Plus récentes");

        Button btnRefresh = accentBtn("Appliquer");
        toolbar.getChildren().addAll(search, catBox, sortBox, btnRefresh);

        FlowPane grid = new FlowPane(14, 14);
        grid.setPrefWrapLength(930);
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Runnable refresh = () -> {
            grid.getChildren().clear();
            List<Annonce> annonces = new ArrayList<>(BD.annonces);
            annonces.removeIf(a -> a.getStatue() == Statue.VENDU);

            String query = search.getText().trim().toLowerCase();
            if (!query.isEmpty()) {
                annonces.removeIf(a -> !a.getTitre().toLowerCase().contains(query)
                        && !a.getDescription().toLowerCase().contains(query));
            }

            if (!"Toutes catégories".equals(catBox.getValue())) {
                annonces.removeIf(a -> !a.getCategorie().getDescription().equals(catBox.getValue()));
            }

            if ("Prix ↑".equals(sortBox.getValue())) {
                annonces.sort(Comparator.comparingDouble(Annonce::getPrix));
            } else if ("Prix ↓".equals(sortBox.getValue())) {
                annonces.sort(Comparator.comparingDouble(Annonce::getPrix).reversed());
            } else {
                annonces.sort(Comparator.comparing(Annonce::getDate).reversed());
            }

            if (annonces.isEmpty()) {
                grid.getChildren().add(label("Aucune annonce trouvée.", C_MUTED, 14, false));
            } else {
                for (Annonce annonce : annonces) {
                    grid.getChildren().add(annonceCard(annonce));
                }
            }
        };

        btnRefresh.setOnAction(e -> refresh.run());
        search.setOnAction(e -> refresh.run());
        catBox.setOnAction(e -> refresh.run());
        sortBox.setOnAction(e -> refresh.run());
        refresh.run();

        page.getChildren().addAll(title, subtitle, toolbar, scroll);
        setContent(page);
    }

    private VBox annonceCard(Annonce annonce) {
        VBox box = new VBox(10);
        box.setPrefWidth(250);
        box.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:14;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:14;-fx-padding:14;-fx-cursor:hand;");
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color:#29293a;-fx-background-radius:14;"
                + "-fx-border-color:" + C_ACCENT + ";-fx-border-radius:14;-fx-padding:14;-fx-cursor:hand;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:14;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:14;-fx-padding:14;-fx-cursor:hand;"));
        box.setOnMouseClicked(e -> showAnnonceDetail(annonce));

        Node image = buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 220, 140);
        String badgeBg = annonce.getStatue() == Statue.DISPONIBLE ? "#173625" : annonce.getStatue() == Statue.EN_COURS ? "#3a2e10" : "#3a1a1a";
        String badgeFg = annonce.getStatue() == Statue.DISPONIBLE ? C_SUCCESS : annonce.getStatue() == Statue.EN_COURS ? C_WARNING : C_DANGER;

        HBox footer = new HBox(8);
        footer.setAlignment(Pos.CENTER_LEFT);
        Label seller = label("@" + annonce.getVendeur().getUsername(), C_MUTED, 11, false);
        Label date = label(annonce.getDate().toString(), C_MUTED, 11, false);
        footer.getChildren().addAll(seller, spacer(), date);

        box.getChildren().addAll(
                image,
                badge(annonce.getStatue().getDescription(), badgeBg, badgeFg),
                label(annonce.getTitre(), C_TEXT, 14, true),
                label(annonce.getPrixFormate(), C_ACCENT2, 16, true),
                label("⭐ " + (annonce.getMoyenneAnnonce() == 0 ? "—" : String.format("%.1f", annonce.getMoyenneAnnonce())), C_WARNING, 12, false),
                footer
        );
        return box;
    }

    private void showAnnonceDetail(Annonce annonce) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");

        Button back = ghostBtn("← Retour au marché");
        back.setOnAction(e -> showMarche());

        HBox header = new HBox(24);
        header.setAlignment(Pos.TOP_LEFT);
        header.getChildren().addAll(
                buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 300, 220),
                buildAnnonceInfoPanel(annonce)
        );

        VBox actions = new VBox(14);
        actions.getChildren().add(label("Actions", C_TEXT, 18, true));
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        User currentUser = Authentification.currentUser;
        boolean owner = currentUser.equals(annonce.getVendeur());
        boolean favorite = currentUser.favoris.contains(annonce);
        boolean pendingBuyer = currentUser.achatsEnCours.contains(annonce) || currentUser.equals(annonce.getAcheteurEnAttente());

        if (!owner) {
            if (annonce.getStatue() == Statue.DISPONIBLE) {
                Button buy = accentBtn("🛒 Acheter");
                buy.setOnAction(e -> {
                    if (!showConfirm("Confirmation", "Acheter \"" + annonce.getTitre() + "\" pour " + annonce.getPrixFormate() + " ?")) return;
                    String error = Commerce.initierAchat(annonce);
                    if (error == null) {
                        refreshSoldeLabel();
                        showAlert("Succès", "Achat initié. Vous pouvez confirmer ou annuler depuis Mes achats.", Alert.AlertType.INFORMATION);
                        showAnnonceDetail(annonce);
                    } else {
                        showAlert("Erreur", error, Alert.AlertType.ERROR);
                    }
                });
                row.getChildren().add(buy);
            }

            Button fav = ghostBtn(favorite ? "💜 Retirer des favoris" : "🤍 Ajouter aux favoris");
            fav.setOnAction(e -> {
                if (currentUser.favoris.contains(annonce)) currentUser.favoris.remove(annonce);
                else currentUser.favoris.add(annonce);
                BD.update();
                showAnnonceDetail(annonce);
            });
            row.getChildren().add(fav);

            if (pendingBuyer) {
                Button finish = successBtn("✔ Confirmer réception");
                finish.setOnAction(e -> {
                    String error = Commerce.finaliserAchat(annonce);
                    if (error == null) {
                        showAlert("Succès", "Achat finalisé.", Alert.AlertType.INFORMATION);
                        showMarche();
                    } else {
                        showAlert("Erreur", error, Alert.AlertType.ERROR);
                    }
                });
                Button cancel = dangerBtn("✖ Annuler");
                cancel.setOnAction(e -> {
                    String error = Commerce.annulerAchat(annonce);
                    if (error == null) {
                        refreshSoldeLabel();
                        showAlert("Annulé", "Achat annulé et remboursé.", Alert.AlertType.INFORMATION);
                        showAnnonceDetail(annonce);
                    } else {
                        showAlert("Erreur", error, Alert.AlertType.ERROR);
                    }
                });
                row.getChildren().addAll(finish, cancel);
            }

            if (annonce.getStatue() == Statue.VENDU) {
                ComboBox<Integer> noteBox = new ComboBox<>();
                noteBox.getItems().addAll(1, 2, 3, 4, 5);
                noteBox.setPromptText("Note");
                noteBox.setStyle(fieldStyle());
                Button rate = ghostBtn("⭐ Noter");
                rate.setOnAction(e -> {
                    if (noteBox.getValue() == null) {
                        showAlert("Info", "Choisissez une note.", Alert.AlertType.INFORMATION);
                        return;
                    }
                    annonce.notes.add(noteBox.getValue());
                    BD.update();
                    showAlert("Merci", "Votre note a été ajoutée.", Alert.AlertType.INFORMATION);
                    showAnnonceDetail(annonce);
                });
                row.getChildren().addAll(noteBox, rate);
            }
        } else {
            if (annonce.getStatue() == Statue.DISPONIBLE) {
                Button edit = ghostBtn("✏ Modifier l'annonce");
                edit.setOnAction(e -> showAnnonceForm(annonce));
                Button delete = dangerBtn("🗑 Supprimer");
                delete.setOnAction(e -> {
                    if (!showConfirm("Supprimer", "Supprimer définitivement cette annonce ?")) return;
                    if (Poste.supprimer(annonce)) {
                        showAlert("Supprimé", "Annonce supprimée.", Alert.AlertType.INFORMATION);
                        showMesVentes();
                    } else {
                        showAlert("Erreur", "Suppression impossible.", Alert.AlertType.ERROR);
                    }
                });
                row.getChildren().addAll(edit, delete);
            } else {
                row.getChildren().add(label("Annonce non modifiable car elle n'est plus disponible.", C_MUTED, 13, false));
            }
        }

        actions.getChildren().add(row);
        page.getChildren().addAll(back, header, sep(), actions);
        scroll.setContent(page);
        setContent(scroll);
    }

    private VBox buildAnnonceInfoPanel(Annonce annonce) {
        VBox info = new VBox(12);
        info.setPrefWidth(720);
        info.getChildren().addAll(
                label(annonce.getTitre(), C_TEXT, 28, true),
                label(annonce.getPrixFormate(), C_ACCENT2, 22, true)
        );

        HBox meta = new HBox(10,
                badge(annonce.getCategorie().getDescription(), C_SURFACE, C_TEXT),
                badge(annonce.getStatue().getDescription(), C_SURFACE,
                        annonce.getStatue() == Statue.DISPONIBLE ? C_SUCCESS : annonce.getStatue() == Statue.EN_COURS ? C_WARNING : C_DANGER),
                label("@" + annonce.getVendeur().getUsername(), C_MUTED, 13, false),
                label(annonce.getDate().toString(), C_MUTED, 13, false)
        );
        meta.setAlignment(Pos.CENTER_LEFT);

        Label descriptionTitle = label("Description", C_MUTED, 13, true);
        Label description = label(annonce.getDescription(), C_TEXT, 14, false);
        Label rating = label(annonce.getMoyenneAnnonce() == 0
                ? "⭐ Aucun avis"
                : String.format("⭐ %.1f / 5 (%d avis)", annonce.getMoyenneAnnonce(), annonce.notes.size()), C_WARNING, 14, false);
        Hyperlink imageLink = new Hyperlink("Voir la source de l'image");
        imageLink.setOnAction(e -> showAlert("Image", annonce.getPhoto(), Alert.AlertType.INFORMATION));

        info.getChildren().addAll(meta, rating, descriptionTitle, description, imageLink);
        return info;
    }

    private void showFavoris() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");
        page.getChildren().add(label("❤ Mes favoris", C_TEXT, 24, true));

        Authentification.currentUser.favoris.removeIf(a -> !BD.annonces.contains(a));
        BD.update();

        FlowPane grid = new FlowPane(14, 14);
        grid.setPrefWrapLength(930);
        if (Authentification.currentUser.favoris.isEmpty()) {
            grid.getChildren().add(label("Aucun favori pour le moment.", C_MUTED, 14, false));
        } else {
            for (Annonce annonce : Authentification.currentUser.favoris) {
                grid.getChildren().add(annonceCard(annonce));
            }
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        page.getChildren().add(scroll);
        setContent(page);
    }

    private void showMesAchats() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");
        page.getChildren().add(label("🛍 Mes achats", C_TEXT, 24, true));

        if (Authentification.currentUser.achatsEnCours.isEmpty() && Authentification.currentUser.historiqueAchats.isEmpty()) {
            page.getChildren().add(label("Aucun achat.", C_MUTED, 14, false));
        } else {
            for (Annonce annonce : new ArrayList<>(Authentification.currentUser.achatsEnCours)) {
                HBox row = new HBox(16);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:12;"
                        + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:12;-fx-padding:14;");
                Button details = ghostBtn("Voir");
                details.setOnAction(e -> showAnnonceDetail(annonce));
                Button finish = successBtn("✔ Confirmer");
                finish.setOnAction(e -> {
                    String error = Commerce.finaliserAchat(annonce);
                    if (error == null) {
                        showAlert("Succès", "Achat finalisé.", Alert.AlertType.INFORMATION);
                        showMesAchats();
                    } else {
                        showAlert("Erreur", error, Alert.AlertType.ERROR);
                    }
                });
                Button cancel = dangerBtn("✖ Annuler");
                cancel.setOnAction(e -> {
                    String error = Commerce.annulerAchat(annonce);
                    if (error == null) {
                        refreshSoldeLabel();
                        showAlert("Annulé", "Achat annulé et remboursé.", Alert.AlertType.INFORMATION);
                        showMesAchats();
                    } else {
                        showAlert("Erreur", error, Alert.AlertType.ERROR);
                    }
                });

                row.getChildren().addAll(
                        buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 90, 70),
                        label(annonce.getTitre(), C_TEXT, 14, true),
                        label(annonce.getPrixFormate(), C_ACCENT2, 14, true),
                        spacer(),
                        details,
                        finish,
                        cancel
                );
                page.getChildren().add(row);
            }
            if (!Authentification.currentUser.historiqueAchats.isEmpty()) {
                page.getChildren().add(label("Achats completes", C_MUTED, 14, true));
                for (Annonce annonce : new ArrayList<>(Authentification.currentUser.historiqueAchats)) {
                    HBox row = new HBox(16);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:12;"
                            + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:12;-fx-padding:14;");
                    Button details = ghostBtn("Voir");
                    details.setOnAction(e -> showAnnonceDetail(annonce));
                    row.getChildren().addAll(
                            buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 90, 70),
                            label(annonce.getTitre(), C_TEXT, 14, true),
                            label(annonce.getPrixFormate(), C_ACCENT2, 14, true),
                            badge("Achete", "#173625", C_SUCCESS),
                            spacer(),
                            details
                    );
                    page.getChildren().add(row);
    }
}
        }
        setContent(page);
    }

    private void showMesVentes() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");
        page.getChildren().add(label("📦 Mes ventes", C_TEXT, 24, true));

        List<Annonce> mine = new ArrayList<>();
        for (Annonce annonce : BD.annonces) {
            if (Authentification.currentUser.equals(annonce.getVendeur())) {
                mine.add(annonce);
            }
        }
        mine.sort(Comparator.comparing(Annonce::getDate).reversed());

        if (mine.isEmpty()) {
            page.getChildren().add(label("Vous n'avez publié aucune annonce.", C_MUTED, 14, false));
        } else {
            for (Annonce annonce : mine) {
                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:12;"
                        + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:12;-fx-padding:14;");

                Button view = ghostBtn("Voir");
                view.setOnAction(e -> showAnnonceDetail(annonce));
                row.getChildren().addAll(
                        buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 90, 70),
                        label(annonce.getTitre(), C_TEXT, 14, true),
                        label(annonce.getPrixFormate(), C_ACCENT2, 14, true),
                        badge(annonce.getStatue().getDescription(), C_SURFACE,
                                annonce.getStatue() == Statue.DISPONIBLE ? C_SUCCESS : annonce.getStatue() == Statue.EN_COURS ? C_WARNING : C_DANGER),
                        spacer(),
                        view
                );

                if (annonce.getStatue() == Statue.DISPONIBLE) {
                    Button edit = ghostBtn("Modifier");
                    edit.setOnAction(e -> showAnnonceForm(annonce));
                    Button delete = dangerBtn("Supprimer");
                    delete.setOnAction(e -> {
                        if (!showConfirm("Suppression", "Supprimer cette annonce ?")) return;
                        if (Poste.supprimer(annonce)) {
                            showAlert("Supprimée", "Annonce supprimée.", Alert.AlertType.INFORMATION);
                            showMesVentes();
                        } else {
                            showAlert("Erreur", "Suppression impossible.", Alert.AlertType.ERROR);
                        }
                    });
                    row.getChildren().addAll(edit, delete);
                }
                page.getChildren().add(row);
            }
        }
        setContent(page);
    }

    private void showPublier() {
        showAnnonceForm(null);
    }

    private void showAnnonceForm(Annonce annonce) {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox page = new VBox(20);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");

        boolean editMode = annonce != null;
        page.getChildren().add(label(editMode ? "✏ Modifier une annonce" : "➕ Publier une annonce", C_TEXT, 24, true));

        VBox form = card(700);
        TextField tfTitre = styledField("Titre");
        TextArea taDescription = new TextArea();
        taDescription.setPromptText("Description détaillée (minimum 30 caractères)");
        taDescription.setPrefRowCount(5);
        taDescription.setStyle(fieldStyle());
        TextField tfPhoto = styledField("URL d'image ou chemin local (.jpg/.png/.gif/.webp)");
        TextField tfPrix = styledField("Prix en DH");

        ComboBox<String> catBox = new ComboBox<>();
        catBox.setStyle(fieldStyle());
        for (Categorie categorie : Categorie.values()) catBox.getItems().add(categorie.getDescription());
        catBox.setPromptText("Catégorie");

        if (editMode) {
            tfTitre.setText(annonce.getTitre());
            taDescription.setText(annonce.getDescription());
            tfPhoto.setText(annonce.getPhoto());
            tfPrix.setText(String.valueOf(annonce.getPrix()));
            catBox.setValue(annonce.getCategorie().getDescription());
        }

        StackPane preview = buildImageBox(tfPhoto.getText(), editMode ? annonce.getCategorie() : Categorie.Autre, 320, 210);
        Label hint = label("Astuce : vous pouvez coller une URL d'image ou cliquer sur “Choisir une image” pour copier un fichier local dans le dossier de l'application.", C_MUTED, 11, false);
        Label storageHint = label("Dossier images : " + BD.getImagesDir(), C_MUTED, 11, false);
        Label errLabel = label("", C_DANGER, 13, false);

        Runnable refreshPreview = () -> {
            Categorie selected = Categorie.Autre;
            if (catBox.getValue() != null) {
                for (Categorie c : Categorie.values()) if (c.getDescription().equals(catBox.getValue())) selected = c;
            }
            preview.getChildren().setAll(buildImageBox(tfPhoto.getText().trim(), selected, 320, 210).getChildren());
            preview.setStyle("-fx-background-color:" + C_SURFACE + ";-fx-background-radius:14;");
            preview.setMinSize(320, 210);
            preview.setPrefSize(320, 210);
        };

        Button chooseImage = ghostBtn("🖼 Choisir une image");
        chooseImage.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choisir une image");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"));
            File selected = chooser.showOpenDialog(primaryStage);
            if (selected == null) return;
            try {
                String stored = ImageStorage.importLocalImage(selected);
                tfPhoto.setText(stored);
                refreshPreview.run();
            } catch (Exception ex) {
                showAlert("Erreur", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        Button previewButton = ghostBtn("Aperçu image");
        previewButton.setOnAction(e -> refreshPreview.run());

        Button save = accentBtn(editMode ? "Enregistrer les modifications" : "Publier l'annonce");
        Button cancel = ghostBtn("Annuler");
        save.setOnAction(e -> {
            try {
                double prix = Double.parseDouble(tfPrix.getText().trim());
                Categorie selectedCategorie = null;
                for (Categorie c : Categorie.values()) {
                    if (c.getDescription().equals(catBox.getValue())) selectedCategorie = c;
                }
                if (selectedCategorie == null) {
                    errLabel.setText("❌ Sélectionnez une catégorie.");
                    return;
                }

                String normalizedPhoto = ImageStorage.normalizeForStorage(tfPhoto.getText().trim());
                boolean ok;
                if (editMode) {
                    ok = Poste.modifier(annonce, tfTitre.getText().trim(), taDescription.getText().trim(), normalizedPhoto, prix, selectedCategorie);
                } else {
                    ok = Poste.publier(tfTitre.getText().trim(), taDescription.getText().trim(), normalizedPhoto, prix, selectedCategorie);
                }

                if (ok) {
                    showAlert("Succès", editMode ? "Annonce modifiée avec succès." : "Annonce publiée avec succès.", Alert.AlertType.INFORMATION);
                    showMesVentes();
                } else {
                    errLabel.setText(editMode
                            ? "❌ Impossible de modifier cette annonce. Vérifiez qu'elle est encore disponible et que le titre n'est pas déjà utilisé."
                            : "❌ Ce titre existe déjà.");
                }
            } catch (NumberFormatException ex) {
                errLabel.setText("❌ Prix invalide.");
            } catch (Exception ex) {
                errLabel.setText("❌ " + ex.getMessage());
            }
        });
        cancel.setOnAction(e -> showMesVentes());

        HBox imageButtons = new HBox(10, chooseImage, previewButton);
        HBox actionButtons = new HBox(10, save, cancel);

        form.getChildren().addAll(
                label("Titre", C_MUTED, 12, true), tfTitre,
                label("Description", C_MUTED, 12, true), taDescription,
                label("Image", C_MUTED, 12, true), tfPhoto, hint, storageHint, imageButtons,
                preview,
                label("Prix", C_MUTED, 12, true), tfPrix,
                label("Catégorie", C_MUTED, 12, true), catBox,
                errLabel,
                actionButtons
        );

        page.getChildren().add(form);
        scroll.setContent(page);
        setContent(scroll);
    }

    private void showProfil() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(28));
        page.setStyle("-fx-background-color:" + C_BG + ";");
        page.getChildren().add(label("👤 Mon profil", C_TEXT, 24, true));

        User user = Authentification.currentUser;
        VBox profileCard = card(520);
        profileCard.getChildren().addAll(
                infoRow("Nom d'utilisateur", "@" + user.getUsername()),
                sep(),
                infoRow("Email", user.getEmail()),
                sep(),
                infoRow("Solde", String.format("%.2f DH", user.getSolde())),
                sep(),
                infoRow("Note vendeur", user.getNoteMoyenne() == 0 ? "Aucune note" : String.format("⭐ %.2f / 5", user.getNoteMoyenne())),
                sep(),
                infoRow("Rôle", user.isAdmin() ? "Administrateur" : "Utilisateur")
        );

        long sold = BD.annonces.stream().filter(a -> user.equals(a.getVendeur()) && a.getStatue() == Statue.VENDU).count();
        long active = BD.annonces.stream().filter(a -> user.equals(a.getVendeur()) && a.getStatue() == Statue.DISPONIBLE).count();
        HBox stats = new HBox(16,
                statCard("Annonces actives", String.valueOf(active)),
                statCard("Articles vendus", String.valueOf(sold)),
                statCard("Achats en cours", String.valueOf(user.achatsEnCours.size())),
                statCard("Favoris", String.valueOf(user.favoris.size()))
        );

        page.getChildren().addAll(profileCard, label("Statistiques", C_MUTED, 14, true), stats);
        setContent(page);
    }

    private HBox infoRow(String key, String value) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        Label k = label(key, C_MUTED, 13, false);
        k.setPrefWidth(170);
        row.getChildren().addAll(k, label(value, C_TEXT, 13, true));
        return row;
    }

    private VBox statCard(String title, String value) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(130);
        box.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:12;"
                + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:12;-fx-padding:16;");
        box.getChildren().addAll(label(value, C_ACCENT2, 26, true), label(title, C_MUTED, 11, false));
        return box;
    }

    private void showAdmin() {
        if (!Authentification.currentUser.isAdmin()) return;

        TabPane tabs = new TabPane();
        tabs.setStyle("-fx-background-color:" + C_BG + ";-fx-tab-min-width:160;");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab usersTab = new Tab("👥 Utilisateurs");
        VBox usersPage = new VBox(14);
        usersPage.setPadding(new Insets(20));
        usersPage.setStyle("-fx-background-color:" + C_BG + ";");
        for (User user : new ArrayList<>(BD.users)) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:10;"
                    + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:10;-fx-padding:12;");
            Button recharge = ghostBtn("💰 Recharger");
            recharge.setOnAction(e -> showRechargeDialog(user));
            Button ban = dangerBtn("🚫 Bannir");
            if (user.isAdmin()) ban.setDisable(true);
            ban.setOnAction(e -> {
                if (!showConfirm("Suppression", "Supprimer l'utilisateur @" + user.getUsername() + " ?")) return;
                BD.annonces.removeIf(a -> user.equals(a.getVendeur()));
                for (User other : BD.users) {
                    other.favoris.removeIf(a -> user.equals(a.getVendeur()));
                    other.achatsEnCours.removeIf(a -> user.equals(a.getVendeur()));
                    other.ventesEnCours.removeIf(a -> user.equals(a.getVendeur()));
                }
                BD.users.remove(user);
                BD.update();
                showAdmin();
            });
            row.getChildren().addAll(
                    label("@" + user.getUsername(), C_TEXT, 13, true),
                    label(user.getEmail(), C_MUTED, 12, false),
                    label(String.format("%.2f DH", user.getSolde()), C_SUCCESS, 12, true),
                    spacer(),
                    recharge,
                    ban
            );
            usersPage.getChildren().add(row);
        }
        ScrollPane usersScroll = new ScrollPane(usersPage);
        usersScroll.setFitToWidth(true);
        usersScroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        usersTab.setContent(usersScroll);

        Tab annoncesTab = new Tab("📋 Annonces");
        VBox annoncesPage = new VBox(14);
        annoncesPage.setPadding(new Insets(20));
        annoncesPage.setStyle("-fx-background-color:" + C_BG + ";");
        for (Annonce annonce : new ArrayList<>(BD.annonces)) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:10;"
                    + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:10;-fx-padding:12;");
            Button view = ghostBtn("Voir");
            view.setOnAction(e -> showAnnonceDetail(annonce));
            Button delete = dangerBtn("Supprimer");
            delete.setOnAction(e -> {
                if (!showConfirm("Suppression", "Supprimer l'annonce \"" + annonce.getTitre() + "\" ?")) return;
                Poste.supprimerAdmin(annonce);
                showAdmin();
            });
            row.getChildren().addAll(
                    buildImageBox(annonce.getPhoto(), annonce.getCategorie(), 90, 70),
                    label(annonce.getTitre(), C_TEXT, 13, true),
                    label("@" + annonce.getVendeur().getUsername(), C_MUTED, 12, false),
                    label(annonce.getPrixFormate(), C_ACCENT2, 12, true),
                    spacer(),
                    view,
                    delete
            );
            annoncesPage.getChildren().add(row);
        }
        ScrollPane annoncesScroll = new ScrollPane(annoncesPage);
        annoncesScroll.setFitToWidth(true);
        annoncesScroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        annoncesTab.setContent(annoncesScroll);

        Tab transactionsTab = new Tab("💳 Transactions");
        VBox transactionsPage = new VBox(14);
        transactionsPage.setPadding(new Insets(20));
        transactionsPage.setStyle("-fx-background-color:" + C_BG + ";");
        if (BD.transactions.isEmpty()) {
            transactionsPage.getChildren().add(label("Aucune transaction enregistrée.", C_MUTED, 14, false));
        } else {
            for (Transaction transaction : BD.transactions) {
                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle("-fx-background-color:" + C_CARD + ";-fx-background-radius:10;"
                        + "-fx-border-color:" + C_BORDER + ";-fx-border-radius:10;-fx-padding:12;");
                row.getChildren().addAll(
                        label(transaction.dateTransaction.toString(), C_MUTED, 12, false),
                        label("@" + (transaction.acheteur == null ? "inconnu" : transaction.acheteur.getUsername()), C_TEXT, 13, false),
                        label("→", C_MUTED, 14, false),
                        label("@" + (transaction.vendeur == null ? "inconnu" : transaction.vendeur.getUsername()), C_TEXT, 13, false),
                        label(String.format("%.2f DH", transaction.prixVente), C_SUCCESS, 14, true)
                );
                transactionsPage.getChildren().add(row);
            }
        }
        ScrollPane transactionsScroll = new ScrollPane(transactionsPage);
        transactionsScroll.setFitToWidth(true);
        transactionsScroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        transactionsTab.setContent(transactionsScroll);

        tabs.getTabs().addAll(usersTab, annoncesTab, transactionsTab);
        VBox page = new VBox(16, label("⚙ Administration", C_TEXT, 24, true), tabs);
        page.setPadding(new Insets(28, 28, 0, 28));
        page.setStyle("-fx-background-color:" + C_BG + ";");
        VBox.setVgrow(tabs, Priority.ALWAYS);
        setContent(page);
    }

    private void showRechargeDialog(User user) {
        TextInputDialog dialog = new TextInputDialog("100");
        dialog.setTitle("Recharger le compte");
        dialog.setHeaderText("Montant à ajouter pour @" + user.getUsername());
        dialog.setContentText("Montant (DH) :");
        dialog.getDialogPane().setStyle("-fx-background-color:" + C_SURFACE + ";");
        dialog.showAndWait().ifPresent(value -> {
            try {
                double amount = Double.parseDouble(value.trim());
                if (amount <= 0) throw new NumberFormatException();
                user.setSolde(user.getSolde() + amount);
                BD.update();
                showAlert("Succès", String.format("+%.2f DH ajoutés à @%s", amount, user.getUsername()), Alert.AlertType.INFORMATION);
                if (Authentification.currentUser.equals(user)) refreshSoldeLabel();
                showAdmin();
            } catch (Exception ex) {
                showAlert("Erreur", "Montant invalide.", Alert.AlertType.ERROR);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
