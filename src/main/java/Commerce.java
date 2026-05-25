public class Commerce {

    private Commerce() {}

    public static String initierAchat(Annonce annonce) {
        if (Authentification.currentUser == null) {
            return "Utilisateur non connecté.";
        }
        if (Authentification.currentUser.equals(annonce.getVendeur())) {
            return "Vous ne pouvez pas acheter votre propre produit.";
        }
        if (annonce.getStatue() != Statue.DISPONIBLE) {
            return "Le produit n'est plus disponible.";
        }
        if (Authentification.currentUser.getSolde() < annonce.getPrix()) {
            return "Solde insuffisant (solde : " + String.format("%.2f", Authentification.currentUser.getSolde()) + " DH).";
        }

        try {
            Authentification.currentUser.setSolde(Authentification.currentUser.getSolde() - annonce.getPrix());
            annonce.setStatue(Statue.EN_COURS);
            annonce.setAcheteurEnAttente(Authentification.currentUser);

            if (!Authentification.currentUser.achatsEnCours.contains(annonce)) {
                Authentification.currentUser.achatsEnCours.add(annonce);
            }
            if (!annonce.getVendeur().ventesEnCours.contains(annonce)) {
                annonce.getVendeur().ventesEnCours.add(annonce);
            }
            BD.update();
            return null;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public static String annulerAchat(Annonce annonce) {
        boolean allowed = Authentification.currentUser != null
                && (Authentification.currentUser.achatsEnCours.contains(annonce)
                || Authentification.currentUser.equals(annonce.getAcheteurEnAttente()));
        if (!allowed) {
            return "Cet achat ne vous appartient pas.";
        }

        try {
            Authentification.currentUser.setSolde(Authentification.currentUser.getSolde() + annonce.getPrix());
            annonce.setStatue(Statue.DISPONIBLE);
            annonce.setAcheteurEnAttente(null);
            Authentification.currentUser.achatsEnCours.remove(annonce);
            annonce.getVendeur().ventesEnCours.remove(annonce);
            BD.update();
            return null;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public static String finaliserAchat(Annonce annonce) {
        boolean allowed = Authentification.currentUser != null
                && (Authentification.currentUser.achatsEnCours.contains(annonce)
                || Authentification.currentUser.equals(annonce.getAcheteurEnAttente()));
        if (!allowed) {
            return "Cet achat ne vous appartient pas.";
        }

        try {
            annonce.getVendeur().setSolde(annonce.getVendeur().getSolde() + annonce.getPrix());
            annonce.setStatue(Statue.VENDU);
            annonce.setAcheteurEnAttente(null);
            if (!Authentification.currentUser.historiqueAchats.contains(annonce)) {
                Authentification.currentUser.historiqueAchats.add(annonce);
            }
            Authentification.currentUser.achatsEnCours.remove(annonce);
            annonce.getVendeur().ventesEnCours.remove(annonce);
            BD.transactions.add(new Transaction(Authentification.currentUser, annonce.getVendeur(), annonce.getPrix(), java.time.LocalDate.now()));
            BD.update();
            return null;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}
