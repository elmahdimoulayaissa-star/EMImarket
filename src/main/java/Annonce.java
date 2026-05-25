import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String titre, description, photo, commentaireAcheteur;
    private double prix;
    private User vendeur;
    private User acheteurEnAttente;
    private Categorie categorie;
    private final LocalDate date;
    private Statue statue;
    ArrayList<Integer> notes = new ArrayList<>();

    public Annonce(String titre, String description, String photo, double prix, Categorie categorie) throws Exception {
        this.id = UUID.randomUUID().toString();
        setTitre(titre);
        setDescription(description);
        setPhoto(photo);
        setPrix(prix);
        this.vendeur = Authentification.currentUser;
        this.categorie = categorie;
        this.date = LocalDate.now();
        this.statue = Statue.DISPONIBLE;
    }

    public String getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getPhoto() { return photo; }
    public double getPrix() { return prix; }
    public User getVendeur() { return vendeur; }
    public User getAcheteurEnAttente() { return acheteurEnAttente; }
    public Categorie getCategorie() { return categorie; }
    public LocalDate getDate() { return date; }
    public Statue getStatue() { return statue; }
    public String getCommentaireAcheteur() { return commentaireAcheteur; }
    public String getPrixFormate() { return String.format("%.2f DH", prix); }

    public void setTitre(String titre) throws Exception {
        if (titre == null || titre.trim().length() < 3) {
            throw new Exception("Titre trop court (min 3 caractères) !");
        }
        this.titre = titre.trim();
    }

    public void setDescription(String description) throws Exception {
        if (description == null || description.trim().length() < 30) {
            throw new Exception("Description trop courte (min 30 caractères) !");
        }
        this.description = description.trim();
    }

    public void setPhoto(String photo) throws Exception {
        if (!ImageStorage.isAcceptedImageReference(photo)) {
            throw new Exception("Image invalide ! Utilisez une URL d'image ou un fichier .jpg/.png/.gif/.webp");
        }
        this.photo = photo.trim();
    }

    public void setPrix(double prix) throws Exception {
        if (prix <= 0) throw new Exception("Prix doit être positif.");
        this.prix = prix;
    }

    public void setVendeur(User vendeur) {
        this.vendeur = vendeur;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setStatue(Statue statue) {
        this.statue = statue;
    }

    public void setCommentaireAcheteur(String commentaireAcheteur) {
        this.commentaireAcheteur = commentaireAcheteur;
    }

    public void setAcheteurEnAttente(User acheteurEnAttente) {
        this.acheteurEnAttente = acheteurEnAttente;
    }

    public double getMoyenneAnnonce() {
        if (notes == null || notes.isEmpty()) return 0;
        int somme = 0;
        for (int note : notes) somme += note;
        return (double) somme / notes.size();
    }

    private Object readResolve() {
        if (id == null || id.isBlank()) id = UUID.randomUUID().toString();
        if (notes == null) notes = new ArrayList<>();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Annonce other)) return false;
        if (id != null && other.id != null) return Objects.equals(id, other.id);
        return Objects.equals(titre, other.titre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id != null ? id : titre);
    }

    @Override
    public String toString() {
        return titre;
    }
}
