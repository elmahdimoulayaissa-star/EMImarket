import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    User acheteur, vendeur;
    double prixVente;
    LocalDate dateTransaction;

    public Transaction(User acheteur, User vendeur, double prixVente, LocalDate dateTransaction) {
        this.acheteur        = acheteur;
        this.vendeur         = vendeur;
        this.prixVente       = prixVente;
        this.dateTransaction = dateTransaction;
    }
}
