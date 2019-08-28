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
import com.grupa1.model.KomponentaSvaPolja;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import util.DocxExport;

public class Nabavka extends Application {
    //objekat za konekciju sa bazom podataka
    Connection conn;
    
    //Liste za pracenje combobox-a
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ObservableList opcijeDobavljac = FXCollections.observableArrayList();
    //Kreiranje 3 ComboBox-a na formi
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);
    ComboBox dobavljacCB = new ComboBox(opcijeDobavljac);
    
    //Liste za pracenje informacija u tabeli
    ObservableList<Komponenta> podaciFiltrirano=FXCollections.observableArrayList();;
    ObservableList<Komponenta> podaciOdabrano=FXCollections.observableArrayList();;
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume (filtraciju)
    TableView tabelaFiltrirano = new TableView();
    //tabela za prikaz odabranih komponenata
    TableView tabelaOdabrano = new TableView();;
    
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button dodavanjeDugme = new Button("Dodaj komponentu");
    Button prihvatiDugme = new Button("Prihvati");
    //Button prihvatiIObrazacDugme = new Button("Prihvati i eksportuj");
    Button nazadDugme = new Button("Nazad");
    Button dobavljacDugme = new Button("Dodaj dobavljača");
    //Button novaKomponentaPotvrda = new Button("Potvrdi");
    //Button novaKomponentaNazad = new Button("Nazad");
    Button noviDobavljacPotvrda = new Button("Dodaj");
    Button noviDobavljacNazad = new Button("Nazad");
    
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Nabavka");
    Label labelFiltriraneKomponente = new Label("DUPLIM KLIKOM ODABERITE ŽELJENU KOMPONENTU");
    Label labelOdabraneKomponente = new Label("Odabrane komponente");
    //Label novaKomponentaLabel = new Label("Dodavanje komponente");
    Label noviDobavljacNaslov = new Label("Dodavanje dobavljača");
    CheckBox aktuelneCB = new CheckBox();
    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    VBox headerHB = new VBox();
    VBox boxZaTabele = new VBox();
    
    //Kreiranje Fonta
    Font font = new Font(25);
    //Kreiranje TextFiedla za novu komponentu i novog dobavljaca
    TextField novaKomponentaNaziv = new TextField();
    TextField novaKomponentaProizvodjac = new TextField();
    TextField novaKomponentaTip = new TextField();
    TextField novaKomponentaKolicina = new TextField();
    TextField novaKomponentaCena = new TextField();
    TextField novaKomponentaSlika= new TextField();
    RadioButton novaKomponentaAktuletno = new RadioButton();
    Label novaKomponentaAktuelnoLabel = new Label("Aktuelno:");
    
    TextField noviDobavljacNaziv = new TextField();
    TextField noviDobavljacUlica = new TextField();
    TextField noviDobavljacBroj = new TextField();
    TextField noviDobavljacGrad = new TextField();
    TextField noviDobavljacPostBroj = new TextField();
    TextField noviDobavljacDrzava = new TextField();
    TextField noviDobavljacTelefon = new TextField();
    
    private Statement st=null;
    private int prijemnicaId;
    Scene scene=null;
    
    @Override
    public void init() throws Exception {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        kreirajTabelu(tabelaFiltrirano);
        tabelaFiltrirano.setId("tabela-filtrirano");
        kreirajTabelu(tabelaOdabrano);
        tabelaFiltrirano.setId("tabela-odabrano");
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvođača");
        proizvodjacCB.setMinSize(150, 25);
        dobavljacCB.setPromptText("Izaberi dobavljača");
        dobavljacCB.setMinSize(150, 25);
        
        //podesavanja za dugme za pretragu
        pretragaDugme.setMinSize(100, 25);
        pretragaDugme.setId("pretragaButton");
        pretragaDugme.setDefaultButton(true);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        comboboxHB.setAlignment(Pos.BOTTOM_LEFT);
        comboboxHB.setId("headerBackground");
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.setMinSize(1000, 100);
        
        deoNaziva.setPromptText("Pretraga");
        
        naslovForme.setTranslateY(-30);
        naslovForme.setTranslateX(-230);
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");
        
        //dodavanje nodova u HBox
        comboboxHB.getChildren().addAll(tipCB, proizvodjacCB, deoNaziva, pretragaDugme,naslovForme);
        
        //###izbaciti opisiCB i comboboxHB i ostaviti samo headerHB
        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.getChildren().addAll(opisiCB, comboboxHB);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        tabelaFiltrirano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaFiltrirano.setMaxSize(800, 250);
        boxZaTabele.setPadding(new Insets(10));
        
        //boxZaTabele.setSpacing(30);
        tabelaOdabrano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOdabrano.setMaxSize(800, 250);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente,tabelaFiltrirano,labelOdabraneKomponente, tabelaOdabrano);
        
        
        //###podesi velicinu Prihvati i stampaj dugmeta
        //podesavanje velicine,pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        prihvatiDugme.setId("buttonStyleNabavka");
        //prihvatiIObrazacDugme.setId("buttonStyle");
        nazadDugme.setId("buttonStyleNabavka");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 410));
        footerHB.setMargin(prihvatiDugme, new Insets(0, 0, 0, 350));
        //footerHB.setMargin(prihvatiIObrazacDugme, new Insets(0, 0, 0, 20));
        //footerHB.getChildren().addAll(prihvatiDugme, prihvatiIObrazacDugme,nazadDugme);
        footerHB.getChildren().addAll(prihvatiDugme, nazadDugme);
        
        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        dodavanjeDugme.setId("buttonStyleNabavka");
        dodavanjeDugme.setMinSize(150, 25);
        desniVB.getChildren().add(dodavanjeDugme);
        desniVB.setAlignment(Pos.TOP_CENTER); //.TOP_LEFT);
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(30);
        dobavljacDugme.setId("buttonStyleNabavka");
        dobavljacDugme.setMinSize(150, 25);
        aktuelneCB.setSelected(true);
        aktuelneCB.setText("Samo aktuelne");
        desniVB.getChildren().addAll(dobavljacCB, dobavljacDugme, aktuelneCB);
        
        //popunjavanje combobox-eva podacima
        ispuniComboBoxZaTip();
        ispuniComboBoxZaProizvodjaca();
        ispuniComboBoxZaDobavljaca();
        
        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            try {
                tabelaFiltrirano.getItems().clear();
                tabelaFiltrirano.getColumns().clear();
                conn=DBUtil.napraviKonekciju();
                preuzmiPodatke();
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'komponenta'!");
            }
        });
        
        //dvostruki klik na gornju tabelu - dodavanje komponente u donju tabelu
        tabelaFiltrirano.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //detektovanje dvostrukog klika levim dugmetom
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                    //ocitavanje odabrane komponente
                    Komponenta komponenta = (Komponenta) tabelaFiltrirano.getSelectionModel().getSelectedItem();
                    if (komponenta==null) return;
                    //kreiranje kolona tabele ukoliko vec nisu kreirane
                    if (tabelaOdabrano.getColumns().size()==0)
                        kreirajTabelu(tabelaOdabrano);
                    
                    //ukoliko komponenta ne postoji u tabeli sa odabranim komponentama - dodavanje
                    if (!podaciOdabrano.contains(komponenta)) {
                        //zadavanje kolicine
                        int kolicina=SpinnerDialog.display("Kolicina", "Odaberi kolicinu", Integer.MAX_VALUE, 1);
                        if (kolicina>0) {
                            komponenta.setKolicina(kolicina);
                            podaciOdabrano.add(komponenta);
                            tabelaOdabrano.setItems(podaciOdabrano);
                        }
                    }
                }
            }
        });
    
        //dvostruki klik na donju tabelu - azuriranje kolicine komponente u donjoj tabeli
        tabelaOdabrano.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //detektovanje dvostrukog klika levim dugmetom
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                    //ukoliko ima upisanih podataka u tabeli tabelaOdabrano
                    if (tabelaOdabrano.getColumns().size()>0) {
                        //ocitavanje odabrane komponente
                        Komponenta komponenta = (Komponenta) tabelaOdabrano.getSelectionModel().getSelectedItem();
                        if (komponenta==null) return;
                        //zadavanje kolicine
                        int kolicina=SpinnerDialog.display("Kolicina", "Odaberi kolicinu", Integer.MAX_VALUE, komponenta.getKolicina());
                        if (kolicina>0) {
                            //korekcija kolicine u listi
                            int indeks=podaciOdabrano.indexOf(komponenta);
                            komponenta.setKolicina(kolicina);
                            podaciOdabrano.set(indeks,komponenta);
                        }
                        else 
                            podaciOdabrano.remove(komponenta);
                    }
                }
            }
        });
        
        //dugme za azuriranje baze i stampanje prijemnice
        prihvatiDugme.setOnAction(e->{
            if (podaciOdabrano.size()!=0) {
                if (dobavljacCB.getSelectionModel().getSelectedIndex()>-1) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Prijem robe");
                    alert.setHeaderText("Ažuriranje stanja u bazi podataka i štampanje prijemnice");
                    alert.setContentText("Izaberi opciju");
                    
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
                    dialogPane.getStyleClass().add("dialogPane");

                    ButtonType dugmeAzuriraj = new ButtonType("Odobri prijem robe");
                    
                    ButtonType dugmeAzurirajStampaj = new ButtonType("Odobri i štampaj");
                    ButtonType dugmeOdustani = new ButtonType("Odustani", ButtonData.CANCEL_CLOSE);
                    
                    //alert box za odabir opcije
                    alert.getButtonTypes().setAll(dugmeAzuriraj, dugmeAzurirajStampaj, dugmeOdustani);
                    Platform.runLater(()->{
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() != dugmeOdustani){
                            Osoba dobavljac=new Osoba();
                            Dokument prijemnica=new Dokument();
                            //azuriranje kolicina i pravljenje prijemnice
                            boolean uspesno=nabavkaRobe(dobavljac, prijemnica);
                            Alert alert1 = new Alert(AlertType.INFORMATION);
                            alert1.setTitle("Obavestenje");
                            alert1.setHeaderText(null);
                            if (uspesno) {
                                alert1.setContentText("Roba uspešno uneta u bazu podataka!");
                                if (result.get() == dugmeAzurirajStampaj) {
                                    try {
                                        DocxExport.exportData(true, dobavljac, prijemnica, podaciOdabrano);
                                    } catch (Exception ex) {
                                        Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            else
                                alert1.setContentText("Došlo je do greške pri unosu podataka!");
                            Platform.runLater( () -> {
                                alert1.showAndWait();
                                nazadDugme.fire();
                            });
                            
                        } else {
                            alert.close();
                        } 
                    });
                } else {
                    poruka("Dobavljač mora biti odabran!");
                }
            } else {
                poruka("Nijedna komponenta nije odabrana!");
            }
            
        });
        
        
        
        //dugme za kreiranje nove komponente
        dodavanjeDugme.setOnAction(e ->{
            KomponentaSvaPolja komponenta=new KomponentaSvaPolja();
            boolean dodata=NovaKomponenta.display(komponenta);
            /*
            if (dodata) {
                komponenta.setNaziv(NovaKomponenta.novaKomponentaNaziv.getText());
                komponenta.setProizvodjac(NovaKomponenta.novaKomponentaProizvodjac.getText());
                //String naziv=(String) dobavljacCB.getSelectionModel().getSelectedItem();
                komponenta.setTip(NovaKomponenta.novaKomponentaTip.getText());
                komponenta.setKolicina(Integer.valueOf(NovaKomponenta.novaKomponentaKolicina.getText()));
                komponenta.setCena(Double.valueOf(NovaKomponenta.novaKomponentaCena.getText()));
                komponenta.setSlika(NovaKomponenta.novaKomponentaSlika.getText());
                komponenta.setAktuelna(NovaKomponenta.novaKomponentaAktuletno.getText().equals("true"));

                PreparedStatement stmt0=null, stmt1=null, stmt2=null, stmt3=null;
                
                int proizvodjacId=0, tipId=0, upisano=0;
                try {
                    
                    stmt0=conn.prepareStatement("SELECT proizvodjac_id FROM proizvodjac WHERE naziv='?'");
                    stmt0.setString(1, komponenta.getProizvodjac());
                    ResultSet rs=stmt0.executeQuery();
                    if (rs.next()) {
                        proizvodjacId=rs.getInt(1);
                    }
                    stmt1=conn.prepareStatement("SELECT tip_id FROM tip WHERE naziv='?'");
                    stmt1.setString(1, komponenta.getTip());
                    ResultSet rs1=stmt1.executeQuery();
                    if (rs1.next()) {
                        tipId=rs1.getInt(1);
                    }
                    
                    
                    stmt2=conn.prepareStatement("INSERT INTO komponenta (naziv, proizvodjac_id, tip_id, kolicina, cena, slika, aktuelna) "
                            + "VALUES (?,?,?,?,?,?,?)");
                    
                    stmt2.setString(1, NovaKomponenta.novaKomponentaNaziv.getText());
                    stmt2.setInt(2, proizvodjacId);
                    stmt2.setInt(3, tipId);
                    stmt2.setInt(4, komponenta.getKolicina());
                    stmt2.setDouble(4, komponenta.getCena());
                    stmt2.setString(5, komponenta.getSlika());
                    stmt2.setBoolean(6, komponenta.getAktuelna());
                    upisano=stmt2.executeUpdate();
                
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
                Alert alert1 = new Alert(AlertType.INFORMATION);
                alert1.setTitle("Obavestenje");
                alert1.setHeaderText(null);
                if (upisano>0) 
                    alert1.setContentText("Komponenta uspešno uneta u bazu podataka!");
                else
                    alert1.setContentText("Došlo je do greške pri unosu podataka!");
                Platform.runLater( () -> {
                    alert1.showAndWait();
                });
            }
            */
            /*
            HBox headerDodajBox = new HBox();
            headerDodajBox.setId("headerBackground");
            headerDodajBox.setAlignment(Pos.CENTER);
            novaKomponentaLabel.setFont(font);
            novaKomponentaLabel.setId("headerLabel");
            headerDodajBox.getChildren().add(novaKomponentaLabel);
            headerDodajBox.setMinSize(500, 80);
            HBox centerDodajBox = new HBox();
            centerDodajBox.setId("bottomStyle");
            centerDodajBox.setAlignment(Pos.CENTER);
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.TOP_CENTER);
            grid.setVgap(20);
            grid.setHgap(20);
            //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
            novaKomponentaPotvrda.setId("buttonStyle");
            novaKomponentaNazad.setId("buttonStyle");
            novaKomponentaPotvrda.setMinSize(100, 25);
            novaKomponentaNazad.setMinSize(100, 25);
            HBox.setMargin(grid, new Insets(40, 0, 0, 0));
            novaKomponentaNaziv.setPromptText("Naziv");
            novaKomponentaProizvodjac.setPromptText("Novi proizvodjač");
            novaKomponentaTip.setPromptText("Tip");
            novaKomponentaKolicina.setPromptText("Količina");
            novaKomponentaCena.setPromptText("Cena");
            novaKomponentaSlika.setPromptText("Slika");
            grid.add(novaKomponentaNaziv, 0, 0);
            grid.add(novaKomponentaProizvodjac, 0, 1);
            grid.add(novaKomponentaTip, 0, 2);
            grid.add(novaKomponentaKolicina, 1, 0);
            grid.add(novaKomponentaCena, 1, 1);
            grid.add(novaKomponentaSlika, 1, 2);
            grid.add(novaKomponentaAktuelnoLabel, 0, 3);
            grid.add(novaKomponentaAktuletno, 1, 3);
            grid.add(novaKomponentaPotvrda, 2, 2);
            grid.add(novaKomponentaNazad, 2, 3);
            centerDodajBox.getChildren().addAll(grid);
            HBox footerDodajBox = new HBox();
            BorderPane dodajBox = new BorderPane();
            dodajBox.setTop(headerDodajBox);
            dodajBox.setCenter(centerDodajBox);
            dodajBox.setBottom(footerDodajBox);
           
            //Kreiranje scene za novu komponentu
            Scene scena = new Scene(dodajBox, 500, 350); 
            Stage noviProzor=new Stage();
            noviProzor.setResizable(false);
            noviProzor.setTitle("Dodavanje Komponente");
            scena.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
            noviProzor.setScene(scena);
            noviProzor.initModality(Modality.WINDOW_MODAL);
            noviProzor.initOwner(primaryStage);
            //aktiviranje i prikaz novog prozora
            noviProzor.show();
            
            //povratak na nabavku sa forme za unosenje nove komponente
            novaKomponentaNazad.setOnAction(ev ->{
                noviProzor.close();
            });
            */
        });
        
        
        
        //Taster za Scenu dodavanja novog dobavljaca
        dobavljacDugme.setOnAction(e ->{
            HBox headerDodajBox = new HBox();
            headerDodajBox.setId("headerBackground");
            headerDodajBox.setAlignment(Pos.CENTER);
            noviDobavljacNaslov.setFont(font);
            noviDobavljacNaslov.setId("headerLabel");
            headerDodajBox.getChildren().add(noviDobavljacNaslov);
            headerDodajBox.setMinSize(500, 80);
            HBox centerDodajBox = new HBox();
            centerDodajBox.setId("bottomStyle");
            centerDodajBox.setAlignment(Pos.CENTER);
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.TOP_CENTER);
            grid.setVgap(20);
            grid.setHgap(20);
            //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
            noviDobavljacPotvrda.setId("buttonStyle");
            noviDobavljacNazad.setId("buttonStyle");
            noviDobavljacPotvrda.setMinSize(100, 25);
            noviDobavljacNazad.setMinSize(100, 25);
            HBox.setMargin(grid, new Insets(40, 0, 0, 0));
            noviDobavljacNaziv.setPromptText("Naziv");
            noviDobavljacUlica.setPromptText("Ulica");
            noviDobavljacBroj.setPromptText("Broj");
            noviDobavljacGrad.setPromptText("Grad");
            noviDobavljacPostBroj.setPromptText("Poštanski Broj");
            noviDobavljacDrzava.setPromptText("Država");
            noviDobavljacTelefon.setPromptText("Telefon");
            grid.add(noviDobavljacNaziv, 0, 0);
            grid.add(noviDobavljacUlica, 0, 1);
            grid.add(noviDobavljacBroj, 0, 2);
            grid.add(noviDobavljacGrad, 1, 0);
            grid.add(noviDobavljacPostBroj, 1, 1);
            grid.add(noviDobavljacDrzava, 1, 2);
            grid.add(noviDobavljacTelefon, 0, 3);
            grid.add(noviDobavljacPotvrda, 1, 3);
            grid.add(noviDobavljacNazad, 2, 3);
            centerDodajBox.getChildren().addAll(grid);
            HBox footerDodajBox = new HBox();
            BorderPane dodajBox = new BorderPane();
            dodajBox.setTop(headerDodajBox);
            dodajBox.setCenter(centerDodajBox);
            dodajBox.setBottom(footerDodajBox);
            
           
            //Kreiranje scene za novog dobavljaca
            Scene scena = new Scene(dodajBox, 500, 350); 
            Stage noviProzor=new Stage();
            noviProzor.setResizable(false);
            noviProzor.setTitle("Dodavanje Komponente");
            scena.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
            noviProzor.setScene(scena);
            noviProzor.initModality(Modality.WINDOW_MODAL);
            noviProzor.initOwner(primaryStage);
            //aktiviranje i prikaz novog prozora
            noviProzor.show();
        });
        
        //Taster prozora novi dobavljac NAZAD
        noviDobavljacNazad.setOnAction(e ->{
            try {
                primaryStage.close();
                new Nabavka().start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        
        
        //Taster prozora Nabavke za nazad
        nazadDugme.setOnAction(e ->{
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
        scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Nabavka - UNDP Offline Store");
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
        Platform.runLater( ()-> alert.showAndWait() );
    }

    private boolean nabavkaRobe(Osoba dobavljac, Dokument prijemnica) {
        
        boolean uspesno=false;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                //upit za nalazenje dobavljac_id vrednosti za zadat naziv dobavljaca
                PreparedStatement stmt0=conn.prepareStatement("SELECT dobavljac_id, naziv, ulica, broj, grad, postanski_broj, "
                        + "drzava, telefon FROM dobavljac WHERE naziv=?");
                String str1=(String) dobavljacCB.getSelectionModel().getSelectedItem();
                stmt0.setString(1, str1);
                ResultSet rs=stmt0.executeQuery();
                //int dobavljacId=-1;
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
                            Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return uspesno;
    }



    
    //ucitavanje vrednosti u ComboBox tipa
    public void ispuniComboBoxZaTip() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip");
                while (rs.next()) {
                    opcijeTip.add(rs.getString("naziv"));
                }
                //opcijeTip.addAll(DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip"));
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje vrednosti u ComboBox proizvodjaca
    public void ispuniComboBoxZaProizvodjaca() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM proizvodjac");
                while (rs.next()) {
                    opcijeProizvodjac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    //ucitavanje vrednosti u ComboBox dobavljaca
    public void ispuniComboBoxZaDobavljaca() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM dobavljac");
                while (rs.next()) {
                    opcijeDobavljac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Nabavka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void preuzmiPodatke() throws SQLException {
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

            String upit="SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena " +
                        "FROM komponenta as k " +
                        "INNER JOIN tip as t ON k.tip_id=t.tip_id " +
                        "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id " + uslov + uslovAktuelne;
            //postavljanje upita nad bazom podataka
            ResultSet rs=DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs!=null) {
                try {
                    podaciFiltrirano = FXCollections.observableArrayList();
                    //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                    if (tabelaFiltrirano.getColumns().size()==0)
                        kreirajTabelu(tabelaFiltrirano);
                    //kreiranje ObservableList-e u koju se smestaju podaci za tabelu        
                    //ObservableList<Komponenta> podaciTabele = FXCollections.observableArrayList();
                    //ubacivanje podataka u listu
                    while (rs.next()) {
                        //kreiranje komponente na osnovu podataka ocitanih iz baze
                        Komponenta kompon=new Komponenta(rs.getInt(1), rs.getString(2),
                                                            rs.getString(3), rs.getString(4), 
                                                            rs.getInt(5), rs.getDouble(6));
                        //dodavanje komponente u listu
                        podaciFiltrirano.add(kompon);
                    }
                    //ucitavanje podataka u tabelu
                    tabelaFiltrirano.setItems(podaciFiltrirano);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn!=null) conn.close();
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
