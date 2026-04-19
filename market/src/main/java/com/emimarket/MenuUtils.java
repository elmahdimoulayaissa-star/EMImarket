package com.emimarket;

public final class MenuUtils {

    public static void afficherTitre(String titre) {
        System.out.println("\n=========================================");
        System.out.println("   " + titre.toUpperCase());
        System.out.println("=========================================");
    }

    public static void menuLogin() {
        afficherTitre("Bienvenue");
        System.out.println("1- Se connecter\n2- S'inscrire\n0- Quitter");
        System.out.print("\nChoix : ");
    }

    public static void menuPrincipal(boolean isAdmin) {
        afficherTitre("Menu Principal");
        System.out.println("1- Mon Profil");
        System.out.println("2- Consulter le Marché (Acheter)");
        System.out.println("3- Gérer mes Ventes");
        if (isAdmin) {
            System.out.println("4- [ADMIN] Panneau d'Administration");
        }
        System.out.println("0- Se déconnecter");
        System.out.print("\nChoix : ");
    }

    public static void menuAnnonces() {
        afficherTitre("Le Marché");
        System.out.println("1- Toutes les annonces\n2- Rechercher par mot-clé\n3- Trier par prix");
        System.out.println("4- Mes Favoris\n5- Mes Achats en attente\n6- Confirmer réception d'un achat");
        System.out.println("7- Publier une annonce\n0- Retour");
        System.out.print("\nChoix : ");
    }

    public static void menuAnnonceParticuliere() {
        System.out.print("\n1- Noter\n2- Favoris\n3- Acheter\n0- Retour\nAction : ");
    }

    public static void menuAdmin() {
        afficherTitre("Panneau d'Administration");
        System.out.println("1- Voir tous les utilisateurs");
        System.out.println("2- Recharger le compte d'un utilisateur");
        System.out.println("3- Supprimer une annonce");
        System.out.println("4- Supprimer un utilisateur (Bannissement)");
        System.out.println("0- Retour");
        System.out.print("\nChoix : ");
    }
    
}
