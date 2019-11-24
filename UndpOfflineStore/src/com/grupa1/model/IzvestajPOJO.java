package com.grupa1.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class IzvestajPOJO {
    
    //id komponente property
    private SimpleIntegerProperty idProperty;
    private SimpleStringProperty nazivProperty;
    private SimpleStringProperty proizvodjacProperty;
    private SimpleIntegerProperty kolicinaProperty;
    private SimpleDoubleProperty cenaProperty;

    //konstruktori
    public IzvestajPOJO() {
        this.idProperty=new SimpleIntegerProperty();
        this.nazivProperty=new SimpleStringProperty();
        this.proizvodjacProperty=new SimpleStringProperty();
        this.kolicinaProperty=new SimpleIntegerProperty();
        this.cenaProperty=new SimpleDoubleProperty();
    }
    
    public IzvestajPOJO(int id, String naziv, String proizvodjac, int kolicina, double cena) {
        this.idProperty=new SimpleIntegerProperty(id);
        this.nazivProperty=new SimpleStringProperty(naziv);
        this.proizvodjacProperty=new SimpleStringProperty(proizvodjac);
        this.kolicinaProperty=new SimpleIntegerProperty(kolicina);
        this.cenaProperty=new SimpleDoubleProperty(cena);
    }
   
    //geteri i seteri
    public int getId() {
        return idProperty.get();
    }
    public void setId(int value) {
        this.idProperty.set(value);
    }
    public IntegerProperty getIdProperty() {
        return idProperty;
    }

    
    public String getNaziv() {
        return this.nazivProperty.get();
    }
    public void setNaziv(String value) {
        this.nazivProperty.set(value);
    }
    public StringProperty getNazivProperty() {
        return nazivProperty;
    }

    public String getProizvodjac() {
        return this.proizvodjacProperty.get();
    }
    public void setProizvodjac(String value) {
        this.proizvodjacProperty.set(value);
    }
    public StringProperty getProizvodjacProperty() {
        return nazivProperty;
    }
    
    public int getKolicina() {
        return kolicinaProperty.get();
    }
    public void setKolicina(int value) {
        this.kolicinaProperty.set(value);
    }
    public IntegerProperty getKolicinaProperty() {
        return idProperty;
    }
    
    public double getCena() {
        return this.cenaProperty.get();
    }
    public void setCena(double value) {
        this.cenaProperty.set(value);
    }
    public DoubleProperty getCenaProperty() {
        return cenaProperty;
    }

}
