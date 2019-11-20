package undp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.grupa1.dbconnection.*;
import com.grupa1.model.KomponentaSaSlikom;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import kontroleri.NabavkaKontroler;
import pomocne.Pomocne;
import pomocne.Tabela;

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
    ObservableList<KomponentaSaSlikom> podaciFiltrirano=FXCollections.observableArrayList();;
    ObservableList<KomponentaSaSlikom> podaciOdabrano=FXCollections.observableArrayList();;
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume (filtraciju)
    TableView tabelaFiltrirano = new TableView();
    //tabela za prikaz odabranih komponenata
    TableView tabelaOdabrano = new TableView();;
    
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button dodavanjeDugme = new Button("Dodaj komponentu");
    Button prihvatiDugme = new Button("Realizuj nabavku");
    Button nazadDugme = new Button("Odustani");
    Button dobavljacDugme = new Button("Dodaj dobavljača");
    Button ponistiDugme = new Button("Poništi");
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Nabavka");
    Label labelFiltriraneKomponente = new Label("DUPLIM KLIKOM ODABERITE ŽELJENU KOMPONENTU");
    Label labelOdabraneKomponente = new Label("Odabrane komponente");
    CheckBox aktuelneCB = new CheckBox();
    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox footerHB = new HBox();

    BorderPane desniBP = new BorderPane();
    HBox headerHB = new HBox();
    VBox boxZaTabele = new VBox();
    VBox filterVB = new VBox();
    VBox donjiVB = new VBox();
    HBox srednjiHB = new HBox();
    VBox dobavljacVB = new VBox();
    
    //Kreiranje Fonta
    Font font = new Font(25);
    
    Scene scene=null;
    
    NabavkaKontroler kontroler=new NabavkaKontroler();
    NabavkaDAO nabavkaDAO=new NabavkaDAO();
    boolean tipOdabran, proizvodjacOdabran;
    
    @Override
    public void init() throws Exception {
        super.init(); 
        Tabela.kreirajTabelu(tabelaFiltrirano, false);
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
       
        //podesavanje header boxa
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");

        headerHB.setAlignment(Pos.CENTER);
        headerHB.setId("headerBackground");
        headerHB.setPadding(new Insets(10));
        headerHB.setMinSize(1000, 100);
        headerHB.getChildren().add(naslovForme);
        

        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvođača");
        proizvodjacCB.setMinSize(150, 25);
        
        //checkbox za aktuelne komponente
        aktuelneCB.setSelected(true);
        aktuelneCB.setText("Samo aktuelne");
        
        //polje za unos reci za pretragu
        deoNaziva.setPromptText("Pretraga");
        
        //podesavanja za dugme za pretragu
        pretragaDugme.setId("buttonStyleNabavka");
        pretragaDugme.setDefaultButton(true);
        
        //dugme za ponistavanje odabranih filtera
        ponistiDugme.setId("buttonStyleNabavka");
        
        //podesavanje velicine, pozicije i izgleda panela sa combobox-evima i dugmicima za filtriranje
        filterVB.getChildren().addAll(tipCB, proizvodjacCB, aktuelneCB, deoNaziva, pretragaDugme, ponistiDugme);
        filterVB.setSpacing(15);
        filterVB.setPadding(new Insets(10));
        filterVB.setAlignment(Pos.CENTER);
        
        //dugme za dodavanje nove komponente
        dodavanjeDugme.setId("buttonStyleNabavka");
        //combobox za odabir dobavljaca
        dobavljacCB.setPromptText("Izaberi dobavljača");
        //dugme za dodavanje novog dobavljaca
        dobavljacDugme.setId("buttonStyleNabavka");
        
        //podesavanje VBox-a za elemente vezane za dodavanje komponente i dobavljaca
        dobavljacVB.setSpacing(15);
        dobavljacVB.setPadding(new Insets(10));
        dobavljacVB.setAlignment(Pos.CENTER);
        dobavljacVB.getChildren().addAll(dodavanjeDugme, dobavljacCB, dobavljacDugme);
        
        //dugme za realizaciju nabavke
        prihvatiDugme.setId("buttonStyleNabavka");
        //dugme za odustajanje
        nazadDugme.setId("buttonStyleNabavka");
        //VBox za smestaj dva zadnja dugmeta
        donjiVB.setPadding(new Insets(10));
        donjiVB.setAlignment(Pos.CENTER);
        donjiVB.getChildren().addAll(prihvatiDugme, nazadDugme);
        
        
        //desni BorderPane sa filterima (gore), tabelama i delom za dobavljaca (sredina) i dugmicima za odustajanje i realizaciju (dole)
        desniBP.setId("bottomStyle");
        desniBP.setPadding(new Insets(10));
        desniBP.setTop(filterVB); 
        desniBP.setCenter(dobavljacVB);
        desniBP.setBottom(donjiVB);
        desniBP.setMinWidth(80);
        
        tabelaFiltrirano.setPlaceholder(new Label(""));
        tabelaOdabrano.setPlaceholder(new Label(""));
        
        //podesavanje VBoxa koji sadrzi tabele
        boxZaTabele.setSpacing(10);
        boxZaTabele.setPadding(new Insets(10));
        
        
        HBox.setHgrow(boxZaTabele, Priority.ALWAYS);
        HBox.setHgrow(srednjiHB, Priority.ALWAYS);
        
        
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente, tabelaFiltrirano, labelOdabraneKomponente, tabelaOdabrano );
        
        //dodavanje boxa za tabele i desnog BorderPane u srednji HBox
        srednjiHB.getChildren().addAll(boxZaTabele, desniBP);
        
        
        //podesavanje velicine,pozicije i izgleda donjeg boxa
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        
        
        //popunjavanje combobox-eva podacima
        kontroler.popuniComboBoxeve(opcijeTip, opcijeProizvodjac, opcijeDobavljac);
        
        //dodavanje boxeva u glavni VBox
        VBox root = new VBox();
        root.getChildren().addAll(headerHB, srednjiHB, footerHB);
        
        
        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        scene = new Scene(root, 1000, 800);
        
        primaryStage.setTitle("Nabavka - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(800);
        
        
        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            kontroler.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB,
                                        podaciFiltrirano, tabelaFiltrirano);
        });
        
        ponistiDugme.setOnAction(e->{
            opcijeTip.clear();
            opcijeProizvodjac.clear();
            PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
            PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);
            tipOdabran=false;
            proizvodjacOdabran=false;
            deoNaziva.setText("");
            podaciFiltrirano.clear();
        });
        
        //dvostruki klik na gornju tabelu - dodavanje komponente u donju tabelu
        tabelaFiltrirano.setOnMouseClicked((MouseEvent event) -> {
            //detektovanje dvostrukog klika levim dugmetom
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                kontroler.odaberiKomponentu(tabelaFiltrirano, tabelaOdabrano, podaciOdabrano);
            }
        });
    
        //dvostruki klik na donju tabelu - azuriranje kolicine komponente u donjoj tabeli
        tabelaOdabrano.setOnMouseClicked((MouseEvent event) -> {
            //detektovanje dvostrukog klika levim dugmetom
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                kontroler.korekcijaKolicine(tabelaOdabrano, podaciOdabrano);
            }
        });
        
        //dugme za azuriranje baze i stampanje prijemnice
        prihvatiDugme.setOnAction(e->{
            kontroler.realizacijaNabavke(podaciOdabrano, dobavljacCB, nazadDugme);
        });
        
        //dugme za kreiranje nove komponente
        dodavanjeDugme.setOnAction(e ->{
            kontroler.dodajNovuKomponentu();
        });
        
        //dugme za kreiranje novog dobavljaca
        dobavljacDugme.setOnAction(e ->{
            kontroler.dodajNovogDobavljaca(dobavljacCB);
        });
        
        //Taster prozora Nabavke za nazad
        nazadDugme.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });
        
        //ucitavanje svih proizvodjaca koji imaju u ponudi trazeni tip
        tipCB.setOnAction(e->{
            tipOdabran=Pomocne.ucitajProizvodjace(proizvodjacOdabran, opcijeProizvodjac, (String) tipCB.getSelectionModel().getSelectedItem());
        });
        
        //ucitavanje svih tipova odabranog proizvodjaca
        proizvodjacCB.setOnAction(e->{
            proizvodjacOdabran=Pomocne.ucitajTipove(tipOdabran, opcijeTip, (String) proizvodjacCB.getSelectionModel().getSelectedItem());
        });
        
        
    }
    
    
}
