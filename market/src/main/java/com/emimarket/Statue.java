package com.emimarket;

public enum Statue {

    DISPONIBLE("Disponile."),
    EN_COURS("Non Disponible."),
    VENDU("Vendu.");

    private final String description;

    private Statue(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    
}
