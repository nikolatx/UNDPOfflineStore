
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import pomocne.Pomocne;
import undp.Prodaja;


public class ProdajaDAO {
    
    //azuriranje kolicina pri prodaji
    public boolean prodajaRobe(String[] podaciKupca, Osoba kupac, 
            Dokument faktura, ObservableList<KomponentaSaSlikom> podaciOdabrano) {

        boolean uspesno = false;
        Connection conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                String naziv=podaciKupca[0].trim();
                String grad=podaciKupca[1].trim();
                //upit za nalazenje kupca po nazivu
                PreparedStatement stmt0=conn.prepareStatement("SELECT kupac_id, naziv, ulica, broj, grad, postanski_broj, "
                        + "drzava, telefon FROM kupac WHERE naziv=? AND grad=?");
                stmt0.setString(1, naziv);
                stmt0.setString(2, grad);
                ResultSet rs=stmt0.executeQuery();
                if (rs.next()) {
                    kupac.setId(rs.getInt(1));
                    kupac.setNaziv(rs.getString(2));
                    kupac.setUlica(rs.getString(3));
                    kupac.setBroj(rs.getString(4));
                    kupac.setGrad(rs.getString(5));
                    kupac.setPostBr(rs.getString(6));
                    kupac.setDrzava(rs.getString(7));
                    kupac.setTelefon(rs.getString(8));
                }
                //priprema za batchupdate
                conn.setAutoCommit(false);

                Date datum = new java.sql.Date(Calendar.getInstance().getTime().getTime());

                //PreparedStatement objekat za drugi upit
                String columnNames[] = new String[]{"faktura_id"};
                PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO faktura (kupac_id, datum) VALUES (?,?)", columnNames);
                stmt1.setInt(1, kupac.getId());
                stmt1.setDate(2, datum);
                //postavljanje polja faktura_id na tekucu vrednost
                if (stmt1.executeUpdate() > 0) {
                    java.sql.ResultSet generatedKeys = stmt1.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        faktura.setDokumentId(generatedKeys.getInt(1));
                    }
                }

                PreparedStatement stmt2 = conn.prepareStatement("UPDATE komponenta SET kolicina=kolicina-? WHERE komponenta_id=?");
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO detaljifakture (faktura_id, komponenta_id, cena, kolicina) VALUES (?,?,?,?)");

                podaciOdabrano.forEach(e -> {
                    try {
                        stmt2.setInt(1, e.getKolicina());
                        stmt2.setInt(2, e.getId());
                        stmt2.addBatch();

                        stmt3.setInt(1, faktura.getDokumentId());
                        stmt3.setInt(2, e.getId());
                        stmt3.setDouble(3, e.getCena());
                        stmt3.setInt(4, e.getKolicina());
                        stmt3.addBatch();

                    } catch (SQLException ex) {
                        Pomocne.poruka(ex.getMessage());
                    }
                });

                stmt1.executeBatch();
                stmt2.executeBatch();
                stmt3.executeBatch();
                conn.commit();
                uspesno = true;
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException ex1) {
                    Pomocne.poruka(ex.getMessage());
                }
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
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
