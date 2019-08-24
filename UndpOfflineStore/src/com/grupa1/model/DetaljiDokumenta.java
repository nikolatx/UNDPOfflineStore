
package com.grupa1.model;

import java.sql.Date;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class DetaljiDokumenta {
    
    private SimpleIntegerProperty dokumentIdProperty;
    private SimpleIntegerProperty komponentaIdProperty;
    private SimpleIntegerProperty kolicinaProperty;
    private SimpleDoubleProperty cenaProperty;

    public DetaljiDokumenta() {
    }

    public DetaljiDokumenta(int dokumentId, int komponentaId, int kolicina, double cena) {
        this.dokumentIdProperty = new SimpleIntegerProperty(dokumentId);
        this.komponentaIdProperty = new SimpleIntegerProperty(komponentaId);
        this.kolicinaProperty = new SimpleIntegerProperty(kolicina);
        this.cenaProperty = new SimpleDoubleProperty(cena);
    }
    
    public int getDokumentId() {
        return dokumentIdProperty.get();
    }
    public void setDokumentId(int value) {
        this.dokumentIdProperty.set(value);
    }
    public SimpleIntegerProperty getDokumentIdProperty() {
        return dokumentIdProperty;
    }
    
    public int getKomponentaId() {
        return komponentaIdProperty.get();
    }
    public void setkomponentaId(int value) {
        this.komponentaIdProperty.set(value);
    }
    public SimpleIntegerProperty getKomponentaIdProperty() {
        return komponentaIdProperty;
    }
    
    public int getKolicina() {
        return this.kolicinaProperty.get();
    }
    public void setKolicina(int value) {
        this.kolicinaProperty.set(value);
    }
    public SimpleIntegerProperty getKolicinaProperty() {
        return kolicinaProperty;
    }
    
    public double getCena() {
        return this.cenaProperty.get();
    }
    
    public void setCena(double value) {
        this.cenaProperty.set(value);
    }
    public SimpleDoubleProperty getCenaProperty() {
        return cenaProperty;
    }
    
}
