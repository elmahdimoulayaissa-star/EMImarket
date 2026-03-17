package com.emimarket;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public final class Annonce implements Serializable {

    private String titre, description, photo, commentaireAcheteur;
    private double prix;
    private final User vendeur;
    private User acheteurEnAttente;
    private Categorie categorie;
    private final LocalDate date;
    private Statue statue;
    ArrayList<Integer> notes;

    public Annonce(String titre, String description, String photo, double prix, Categorie categorie) throws Exception {
        this.setTitre(titre);
        this.setDescription(description);
        this.setPhoto(photo);
        this.setPrix(prix);
        this.vendeur = Authentification.currentUser;
        this.categorie = categorie;
        this.date = LocalDate.now();
        statue = Statue.DISPONIBLE;
        this.notes = new ArrayList<>();
    }

    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) throws Exception {
        if (titre.trim().length() < 3) {
            throw new Exception("Titre trop court !");
        } else {
            this.titre = titre.trim();
        }
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) throws Exception {
        if (description.trim().length() < 30) {
            throw new Exception("Description tres courte !");
        } else {
            this.description = description.trim();
        }
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) throws Exception {
        if (!photo.matches("^[a-zA-Z0-9]+\\.(jpeg|jpg|png|gif)$")) {
            throw new Exception("Photo non valide !");
        } else {
            this.photo = photo;
        }
    }

    public double getPrix() {
        return prix;
    }
    public String getPrixFormate() {
        return String.format("%.2f DH", this.prix);
    }
    public void setPrix(double prix) throws Exception {
        if (prix <= 0) {
            throw new Exception("Prix negatif ou nul.");
        } else {
            this.prix = prix;
        }
    }

    public User getVendeur() {
        return vendeur;
    }
    
    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public LocalDate getDate() {
        return date;
    }

    public Statue getStatue() {
        return statue;
    }
    public void setStatue(Statue statue) {
        this.statue = statue;
    }

    public String getCommentaireAcheteur() {
        return commentaireAcheteur;
    }
    public void setCommentaireAcheteur(String commentaireAcheteur) {
        this.commentaireAcheteur = commentaireAcheteur;
    }

    public User getAcheteurEnAttente() {
        return acheteurEnAttente;
    }
    public void setAcheteurEnAttente(User acheteurEnAttente) {
        this.acheteurEnAttente = acheteurEnAttente;
    }

    @Override
    public String toString() {
        return String.format("""
                -----------------------------------------
                Titre               : %s
                -----------------------------------------
                Vendeur             : %s
                Date de publication : %s
                Categorie           : %s
                Statue              : %s
                Photo               : %s
                Prix                : %.2f DH
                Description         : 
                %s
                -----------------------------------------
                
                """,
                titre,
                vendeur.getUsername(),
                date.toString(),
                categorie.getDescription(),
                statue.getDescription(),
                photo,
                prix,
                description
            );
    }

    public double getMoyenneAnnonce() {
        if(notes.size() == 0)return 0;
        int s = 0;
        for (int n : notes) {
            s += n;
        }
        return s / notes.size();
    }

}
