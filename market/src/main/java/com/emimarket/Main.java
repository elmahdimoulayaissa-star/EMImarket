package com.emimarket;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        BD.load();
        runLoginMenu();
        scanner.close();
    }

    private static int readInt() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre : ");
            }
        }
    }

    private static String prompt(String label) {
        System.out.print(label + " : ");
        return scanner.nextLine().trim();
    }



    private static void runLoginMenu() {
        boolean running = true;
        while (running) {
            MenuUtils.menuLogin();
            switch (readInt()) {
                case 0  -> running = false;
                case 1  -> runMainMenu();
                case 2  -> inscrire();
                default -> System.out.println("Choix invalide !");
            }
        }
        System.out.println("\nAu revoir !");
    }


    private static void runMainMenu() {
        if (!login()) return;

        boolean running = true;
        while (running) {
            MenuUtils.menuPrincipal(Authentification.currentUser.isAdmin());
            switch (readInt()) {
                case 0  -> { Authentification.logout(); running = false; }
                case 1  -> Authentification.afficherProfil();
                case 2  -> runAnnoncesMenu();
                case 3  -> runVentesMenu();
                case 4  -> { if (Authentification.currentUser.isAdmin()) runAdminMenu();
                             else System.out.println("Choix invalide !"); }
                default -> System.out.println("Choix invalide !");
            }
        }
    }



    private static void runAnnoncesMenu() {
        boolean running = true;
        while (running) {
            MenuUtils.menuAnnonces();
            switch (readInt()) {
                case 0  -> running = false;
                case 1  -> afficherToutesLesAnnonces();
                case 2  -> rechercherParMotCle();
                case 3  -> afficherTrieParPrix();
                case 4  -> afficherFavoris();
                case 5  -> afficherAchatsEnAttente();
                case 6  -> confirmerReception();
                case 7  -> publierAnnonce();
                default -> System.out.println("Choix invalide !");
            }
        }
    }



    private static void runVentesMenu() {
        ArrayList<Annonce> ventes = Authentification.currentUser.ventesEnCours;
        if (ventes.isEmpty()) {
            System.out.println("\nVous n'avez aucune vente en cours.");
            return;
        }

        System.out.println("\n--- MES VENTES EN COURS ---");
        for (int i = 0; i < ventes.size(); i++) {
            System.out.printf("%d- %s (%.2f DH) – %s%n",
                i + 1,
                ventes.get(i).getTitre(),
                ventes.get(i).getPrix(),
                ventes.get(i).getStatue().getDescription());
        }
        System.out.println("0- Retour");
        System.out.print("\nChoix : ");

        int choix = readInt();
        if (choix == 0 || choix > ventes.size()) return;

        Annonce annonce = ventes.get(choix - 1);
        System.out.println(annonce);

        System.out.println("1- Supprimer cette annonce\n0- Retour");
        System.out.print("Action : ");
        if (readInt() == 1) {
            if (Poste.supprimer(annonce)) {
                System.out.println("Annonce supprimée.");
            } else {
                System.out.println("Impossible de supprimer cette annonce (déjà en cours de transaction).");
            }
        }
    }



    private static void runAdminMenu() {
        boolean running = true;
        while (running) {
            MenuUtils.menuAdmin();
            switch (readInt()) {
                case 0  -> running = false;
                case 1  -> afficherTousLesUtilisateurs();
                case 2  -> rechargerCompte();
                case 3  -> supprimerAnnonceAdmin();
                case 4  -> bannirUtilisateur();
                default -> System.out.println("Choix invalide !");
            }
        }
    }



    private static boolean login() {
        String username = prompt("Username");
        String password = prompt("Password");
        if (Authentification.login(username, password)) return true;
        System.out.println("Username ou password incorrects !");
        return false;
    }

    private static void inscrire() {
        String username = prompt("Username");
        String password = prompt("Password");
        String email    = prompt("Email");
        try {
            boolean ok = Authentification.inscrire(username, password, email);
            System.out.println(ok ? "Inscription réussie !" : "Ce username existe déjà.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }



    private static void afficherToutesLesAnnonces() {
        System.out.println();
        Poste.afficherToutesLesAnnonces();
        ouvrirAnnonceParTitre();
    }

    private static void rechercherParMotCle() {
        String motCle = prompt("Mot-clé");
        ArrayList<Annonce> resultats = Poste.filtrerParMotCle(motCle);
        if (resultats.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        resultats.forEach(System.out::println);
        ouvrirAnnonceDepuisListe(resultats);
    }

    private static void afficherTrieParPrix() {
        ArrayList<Annonce> tries = Poste.trierParPrix();
        if (tries.isEmpty()) {
            System.out.println("Aucune annonce disponible.");
            return;
        }
        tries.forEach(System.out::println);
        ouvrirAnnonceDepuisListe(tries);
    }

    private static void afficherFavoris() {
        ArrayList<Annonce> favoris = Authentification.currentUser.favoris;
        if (favoris.isEmpty()) {
            System.out.println("\nVous n'avez aucun favori.");
            return;
        }
        favoris.forEach(System.out::println);
        ouvrirAnnonceDepuisListe(favoris);
    }

    private static void afficherAchatsEnAttente() {
        ArrayList<Annonce> achats = Authentification.currentUser.achatsEnCours;
        if (achats.isEmpty()) {
            System.out.println("\nAucun achat en attente.");
            return;
        }
        for (int i = 0; i < achats.size(); i++) {
            System.out.printf("%d- %s (%.2f DH)%n", i + 1, achats.get(i).getTitre(), achats.get(i).getPrix());
        }
        System.out.println("0- Retour");
        System.out.print("\nChoix : ");

        int choix = readInt();
        if (choix == 0 || choix > achats.size()) return;

        Annonce annonce = achats.get(choix - 1);
        System.out.println("\n1- Annuler l'achat\n0- Retour");
        System.out.print("Action : ");
        if (readInt() == 1) {
            boolean ok = Commerce.annulerAchat(annonce);
            System.out.println(ok ? "Achat annulé, solde remboursé." : "Annulation impossible (délai de 3 jours dépassé).");
        }
    }

    private static void confirmerReception() {
        ArrayList<Annonce> achats = Authentification.currentUser.achatsEnCours;
        if (achats.isEmpty()) {
            System.out.println("\nAucun achat en attente de confirmation.");
            return;
        }
        for (int i = 0; i < achats.size(); i++) {
            System.out.printf("%d- %s (%.2f DH)%n", i + 1, achats.get(i).getTitre(), achats.get(i).getPrix());
        }
        System.out.println("0- Retour");
        System.out.print("\nChoix : ");

        int choix = readInt();
        if (choix == 0 || choix > achats.size()) return;

        Annonce annonce = achats.get(choix - 1);
        boolean ok = Commerce.finaliserAchat(annonce);
        System.out.println(ok ? "Réception confirmée, transaction finalisée !" : "Erreur lors de la confirmation.");
    }

    private static void publierAnnonce() {
        String titre       = prompt("Titre");
        String description = prompt("Description");
        String photo       = prompt("Photo (ex: image.jpg)");

        double prix = 0;
        while (prix <= 0) {
            try {
                prix = Double.parseDouble(prompt("Prix (DH)"));
            } catch (NumberFormatException e) {
                System.out.println("Prix invalide, réessayez.");
            }
        }

        System.out.println("Catégories : ");
        Categorie[] cats = Categorie.values();
        for (int i = 0; i < cats.length; i++) {
            System.out.printf("%d- %s%n", i + 1, cats[i].getDescription());
        }
        System.out.print("Choix : ");
        int catChoix = readInt();
        if (catChoix < 1 || catChoix > cats.length) {
            System.out.println("Catégorie invalide.");
            return;
        }
        Categorie categorie = cats[catChoix - 1];

        try {
            boolean ok = Poste.publier(titre, description, photo, prix, categorie);
            System.out.println(ok ? "Annonce publiée !" : "Une annonce avec ce titre existe déjà.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }



    private static void ouvrirAnnonceParTitre() {
        String titre = prompt("\nEntrer le titre d'une annonce pour plus de détails (ou laisser vide pour retour)");
        if (titre.isEmpty()) return;
        for (Annonce a : BD.annonces) {
            if (a.getTitre().equalsIgnoreCase(titre)) {
                ouvrirDetailAnnonce(a);
                return;
            }
        }
        System.out.println("Annonce introuvable.");
    }

    private static void ouvrirAnnonceDepuisListe(ArrayList<Annonce> liste) {
        String titre = prompt("\nEntrer le titre d'une annonce pour plus de détails (ou laisser vide pour retour)");
        if (titre.isEmpty()) return;
        for (Annonce a : liste) {
            if (a.getTitre().equalsIgnoreCase(titre)) {
                ouvrirDetailAnnonce(a);
                return;
            }
        }
        System.out.println("Annonce introuvable.");
    }

    private static void ouvrirDetailAnnonce(Annonce annonce) {
        System.out.println(annonce);
        MenuUtils.menuAnnonceParticuliere();
        switch (readInt()) {
            case 0  -> { /* retour */ }
            case 1  -> noterAnnonce(annonce);
            case 2  -> toggleFavori(annonce);
            case 3  -> acheterAnnonce(annonce);
            default -> System.out.println("Choix invalide !");
        }
    }

    private static void noterAnnonce(Annonce annonce) {
        System.out.print("Note (1-5) : ");
        int note = readInt();
        if (note < 1 || note > 5) {
            System.out.println("Note invalide.");
            return;
        }
        annonce.notes.add(note);
        BD.update();
        System.out.println("Note enregistrée !");
    }

    private static void toggleFavori(Annonce annonce) {
        ArrayList<Annonce> favoris = Authentification.currentUser.favoris;
        if (favoris.contains(annonce)) {
            favoris.remove(annonce);
            System.out.println("Retiré des favoris.");
        } else {
            favoris.add(annonce);
            System.out.println("Ajouté aux favoris !");
        }
        BD.update();
    }

    private static void acheterAnnonce(Annonce annonce) {
        boolean ok = Commerce.initierAchat(annonce);
        System.out.println(ok ? "Achat initié ! En attente de confirmation." : "Achat échoué.");
    }



    private static void afficherTousLesUtilisateurs() {
        System.out.println("\n--- LISTE DES UTILISATEURS ---");
        for (User u : BD.users) {
            System.out.printf("%-15s | %-30s | %.2f DH | %s%n",
                u.getUsername(),
                u.getEmail(),
                u.getSolde(),
                u.isAdmin() ? "ADMIN" : "user");
        }
        System.out.println("------------------------------\n");
    }

    private static void rechargerCompte() {
        String username = prompt("Username à recharger");
        for (User u : BD.users) {
            if (u.getUsername().equals(username)) {
                System.out.print("Montant à ajouter : ");
                try {
                    double montant = Double.parseDouble(scanner.nextLine().trim());
                    u.setSolde(u.getSolde() + montant);
                    BD.update();
                    System.out.printf("Solde mis à jour : %.2f DH%n", u.getSolde());
                } catch (Exception e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Utilisateur introuvable.");
    }

    private static void supprimerAnnonceAdmin() {
        String titre = prompt("Titre de l'annonce à supprimer");
        for (Annonce a : BD.annonces) {
            if (a.getTitre().equalsIgnoreCase(titre)) {
                BD.annonces.remove(a);
                for (User u : BD.users) u.favoris.remove(a);
                BD.update();
                System.out.println("Annonce supprimée.");
                return;
            }
        }
        System.out.println("Annonce introuvable.");
    }

    private static void bannirUtilisateur() {
        String username = prompt("Username à bannir");
        if (username.equals(Authentification.currentUser.getUsername())) {
            System.out.println("Vous ne pouvez pas vous bannir vous-même.");
            return;
        }

        User cible = BD.users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);

        if (cible == null) {
            System.out.println("Utilisateur introuvable.");
            return;
        }

        BD.users.remove(cible);
        BD.annonces.removeIf(a -> a.getVendeur().equals(cible));
        for (User u : BD.users) u.favoris.removeIf(a -> a.getVendeur().equals(cible));
        BD.update();
        System.out.println("Utilisateur banni et ses annonces supprimées.");
    }

}