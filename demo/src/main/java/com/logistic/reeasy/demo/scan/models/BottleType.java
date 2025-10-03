package com.logistic.reeasy.demo.scan.models;

public enum BottleType {
    PEAD(1),
    PEBD(2),
    PET(3);

    // --- Código a añadir ---
    private final int id;

    BottleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
