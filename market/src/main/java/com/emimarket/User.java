package com.emimarket;

import java.io.Serializable;
import java.util.ArrayList;

public final class User implements Serializable {

    private String username, password, email;
    private double solde;
    private boolean isAdmin = false;
    ArrayList<Annonce> favoris;
    ArrayList<Annonce> achatsEnCours;
    ArrayList<Annonce> ventesEnCours;

    public User(String username, String password, String email, double solde) throws Exception {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setSolde(solde);
        this.favoris = new ArrayList<>();
        this.achatsEnCours = new ArrayList<>();
        this.ventesEnCours = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) throws Exception {
        if (username != null && username.matches("^[a-zA-Z][a-zA-Z0-9._]{2,14}$")) {
            this.username = username;
        } else {
            throw new Exception("Username invalide !");
        }
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) throws Exception {
        if (password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            this.password = password;
        } else {
            throw new Exception("""
                    Le mot de passe doit contenir :
                        - Une lettre majuscule (A-Z)
                        - Une lettre minuscule (a-z)
                        - Un chiffre (0-9)
                        - Un caractère spécial (ex: @, $, !, #)
                        - Au moins 8 caractères
                    """);
        }
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) throws Exception {
        if (email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            this.email = email;
        } else {
            throw new Exception("Email invalide !");
        }
    }

    public double getSolde() {
        return solde;
    }
    public void setSolde(double solde) throws Exception {
        if (solde >= 0) {
            this.solde = solde;
        } else {
            throw new Exception("Le solde ne peut pas être négatif !");
        }
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public double getNoteMoyenne() {
        int s = 0, compteur = 0;
        for (Annonce a : BD.annonces) {
            if (a.getVendeur() == Authentification.currentUser) {
                s += a.getMoyenneAnnonce();
                compteur++;
            }
        }
        if (compteur == 0 ) return 0;
        return s / compteur;
    }
    
}
