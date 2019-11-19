
package com.grupa1.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class KomponentaSaSlikom extends Komponenta {
    
    private ObjectProperty slikaProperty = new SimpleObjectProperty(); //SimpleStringProperty slikaProperty; 

    public KomponentaSaSlikom() {
        super();
        slikaProperty=new SimpleObjectProperty();
    }

    public KomponentaSaSlikom(int idProperty, String nazivProperty, String proizvodjacProperty, String tipProperty, int kolicinaProperty, double cenaProperty, Slika slika) {
        super(idProperty, nazivProperty, proizvodjacProperty, tipProperty, kolicinaProperty, cenaProperty);
        slikaProperty.set(slika);
        //setSlika(slika);
    }
    
    public Object getSlika() {
        return slikaProperty.get();
  }
    
    public void setSlika(Slika value) {
        slikaProperty.set(value);
    }
  
    public ObjectProperty slikaProperty() {
        return slikaProperty;
  }
    
}
