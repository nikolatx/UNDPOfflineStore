
package com.grupa1.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class KomponentaSaSlikom extends Komponenta {
    
    private ObjectProperty slikaProperty = new SimpleObjectProperty(); //SimpleStringProperty slikaProperty; 

    public KomponentaSaSlikom() {
        super();
        //this.slikaProperty=new SimpleStringProperty();
        slikaProperty=new SimpleObjectProperty();
    }

    public KomponentaSaSlikom(int idProperty, String nazivProperty, String proizvodjacProperty, String tipProperty, int kolicinaProperty, double cenaProperty, Slika slika) {
        super(idProperty, nazivProperty, proizvodjacProperty, tipProperty, kolicinaProperty, cenaProperty);
        //this.slikaProperty = new SimpleStringProperty(slikaProperty);
        //this.slikaProperty = new SimpleObjectProperty(slikaProperty);
        setSlika(slika);
    }
    
//  public String getSlika() {
        //return this.slikaProperty.get();
//  }
    public Object getSlika() {
        return slikaProperty.get();
  }
    
    
    public void setSlika(Slika value) {
        slikaProperty.set(value);
    }
//    public SimpleStringProperty getSlikaProperty() {
//        return slikaProperty;
//    }
  
    public ObjectProperty slikaProperty() {
        return slikaProperty;
  }
    
    
}
