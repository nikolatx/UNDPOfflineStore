//modelska klasa za upis nove komponente u bazu podataka, sadrzi sva polja
package com.grupa1.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class KomponentaSvaPolja extends KomponentaSaSlikom {
    
    private SimpleBooleanProperty aktuelnaProperty; 

    public KomponentaSvaPolja() {
        super();
        this.aktuelnaProperty=new SimpleBooleanProperty(true);
    }

    public KomponentaSvaPolja(int idProperty, String nazivProperty, String proizvodjacProperty, String tipProperty, int kolicinaProperty, double cenaProperty, Slika slika, boolean aktuelna) {
        super(idProperty, nazivProperty, proizvodjacProperty, tipProperty, kolicinaProperty, cenaProperty, slika);
        this.aktuelnaProperty = new SimpleBooleanProperty(aktuelna);
    }
    
    public boolean getAktuelna() {
        return this.aktuelnaProperty.get();
    }
    public void setAktuelna(boolean value) {
        this.aktuelnaProperty.set(value);
    }
    public SimpleBooleanProperty getAktuelnaProperty() {
        return aktuelnaProperty;
    }
    
}
