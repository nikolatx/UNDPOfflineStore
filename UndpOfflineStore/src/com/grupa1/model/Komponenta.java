package com.grupa1.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Komponenta {

    //id komponente property
    private SimpleIntegerProperty idProperty;
    private SimpleStringProperty nazivProperty;
    private SimpleStringProperty proizvodjacProperty;
    private SimpleStringProperty tipProperty;
    private SimpleIntegerProperty kolicinaProperty;
    private SimpleDoubleProperty cenaProperty;

    public Komponenta() {
        this.idProperty=new SimpleIntegerProperty();
        this.nazivProperty=new SimpleStringProperty();
        this.proizvodjacProperty =new SimpleStringProperty();
        this.tipProperty=new SimpleStringProperty();
        this.kolicinaProperty=new SimpleIntegerProperty();
        this.cenaProperty=new SimpleDoubleProperty();
    }

    public Komponenta(int idProperty, String nazivProperty, String proizvodjacProperty, String tipProperty, int kolicinaProperty, double cenaProperty) {
        this.idProperty = new SimpleIntegerProperty(idProperty);
        this.nazivProperty = new SimpleStringProperty(nazivProperty);
        this.proizvodjacProperty = new SimpleStringProperty(proizvodjacProperty);
        this.tipProperty = new SimpleStringProperty(tipProperty);
        this.kolicinaProperty = new SimpleIntegerProperty(kolicinaProperty);
        this.cenaProperty = new SimpleDoubleProperty(cenaProperty);
    }
    
    
    
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

    
    public StringProperty getProizvodjacProperty() {
        return proizvodjacProperty;
    }

    public String getProizvodjac() {
        return this.proizvodjacProperty.get();
    }
    
    public void setProizvodjac(String value) {
        this.proizvodjacProperty.set(value);
    }
    
    
    

    public String getTip() {
        return this.tipProperty.get();
    }
    public void setTip(String value) {
        this.tipProperty.set(value);
    }
    
    public StringProperty getTipProperty() {
        return tipProperty;
    }

   
    
    public int getKolicina() {
        return this.kolicinaProperty.get();
    }
    
    public void setKolicina(int value) {
        this.kolicinaProperty.set(value);
    }
    
    public IntegerProperty getKolicinaProperty() {
        return kolicinaProperty;
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

    
    //hashCode i equals metode - da se onemoguci ponovno dodavanje iste komponente u listu
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.idProperty);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Komponenta other = (Komponenta) obj;
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    
    
    
    
    

    
}
