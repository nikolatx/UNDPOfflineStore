
package com.grupa1.dbconnection;

import com.grupa1.model.Podesavanja;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import konstante.Konst;
import pomocne.Pomocne;

/**
 *
 * @author nikolatimotijevic
 */
public class PodesavanjaDAO {
    
    public static boolean sacuvaj(boolean prviUpis, Podesavanja podesavanja) {
        
        boolean uspesno=false;
        
        Connection conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            
            if (prviUpis) {
                try {
                    //priprema za batchupdate
                    conn.setAutoCommit(false);
                    //upit za azuriranje kolicine komponente
                    PreparedStatement stmt1=conn.prepareStatement("INSERT INTO podesavanje (parametar_id, parametar, vrednost) VALUES (?,?,?)");
                    //batch update kolicine
                    stmt1.setInt(1, 1);
                    stmt1.setString(2, "Putanja do slika");
                    stmt1.setString(3, podesavanja.getSlikePath());
                    stmt1.addBatch();
                    stmt1.setInt(1, 2);
                    stmt1.setString(2, "Velicina zumirane slike");
                    stmt1.setString(3, String.valueOf(podesavanja.getSlikeVelicina()));
                    stmt1.addBatch();
                    stmt1.setInt(1, 3);
                    stmt1.setString(2, "Brisanje bookmarka po upisu u docx");
                    stmt1.setString(3, String.valueOf(podesavanja.isDeleteBookmark()?1:0));
                    stmt1.addBatch();
                    stmt1.setInt(1, 4);
                    stmt1.setString(2, "Putanja do prijemnica");
                    stmt1.setString(3, podesavanja.getPrijemnicePath());
                    stmt1.addBatch();
                    stmt1.setInt(1, 5);
                    stmt1.setString(2, "Template fajl za prijemnice");
                    stmt1.setString(3, podesavanja.getPrijemnicaTemplate());
                    stmt1.addBatch();
                    stmt1.setInt(1, 6);
                    stmt1.setString(2, "Putanja do faktura");
                    stmt1.setString(3, podesavanja.getFakturePath());
                    stmt1.addBatch();
                    stmt1.setInt(1, 7);
                    stmt1.setString(2, "Template fajl za fakture");
                    stmt1.setString(3, podesavanja.getFaktureTemplate());
                    stmt1.addBatch();
                    stmt1.executeBatch();
                    conn.commit();
                    uspesno=true;
                    Konst.podesiParametre(podesavanja);
                } catch (SQLException ex) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex1) {
                        Pomocne.poruka(ex1.getMessage());
                    }
                    Pomocne.poruka(ex.getMessage());
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Pomocne.poruka(ex.getMessage());
                    }
                }
            }
            else {
            
                try {
                    //priprema za batchupdate
                    conn.setAutoCommit(false);
                    //upit za azuriranje kolicine komponente
                    PreparedStatement stmt1=conn.prepareStatement("UPDATE podesavanje SET vrednost=? WHERE parametar_id=?");
                    //batch update kolicine
                    stmt1.setInt(2, 1);
                    stmt1.setString(1, podesavanja.getSlikePath());
                    stmt1.addBatch();
                    
                    stmt1.setInt(2, 2);
                    stmt1.setString(1, String.valueOf(podesavanja.getSlikeVelicina()));
                    stmt1.addBatch();
                    stmt1.setInt(2, 3);
                    stmt1.setString(1, String.valueOf(podesavanja.isDeleteBookmark()?1:0));
                    stmt1.addBatch();
                    stmt1.setInt(2, 4);
                    stmt1.setString(1, podesavanja.getPrijemnicePath());
                    stmt1.addBatch();
                    stmt1.setInt(2, 5);
                    stmt1.setString(1, podesavanja.getPrijemnicaTemplate());
                    stmt1.addBatch();
                    stmt1.setInt(2, 6);
                    stmt1.setString(1, podesavanja.getFakturePath());
                    stmt1.addBatch();
                    stmt1.setInt(2, 7);
                    stmt1.setString(1, podesavanja.getFaktureTemplate());
                    stmt1.addBatch();
                    int[] res=new int[7];
                    res=stmt1.executeBatch();
                    conn.commit();
                    uspesno=true;
                    Konst.podesiParametre(podesavanja);
                } catch (SQLException ex) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex1) {
                        Pomocne.poruka(ex1.getMessage());
                    }
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
        return uspesno;
    }

    public static Podesavanja ucitaj() {
        
        Podesavanja podesavanja = new Podesavanja();
        
        Connection conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                
                PreparedStatement stmt=conn.prepareStatement("SELECT parametar_id, parametar, vrednost FROM podesavanje");

                ResultSet rs=stmt.executeQuery();
                
                int id=1;
                while (rs.next() && id<8) {
                    switch(id++) {
                        case(1): podesavanja.setSlikePath(rs.getString(3));break;
                        case(2): podesavanja.setSlikeVelicina(Integer.parseInt(rs.getString(3)));break;
                        case(3): podesavanja.setDeleteBookmark("1".equals(rs.getString(3).trim()));break;
                        case(4): podesavanja.setPrijemnicePath(rs.getString(3));break;
                        case(5): podesavanja.setPrijemnicaTemplate(rs.getString(3));break;
                        case(6): podesavanja.setFakturePath(rs.getString(3));break;
                        case(7): podesavanja.setFaktureTemplate(rs.getString(3));break;
                    }
                }
                if (id==1)
                    return null;
                else
                    return podesavanja;
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
        return null;
    }
    
    
    
    
}
