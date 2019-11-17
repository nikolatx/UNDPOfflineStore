package com.grupa1.dbconnection;

import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.KomponentaSvaPolja;
import com.grupa1.model.Osoba;
import com.grupa1.model.Slika;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pomocne.Pomocne;

public class DBUtil {

    private static final String dbUrl = "jdbc:mysql://localhost/";
    private static final String dbName = "undpofflinestore";
    private static final String userName = "root";
    private static final String password = "";

    public static Connection napraviKonekciju()  {
        Connection conn=null;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
        } catch (SQLException ex) {
            System.out.println("Neuspešna konekcija");
        }
        return conn;
    }
    
    //prikupljanje podataka iz baze pomocu upita
    public static ResultSet prikupiPodatke(Connection conn, String upit) {
        ResultSet rs = null;
        List<String> lista=new ArrayList<>();
        try {
            rs = conn.createStatement().executeQuery(upit);
        } catch (SQLException ex) {
            System.out.println("Neuspešna konekcija");
            Pomocne.poruka("Proverite da li je pokrenut MySQL server!");
        }
        return rs;
    }
    
    //prikupljanje podataka iz baze pomocu upita sa tri parametra tipa String
    public static List<Integer> prikupiPodatkeParam(String upit, String param1, String nazivPolja) {
        Connection conn=null;
        ResultSet rs = null;
        Statement statement=null;
        List<Integer> lista=new ArrayList<>();
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps=conn.prepareStatement(upit);
            ps.setString(1,param1);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt(nazivPolja));
            }
        } catch (SQLException ex) {
            System.out.println("Neuspešna konekcija");
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                if (conn!=null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
    
    //promena podataka u bazi pomocu upita
    public static int izvrsiUpdateUpit(String upit) {
        Connection conn=null;
        int promenjeno=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            promenjeno = conn.createStatement().executeUpdate(upit);
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
        return promenjeno;
    }
    
    //preuzima aktuelno stanje komponente ciji se id prosledjuje kao parametar
    public static int preuzmiAktuelnoStanje(int id) {
        Connection conn=null;
        ResultSet rs = null;
        Statement statement=null;
        int result=-1;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            String upit="SELECT kolicina FROM komponenta WHERE komponenta_id=?";
            PreparedStatement ps=conn.prepareStatement(upit);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next())
                result=rs.getInt(1);
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
        return result;
    }
    
    //insert nove komponente u bazu podataka
    public static int ubaciKomponentu(KomponentaSvaPolja komponenta, int proizvodjacId, int tipId) {
        Connection conn=null;
        int upisano=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("INSERT INTO komponenta (naziv, proizvodjac_id, tip_id, kolicina, cena, slika, aktuelna) "
                            + "VALUES (?,?,?,?,?,?,?)");
            ps1.setString(1, komponenta.getNaziv());
            ps1.setInt(2, proizvodjacId);
            ps1.setInt(3, tipId);
            ps1.setInt(4, komponenta.getKolicina());
            ps1.setDouble(5, komponenta.getCena());
            ps1.setString(6, ((Slika)komponenta.getSlika()).getNaziv());  //komponenta.getSlika());
            ps1.setBoolean(7, komponenta.getAktuelna());
            upisano=ps1.executeUpdate();
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
        return upisano;
    }

    //insert novog dobavljaca u bazu podataka
    public static int ubaciDobavljaca(Osoba dobavljac) {
        Connection conn=null;
        int upisano=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("INSERT INTO dobavljac (naziv, ulica, broj,"
                    + " grad, postanski_broj, drzava, telefon) VALUES (?,?,?,?,?,?,?)");
            ps1.setString(1, dobavljac.getNaziv());
            ps1.setString(2, dobavljac.getUlica());
            ps1.setString(3, dobavljac.getBroj());
            ps1.setString(4, dobavljac.getGrad());
            ps1.setString(5, dobavljac.getPostBr());
            ps1.setString(6, dobavljac.getDrzava());
            ps1.setString(7, dobavljac.getTelefon());
            upisano=ps1.executeUpdate();
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
        return upisano;
    }
    
    //insert novog kupca u bazu podataka
    public static int ubaciKupca(Osoba kupac) {
        Connection conn=null;
        int upisano=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("INSERT INTO kupac (naziv, ulica, broj,"
                    + " grad, postanski_broj, drzava, telefon) VALUES (?,?,?,?,?,?,?)");
            ps1.setString(1, kupac.getNaziv());
            ps1.setString(2, kupac.getUlica());
            ps1.setString(3, kupac.getBroj());
            ps1.setString(4, kupac.getGrad());
            ps1.setString(5, kupac.getPostBr());
            ps1.setString(6, kupac.getDrzava());
            ps1.setString(7, kupac.getTelefon());
            upisano=ps1.executeUpdate();
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
        return upisano;
    }
    
    public static int proveriDuplikatKomponente(String naziv, int proizvodjacId, int tipId) {
        Connection conn=null;
        ResultSet rs=null;
        int postoji=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("SELECT COUNT(*) FROM komponenta as k "
                    + "WHERE k.naziv=? AND k.proizvodjac_id=? AND k.tip_id=?");
            ps1.setString(1, naziv);
            ps1.setInt(2, proizvodjacId);
            ps1.setInt(3, tipId);
            rs=ps1.executeQuery();
            if (rs.next())
                postoji=rs.getInt(1);
            
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
        return postoji;
    }
    
    //provera da li je unet duplikat dobavljaca
    public static int proveriDuplikatDobavljaca(String naziv, String ulica, String broj, 
            String grad) {
        Connection conn=null;
        ResultSet rs=null;
        int postoji=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("SELECT COUNT(*) FROM dobavljac as d "
                    + "WHERE UPPER(d.naziv)=UPPER(?) AND UPPER(d.ulica)=UPPER(?) "
                    + "AND UPPER(d.broj)=UPPER(?) AND UPPER(d.grad)=UPPER(?)");
            ps1.setString(1, naziv);
            ps1.setString(2, ulica);
            ps1.setString(3, broj);
            ps1.setString(4, grad);
            rs=ps1.executeQuery();
            if (rs.next())
                postoji=rs.getInt(1);
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
        return postoji;
    }
    
    //provera da li je unet duplikat kupca
    public static int proveriDuplikatKupca(String naziv, String ulica, String broj, 
            String grad) {
        Connection conn=null;
        ResultSet rs=null;
        int postoji=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=conn.prepareStatement("SELECT COUNT(*) FROM kupac as k "
                    + "WHERE UPPER(k.naziv)=UPPER(?) AND UPPER(k.ulica)=UPPER(?) "
                    + "AND UPPER(k.broj)=UPPER(?) AND UPPER(k.grad)=UPPER(?)");
            ps1.setString(1, naziv);
            ps1.setString(2, ulica);
            ps1.setString(3, broj);
            ps1.setString(4, grad);
            rs=ps1.executeQuery();
            if (rs.next())
                postoji=rs.getInt(1);
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
        return postoji;
    }
    
    //ubacivanje jednog String polja u tabelu
    public static int dodajProizvodjacaIliTip(String nazivTabele, String vrednost) {
        Connection conn=null;
        int upisano=0;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspešna konekcija");
            PreparedStatement ps1=null;
            if (nazivTabele.equals("tip"))
                ps1=conn.prepareStatement("INSERT INTO tip (naziv) VALUES (?)");
            else
                ps1=conn.prepareStatement("INSERT INTO proizvodjac (naziv) VALUES (?)");
            ps1.setString(1, vrednost);
            upisano=ps1.executeUpdate();
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
        return upisano;
    }
    
    //preuzimanje podataka iz baze podataka za popunu tabela
    public static void preuzmiPodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
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
                                                        rs.getInt(5), rs.getDouble(6), new Slika(rs.getString(7)) );
                    //dodavanje komponente u listu
                    podaciFiltrirano.add(kompon);
                }
            }
        } else
            Pomocne.poruka("Proverite da li je pokrenut MySQL server!");
        if (conn!=null) conn.close();
    }
    
    
}
