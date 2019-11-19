package undp;

import com.grupa1.dbconnection.PomocneDAO;
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
import com.grupa1.model.KomponentaSaSlikom;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Priority;
import kontroleri.PretragaKontroler;
import pomocne.Tabela;


public class Pretraga extends Application {
    
    //Liste sa opcijama ComboBox-ova
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    
    //Kreiranje 3 ComboBox-a na formi
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);

    
    CheckBox aktuelneCB = new CheckBox();
    
    //Liste za pracenje informacija u tabelama
    ObservableList<KomponentaSaSlikom> podaciFiltrirano=FXCollections.observableArrayList();;
    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView<KomponentaSaSlikom> tabelaFiltrirano = new TableView<>();
        
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button ponistiDugme = new Button("Poništi");
    Button nazadDugme = new Button("Nazad");
    
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Pretraga");
    Label labelFiltriraneKomponente = new Label("Rezultat pretrage");

    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    //VBox desniVB = new VBox();
    BorderPane desniVB = new BorderPane();
    VBox filterVB=new VBox();
    VBox headerVB = new VBox();
    VBox boxZaTabele = new VBox();
    
    //Kreiranje Fonta
    Font font = new Font(25);
    
    boolean tipOdabran, proizvodjacOdabran;
    
    PretragaKontroler kontroler=new PretragaKontroler();
    
    @Override
    public void init() throws Exception {
        super.init();
        Tabela.kreirajTabelu(tabelaFiltrirano, false);
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvođača");
        proizvodjacCB.setMinSize(150, 25);
        
        //podesavanje checkbox-a za prikaz aktuelnih komponenata
        aktuelneCB.setText("Samo aktuelne");
        aktuelneCB.setSelected(true);
        
        //podesavanje polja za unos dela naziva komponente za pretragu
        deoNaziva.setPromptText("Pretraga");
        
        //podesavanja dugmeta za pretragu
        pretragaDugme.setMinSize(100, 25);
        pretragaDugme.setId("buttonStyle");
        pretragaDugme.setDefaultButton(true);
        ponistiDugme.setMinSize(100, 25);
        ponistiDugme.setId("buttonStyle");
        
        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        nazadDugme.setId("buttonStyle");
        
        //podesavanje naslova forme
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");
        
        //centralni gornji box koji sadrzi naslov forme - Pretraga
        headerVB.setAlignment(Pos.CENTER);
        headerVB.setId("headerBackground");
        headerVB.setPadding(new Insets(10));
        headerVB.setMinSize(1000, 100);
        
        
        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerVB.getChildren().addAll(naslovForme, comboboxHB);
        
        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        boxZaTabele.setPadding(new Insets(10));
        tabelaFiltrirano.setPlaceholder(new Label(""));
        
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setAlignment(Pos.CENTER_LEFT);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente,tabelaFiltrirano);
        
        //prostor ispod tabele
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(5));
        
        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        filterVB.getChildren().addAll(tipCB, proizvodjacCB, aktuelneCB, deoNaziva, pretragaDugme, ponistiDugme);
        filterVB.setSpacing(15);
        filterVB.setPadding(new Insets(10));
        filterVB.setAlignment(Pos.CENTER);
        
        //desni box sa filterima i dugmetom za nazad
        desniVB.setMinSize(100, 530);
        desniVB.setId("bottomStyle");
        desniVB.setPadding(new Insets(10));
        desniVB.setTop(filterVB); 
        desniVB.setBottom(nazadDugme);
        BorderPane.setAlignment(nazadDugme, Pos.CENTER);
        desniVB.setMinWidth(100);
        
        //glavni box u koji se pakuju svi ostali boxevi
        VBox root = new VBox();
        //box koji sadrzi tabele i desniVB
        HBox srednjiHB=new HBox();
        srednjiHB.getChildren().addAll(tabelaFiltrirano, desniVB);
        HBox.setHgrow(tabelaFiltrirano, Priority.ALWAYS);
        VBox.setVgrow(srednjiHB, Priority.ALWAYS);
        root.getChildren().addAll(headerVB, srednjiHB, footerHB);

        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 1000, 650);
        
        primaryStage.setTitle("Pretraga - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        //popunjavanje combobox-eva podacima
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);

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
        
        nazadDugme.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });
        
        
        tipCB.setOnAction(e->{
            //ucitavanje svih proizvodjaca koji imaju u ponudi trazeni tip
            if (!proizvodjacOdabran) {
                opcijeProizvodjac.clear();
                PomocneDAO.ispuniComboBoxZaProizvodjacaPoTipu(opcijeProizvodjac, (String) tipCB.getSelectionModel().getSelectedItem(), false);
                tipOdabran=true;
            }
        });
        
        proizvodjacCB.setOnAction(e->{
            //ucitavanje svih tipova odabranog proizvodjaca
            if (!tipOdabran) {
                opcijeTip.clear();
                PomocneDAO.ispuniComboBoxZaTipProizvodjaca(opcijeTip, (String) proizvodjacCB.getSelectionModel().getSelectedItem(), false);
                proizvodjacOdabran=true;
            }
        });
        
        
    }
    
    
}
