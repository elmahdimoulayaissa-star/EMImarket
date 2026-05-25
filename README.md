# 🛒 Marketplace FX

Interface graphique **JavaFX** complète pour l'application Marketplace.

---

## 📁 Structure du projet

```
marketplace-fx/
├── pom.xml                          ← Build Maven
└── src/main/java/
    ├── MarketplaceApp.java          ← Application JavaFX principale
    ├── Annonce.java
    ├── Authentification.java
    ├── BD.java
    ├── Categorie.java
    ├── Commerce.java
    ├── Poste.java
    ├── Statue.java
    ├── Transaction.java
    └── User.java
```

---

## ⚙️ Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Java  | 17 (ou 21)      |
| Maven | 3.8+            |

> **JavaFX est téléchargé automatiquement par Maven** — vous n'avez rien à installer manuellement.

---

## 🚀 Lancer l'application

### Option 1 — Maven (recommandé)

```bash
cd marketplace-fx
mvn clean javafx:run
```

### Option 2 — Créer un JAR exécutable

```bash
mvn clean package
java -jar target/marketplace-fx-1.0.0.jar
```

> **Note :** Le JAR shade inclut JavaFX. Sur certains systèmes, JavaFX doit être dans le module-path :
> ```bash
> java --module-path /chemin/vers/javafx-sdk/lib \
>      --add-modules javafx.controls,javafx.fxml \
>      -jar target/marketplace-fx-1.0.0.jar
> ```

### Option 3 — IDE (IntelliJ / Eclipse / VS Code)

1. Ouvrir le dossier `marketplace-fx/` comme projet Maven.
2. Laisser l'IDE télécharger les dépendances.
3. Exécuter `MarketplaceApp.main()`.

---

## 🎨 Fonctionnalités de l'interface

### Écrans

| Écran | Description |
|-------|-------------|
| **Connexion** | Login avec animation shake sur erreur |
| **Inscription** | Validation complète username/password/email |
| **Marché** | Grille d'annonces avec recherche, filtre catégorie, tri par prix |
| **Détail annonce** | Infos complètes, achat, favoris, notation |
| **Mes Favoris** | Liste des annonces sauvegardées |
| **Mes Achats** | Achats en cours : confirmer ou annuler |
| **Mes Ventes** | Toutes les annonces du vendeur connecté |
| **Publier** | Formulaire de publication avec validation |
| **Profil** | Infos + statistiques personnelles |
| **Administration** | (admin seulement) Gérer users, annonces, voir transactions |

### Palette graphique

- Thème sombre avec accent violet `#7c5cfc`
- Cartes avec hover effects
- Badges colorés (statut, catégorie)
- Animations fade + shake
- Sidebar de navigation

---

## 💾 Persistance des données

Les fichiers `.ser` sont créés dans le **répertoire courant** au lancement :

```
users.ser
annonces.ser
transactions.ser
```

---

## 👤 Compte administrateur

Créez un compte avec le nom d'utilisateur **`admin`** — il obtient automatiquement les droits admin et accès au panneau d'administration.

---

# Users 

user = admin 
password = Azerty@123

user = Hiii
password = Azerty@123




