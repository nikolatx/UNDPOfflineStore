
package com.grupa1.dbconnection;

import com.grupa1.model.Dokument;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Osoba;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.collections.ObservableList;
import pomocne.Pomocne;


public class NabavkaDAO {

    private int prijemnicaId;
    
    //realizcija nabavke robe
    public boolean nabavkaRobe(String[] podaciDobavljaca, Osoba dobavljac, 
            Dokument prijemnica, ObservableList<KomponentaSaSlikom> podaciOdabrano) {
        
        boolean uspesno=false;
        
        Connection conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                String naziv=podaciDobavljaca[0].trim();
                String grad=podaciDobavljaca[1].trim();
                //upit za nalazenje dobavljac_id vrednosti za zadat naziv dobavljaca
                PreparedStatement stmt0=conn.prepareStatement("SELECT dobavljac_id, naziv, ulica, broj, "
                        + "grad, postanski_broj, drzava, telefon FROM dobavljac WHERE naziv=? AND grad=?");
                
                stmt0.setString(1, naziv);
                stmt0.setString(2, grad);
                ResultSet rs=stmt0.executeQuery();
                if (rs.next()) {
                    dobavljac.setId(rs.getInt(1));
                    dobavljac.setNaziv(rs.getString(2));
                    dobavljac.setUlica(rs.getString(3));
                    dobavljac.setBroj(rs.getString(4));
                    dobavljac.setGrad(rs.getString(5));
                    dobavljac.setPostBr(rs.getString(6));
                    dobavljac.setDrzava(rs.getString(7));
                    dobavljac.setTelefon(rs.getString(8));
                }
                //priprema za batchupdate
                conn.setAutoCommit(false);
                //kreiranje nove prijemnice
                //tekuci datum
                Date datum= new java.sql.Date(Calendar.getInstance().getTime().getTime());
                
                //PreparedStatement objekat za drugi upit - kreiranje nove prijemnice
                //potrebno je sacuvati vrednost prijemnica_id novokreirane prijemnice
                String columnNames[] = new String[] { "prijemnica_id" };
                PreparedStatement stmt1=conn.prepareStatement("INSERT INTO prijemnica (dobavljac_id, datum) VALUES (?,?)", columnNames);
                stmt1.setInt(1, dobavljac.getId());
                stmt1.setDate(2, datum);
                prijemnicaId=0;
                if (stmt1.executeUpdate()>0) {
                    java.sql.ResultSet generatedKeys=stmt1.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        prijemnicaId=generatedKeys.getInt(1);
                    }
                    int dob=dobavljac.getId();
                    prijemnica.setOsobaId(dob);
                    prijemnica.setDokumentId(prijemnicaId);
                    prijemnica.setDatum(datum);
                    
                    //upit za azuriranje kolicine komponente
                    PreparedStatement stmt2=conn.prepareStatement("UPDATE komponenta SET kolicina=kolicina+? WHERE komponenta_id=?");
                    //upit za kreiranje detalja prijemnice
                    PreparedStatement stmt3=conn.prepareStatement("INSERT INTO detaljiprijemnice (prijemnica_id, komponenta_id, cena, kolicina) VALUES (?,?,?,?)");
                    //priprema za batch update i insert
                    podaciOdabrano.forEach(e->{
                        try {
                            //batch update kolicine
                            stmt2.setInt(1, e.getKolicina());
                            stmt2.setInt(2, e.getId());
                            stmt2.addBatch();
                            //batch update detalja prijemnice
                            stmt3.setInt(1, prijemnicaId);
                            stmt3.setInt(2, e.getId());
                            stmt3.setDouble(3, e.getCena());
                            stmt3.setInt(4, e.getKolicina());
                            stmt3.addBatch();
                        } catch (SQLException ex) {
                            Pomocne.poruka(ex.getMessage());
                        } 
                    });

                    //izvrsavanje batch update upita
                    stmt1.executeBatch();
                    stmt2.executeBatch();
                    stmt3.executeBatch();
                    conn.commit();
                    uspesno=true;
                }
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
        return uspesno;
    }
    
}
