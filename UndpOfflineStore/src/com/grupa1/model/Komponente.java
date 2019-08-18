package com.grupa1.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//POJO class
public class Komponente {

    //id komponente property
    public static IntegerProperty id;
    public static StringProperty naziv;
    public static IntegerProperty tip;
    public static IntegerProperty kolicina;
    public static DoubleProperty cena;

    public Komponente(int name, String naziv, int tip, int kolicina, double cena) {
        this.id = new SimpleIntegerProperty();
        this.naziv = new SimpleStringProperty();
        this.tip = new SimpleIntegerProperty();
        this.kolicina = new SimpleIntegerProperty();
        this.cena = new SimpleDoubleProperty();
    }

    public static int getId() {
        return id.get();
    }

    public static void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    //naziv property
    public static String getNaziv() {
        return naziv.get();
    }

    public static void setNaziv(String value) {
        naziv.set(value);
    }

    public StringProperty nazivProperty() {
        return naziv;
    }

    //tip property
    public static int getTip() {
        return tip.get();
    }

    public static void setTip(int value) {
        tip.set(value);
    }

    public IntegerProperty tipProperty() {
        return tip;
    }

    //kolicina property
    public static int getKolicina() {
        return kolicina.get();
    }

    public static void setKolicina(int value) {
        kolicina.set(value);
    }

    public IntegerProperty kolicinaProperty() {
        return kolicina;
    }

    //cena property
    public static double getCena() {
        return cena.get();
    }

    public static void setCena(double value) {
        cena.set(value);
    }

    public DoubleProperty cenaProperty() {
        return cena;
    }
}
