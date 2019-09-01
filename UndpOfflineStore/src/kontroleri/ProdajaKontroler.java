
package kontroleri;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.dbconnection.ProdajaDAO;
import com.grupa1.model.Dokument;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Osoba;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pomocne.Pomocne;
import undp.NoviKupac;
import undp.SpinnerDialog;
import util.DocxExport;


public class ProdajaKontroler {
    
    ProdajaDAO prodajaDAO=new ProdajaDAO();
    
    //dodavanje novog kupca u bazu podataka
    public void dodajNovogKupca(ComboBox kupacCB) {
        Osoba kupac=new Osoba();
        NoviKupac nkpForm=new NoviKupac();
        nkpForm.pokreni(kupac, kupacCB);
    }
    
    //korekcija kolicine odabrane komponente (u donjoj tabeli)
    public void korekcijaKolicine(TableView tabelaOdabrano, ObservableList<KomponentaSaSlikom> podaciOdabrano) {
        //ukoliko ima upisanih podataka u tabeli tabelaOdabrano
        if (tabelaOdabrano.getColumns().size() > 0) {
            //ocitavanje odabrane komponente
            KomponentaSaSlikom komponenta = (KomponentaSaSlikom) tabelaOdabrano.getSelectionModel().getSelectedItem();
            if (komponenta == null) {
                return;
            }
            //preuzimanje aktuelne kolicine odabrane komponente
            int maksKolicina = DBUtil.preuzmiAktuelnoStanje(komponenta.getId());
            if (maksKolicina > 0) {
                SpinnerDialog spDialog=new SpinnerDialog();
                int kolicina=spDialog.display("Kolicina", "Odaberi kolicinu", maksKolicina, komponenta.getKolicina());
                if (kolicina>0) {
                    //korekcija kolicine u listi
                    int indeks=podaciOdabrano.indexOf(komponenta);
                    komponenta.setKolicina(kolicina);
                    podaciOdabrano.set(indeks,komponenta);
                } else if (kolicina==0) {
                    podaciOdabrano.remove(komponenta);
                }
            }
        }
    }
    
    //odabir komponente iz gornje tabele i podesavanje kolicine iste
    public void odaberiKomponentu(TableView tabelaFiltrirano, TableView tabelaOdabrano,
                                    ObservableList<KomponentaSaSlikom> podaciOdabrano) {
        //ocitavanje odabrane komponente
        KomponentaSaSlikom komponenta = (KomponentaSaSlikom) tabelaFiltrirano.getSelectionModel().getSelectedItem();
        if (komponenta == null) {
            return;
        }
        //kreiranje kolona tabele ukoliko vec nisu kreirane
        if (tabelaOdabrano.getColumns().isEmpty()) {
            kreirajTabelu(tabelaOdabrano);
        }
        //ukoliko komponenta ne postoji u tabeli sa odabranim komponentama - dodavanje
        if (!podaciOdabrano.contains(komponenta)) {
            //zadavanje kolicine
            SpinnerDialog spDialog=new SpinnerDialog();
            int kolicina=spDialog.display("Kolicina", "Odaberi kolicinu", komponenta.getKolicina(), 1);
            if (kolicina>0) {
                komponenta.setKolicina(kolicina);
                podaciOdabrano.add(komponenta);
                tabelaOdabrano.setItems(podaciOdabrano);
            }
        }
    }
    
    //preuzimanje filtriranih podataka iz baze podataka i smestanje u tabelu
    public void preuzmiPodatke(ComboBox tipCB, ComboBox proizvodjacCB, TextField deoNaziva, CheckBox aktuelneCB, 
                               ObservableList<KomponentaSaSlikom> podaciFiltrirano, TableView tabelaFiltrirano) {
        tabelaFiltrirano.getItems().clear();
        tabelaFiltrirano.getColumns().clear();
        try {
            DBUtil.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, podaciFiltrirano);
            //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
            if (tabelaFiltrirano.getColumns().isEmpty())
                kreirajTabelu(tabelaFiltrirano);
            //ucitavanje podataka u tabelu
            tabelaFiltrirano.setItems(podaciFiltrirano);
        } catch (SQLException ex) {
            Pomocne.poruka(ex.getMessage());
        }
    }
    
    //pozivanje metode kreiranja obe tabele
    public void kreirajTabele(TableView tFilt, TableView tOdab) {
        kreirajTabelu(tFilt);
        tFilt.setId("tabela-filtrirano");
        kreirajTabelu(tOdab);
        tFilt.setId("tabela-odabrano");
    }
    
    //kreiranje tabele
    private void kreirajTabelu(TableView tabela) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("id"));
        //definisanje procentualne sirine kolone
        kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("naziv"));
        kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 37); // 37% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("proizvodjac"));
        kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 23); // 23% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("tip"));
        kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 16); // 16% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("kolicina"));
        kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Double>("cena"));
        kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<KomponentaSaSlikom, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", value.doubleValue()));
                }
            }
        });
        //dodavanje kolona u tabelu
        tabela.getColumns().addAll(kolonaSifra, kolonaOpis, kolonaProizvodjac,
                                kolonaTip, kolonaKolicina, kolonaCena);
    }
    
    //realizacija prodaje
    public void realizacijaProdaje(ObservableList<KomponentaSaSlikom> podaciOdabrano, ComboBox kupacCB, Button nazadDugme) {
        if (!podaciOdabrano.isEmpty()) {
            if (kupacCB.getSelectionModel().getSelectedIndex() > -1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Prodaja robe");
                alert.setHeaderText("Azuriranje stanja u bazi podataka i stampanje fakture");
                alert.setContentText("Izaberi opciju");

                ButtonType dugmeAzuriraj = new ButtonType("Odobri prodaju robe");
                ButtonType dugmeAzurirajStampaj = new ButtonType("Odobri i stampaj");
                ButtonType dugmeOdustani = new ButtonType("Odustani", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(dugmeAzuriraj, dugmeAzurirajStampaj, dugmeOdustani);
                Platform.runLater(() -> {
                    Optional<ButtonType> result = alert.showAndWait();
                    int a = 1;
                    if (result.get() != dugmeOdustani) {
                        Osoba kupac=new Osoba();
                        Dokument faktura=new Dokument();
                        //pokusaj azuriranja kolicina
                        String[] podaciKupca=((String) kupacCB.getSelectionModel().getSelectedItem()).split(",");
                        boolean uspesno=prodajaDAO.prodajaRobe(podaciKupca, kupac, faktura, podaciOdabrano);
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Obaveštenje");
                        alert1.setHeaderText(null);
                        if (uspesno) {
                            alert1.setContentText("Prodaja robe uspešno obavljena!");
                            if (result.get() == dugmeAzurirajStampaj) {
                                try {
                                    DocxExport.exportData(false, kupac, faktura, podaciOdabrano);
                                    alert1.setContentText("Prodaja robe uspešno obavljena uz uspešno pravljenje fakture!");
                                } catch (Exception ex) {
                                    Pomocne.poruka(ex.getMessage());
                                }
                            }
                        } else {
                            alert1.setContentText("Došlo je do greške pri prodaji!");
                        }
                        Platform.runLater(() -> {
                            alert1.showAndWait();
                            nazadDugme.fire();
                        });
                    } else {
                        alert.close();
                    }
                });
            } else 
                Pomocne.poruka("Kupac mora biti odabran!");
        } else 
            Pomocne.poruka("Nijedna komponenta nije odabrana!");
    }



}
