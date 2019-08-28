
package com.grupa1.model;

import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class Dokument {
    
    private SimpleIntegerProperty dokumentIdProperty;
    private SimpleIntegerProperty osobaIdProperty;
    private SimpleObjectProperty<Date> datumProperty;
    
    public Dokument() {
        this.dokumentIdProperty = new SimpleIntegerProperty();
        this.osobaIdProperty = new SimpleIntegerProperty();
        this.datumProperty = new SimpleObjectProperty<Date>();
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
    
    public int getOsobaId() {
        return osobaIdProperty.get();
    }
    public void setOsobaId(int value) {
        this.osobaIdProperty.set(value);
    }
    public SimpleIntegerProperty getOsobaIdProperty() {
        return osobaIdProperty;
    }
    public Date getDatum() {
        return datumProperty.get();
    }
    public void setDatum(Date value) {
        this.datumProperty.set(value);
    }
    public SimpleObjectProperty<Date> getDatumProperty() {
        return datumProperty;
    }
    
    
}
