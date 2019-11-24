
package com.grupa1.dbconnection;

import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.KomponentaSvaPolja;
import com.grupa1.model.Slika;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pomocne.Pomocne;


public class AzuriranjeDAO {
    
    //preuzima sve podatke za KomponentaSaSlikom (neukljucujuci polje aktuelna)
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
    
    //preuzima sve podatke za KomponentaSvaPolja (ukljucujuci i polje aktuelna)
    public void preuzmiSvePodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
            ObservableList<KomponentaSvaPolja> podaciFiltrirano) throws SQLException {
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

            String upit="SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena, k.slika, k.aktuelna " +
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
                    KomponentaSvaPolja kompon=new KomponentaSvaPolja(rs.getInt(1), rs.getString(2),
                                                        rs.getString(3), rs.getString(4), 
                                                        rs.getInt(5), rs.getDouble(6), new Slika(rs.getString(7)), (rs.getInt(8) != 0));
                    //dodavanje komponente u listu
                    podaciFiltrirano.add(kompon);
                }
                if (conn!=null) conn.close();
            }
        }  
    }
    
    //brise komponentu u bazi podataka postavljanjem polja aktuelna na false
    public KomponentaSvaPolja obrisiKomponentu(KomponentaSvaPolja komponenta) {
        Connection conn=DBUtil.napraviKonekciju();
        int izmenjeno=0;
        try {
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("UPDATE komponenta SET aktuelna=? WHERE komponenta_id=?");
            ps1.setBoolean(1, komponenta.getAktuelna());
            ps1.setInt(2, komponenta.getId());
            izmenjeno=ps1.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Neuspešna konekcija");
            Pomocne.poruka("Proverite da li je pokrenut MySQL server!");
        }
        finally {
            try {
                if (conn!=null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (izmenjeno>0)
            return komponenta;
        else
            return null;
    }
    
}
