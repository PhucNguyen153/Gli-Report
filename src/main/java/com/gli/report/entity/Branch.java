package com.gli.report.entity;


public enum Branch {

    CHIEN(1, "Chiên Con"),
    AU(2, "Ấu Nhi"),
    THIEU (3, "Thiếu Nhi"),
    NGHIA (4, "Nghĩa Sĩ"),
    HIEP (5, "Hiệp Sĩ");

    private int id;
    private String name;

    Branch(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Branch getByShortName(String shortName) {
        switch (shortName) {
            case "au": return AU;
            case "thieu": return THIEU;
            case "nghia": return NGHIA;
            case "hiep": return HIEP;
            default: return CHIEN;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
