
package com.grupa1.dbconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import pomocne.Pomocne;


public class PomocneDAO {

    private static Connection conn;
    
    //ucitavanje vrednosti u ComboBox tipa
    public static void ispuniComboBoxZaTip(ObservableList<String> opcijeTip, boolean dodajNovi) {
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
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
                }
            }
        }
    }
    //ucitavanje vrednosti u ComboBox tipa za zadatog proizvodjaca
    public static void ispuniComboBoxZaTipProizvodjaca(ObservableList<String> opcijeTip, String proizvodjac, boolean dodajNovi) {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String upit="SELECT DISTINCT t.naziv FROM tip as t "
                        + "INNER JOIN komponenta as k ON k.tip_id=t.tip_id "
                        + "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id "
                        + "WHERE p.naziv='"+proizvodjac+"'";
                rs=DBUtil.prikupiPodatke(conn, upit);
                if (dodajNovi) opcijeTip.add(">>dodaj novi<<");
                while (rs.next()) {
                    opcijeTip.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
                }
            }
        }
    }
    //ucitavanje vrednosti u ComboBox proizvodjaca
    public static void ispuniComboBoxZaProizvodjaca(ObservableList<String> opcijeProizvodjac, boolean dodajNovog) {
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
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
                }
            }
        }
    }
    //ucitavanje vrednosti u ComboBox proizvodjaca
    public static void ispuniComboBoxZaProizvodjacaPoTipu(ObservableList<String> opcijeProizvodjac, String tip, boolean dodajNovog) {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String upit="SELECT DISTINCT p.naziv FROM proizvodjac as p "
                        + "INNER JOIN komponenta as k ON k.proizvodjac_id=p.proizvodjac_id "
                        + "INNER JOIN tip as t ON k.tip_id=t.tip_id "
                        + "WHERE t.naziv='"+tip+"'";
                rs=DBUtil.prikupiPodatke(conn, upit);
                if (dodajNovog) opcijeProizvodjac.add(">>dodaj novog<<");
                while (rs.next()) {
                    opcijeProizvodjac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
                }
            }
        }
    }
    //ucitavanje vrednosti u ComboBox dobavljaca
    public static void ispuniComboBoxZaDobavljaca(ObservableList<String> opcijeDobavljac) {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv, grad FROM dobavljac");
                while (rs.next()) {
                    opcijeDobavljac.add(rs.getString("naziv") + ", " + rs.getString("grad"));
                }
            } catch (SQLException ex) {
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
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
                Pomocne.poruka(ex.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Pomocne.poruka(ex.getMessage());
                }
            }
        }
    }
    
    
    
}
