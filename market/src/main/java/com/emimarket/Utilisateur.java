package com.emimarket;

public final class Utilisateur {

    private String nom_utilisateur, mot_de_passe, email;
    private double solde = 100;
    public Utilisateur(String nom_utilisateur, String mot_de_passe, String email, double solde) {
        this.nom_utilisateur = nom_utilisateur;
        this.mot_de_passe = mot_de_passe;
        this.email = email;
        this.solde = solde;
    }
    public Utilisateur() {
    }
    public String getNom_utilisateur() {
        return nom_utilisateur;
    }
    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }
    public String getMot_de_passe() {
        return mot_de_passe;
    }
    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getSolde() {
        return solde;
    }
    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public String toString() {
        return nom_utilisateur + ";" +mot_de_passe + ";" + email + ";" + solde ;
    }
    


    
}