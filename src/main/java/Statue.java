public enum Statue {

    DISPONIBLE("Disponible"),
    EN_COURS("En cours"),
    VENDU("Vendu");

    private final String description;

    Statue(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
