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
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.grupa1.dbconnection.PomocneDAO;
import com.grupa1.model.KomponentaSaSlikom;
import javafx.scene.control.CheckBox;
import kontroleri.AzuriranjeKontroler;
import pomocne.Tabela;

public class Azuriranje extends Application {

    //objekat za konekciju sa bazom podataka
    Connection conn;

    //Liste sa opcijama ComboBox-ova
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();

    //Kreiranje 3 ComboBox-a na formi
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);
    CheckBox aktuelneCB = new CheckBox();

    //Liste za pracenje informacija u tabelama
    ObservableList<KomponentaSaSlikom> podaciFiltrirano = FXCollections.observableArrayList();

    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabelaFiltrirano = new TableView();

    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button nazadDugme = new Button("Nazad");
    Button izmeniDugme = new Button("Potvrdi");
    Button brisiDugme = new Button("Izbriši");

    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Ažuriranje");
    Label labelFiltriraneKomponente = new Label("Rezultat pretrage");

    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    VBox headerHB = new VBox();
    VBox boxZaTabele = new VBox();

    //Kreiranje Fonta
    Font font = new Font(25);
    
   
    AzuriranjeKontroler kontroler=new AzuriranjeKontroler();

    @Override
    public void init() throws Exception {
        super.init();
        Tabela.kreirajTabelu(tabelaFiltrirano, true);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvodjača");
        proizvodjacCB.setMinSize(150, 25);
        aktuelneCB.setText("Samo aktuelne");
        aktuelneCB.setSelected(true);
        
        
        //podesavanja dugmeta za pretragu
        pretragaDugme.setMinSize(100, 25);
        pretragaDugme.setId("pretragaButton");
        pretragaDugme.setDefaultButton(true);


        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        comboboxHB.setAlignment(Pos.BOTTOM_LEFT);
        comboboxHB.setId("headerBackground");
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.setMinSize(1000, 120);

        //podesavanje polja za unos dela naziva komponente za pretragu
        deoNaziva.setPromptText("Pretraga");

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
        //tabelaFiltrirano.setMaxSize(800, 250);
        boxZaTabele.setPadding(new Insets(10));

        tabelaFiltrirano.setMinSize(800, 400);
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setMaxSize(800, 430);
        boxZaTabele.setAlignment(Pos.CENTER_LEFT);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente, tabelaFiltrirano);

        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(10);
        footerHB.setMinSize(1000, 100);
        footerHB.setId("bottomStyle");
        nazadDugme.setId("buttonStyle");
        HBox.setMargin(nazadDugme, new Insets(0, 0, 0, 860));
        HBox.setMargin(izmeniDugme, new Insets(0, 0, 90, 0));
        izmeniDugme.setMinSize(110, 25);       
        izmeniDugme.setId("buttonStyle");       
        footerHB.getChildren().addAll(nazadDugme);
        
        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        desniVB.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(brisiDugme, new Insets(50, 0, 0, 0));
        VBox.setMargin(brisiDugme, new Insets(0, 0, 0, 0));
        desniVB.setMinSize(200, 430);
        desniVB.setId("bottomStyle");
        desniVB.setSpacing(20);
        brisiDugme.setMinSize(150, 25);
        izmeniDugme.setMinSize(150, 25);
        brisiDugme.setId("buttonStyle");
        desniVB.getChildren().addAll( izmeniDugme,brisiDugme);

        //popunjavanje combobox-eva podacima
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);

        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            kontroler.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, 
                               podaciFiltrirano, tabelaFiltrirano);
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
        primaryStage.setTitle("Ažuriranje - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void promeniPodatkeTabele(TableColumn.CellEditEvent<KomponentaSaSlikom, String> komponentaStringEdit){
        
        KomponentaSaSlikom k = (KomponentaSaSlikom) tabelaFiltrirano.getSelectionModel().getSelectedItem();
        k.setNaziv(komponentaStringEdit.getNewValue());
        k.setTip(komponentaStringEdit.getNewValue());
        k.setProizvodjac(komponentaStringEdit.getNewValue());
    }
   
        
}

