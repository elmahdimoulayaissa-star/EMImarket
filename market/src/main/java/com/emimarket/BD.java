package com.emimarket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BD {

    private final static String F1 = "users.ser", F2 = "annonces.ser", F3 = "transactions.ser";
    
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Annonce> annonces = new ArrayList<>();
    public static ArrayList<Transaction> transactions = new ArrayList<>();

    private BD() {
    }

    public static void load() {
        try {
            File fileUsers = new File(F1);
            if (fileUsers.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileUsers));
                users = (ArrayList<User>) ois.readObject();
                ois.close();
            }

            File fileAnnonces = new File(F2);
            if (fileAnnonces.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileAnnonces));
                annonces = (ArrayList<Annonce>) ois.readObject();
                ois.close();
            }

            File fileTransactions = new File(F3);
            if (fileTransactions.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileTransactions));
                transactions = (ArrayList<Transaction>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Info : Aucun fichier de données trouvé ou erreur de lecture.");
        }
    }

    public static void update() {
        try (
            ObjectOutputStream oosUsers = new ObjectOutputStream(new FileOutputStream(F1));
            ObjectOutputStream oosAnnonces = new ObjectOutputStream(new FileOutputStream(F2));
            ObjectOutputStream oosTransactions = new ObjectOutputStream(new FileOutputStream(F3))
        ) {
            oosUsers.writeObject(users);
            oosAnnonces.writeObject(annonces);
            oosTransactions.writeObject(transactions);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

}
