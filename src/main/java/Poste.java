import java.util.ArrayList;

public class Poste {

    private Poste() {}

    public static boolean existe(String titre) {
        return existe(titre, null);
    }

    private static boolean existe(String titre, Annonce ignore) {
        for (Annonce annonce : BD.annonces) {
            if (annonce == ignore) continue;
            if (annonce.getTitre().equalsIgnoreCase(titre)) return true;
        }
        return false;
    }

    public static boolean publier(String titre, String description, String photo, double prix, Categorie categorie) throws Exception {
        if (existe(titre, null)) return false;
        BD.annonces.add(new Annonce(titre, description, photo, prix, categorie));
        BD.update();
        return true;
    }

    public static boolean modifier(Annonce annonce, String titre, String description, String photo, double prix, Categorie categorie) throws Exception {
        if (annonce == null) return false;
        boolean allowed = Authentification.currentUser != null
                && (Authentification.currentUser.equals(annonce.getVendeur()) || Authentification.currentUser.isAdmin());
        if (!allowed || annonce.getStatue() != Statue.DISPONIBLE) return false;
        if (existe(titre, annonce)) return false;

        annonce.setTitre(titre);
        annonce.setDescription(description);
        annonce.setPhoto(photo);
        annonce.setPrix(prix);
        annonce.setCategorie(categorie);
        BD.update();
        return true;
    }

    public static boolean supprimer(Annonce annonce) {
        boolean allowed = annonce != null
                && annonce.getVendeur().equals(Authentification.currentUser)
                && annonce.getStatue() == Statue.DISPONIBLE;
        if (!allowed) return false;
        return supprimerInterne(annonce);
    }

    public static boolean supprimerAdmin(Annonce annonce) {
        if (annonce == null) return false;
        return supprimerInterne(annonce);
    }

    private static boolean supprimerInterne(Annonce annonce) {
        BD.annonces.remove(annonce);
        for (User user : BD.users) {
            user.favoris.remove(annonce);
            user.achatsEnCours.remove(annonce);
            user.ventesEnCours.remove(annonce);
        }
        if (annonce.getAcheteurEnAttente() != null) {
            annonce.setAcheteurEnAttente(null);
        }
        BD.update();
        return true;
    }

    public static ArrayList<Annonce> filtrerParMotCle(String motCle) {
        ArrayList<Annonce> resultats = new ArrayList<>();
        for (Annonce annonce : BD.annonces) {
            boolean correspond = annonce.getTitre().toLowerCase().contains(motCle.toLowerCase())
                    || annonce.getDescription().toLowerCase().contains(motCle.toLowerCase());
            if (annonce.getStatue() != Statue.VENDU && correspond) {
                resultats.add(annonce);
            }
        }
        return resultats;
    }

    public static ArrayList<Annonce> filtrerParCategorie(Categorie categorie) {
        ArrayList<Annonce> resultats = new ArrayList<>();
        for (Annonce annonce : BD.annonces) {
            if (annonce.getCategorie() == categorie && annonce.getStatue() != Statue.VENDU) {
                resultats.add(annonce);
            }
        }
        return resultats;
    }

    public static ArrayList<Annonce> trierParPrix(boolean ascendant) {
        ArrayList<Annonce> copie = new ArrayList<>(BD.annonces);
        copie.removeIf(annonce -> annonce.getStatue() == Statue.VENDU);
        copie.sort((a, b) -> ascendant
                ? Double.compare(a.getPrix(), b.getPrix())
                : Double.compare(b.getPrix(), a.getPrix()));
        return copie;
    }
}
