package com.emimarket;

public class Authentification {

    public static User currentUser;

    private Authentification() {
    }

    public static boolean existe(String username) {
        for (User user : BD.users) {
            if (user.getUsername().equals(username)) return true;
        }
        return false;
    }

    public static boolean inscrire(String username, String password, String email) throws Exception {
        if (existe(username)) return false;
        User nouveau = new User(username, password, email, 100.0);
        if (username.equals("admin")) nouveau.setAdmin(true);
        BD.users.add(nouveau);
        BD.update();
        return true;
    }

    public static boolean login(String username, String password) {
        for (User user : BD.users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static void afficherProfil() {
        System.out.println("\n--- VOTRE PROFIL ---");
        System.out.println("Nom d'utilisateur : " + currentUser.getUsername());
        System.out.println("Email             : " + currentUser.getEmail());
        System.out.println("Solde actuel      : " + currentUser.getSolde() + " DH");
        double moyenne = currentUser.getNoteMoyenne();
        System.out.print("Note vendeur      : ");
        if (moyenne == 0) {
            System.out.println("Aucune note pour le moment");
        } else {
            System.out.printf("%.2f / 5\n", moyenne);
        }
        System.out.println("--------------------\n");
    }
    
}
