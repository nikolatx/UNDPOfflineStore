
package kontroleri;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.dbconnection.NabavkaDAO;
import com.grupa1.model.KomponentaSvaPolja;
import com.grupa1.model.Osoba;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.grupa1.dbconnection.PomocneDAO;
import com.grupa1.model.Dokument;
import com.grupa1.model.KomponentaSaSlikom;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import pomocne.Pomocne;
import pomocne.Tabela;
import undp.NovaKomponenta;
import undp.NoviDobavljac;
import undp.SpinnerDialog;
import util.DocxExport;


public class NabavkaKontroler {
    
    NabavkaDAO nabavkaDAO=new NabavkaDAO();

    boolean uspesnoRes;
    
    //korekcija kolicine odabranog artikla iz donje tabele
    public void korekcijaKolicine(TableView tabelaOdabrano, ObservableList<KomponentaSaSlikom> podaciOdabrano) {
        //ukoliko ima upisanih podataka u tabeli tabelaOdabrano
        if (tabelaOdabrano.getColumns().size()>0) {
            //ocitavanje odabrane komponente
            KomponentaSaSlikom komponenta = (KomponentaSaSlikom) tabelaOdabrano.getSelectionModel().getSelectedItem();
            if (komponenta==null) return;
            //zadavanje kolicine
            SpinnerDialog spDialog=new SpinnerDialog();
            int kolicina=spDialog.display("Kolicina", "Odaberi kolicinu", Integer.MAX_VALUE, komponenta.getKolicina());
            if (kolicina>0) {
                //korekcija kolicine u listi
                int indeks=podaciOdabrano.indexOf(komponenta);
                komponenta.setKolicina(kolicina);
                podaciOdabrano.set(indeks,komponenta);
            } else if (kolicina==0)
                podaciOdabrano.remove(komponenta);
        }
    }
    
    //dodavanje odabrane komponente iz gornje tabele u donju tabelu
    public void odaberiKomponentu(TableView tabelaFiltrirano, TableView tabelaOdabrano,
                                    ObservableList<KomponentaSaSlikom> podaciOdabrano) {
        //ocitavanje odabrane komponente
        KomponentaSaSlikom komponenta = (KomponentaSaSlikom) tabelaFiltrirano.getSelectionModel().getSelectedItem();
        if (komponenta==null) return;
        //kreiranje kolona tabele ukoliko vec nisu kreirane
        if (tabelaOdabrano.getColumns().isEmpty())
            Tabela.kreirajTabeluBezSlike(tabelaOdabrano, false);

        //ukoliko komponenta ne postoji u tabeli sa odabranim komponentama - dodavanje
        if (!podaciOdabrano.contains(komponenta)) {
            //zadavanje kolicine
            SpinnerDialog spDialog=new SpinnerDialog();
            int kolicina=spDialog.display("Kolicina", "Odaberi kolicinu", Integer.MAX_VALUE, 1);
            if (kolicina>0) {
                komponenta.setKolicina(kolicina);
                podaciOdabrano.add(komponenta);
                tabelaOdabrano.setItems(podaciOdabrano);
            }
        }
    }
    
    //preuzimanje podataka iz baze podataka i smestanje u listu i tabelu
    public void preuzmiPodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
                               ObservableList<KomponentaSaSlikom> podaciFiltrirano, TableView tabelaFiltrirano) {
        tabelaFiltrirano.getItems().clear();
        tabelaFiltrirano.getColumns().clear();
        try {
            DBUtil.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, podaciFiltrirano);
            //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
            if (tabelaFiltrirano.getColumns().isEmpty())
                Tabela.kreirajTabeluBezSlike(tabelaFiltrirano, false);
            //ucitavanje podataka u tabelu
            tabelaFiltrirano.setItems(podaciFiltrirano);
        } catch (SQLException ex) {
            Pomocne.poruka(ex.getMessage());
        }
    }
    
    //pokretanje forme za dodavanje novog dobavljaca
    public void dodajNovogDobavljaca(ComboBox dobavljacCB) {
        Osoba dobavljac=new Osoba();
        NoviDobavljac ndobForm=new NoviDobavljac();
        ndobForm.pokreni(dobavljac, dobavljacCB);
    }
    
    //pokretanje forme za dodavanje nove komponente
    public void dodajNovuKomponentu() {
        KomponentaSvaPolja komponenta=new KomponentaSvaPolja();
        NovaKomponenta nkpForm=new NovaKomponenta();
        nkpForm.pokreni(komponenta);
    }
    
    //popunjavanje comboboxeva podacima iz baze podataka
    public void popuniComboBoxeve(ObservableList<String> opcijeTip, ObservableList<String> opcijeProizvodjac, 
                                    ObservableList<String> opcijeDobavljac) {
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);
        PomocneDAO.ispuniComboBoxZaDobavljaca(opcijeDobavljac);
    }
    
    //kreiranje tabela
    public void kreirajTabele(TableView tFilt, TableView tOdab) {
        Tabela.kreirajTabeluBezSlike(tFilt, false);
        tFilt.setId("tabela-filtrirano");
        Tabela.kreirajTabeluBezSlike(tOdab, false);
        tFilt.setId("tabela-odabrano");
    }
    
    //realizacija nabavke
    public void realizacijaNabavke(ObservableList<KomponentaSaSlikom> podaciOdabrano, ComboBox dobavljacCB, Button nazadDugme) {
        if (!podaciOdabrano.isEmpty()) {
            if (dobavljacCB.getSelectionModel().getSelectedIndex()>-1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Prijem robe");
                alert.setHeaderText("Ažuriranje stanja u bazi podataka i štampanje prijemnice");
                alert.setContentText("Izaberi opciju");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
                dialogPane.getStyleClass().add("dialogPane");
                ButtonType dugmeAzuriraj = new ButtonType("Odobri prijem robe");
                ButtonType dugmeAzurirajStampaj = new ButtonType("Odobri i štampaj");
                ButtonType dugmeOdustani = new ButtonType("Odustani", ButtonBar.ButtonData.CANCEL_CLOSE);
                //alert box za odabir opcije
                alert.getButtonTypes().setAll(dugmeAzuriraj, dugmeAzurirajStampaj, dugmeOdustani);
                Platform.runLater(()->{
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() != dugmeOdustani){
                        Osoba dobavljac=new Osoba();
                        Dokument prijemnica=new Dokument();
                        //azuriranje kolicina i pravljenje prijemnice
                        String[] podaciDobavljaca=((String) dobavljacCB.getSelectionModel().getSelectedItem()).split(",");
                        boolean uspesno=nabavkaDAO.nabavkaRobe(podaciDobavljaca, dobavljac, prijemnica, podaciOdabrano);
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Obavestenje");
                        alert1.setHeaderText(null);
                        if (uspesno) {
                            alert1.setContentText("Nabavka robe uspešno obavljena!");
                            if (result.get() == dugmeAzurirajStampaj)
                                try {
                                    DocxExport.exportData(true, dobavljac, prijemnica, podaciOdabrano);
                                    alert1.setContentText("Nabavka robe uspešno obavljena uz uspešno pravljenje prijemnice!");
                                } catch (Exception ex) {
                                    Pomocne.poruka(ex.getMessage());
                                }
                        } else
                            alert1.setContentText("Došlo je do greške pri upisu u bazu podataka!");
                        Platform.runLater( () -> {
                            alert1.showAndWait();
                            nazadDugme.fire();
                        });
                    } else {
                        alert.close();
                    } 
                });
            } else 
                Pomocne.poruka("Dobavljač mora biti odabran!");
        } else 
            Pomocne.poruka("Nijedna komponenta nije odabrana!");
    }
}
