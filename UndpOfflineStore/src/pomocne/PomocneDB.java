/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocne;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.Komponenta;
import com.grupa1.model.KomponentaSvaPolja;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import undp.Nabavka;
import undp.Prodaja;



public class PomocneDB {

    private static Connection conn;
    
    //ucitavanje vrednosti u ComboBox tipa
    public static void ispuniComboBoxZaTip(ObservableList<String> opcijeTip, boolean dodajNovi) {
        opcijeTip.forEach(e->opcijeTip.remove(e));
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip");
                if (dodajNovi) opcijeTip.add(">>dodaj novi<<");
                while (rs.next()) {
                    opcijeTip.add(rs.getString("naziv"));
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje vrednosti u ComboBox proizvodjaca
    public static void ispuniComboBoxZaProizvodjaca(ObservableList<String> opcijeProizvodjac, boolean dodajNovog) {
        opcijeProizvodjac.forEach(e->opcijeProizvodjac.remove(e));
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM proizvodjac");
                if (dodajNovog) opcijeProizvodjac.add(">>dodaj novog<<");
                while (rs.next()) {
                    opcijeProizvodjac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje vrednosti u ComboBox dobavljaca
    public static void ispuniComboBoxZaDobavljaca(ObservableList<String> opcijeDobavljac) {
        opcijeDobavljac.forEach(e->opcijeDobavljac.remove(e));
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv, grad FROM dobavljac");
                while (rs.next()) {
                    opcijeDobavljac.add(rs.getString("naziv") + ", " + rs.getString("grad"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje vrednosti u ComboBox kupca
    public static void ispuniComboBoxZaKupca(ObservableList<String> opcijeKupac) {
        opcijeKupac.forEach(e->opcijeKupac.remove(e));
        ResultSet rs = null;
        conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                rs = DBUtil.prikupiPodatke(conn, "SELECT naziv, grad FROM kupac");
                while (rs.next()) {
                    opcijeKupac.add(rs.getString("naziv") + ", " + rs.getString("grad"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje podataka po zadatim kriterijuma pretrage
    public static void preuzmiPodatke(Connection conn, String tip, String proizvodjac, String komponenta, 
            ObservableList<Komponenta> podaciFiltrirano, TableView tabelaFiltrirano) throws SQLException {
        if (conn != null) {
            //formiranje SQL upita koji treba da vrati komponente koje zadovoljavaju
            //sve unete parametre (combobox-evi i uneti tekst za pretragu)
            if (tip == null) {
                tip = "";
            }
            if (proizvodjac == null) {
                proizvodjac = "";
            }
            String uslov = "";
            if (tip.equals("") && proizvodjac.equals("")) {
                uslov = "WHERE k.naziv like '%" + komponenta + "%'";
            } else if (tip.equals("")) {
                uslov = "WHERE p.naziv='" + proizvodjac + "' AND k.naziv like '%" + komponenta + "%'";
            } else if (proizvodjac.equals("")) {
                uslov = "WHERE t.naziv='" + tip + "' AND k.naziv like '%" + komponenta + "%'";
            } else {
                uslov = "WHERE t.naziv='" + tip + "' AND p.naziv='" + proizvodjac + "' AND k.naziv like '%" + komponenta + "%'";
            }

            String upit = "SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena "
                    + "FROM komponenta as k "
                    + "INNER JOIN tip as t ON k.tip_id=t.tip_id "
                    + "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id " + uslov;
            //postavljanje upita nad bazom podataka
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs != null) {
                try {
                    podaciFiltrirano = FXCollections.observableArrayList();
                    //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                    if (tabelaFiltrirano.getColumns().isEmpty()) {
                        Tabela.kreirajTabelu(tabelaFiltrirano);
                    }
                    //ubacivanje podataka u listu
                    while (rs.next()) {
                        //dodavanje komponente u listu
                        podaciFiltrirano.add(new Komponenta(rs.getInt(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getInt(5), rs.getDouble(6)));
                    }
                    //ubacivanje podataka u tabelu
                    tabelaFiltrirano.setItems(podaciFiltrirano);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.close();
                    }
                }
            }
        }
    }
    
    
    
    
}
