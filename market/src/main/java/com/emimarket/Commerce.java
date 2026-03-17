package com.emimarket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Commerce {

    private Commerce() {
    }

    public static boolean initierAchat(Annonce annonce) {
        if (Authentification.currentUser.equals(annonce.getVendeur())) {
            System.out.println("Vous ne pouvez pas acheter votre propre produit.");
            return false;
        } else if (Authentification.currentUser.getSolde() < annonce.getPrix()) {
            System.out.println("Solde insuffisant.");
            return false;
        } else if (annonce.getStatue() != Statue.DISPONIBLE) {
            System.out.println("Le produit n'est plus disponible.");
            return false;
        } else {
            try {
                Authentification.currentUser.setSolde(Authentification.currentUser.getSolde() - annonce.getPrix());
                annonce.setStatue(Statue.EN_COURS);
                annonce.setAcheteurEnAttente(Authentification.currentUser);
                Authentification.currentUser.achatsEnCours.add(annonce);
                annonce.getVendeur().ventesEnCours.add(annonce);
                BD.update();
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            return true;
        }
    }

    public static boolean annulerAchat(Annonce a) {
        if (Authentification.currentUser.achatsEnCours.contains(a) &&
            ChronoUnit.DAYS.between(LocalDate.now(), a.getDate()) >= 3) {
                try {
                    Authentification.currentUser.setSolde(Authentification.currentUser.getSolde() + a.getPrix());
                    a.setStatue(Statue.DISPONIBLE);
                    a.setAcheteurEnAttente(null);
                    Authentification.currentUser.achatsEnCours.remove(a);
                    a.getVendeur().ventesEnCours.remove(a);
                    BD.update();
                } catch (Exception e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
                return true;
            } else return false;
    }

    public static boolean finaliserAchat(Annonce a) {
        if (Authentification.currentUser.achatsEnCours.contains(a)) {
            try {
                a.getVendeur().setSolde(a.getVendeur().getSolde() + a.getPrix());
                a.setStatue(Statue.VENDU);
                a.setAcheteurEnAttente(null);
                Authentification.currentUser.achatsEnCours.remove(a);
                a.getVendeur().ventesEnCours.remove(a);
                BD.transactions.add(new Transaction(Authentification.currentUser, a.getVendeur(), a.getPrix(), LocalDate.now()));
                BD.update();
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            return true;
        } else return false;
    }

}
