
package com.grupa1.model;

import javafx.beans.property.SimpleStringProperty;

public class KomponentaSaSlikom extends Komponenta {
    
    private SimpleStringProperty slikaProperty; 

    public KomponentaSaSlikom() {
        super();
        this.slikaProperty=new SimpleStringProperty();
    }

    public KomponentaSaSlikom(int idProperty, String nazivProperty, String proizvodjacProperty, String tipProperty, int kolicinaProperty, double cenaProperty, String slikaProperty) {
        super(idProperty, nazivProperty, proizvodjacProperty, tipProperty, kolicinaProperty, cenaProperty);
        this.slikaProperty = new SimpleStringProperty(slikaProperty);
    }
    
    public String getSlika() {
        return this.slikaProperty.get();
    }
    public void setSlika(String value) {
        this.slikaProperty.set(value);
    }
    public SimpleStringProperty getSlikaProperty() {
        return slikaProperty;
    }
    
}
