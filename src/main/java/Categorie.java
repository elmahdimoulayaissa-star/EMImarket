public enum Categorie {

    Electronique("Electronique"),
    Livre("Livre"),
    MODE("Mode"),
    Autre("Autre");

    private final String description;

    Categorie(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
