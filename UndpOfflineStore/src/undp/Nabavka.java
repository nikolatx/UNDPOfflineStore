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
import kontroleri.NabavkaKontroler;

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
    Button prihvatiDugme = new Button("Prihvati");
    Button nazadDugme = new Button("Nazad");
    Button dobavljacDugme = new Button("Dodaj dobavljača");
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Nabavka");
    Label labelFiltriraneKomponente = new Label("DUPLIM KLIKOM ODABERITE ŽELJENU KOMPONENTU");
    Label labelOdabraneKomponente = new Label("Odabrane komponente");
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
    
    Scene scene=null;
    
    NabavkaKontroler kontroler=new NabavkaKontroler();
    NabavkaDAO nabavkaDAO=new NabavkaDAO();
    
    
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
        
        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.getChildren().addAll(opisiCB, comboboxHB);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        tabelaFiltrirano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaFiltrirano.setMaxSize(800, 250);
        boxZaTabele.setPadding(new Insets(10));
        
        tabelaOdabrano.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOdabrano.setMaxSize(800, 250);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente,tabelaFiltrirano,labelOdabraneKomponente, tabelaOdabrano);
        
        
        //podesavanje velicine,pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        prihvatiDugme.setId("buttonStyleNabavka");
        nazadDugme.setId("buttonStyleNabavka");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 410));
        footerHB.setMargin(prihvatiDugme, new Insets(0, 0, 0, 350));
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
        kontroler.popuniComboBoxeve(opcijeTip, opcijeProizvodjac, opcijeDobavljac);
        
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
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();


        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            kontroler.preuzmiPodatke(tipCB, proizvodjacCB, deoNaziva, aktuelneCB,
                                        podaciFiltrirano, tabelaFiltrirano);
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
    }
    
    
}
