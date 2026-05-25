import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username, password, email;
    private double solde;
    private boolean isAdmin = false;
    ArrayList<Annonce> favoris = new ArrayList<>();
    ArrayList<Annonce> achatsEnCours = new ArrayList<>();
    ArrayList<Annonce> ventesEnCours = new ArrayList<>();
    public ArrayList<Annonce> historiqueAchats = new ArrayList<>();

    public User(String username, String password, String email, double solde) throws Exception {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setSolde(solde);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public double getSolde() { return solde; }
    public boolean isAdmin() { return isAdmin; }

    public void setUsername(String username) throws Exception {
        if (username != null && username.matches("^[a-zA-Z][a-zA-Z0-9._]{2,14}$")) {
            this.username = username;
        } else {
            throw new Exception("Username invalide ! (3-15 car., commence par une lettre)");
        }
    }

    public void setPassword(String password) throws Exception {
        if (password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            this.password = password;
        } else {
            throw new Exception("Mot de passe doit contenir : majuscule, minuscule, chiffre, caractère spécial (@$!%*?&), min 8 caractères.");
        }
    }

    public void setEmail(String email) throws Exception {
        if (email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            this.email = email;
        } else {
            throw new Exception("Email invalide !");
        }
    }

    public void setSolde(double solde) throws Exception {
        if (solde >= 0) {
            this.solde = solde;
        } else {
            throw new Exception("Le solde ne peut pas être négatif !");
        }
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public double getNoteMoyenne() {
        double somme = 0;
        int compteur = 0;
        for (Annonce a : BD.annonces) {
            if (this.equals(a.getVendeur()) && a.notes != null && !a.notes.isEmpty()) {
                somme += a.getMoyenneAnnonce();
                compteur++;
            }
        }
        return compteur == 0 ? 0 : somme / compteur;
    }

    private Object readResolve() {
        if (favoris == null) favoris = new ArrayList<>();
        if (achatsEnCours == null) achatsEnCours = new ArrayList<>();
        if (ventesEnCours == null) ventesEnCours = new ArrayList<>();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User other)) return false;
        return username != null && other.username != null
                && username.toLowerCase(Locale.ROOT).equals(other.username.toLowerCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return Objects.hash(username == null ? null : username.toLowerCase(Locale.ROOT));
    }
}
