
package com.grupa1.dbconnection;

import com.grupa1.model.Komponenta;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Slika;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class AzuriranjeDAO {
    
    public void preuzmiPodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
            ObservableList<KomponentaSaSlikom> podaciFiltrirano) throws SQLException {
        Connection conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            //uzimanje podataka iz combobox-eva i polja za unos teksta
            String tip=(String)tipCB.getSelectionModel().getSelectedItem();
            String proizvodjac=(String)proizvodjacCB.getSelectionModel().getSelectedItem();
            String komponenta=deoNaziva.getText();
            //formiranje SQL upita koji treba da vrati komponente koje zadovoljavaju
            //sve unete parametre (combobox-evi i uneti tekst za pretragu)
            if (tip==null) tip="";
            if (proizvodjac==null) proizvodjac="";
            String uslovAktuelne="";
            if (aktuelneCB.isSelected())
                uslovAktuelne=" AND k.aktuelna=1";
            String uslov="";
            if (tip.equals("") && proizvodjac.equals(""))
                uslov="WHERE k.naziv like '%" + komponenta + "%'";
            else if (tip.equals(""))
                uslov="WHERE p.naziv='" + proizvodjac + "' AND k.naziv like '%"+ komponenta + "%'";
            else if (proizvodjac.equals(""))
                uslov="WHERE t.naziv='" + tip + "' AND k.naziv like '%"+ komponenta + "%'";
            else
                uslov="WHERE t.naziv='" + tip + "' AND p.naziv='" + proizvodjac + "' AND k.naziv like '%"+ komponenta + "%'";

            String upit="SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena, k.slika " +
                        "FROM komponenta as k " +
                        "INNER JOIN tip as t ON k.tip_id=t.tip_id " +
                        "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id " + uslov + uslovAktuelne;
            //postavljanje upita nad bazom podataka
            ResultSet rs=DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs!=null) {
                //ubacivanje podataka u listu
                while (rs.next()) {
                    //kreiranje komponente na osnovu podataka ocitanih iz baze
                    KomponentaSaSlikom kompon=new KomponentaSaSlikom(rs.getInt(1), rs.getString(2),
                                                        rs.getString(3), rs.getString(4), 
                                                        rs.getInt(5), rs.getDouble(6), new Slika(rs.getString(7)));
                    //dodavanje komponente u listu
                    podaciFiltrirano.add(kompon);
                }
                if (conn!=null) conn.close();
            }
        }  
    }
    
    
    
    
    
}
