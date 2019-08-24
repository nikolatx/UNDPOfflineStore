package com.grupa1.dbconnection;

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

public class DBUtil {

    private static final String dbUrl = "jdbc:mysql://localhost/";
    private static final String dbName = "undpofflinestore";
    private static final String userName = "root";
    private static final String password = "";

    public static Connection napraviKonekciju() {
        Connection conn=null;
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspesna konekcija");
        } catch (SQLException ex) {
            System.out.println("Neuspesna konekcija");
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("Neuspesna konekcija");
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    //prikupljanje podataka iz baze pomocu upita sa tri parametra tipa String
    public static List<String> prikupiPodatkeParam(String upit, String param1) {
        Connection conn=null;
        ResultSet rs = null;
        Statement statement=null;
        List<String> lista=new ArrayList<>();
        try {
            conn = DriverManager.getConnection(dbUrl + dbName, userName, password);
            System.out.println("Uspesna konekcija");
            statement=conn.createStatement();
            PreparedStatement ps=conn.prepareStatement(upit);
            ps.setString(1,param1);
            rs = conn.createStatement().executeQuery(upit);
            while (rs.next()) {
                lista.add(rs.getString("naziv"));
            }
        } catch (SQLException ex) {
            System.out.println("Neuspesna konekcija");
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
            System.out.println("Uspesna konekcija");
            promenjeno = conn.createStatement().executeUpdate(upit);
        } catch (SQLException ex) {
            System.out.println("Neuspesna konekcija");
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("Uspesna konekcija");
            String upit="SELECT kolicina FROM komponenta WHERE komponenta_id=?";
            PreparedStatement ps=conn.prepareStatement(upit);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if (rs.next())
                result=rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Neuspesna konekcija");
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    
}
