package com;
public class Symbole {
    private String identifant ;
    private String type ;
    private boolean isConstante;

    public Symbole(){
        this.isConstante = false;
    }

    public String getIdentifant() {
        return identifant;
    }

    public void setIdentifant(String identifant) {
        this.identifant = identifant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isConstante() {
        return isConstante;
    }

    public void setConstante(boolean constante) {
        isConstante = constante;
    }

    @Override
    public String toString() {
        return "Symbole{" +
                "identifant='" + identifant + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}