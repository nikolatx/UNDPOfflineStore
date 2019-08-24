
package com.grupa1.model;

import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Dokument {
    
    private SimpleIntegerProperty dokumentIdProperty;
    private SimpleIntegerProperty osobaIdProperty;
    private ObjectProperty<Date> datumProperty;
    
    
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
    public ObjectProperty<Date> getDatumProperty() {
        return datumProperty;
    }
    
    
}
