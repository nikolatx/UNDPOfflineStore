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
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import com.grupa1.dbconnection.PomocneDAO;
import com.grupa1.model.KomponentaSaSlikom;
import kontroleri.ProdajaKontroler;

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
    ObservableList<KomponentaSaSlikom> podaciFiltrirano = FXCollections.observableArrayList();
    ObservableList<KomponentaSaSlikom> podaciOdabrano = FXCollections.observableArrayList();
    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabelaFiltrirano = new TableView();
    //tabela za prikaz odabranih komponenata
    TableView tabelaOdabrano = new TableView();
    
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();
    
    //kontrole za odabir samo aktuelnih komponenata
    //Label aktuelneLabel = new Label("Samo aktuelne");
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
    private int prijemnicaId;
    ProdajaKontroler kontroler=new ProdajaKontroler();
    ProdajaDAO prodajaDAO=new ProdajaDAO();

    @Override
    public void init() throws Exception {
        super.init();
        kontroler.kreirajTabele(tabelaFiltrirano, tabelaOdabrano);
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
        tabelaFiltrirano.setPlaceholder(new Label(""));
        tabelaOdabrano.setPlaceholder(new Label(""));
        
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
        VBox.setMargin(aktuelneCB, new Insets(0, 40, 0, 0));
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(20);
        kupacDugme.setMinSize(150, 25);
        kupacDugme.setId("buttonStyle");
        desniVB.setId("bottomStyle");
        desniVB.getChildren().addAll(kupacCB, kupacDugme, aktuelneCB);

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
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //popunjavanje combobox-eva podacima
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);
        PomocneDAO.ispuniComboBoxZaKupca(opcijeKupac);

        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            kontroler.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB,
                                        podaciFiltrirano, tabelaFiltrirano);
        });

        //dvostruki klik na vrstu gornje tabele - dodavanje komponente u donju tabelu
        tabelaFiltrirano.setOnMouseClicked((MouseEvent event) -> {
            //detektovanje dvostrukog klika levim dugmetom
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) 
                kontroler.odaberiKomponentu(tabelaFiltrirano, tabelaOdabrano, podaciOdabrano);
        });

        //dvostruki klik na donju tabelu - azuriranje kolicine komponente u donjoj tabeli
        tabelaOdabrano.setOnMouseClicked((MouseEvent event) -> {
            //detektovanje dvostrukog klika levim dugmetom
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                kontroler.korekcijaKolicine(tabelaOdabrano, podaciOdabrano);
        });

        //dugme za azuriranje baze i stampanje dopremnice/fakture
        prihvatiDugme.setOnAction(e -> {
            kontroler.realizacijaProdaje(podaciOdabrano, kupacCB, nazadDugme);
        });

        //dodavanje novog kupca
        kupacDugme.setOnAction(e ->{
            kontroler.dodajNovogKupca(kupacCB);
        });
        
        //povratak na pocetnu formu
        nazadDugme.setOnAction(e -> {
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });
        
    }
   
    
}
