package com.logistic.reeasy.demo.scan.models;

public enum BottleType {
    PET1(1),
    HDPE(2),
    PP(3);

    // --- Código a añadir ---
    private final int id;

    BottleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
