
package com.grupa1.dbconnection;

import com.grupa1.model.IzvestajPOJO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import pomocne.Pomocne;
import pomocne.Tabela;

/**
 *
 * @author nikolatimotijevic
 */
public class IzvestajDAO {
    
    //prikupljanje podataka za crtanje grafika prodaje i nabavke u poslednjih 12 meseci
    public void godisnjiGrafik(XYChart.Series podaci, boolean prodaja) {
        Connection conn;
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String upit="";
                if (prodaja)
                    upit="SELECT MONTHNAME(f.datum), SUM(df.cena*df.kolicina) FROM faktura as f "
                        + "INNER JOIN detaljifakture AS df ON f.faktura_id=df.faktura_id "
                        + "WHERE f.datum BETWEEN date_sub(NOW(), INTERVAL 365 DAY) AND NOW() "
                        + "GROUP BY MONTH(f.datum) ORDER BY MONTH(f.datum)";
                else
                    upit="SELECT MONTHNAME(p.datum), SUM(dp.cena*dp.kolicina) FROM prijemnica as p "
                        + "INNER JOIN detaljiprijemnice AS dp ON p.prijemnica_id=dp.prijemnica_id "
                        + "WHERE p.datum BETWEEN date_sub(NOW(), INTERVAL 365 DAY) AND NOW() "
                        + "GROUP BY MONTH(p.datum) ORDER BY MONTH(p.datum)";
                rs=conn.createStatement().executeQuery(upit);
                while (rs.next()) {
                    String month1=rs.getString(1);
                    double cena1=rs.getDouble(2);
                    podaci.getData().add(new XYChart.Data(month1, cena1));
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
    
    //prikupljanje podataka za crtanje grafika najisplativijih komponenata u poslednjih 12 meseci
    public void godisnjiGrafikNajisplativije(XYChart.Series podaci) {
        Connection conn;
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String upit="SELECT k.naziv, SUM(df.cena*df.kolicina) AS suma FROM faktura as f " +
                            "INNER JOIN detaljifakture AS df ON f.faktura_id=df.faktura_id " +
                            "INNER JOIN komponenta AS k ON df.komponenta_id=k.komponenta_id " +
                            "WHERE f.datum BETWEEN date_sub(NOW(), INTERVAL 365 DAY) AND NOW() " +
                            "GROUP BY k.komponenta_id ORDER BY suma DESC " +
                            "LIMIT 5";
                rs=conn.createStatement().executeQuery(upit);
                //popunjavanje serije u nazad da bi prvo bila najisplativija komponenta
                rs.afterLast();
                while (rs.previous()) {
                    String naziv1=rs.getString(1);
                    double suma1=rs.getDouble(2);
                    podaci.getData().add(new XYChart.Data(suma1, naziv1));
                    
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
    
    //prikupljanje podataka za crtanje grafika najprodavanijih komponenata u poslednjih 12 meseci
    public void godisnjiGrafikNajprodavanije(XYChart.Series podaci) {
        Connection conn;
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String upit="SELECT k.naziv, SUM(df.kolicina) AS suma FROM faktura as f " +
                            "INNER JOIN detaljifakture AS df ON f.faktura_id=df.faktura_id " +
                            "INNER JOIN komponenta AS k ON df.komponenta_id=k.komponenta_id " +
                            "WHERE f.datum BETWEEN date_sub(NOW(), INTERVAL 365 DAY) AND NOW() " +
                            "GROUP BY k.komponenta_id ORDER BY suma DESC " +
                            "LIMIT 5";
                rs=conn.createStatement().executeQuery(upit);
                //popunjavanje serije u nazad da bi prvo bila najprodavanija komponenta
                rs.afterLast();
                while (rs.previous()) {
                    String naziv1=rs.getString(1);
                    double suma1=rs.getDouble(2);
                    podaci.getData().add(new XYChart.Data(suma1, naziv1));
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
    
    //univerzalna metoda za kreiranje svih izvestaja sa zadatim uslovom
    public void kreirajIzvestaj(TableView tabela, String uslov, Label labela, boolean prodaja) {
        Connection conn=null;
        tabela.getItems().clear();
        tabela.getColumns().clear();
        try {
            conn = DBUtil.napraviKonekciju();
            labela.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(conn, uslov, prodaja)));
            preuzmiPodatke(conn, tabela, uslov, prodaja);
        } catch (SQLException ex) {
            Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke");
            if (conn!=null) try {
                conn.close();
            } catch (SQLException ex1) {}
        }
    }
    
    //racuna ukupnu sumu prodaje/nabavke za zadati period
    public double izracunajSumu(Connection conn, String uslov, boolean prodaja) throws SQLException {
        double suma = 0;
        if (conn != null) {
            String upit = "SELECT SUM(dp.cena) "
                    + "FROM detaljiprijemnice as dp "
                    + "INNER JOIN prijemnica as pr ON dp.prijemnica_id=pr.prijemnica_id "
                    + uslov;
            if (prodaja) {
                upit=upit.replaceAll("prijemnice", "fakture");
                upit=upit.replaceAll("prijemnica", "faktura");
            }
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            if ((rs != null) && (rs.next())) {
                    suma += rs.getDouble(1);
            }
        }
        return suma;
    }
    
    //preuzimanje podataka iz baze po zadatom upitu
    public void preuzmiPodatke(Connection conn, TableView tabela, String uslov, boolean prodaja) throws SQLException {
        if (conn != null) {
            //formulisanje upita
            String upit = "SELECT k.komponenta_id, k.naziv, p.naziv, SUM(dp.kolicina), SUM(dp.cena) "
                    + "FROM prijemnica as pr "
                    + "INNER JOIN detaljiprijemnice as dp ON pr.prijemnica_id=dp.prijemnica_id "
                    + "INNER JOIN komponenta as k ON k.komponenta_id=dp.komponenta_id "
                    + "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id "
                    + uslov 
                    + "GROUP BY dp.komponenta_id";
            if (prodaja) {
                upit=upit.replaceAll("prijemnice", "fakture");
                upit=upit.replaceAll("prijemnica", "faktura");
            }
            //postavljanje upita nad bazom podataka
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs != null) {
                ObservableList podaci = FXCollections.observableArrayList();
                //ubacivanje podataka u listu
                while (rs.next()) {
                    podaci.add(new IzvestajPOJO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDouble(5)));
                }
                //ubacivanje podataka u tabelu
                if (podaci.size()>0) {
                    if (tabela.getColumns().isEmpty()) 
                        Tabela.kreirajTabeluIzvestaja(tabela);
                    tabela.setItems(podaci);
                }
            }
        }
    }
    
    
    
}
