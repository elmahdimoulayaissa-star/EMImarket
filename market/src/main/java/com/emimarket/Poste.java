package com.emimarket;

import java.util.ArrayList;

public class Poste {

    private Poste() {
    }

    public static boolean existe(String titre) {
        for (Annonce a : BD.annonces) {
            if (a.getTitre().equals(titre)) return true;
        }
        return false;
    }

    public static boolean publier(String titre, String description, String photo, double prix, Categorie categorie) throws Exception {
        if (existe(titre)) return false;
        BD.annonces.add(new Annonce(titre, description, photo, prix, categorie));
        BD.update();
        return true;
    }

    public static boolean supprimer(Annonce annonce) {
        if (annonce.getVendeur().equals(Authentification.currentUser) && annonce.getStatue() == Statue.DISPONIBLE) {
            BD.annonces.remove(annonce);
            for (User user : BD.users) {
                user.favoris.remove(annonce);
            }
            BD.update();
            return true;
        }
        return false;
    }

    public static void afficherToutesLesAnnonces() {
        for (Annonce a : BD.annonces) {
            if (a.getStatue() != Statue.VENDU) System.out.println(a.toString());
        }
    }

    public static void voirDetailsAnnonce(Annonce annonce) {
        System.out.println(annonce.toString());
    }
    
    public static void voirDetailsAnnonce(String titre) {
        for (Annonce a : BD.annonces) {
            if (a.getTitre().equals(titre)) voirDetailsAnnonce(a);
            break;
        }
    }

    public static ArrayList<Annonce> filtrerParMotCle(String motCle) {
        ArrayList<Annonce> resultats = new ArrayList<>();
        for (Annonce a : BD.annonces) {
            if (a.getTitre().toLowerCase().contains(motCle.toLowerCase()) ||
                a.getDescription().toLowerCase().contains(motCle.toLowerCase())) {
                resultats.add(a);
            }
        }
        return resultats;
    }

    public static ArrayList<Annonce> filtrerParCategorie(Categorie c) {
        ArrayList<Annonce> resultats = new ArrayList<>();
        for (Annonce a : BD.annonces) {
            if (a.getCategorie() == c) {
                resultats.add(a);
            }
        }
        return resultats;
    }

    public static ArrayList<Annonce> trierParPrix() {
        ArrayList<Annonce> copie = new ArrayList<>(BD.annonces);
        int n = copie.size();
        boolean echange;
        for (int i = 0; i < n - 1; i++) {
            echange = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (copie.get(j).getPrix() > copie.get(j + 1).getPrix()) {
                    Annonce temp = copie.get(j);
                    copie.set(j, copie.get(j + 1));
                    copie.set(j + 1, temp);
                    echange = true;
                }
            }
            if (!echange) break;
        }
        return copie;
    }

    public static void afficherFavoris() {
        for (Annonce a : Authentification.currentUser.favoris) {
            System.out.println(a.toString());
        }
    }
    
}
