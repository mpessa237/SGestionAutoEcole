package com.herve.SGAE.enums;


public enum PermitCategory {
    A(30000), //moto
    B(150000), //voiture
    C(250000); //poids lourds

    private final int price;

    PermitCategory(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
