package com.grupa1.model;

import java.time.LocalDate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class IzvestajPOJO {
    
    
    //id komponente property
    private SimpleIntegerProperty idProperty;
    private SimpleStringProperty nazivProperty;
    private SimpleDoubleProperty cenaProperty;
    private ObjectProperty<LocalDate> datumProperty ;

    //konstruktori
    public IzvestajPOJO() {
        this.idProperty=new SimpleIntegerProperty();
        this.nazivProperty=new SimpleStringProperty();
        this.cenaProperty=new SimpleDoubleProperty();
        this.datumProperty= new SimpleObjectProperty<>();
    }
    
   public IzvestajPOJO(int idProperty, String nazivProperty, double cenaProperty, LocalDate datumProperty) {
        this.idProperty = new SimpleIntegerProperty(idProperty);
        this.nazivProperty = new SimpleStringProperty(nazivProperty);
        this.cenaProperty = new SimpleDoubleProperty(cenaProperty);
        this.datumProperty = new SimpleObjectProperty<LocalDate>(datumProperty);
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

    public double getCena() {
        return this.cenaProperty.get();
    }
    
    public void setCena(double value) {
        this.cenaProperty.set(value);
    }
    
    public DoubleProperty getCenaProperty() {
        return cenaProperty;
    }

     public final void setDatumProperty(LocalDate datumProperty) {
        this.datumProperty.set(datumProperty);
    }

    public final LocalDate getClientDate() {
        return datumProperty.get();
    }
    
    public final ObjectProperty<LocalDate> datumProperty() {
        return datumProperty;
    }
    
}
