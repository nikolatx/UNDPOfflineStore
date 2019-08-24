
package com.grupa1.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Osoba {
    public SimpleIntegerProperty idProperty;
    public SimpleStringProperty nazivProperty;
    public SimpleStringProperty ulicaProperty;
    public SimpleStringProperty brojProperty;
    public SimpleStringProperty gradProperty;
    public SimpleStringProperty postBrProperty;
    public SimpleStringProperty drzavaProperty;
    public SimpleStringProperty telefonProperty;

    public Osoba() {
        this.idProperty = new SimpleIntegerProperty();
        this.nazivProperty = new SimpleStringProperty();
        this.ulicaProperty = new SimpleStringProperty();
        this.brojProperty = new SimpleStringProperty();
        this.gradProperty = new SimpleStringProperty();
        this.postBrProperty = new SimpleStringProperty();
        this.drzavaProperty = new SimpleStringProperty();
        this.telefonProperty = new SimpleStringProperty();
    }

    public Osoba(int id, String naziv, String ulica, String broj, String grad, String postBr, String drzava, String telefon) {
        this.idProperty = new SimpleIntegerProperty(id);
        this.nazivProperty = new SimpleStringProperty(naziv);
        this.ulicaProperty = new SimpleStringProperty(ulica);
        this.brojProperty = new SimpleStringProperty(broj);
        this.gradProperty = new SimpleStringProperty(grad);
        this.postBrProperty = new SimpleStringProperty(postBr);
        this.drzavaProperty = new SimpleStringProperty(drzava);
        this.telefonProperty = new SimpleStringProperty(telefon);
    }
    
    public int getId() {
        return idProperty.get();
    }
    public void setId(int value) {
        this.idProperty.set(value);
    }
    public SimpleIntegerProperty getIdProperty() {
        return idProperty;
    }
    
    public String getNaziv() {
        return nazivProperty.get();
    }
    public void setNaziv(String value) {
        this.nazivProperty.set(value);
    }
    public SimpleStringProperty getNazivProperty() {
        return nazivProperty;
    }
    
    public String getUlica() {
        return ulicaProperty.get();
    }
    public void setUlica(String value) {
        this.ulicaProperty.set(value);
    }
    public SimpleStringProperty getUlicaProperty() {
        return ulicaProperty;
    }
    
    public String getBroj() {
        return brojProperty.get();
    }
    public void setBroj(String value) {
        this.brojProperty.set(value);
    }
    public SimpleStringProperty getBrojProperty() {
        return brojProperty;
    }
    
    public String getGrad() {
        return gradProperty.get();
    }
    public void setGrad(String value) {
        this.gradProperty.set(value);
    }
    public SimpleStringProperty getGradProperty() {
        return gradProperty;
    }
    
    public String getPostBr() {
        return postBrProperty.get();
    }
    public void setPostBr(String value) {
        this.postBrProperty.set(value);
    }
    public SimpleStringProperty getPostBrProperty() {
        return postBrProperty;
    }
    
    public String getDrzava() {
        return drzavaProperty.get();
    }
    public void setDrzava(String value) {
        this.drzavaProperty.set(value);
    }
    public SimpleStringProperty getDrzavaProperty() {
        return drzavaProperty;
    }
    
    public String getTelefon() {
        return telefonProperty.get();
    }
    public void setTelefon(String value) {
        this.telefonProperty.set(value);
    }
    public SimpleStringProperty getTelefonProperty() {
        return telefonProperty;
    }

}
