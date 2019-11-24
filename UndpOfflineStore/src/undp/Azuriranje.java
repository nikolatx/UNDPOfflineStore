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
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.grupa1.dbconnection.PomocneDAO;
import com.grupa1.model.KomponentaSvaPolja;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Priority;
import kontroleri.AzuriranjeKontroler;
import pomocne.Pomocne;
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

    //Lista za pracenje informacija u tabelama
    ObservableList<KomponentaSvaPolja> podaciFiltrirano = FXCollections.observableArrayList();

    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabelaFiltrirano = new TableView();

    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button nazadDugme = new Button("Nazad");
    Button izmeniDugme = new Button("Izmeni");
    Button brisiDugme = new Button("Izbriši");
    Button ponistiDugme = new Button("Poništi");
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Ažuriranje");
    Label labelFiltriraneKomponente = new Label("Rezultat pretrage");

    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    
    VBox headerHB = new VBox();
    HBox footerHB = new HBox();

    VBox desniGornjiVB = new VBox();
    VBox desniSrednjiVB = new VBox();
    VBox desniDonjiVB = new VBox();
    BorderPane desniBP = new BorderPane();
    
    VBox boxZaTabelu = new VBox();
    BorderPane root = new BorderPane();
    
    //Kreiranje Fonta
    Font font = new Font(25);
    
    boolean tipOdabran, proizvodjacOdabran;
    AzuriranjeKontroler kontroler=new AzuriranjeKontroler();

    @Override
    public void init() throws Exception {
        super.init();
        Tabela.kreirajTabelu(tabelaFiltrirano, false);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {

        //podesavanje naslova forme
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");
        //podesavanje velicine,pozicije i izgleda header panela
        headerHB.setMinSize(1000, 100);
        headerHB.setId("headerBackground");    
        headerHB.setAlignment(Pos.CENTER);
        headerHB.getChildren().add(naslovForme);

        //podesavanje CB-a
        tipCB.setPromptText("Tip");
        tipCB.setMaxSize(130, 25);
        proizvodjacCB.setPromptText("Proizvođač");
        proizvodjacCB.setMaxSize(130, 25);
        aktuelneCB.setText("Samo aktuelne");
        aktuelneCB.setSelected(true);
        //polje za unos reci za pretragu
        deoNaziva.setPromptText("Pretraga");
        deoNaziva.setMaxSize(130, 25);
        
        //podesavanja za dugme za pretragu
        pretragaDugme.setId("buttonStyleNabavka");
        pretragaDugme.setDefaultButton(true);
                
        //dugme za ponistavanje odabranih filtera
        ponistiDugme.setId("buttonStyleNabavka");

        //podesavanje velicine, pozicije i izgleda panela sa combobox-evima i dugmicima za filtriranje
        desniGornjiVB.getChildren().addAll(tipCB, proizvodjacCB, aktuelneCB, deoNaziva, pretragaDugme, ponistiDugme);
        desniGornjiVB.setSpacing(15);
        desniGornjiVB.setPadding(new Insets(10));
        desniGornjiVB.setAlignment(Pos.CENTER);
        
        //dugme za dodavanje nove komponente
        izmeniDugme.setMinSize(150, 25); 
        izmeniDugme.setId("buttonStyleNabavka");
        //dugme za dodavanje novog dobavljaca
        brisiDugme.setId("buttonStyleNabavka");
        brisiDugme.setMinSize(150, 25); 
        desniSrednjiVB.getChildren().addAll(izmeniDugme, brisiDugme);
        desniSrednjiVB.setSpacing(15);
        desniSrednjiVB.setPadding(new Insets(10));
        desniSrednjiVB.setAlignment(Pos.CENTER);
        
        //VBox za smestaj dva zadnja dugmeta
        desniDonjiVB.setAlignment(Pos.BOTTOM_CENTER);
        desniDonjiVB.setPadding(new Insets(10));
        desniDonjiVB.getChildren().add(nazadDugme);
        
        //smestanje prethodnih VBox-ova u desni deo forme
        desniBP.setTop(desniGornjiVB);
        desniBP.setCenter(desniSrednjiVB);
        desniBP.setBottom(desniDonjiVB);

        //podesavanje velicine,pozicije i izgleda panela sa tabelom
        HBox.setHgrow(tabelaFiltrirano, Priority.ALWAYS);
        VBox.setVgrow(tabelaFiltrirano, Priority.ALWAYS);
        boxZaTabelu.setPadding(new Insets(10));
        tabelaFiltrirano.setPlaceholder(new Label(""));
        boxZaTabelu.setId("bottomStyle");
        boxZaTabelu.setAlignment(Pos.CENTER_LEFT);
        boxZaTabelu.getChildren().addAll(labelFiltriraneKomponente, tabelaFiltrirano);
        
        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        nazadDugme.setMinSize(150, 25); 
        nazadDugme.setId("buttonStyleNabavka");
        footerHB.setId("bottomStyle");
        footerHB.setPadding(new Insets(10));
        
        //popunjavanje combobox-eva podacima
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, false);
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, false);

        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            kontroler.preuzmiSvePodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB, 
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

        nazadDugme.setOnAction(e -> {
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
        
        //izmena podataka u selektovanoj vrsti
        izmeniDugme.setOnAction(e -> {
            if (tabelaFiltrirano.getSelectionModel().getSelectedItem()!=null) {
                
                AzuriranjeKomponente azuriranje = new AzuriranjeKomponente();
                KomponentaSvaPolja komponenta = (KomponentaSvaPolja)tabelaFiltrirano.getSelectionModel().getSelectedItem();
                azuriranje.pokreni(komponenta, podaciFiltrirano);
            }
            else
                Pomocne.poruka("Prvo morate selektovati komponentu u tabeli!");
            
        });
        
        //brisanje komponente u selektovanoj vrsti
        brisiDugme.setOnAction(e -> {
            if (tabelaFiltrirano.getSelectionModel().getSelectedItem()!=null) {
                int obrisano = kontroler.obrisiKomponentu(tabelaFiltrirano, podaciFiltrirano);
                //priprema obavestenja
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Obaveštenje");
                alert2.setHeaderText(null);
                if (obrisano==1)
                    alert2.setContentText("Komponenta uspešno obrisana!");
                else
                    alert2.setContentText("Došlo je do greške pri upisu u bazu podataka!");
                Platform.runLater( () -> {
                    alert2.showAndWait();
                });
            }
            else
                Pomocne.poruka("Prvo morate selektovati komponentu u tabeli!");
        });
        
        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(boxZaTabelu);
        root.setBottom(footerHB);
        root.setRight(desniBP);

        //Kreiranje scene ,velicine i povezivanje sa CSS-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setMinWidth(1010);
        primaryStage.setMinHeight(650);
        primaryStage.setTitle("Ažuriranje - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
        
}

