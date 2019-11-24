
package kontroleri;

import com.grupa1.dbconnection.AzuriranjeDAO;
import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.KomponentaSvaPolja;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pomocne.Pomocne;
import pomocne.Tabela;


public class AzuriranjeKontroler {
    
    
   AzuriranjeDAO azuriranjeDAO=new AzuriranjeDAO();
    
    //preuzimanje podataka iz baze podataka i punjenje tabele podacima
    public void preuzmiPodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
                               ObservableList<KomponentaSaSlikom> podaciFiltrirano, TableView tabelaFiltrirano) {
        tabelaFiltrirano.getItems().clear();
        tabelaFiltrirano.getColumns().clear();
        try {
            azuriranjeDAO.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, podaciFiltrirano);
            //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
            if (tabelaFiltrirano.getColumns().isEmpty())
                Tabela.kreirajTabelu(tabelaFiltrirano, false);
            //ucitavanje podataka u tabelu
            tabelaFiltrirano.setItems(podaciFiltrirano);
        } catch (SQLException ex) {
            Pomocne.poruka(ex.getMessage());
        }
    } 
    
    //preuzimanje podataka iz baze podataka i punjenje tabele podacima
    public void preuzmiSvePodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
                               ObservableList<KomponentaSvaPolja> podaciFiltrirano, TableView tabelaFiltrirano) {
        tabelaFiltrirano.getItems().clear();
        tabelaFiltrirano.getColumns().clear();
        try {
            azuriranjeDAO.preuzmiSvePodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, podaciFiltrirano);
            //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
            if (tabelaFiltrirano.getColumns().isEmpty())
                Tabela.kreirajTabelu(tabelaFiltrirano, false);
            //ucitavanje podataka u tabelu
            tabelaFiltrirano.setItems(podaciFiltrirano);
        } catch (SQLException ex) {
            Pomocne.poruka(ex.getMessage());
        }
    } 
    
    //brise komponentu
    public int obrisiKomponentu(TableView tabelaFiltrirano, ObservableList<KomponentaSvaPolja> podaci) {
        KomponentaSvaPolja komponenta = (KomponentaSvaPolja)tabelaFiltrirano.getSelectionModel().getSelectedItem();
        komponenta.setAktuelna(false);
        komponenta = azuriranjeDAO.obrisiKomponentu(komponenta);
        if (komponenta!=null) {
            podaci.remove(komponenta);
            return 1;
        }
        return 0;
    } 
    
}
