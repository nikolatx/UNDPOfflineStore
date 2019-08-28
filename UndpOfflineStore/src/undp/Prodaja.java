package undp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.grupa1.dbconnection.*;
import com.grupa1.model.Dokument;
import com.grupa1.model.Komponenta;
import com.grupa1.model.Osoba;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import util.DocxExport;

public class Prodaja extends Application {

    //objekat za konekciju sa bazom podataka
    Connection conn;

    //Liste sa opcijama ComboBox-ova
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ObservableList opcijeKupac = FXCollections.observableArrayList();

    //Kreiranje 3 ComboBox-a na formi
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);
    ComboBox kupacCB = new ComboBox(opcijeKupac);

    //Liste za pracenje informacija u tabelama
    ObservableList<Komponenta> podaciFiltrirano = FXCollections.observableArrayList();
    ObservableList<Komponenta> podaciOdabrano = FXCollections.observableArrayList();
    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabelaFiltrirano = new TableView();
    //tabela za prikaz odabranih komponenata
    TableView tabelaOdabrano = new TableView();
    
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();
    
    //kontrole za odabir samo aktuelnih komponenata
    Label aktuelneLabel = new Label("Samo aktuelne");
    CheckBox aktuelneCB = new CheckBox();
    
    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button prihvatiDugme = new Button("Prihvati");
    Button nazadDugme = new Button("Nazad");
    Button kupacDugme = new Button("Dodaj kupca");
    Button noviKupacPotvrda = new Button("Dodaj");
    Button noviKupacNazad = new Button("Nazad");
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Prodaja");
    Label labelFiltriraneKomponente = new Label("Rezultat pretrage - DUPLIM KLIKOM ODABERITE ŽELJENU KOMPONENTU");
    Label labelOdabraneKomponente = new Label("Odabrane komponente");
    Label noviKupacNaslov = new Label("Dodavanje kupca");
    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    VBox headerHB = new VBox();
    VBox boxZaTabele = new VBox();

    //Kreiranje Fonta
    Font font = new Font(25);

    //kreiranje pomocnih promenljivih koje ce da se koriste u lambda izrazima
    private Statement st = null;
    private int prijemnicaId;

    @Override
    public void init() throws Exception {
        super.init();
        kreirajTabelu(tabelaFiltrirano);
        kreirajTabelu(tabelaOdabrano);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvodjača");
        proizvodjacCB.setMinSize(150, 25);
        kupacCB.setPromptText("Izaberi kupca");
        kupacCB.setMinSize(150, 25);

        //podesavanja dugmeta za pretragu
        pretragaDugme.setMinSize(100, 25);
        pretragaDugme.setId("pretragaButton");
        pretragaDugme.setDefaultButton(true);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        comboboxHB.setAlignment(Pos.BOTTOM_LEFT);
        comboboxHB.setId("headerBackground");
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.setMinSize(1000, 100);

        //podesavanje polja za unos dela naziva komponente za pretragu
        deoNaziva.setPromptText("Prodaja");

        //podesavanje naslova forme
        naslovForme.setTranslateY(-30);
        naslovForme.setTranslateX(-230);
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");

        //dodavanje nodova u HBox
        comboboxHB.getChildren().addAll(tipCB, proizvodjacCB, deoNaziva, pretragaDugme, naslovForme);

        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.getChildren().addAll(opisiCB, comboboxHB);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        tabelaFiltrirano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaFiltrirano.setMaxSize(800, 250);
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setPadding(new Insets(10));

        //boxZaTabele.setSpacing(30);
        tabelaOdabrano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOdabrano.setMaxSize(800, 250);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente, tabelaFiltrirano, labelOdabraneKomponente, tabelaOdabrano);

        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setId("bottomStyle");
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        prihvatiDugme.setId("buttonStyle");
        nazadDugme.setId("buttonStyle");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 320));
        footerHB.setMargin(prihvatiDugme, new Insets(0, 0, 0, 250));

        footerHB.getChildren().addAll(prihvatiDugme, nazadDugme);
        
        //prikaz aktuelnih komponenata
        aktuelneCB.setSelected(true);
        aktuelneCB.setText("Samo aktuelne");
        
        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        desniVB.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(kupacCB, new Insets(20, 40, 0, 0));
        VBox.setMargin(kupacDugme, new Insets(0, 40, 0, 0));
        VBox.setMargin(aktuelneLabel, new Insets(0, 40, 0, 0));
        VBox.setMargin(aktuelneCB, new Insets(0, 40, 0, 0));
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(20);
        kupacDugme.setMinSize(150, 25);
        kupacDugme.setId("buttonStyle");
        desniVB.setId("bottomStyle");
        desniVB.getChildren().addAll(kupacCB, kupacDugme, aktuelneCB);

        //popunjavanje combobox-eva podacima
        ispuniComboBoxZaTip();
        ispuniComboBoxZaProizvodjaca();
        ispuniComboBoxZaKupca();

        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            try {
                tabelaFiltrirano.getItems().clear();
                tabelaFiltrirano.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                preuzmiPodatke();
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'komponenta'!");
            }
        });

        //dvostruki klik na vrstu gornje tabele - dodavanje komponente u donju tabelu
        tabelaFiltrirano.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //detektovanje dvostrukog klika levim dugmetom
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    //ocitavanje odabrane komponente
                    Komponenta komponenta = (Komponenta) tabelaFiltrirano.getSelectionModel().getSelectedItem();
                    if (komponenta == null) {
                        return;
                    }
                    //kreiranje kolona tabele ukoliko vec nisu kreirane
                    if (tabelaOdabrano.getColumns().size() == 0) {
                        kreirajTabelu(tabelaOdabrano);
                    }

                    //ukoliko komponenta ne postoji u tabeli sa odabranim komponentama - dodavanje
                    if (!podaciOdabrano.contains(komponenta)) {

                        Button dugmePotvrdi = new Button("Potvrdi");
                        Button dugmeOdustani = new Button("Odustani");
                        dugmePotvrdi.setId("buttonStyle");
                        dugmeOdustani.setId("buttonStyle");
                        //boxovi za smestaj kontrola na sceni
                        VBox rootBox = new VBox();
                        HBox gornjiHBox = new HBox();
                        HBox donjiHBox = new HBox();

                        Label labela1 = new Label("Unesi željenu kolicinu:");
                        labela1.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
                        //spinner-om se vrsi odabir kolicine komponente koja se dodaje
                        Spinner spinner = new Spinner();
                        spinner.setId("buttonStyleNabavka");

                        //ocitaj raspolozivu kolicinu komponente
                        komponenta.setKolicina(DBUtil.preuzmiAktuelnoStanje(komponenta.getId()));

                        //podesavanje granicnih vrednosti spinnera i podesene vrednosti
                        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, komponenta.getKolicina(), 1));

                        //dodavanje u gornji red labele i spinnera
                        gornjiHBox.getChildren().addAll(labela1, spinner);
                        gornjiHBox.setAlignment(Pos.CENTER);
                        //dodavanje u donji red dugmica
                        donjiHBox.getChildren().addAll(dugmePotvrdi, dugmeOdustani);
                        donjiHBox.setAlignment(Pos.CENTER);
                        //ubacivanje komponenti u glavni VBox a zatim i u novu scenu
                        rootBox.getChildren().addAll(gornjiHBox, donjiHBox);
                        Scene scena = new Scene(rootBox, 350, 130);
                        scena.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
                        //podesavanje novog prozora
                        Stage noviProzor = new Stage();
                        noviProzor.setTitle("Odabir količine");
                        noviProzor.setScene(scena);
                        noviProzor.initModality(Modality.WINDOW_MODAL);
                        noviProzor.initStyle(StageStyle.UNDECORATED);
                        noviProzor.initOwner(primaryStage);
                        //aktiviranje i prikaz novog prozora
                        noviProzor.show();

                        //dugme potvrdi - promena kolicine komponente i ubacivanje u listu odabranih
                        dugmePotvrdi.setOnAction(e -> {
                            int kolicina = (Integer) spinner.getValue();
                            if (kolicina > 0) {
                                komponenta.setKolicina(kolicina);
                                podaciOdabrano.add(komponenta);
                                tabelaOdabrano.setItems(podaciOdabrano);
                            }
                            noviProzor.close();
                        });

                        //zatvranje novog prozora ukoliko je odabrano dugme odustani
                        dugmeOdustani.setOnAction(e -> noviProzor.close());
                    }
                }
            }
        });

        //dvostruki klik na donju tabelu - azuriranje kolicine komponente u donjoj tabeli
        tabelaOdabrano.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //detektovanje dvostrukog klika levim dugmetom
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {

                    //ukoliko ima upisanih podataka u tabeli tabelaOdabrano
                    if (tabelaOdabrano.getColumns().size() > 0) {
                        //ocitavanje odabrane komponente
                        Komponenta komponenta = (Komponenta) tabelaOdabrano.getSelectionModel().getSelectedItem();
                        if (komponenta == null) {
                            return;
                        }
                        //preuzimanje aktuelne kolicine odabrane komponente
                        int maksKolicina = DBUtil.preuzmiAktuelnoStanje(komponenta.getId());
                        if (maksKolicina > 0) {
                            //otvaranje novog prozora za izmenu kolicine odabrane komponente
                            Button dugmePotvrdi = new Button("Potvrdi");
                            Button dugmeOdustani = new Button("Odustani");
                            //boxovi za smestaj kontrola na sceni
                            VBox rootBox = new VBox();
                            HBox gornjiHBox = new HBox();
                            HBox donjiHBox = new HBox();

                            Label labela1 = new Label("Unesi željenu količinu:");
                            //spinner-om se vrsi odabir kolicine komponente
                            Spinner spinner = new Spinner();
                            //podesavanje granicnih vrednosti spinnera i podesene vrednosti
                            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maksKolicina, komponenta.getKolicina()));

                            //dodavanje u gornji red labele i spinnera
                            gornjiHBox.getChildren().addAll(labela1, spinner);
                            //dodavanje u donji red dugmica
                            donjiHBox.getChildren().addAll(dugmePotvrdi, dugmeOdustani);
                            //ubacivanje komponenti u glavni VBox a zatim i u novu scenu
                            rootBox.getChildren().addAll(gornjiHBox, donjiHBox);
                            Scene scena = new Scene(rootBox, 350, 150);

                            //podesavanje novog prozora
                            Stage noviProzor = new Stage();
                            noviProzor.setTitle("Korekcija količine");
                            noviProzor.setScene(scena);
                            noviProzor.initModality(Modality.WINDOW_MODAL);
                            noviProzor.initOwner(primaryStage);
                            //aktiviranje i prikaz novog prozora
                            noviProzor.show();

                            //dugme potvrdi - promena kolicine komponente i ubacivanje u listu odabranih
                            dugmePotvrdi.setOnAction(e -> {
                                int kolicina = (Integer) spinner.getValue();
                                if (kolicina > 0) {
                                    int indeks = podaciOdabrano.indexOf(komponenta);
                                    komponenta.setKolicina(kolicina);
                                    podaciOdabrano.set(indeks, komponenta);
                                } else {
                                    podaciOdabrano.remove(komponenta);
                                }
                                noviProzor.close();
                            });

                            //zatvranje novog prozora ukoliko je odabrano dugme odustani
                            dugmeOdustani.setOnAction(e -> noviProzor.close());
                        } else {
                            podaciOdabrano.remove(komponenta);
                        }
                    }
                }
            }
        });

        //dugme za azuriranje baze i stampanje dopremnice/fakture
        prihvatiDugme.setOnAction(e -> {
            if (podaciOdabrano.size() != 0) {
                if (kupacCB.getSelectionModel().getSelectedIndex() > -1) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Prodaja robe");
                    alert.setHeaderText("Azuriranje stanja u bazi podataka i stampanje fakture");
                    alert.setContentText("Izaberi opciju");

                    ButtonType dugmeAzuriraj = new ButtonType("Odobri prodaju robe");
                    ButtonType dugmeAzurirajStampaj = new ButtonType("Odobri i stampaj");
                    ButtonType dugmeOdustani = new ButtonType("Odustani", ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(dugmeAzuriraj, dugmeAzurirajStampaj, dugmeOdustani);
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = alert.showAndWait();
                        int a = 1;
                        if (result.get() != dugmeOdustani) {
                            Osoba kupac=new Osoba();
                            Dokument faktura=new Dokument();
                            //pokusaj azuriranja kolicina
                            boolean uspesno = prodajaRobe(kupac, faktura);
                            Alert alert1 = new Alert(AlertType.INFORMATION);
                            alert1.setTitle("Obaveštenje");
                            alert1.setHeaderText(null);
                            if (uspesno) {
                                alert1.setContentText("Prodaja robe uspešno obavljena!");
                                if (result.get() == dugmeAzurirajStampaj) {
                                    try {
                                        DocxExport.exportData(false, kupac, faktura, podaciOdabrano);
                                    } catch (Exception ex) {
                                        Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
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
                } else {
                    poruka("Kupac mora biti odabran!");
                }
            } else {
                poruka("Nijedna komponenta nije odabrana!");
            }

        });

        nazadDugme.setOnAction(e -> {
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });

        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(boxZaTabele);
        root.setBottom(footerHB);
        root.setRight(desniVB);

        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Prodaja - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void poruka(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Greška");
        alert.setContentText(msg);
        Platform.runLater(() -> alert.showAndWait());
    }

    //ayuriranje kolicina pri prodaji
    private boolean prodajaRobe(Osoba kupac, Dokument faktura) {

        boolean uspesno = false;
        conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                //upit za nalazenje kupca po nazivu
                PreparedStatement stmt0=conn.prepareStatement("SELECT kupac_id, naziv, ulica, broj, grad, postanski_broj, "
                        + "drzava, telefon FROM kupac WHERE naziv=?");
                String nazivKupca=(String) kupacCB.getSelectionModel().getSelectedItem();
                stmt0.setString(1, nazivKupca);
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
                        Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return uspesno;
    }

    //ucitavanje vrednosti u ComboBox tipa
    public void ispuniComboBoxZaTip() {
        ResultSet rs = null;
        conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                rs = DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip");
                while (rs.next()) {
                    opcijeTip.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //ucitavanje vrednosti u ComboBox proizvodjaca
    public void ispuniComboBoxZaProizvodjaca() {
        ResultSet rs = null;
        conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                rs = DBUtil.prikupiPodatke(conn, "SELECT naziv FROM proizvodjac");
                while (rs.next()) {
                    opcijeProizvodjac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //ucitavanje vrednosti u ComboBox kupca
    public void ispuniComboBoxZaKupca() {
        ResultSet rs = null;
        conn = DBUtil.napraviKonekciju();
        if (conn != null) {
            try {
                rs = DBUtil.prikupiPodatke(conn, "SELECT naziv FROM kupac");
                while (rs.next()) {
                    opcijeKupac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Prodaja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void preuzmiPodatke() throws SQLException {
        if (conn != null) {
            //uzimanje podataka iz combobox-eva i polja za unos teksta
            String tip = (String) tipCB.getSelectionModel().getSelectedItem();
            String proizvodjac = (String) proizvodjacCB.getSelectionModel().getSelectedItem();
            String komponenta = deoNaziva.getText();
            //formiranje SQL upita koji treba da vrati komponente koje zadovoljavaju
            //sve unete parametre (combobox-evi i uneti tekst za pretragu)
            if (tip == null) {
                tip = "";
            }
            if (proizvodjac == null) {
                proizvodjac = "";
            }
            String uslovAktuelne="";
            if (aktuelneCB.isSelected())
                uslovAktuelne=" AND k.aktuelna=1";
            String uslov = "";
            if (tip.equals("") && proizvodjac.equals("")) {
                uslov = "WHERE k.naziv like '%" + komponenta + "%'";
            } else if (tip.equals("")) {
                uslov = "WHERE p.naziv='" + proizvodjac + "' AND k.naziv like '%" + komponenta + "%'";
            } else if (proizvodjac.equals("")) {
                uslov = "WHERE t.naziv='" + tip + "' AND k.naziv like '%" + komponenta + "%'";
            } else {
                uslov = "WHERE t.naziv='" + tip + "' AND p.naziv='" + proizvodjac + "' AND k.naziv like '%" + komponenta + "%'";
            }

            String upit = "SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena "
                    + "FROM komponenta as k "
                    + "INNER JOIN tip as t ON k.tip_id=t.tip_id "
                    + "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id " + uslov + uslovAktuelne;
            //postavljanje upita nad bazom podataka
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs != null) {
                try {
                    podaciFiltrirano = FXCollections.observableArrayList();
                    //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                    if (tabelaFiltrirano.getColumns().size() == 0) {
                        kreirajTabelu(tabelaFiltrirano);
                    }
                    //ubacivanje podataka u listu
                    while (rs.next()) {
                        //dodavanje komponente u listu
                        podaciFiltrirano.add(new Komponenta(rs.getInt(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getInt(5), rs.getDouble(6)));
                    }
                    //ubacivanje podataka u tabelu
                    tabelaFiltrirano.setItems(podaciFiltrirano);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.close();
                    }
                }
            }
        }
    }

    private void kreirajTabelu(TableView tabela) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("id"));
        //definisanje procentualne sirine kolone
        kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("naziv"));
        kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 37); // 37% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("proizvodjac"));
        kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 23); // 23% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("tip"));
        kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 16); // 16% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("kolicina"));
        kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<Komponenta, Double>("cena"));
        kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<Komponenta, Number>() {
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

}
