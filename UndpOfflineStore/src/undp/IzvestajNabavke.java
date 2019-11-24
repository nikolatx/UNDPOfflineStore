package undp;

import com.grupa1.model.IzvestajPOJO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kontroleri.IzvestajKontroler;
import pomocne.Tabela;

public class IzvestajNabavke extends Application {

    //objekat za konekciju sa bazom podataka
    Connection conn;

    //Lista za pracenje informacija u tabeli
    ObservableList<IzvestajPOJO> podaci = FXCollections.observableArrayList();
    TableView tabela = new TableView();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button nazadDugme = new Button("Nazad");

    //Kreiranje opisa na formi
    Label naslovForme = new Label("Izveštaji nabavke");
    Label labelFiltriraneKomponente = new Label("Izveštaj nabavke");

    //Kreiranje dugmica za odredjene funkcije
    Button dnevniButton = new Button("Dnevni izveštaj");
    Button nedeljniButton = new Button("Nedeljni izveštaj");
    Button mesecniButton = new Button("Mesečni izveštaj");
    Button godisnjiButton = new Button("Godišnji izveštaj");
    Button odDoButton = new Button("Odabrani datum");
    Button nazadButton = new Button("Nazad");

    //Kreiranje DatePicker-a za izbor datuma za pretragu
    DatePicker odPicker = new DatePicker();
    DatePicker doPicker = new DatePicker();
    SimpleDateFormat prostDatum = new SimpleDateFormat("dd/MM/yyyy");

    //Kreiranje labela
    Label sumaLabel = new Label();
    Label nazivLabel = new Label("Izveštaji nabavke");

    //Kreiranje fonta
    Font font = new Font(25);

    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox headerHB = new HBox();
    HBox footerHB = new HBox();

    BorderPane desniBP = new BorderPane();
    VBox desniGornjiVB = new VBox();
    VBox desniDonjiVB = new VBox();
    VBox srednjiVB = new VBox();
    HBox sumaHB = new HBox();
    
    IzvestajKontroler kontroler = new IzvestajKontroler();
    
    @Override
    public void init() throws Exception {
        super.init();
        Tabela.kreirajTabeluIzvestaja(tabela);
    }

    public void start(Stage primaryStage) throws SQLException {

        //podesavanje naslova forme
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");
        //podesavanje velicine,pozicije i izgleda panela
        headerHB.setAlignment(Pos.CENTER);
        headerHB.setId("headerBackground");
        headerHB.setMinSize(1000, 120);
        headerHB.getChildren().add(naslovForme);

        //podesavanje velicine,pozicije i izgleda panela sa tableView 
        srednjiVB.setPadding(new Insets(10));
        srednjiVB.setId("bottomStyle");
        srednjiVB.setAlignment(Pos.CENTER_LEFT);
        //podesavanje tabele da raste po x i y osi
        HBox.setHgrow(tabela, Priority.ALWAYS);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        tabela.setId("my-table");
        tabela.setPlaceholder(new Label(""));
        //polje koje prikazuje ukupnu cenu ispod donjeg desnog ugla tabele
        sumaHB.setAlignment(Pos.CENTER_RIGHT);
        sumaHB.getChildren().add(sumaLabel);
        srednjiVB.getChildren().addAll(labelFiltriraneKomponente, tabela, sumaHB);
        
        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setId("bottomStyle");
        nazadDugme.setId("buttonStyle");

        //podesavanje velicine izgleda i dodavanje komponenti u boxove
        desniGornjiVB.setId("bottomStyle");
        desniGornjiVB.setAlignment(Pos.CENTER);
        desniGornjiVB.setPadding(new Insets(10));
        desniGornjiVB.setSpacing(10);
        dnevniButton.setMaxSize(150, 25);
        dnevniButton.setId("buttonStyleNabavka");
        nedeljniButton.setMaxSize(150, 25);
        nedeljniButton.setId("buttonStyleNabavka");
        mesecniButton.setMaxSize(150, 25);
        mesecniButton.setId("buttonStyleNabavka");
        godisnjiButton.setMaxSize(150, 25);
        godisnjiButton.setId("buttonStyleNabavka");
        odDoButton.setMaxSize(150, 25);
        odDoButton.setId("buttonStyleNabavka");
        odPicker.setMaxSize(130, 25);
        doPicker.setMaxSize(130, 25);
        desniGornjiVB.getChildren().addAll(dnevniButton, nedeljniButton, mesecniButton, godisnjiButton, odPicker, doPicker, odDoButton);
        
        //VBox za smestaj dva zadnja dugmeta
        desniDonjiVB.setAlignment(Pos.BOTTOM_CENTER);
        desniDonjiVB.getChildren().add(nazadDugme);
        desniDonjiVB.setPadding(new Insets(10));
        desniBP.setMinWidth(200);
        desniBP.setTop(desniGornjiVB);
        desniBP.setBottom(desniDonjiVB);
        
        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(srednjiVB);
        root.setBottom(footerHB);
        root.setRight(desniBP);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Izveštaj nabavke - UNDP Offline Store");

        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //izvestaj za danas
        dnevniButton.setOnAction(e -> {
            kontroler.dnevniIzvestaj(tabela, sumaLabel, false);
        });
        
        //izvestaj za tekucu nedelju
        nedeljniButton.setOnAction(e -> {
            kontroler.nedeljniIzvestaj(tabela, sumaLabel, false);
        });
        
        //izvestaj za tekuci mesec
        mesecniButton.setOnAction(e -> {
            kontroler.mesecniIzvestaj(tabela, sumaLabel, false);
        });
        
        //izvestaj za tekucu godinu
        godisnjiButton.setOnAction(e -> {
            kontroler.godisnjiIzvestaj(tabela, sumaLabel, false);
        });
        
        //izvestaj za zadati interval
        odDoButton.setOnAction((ActionEvent e) -> {
            //Preuzimanje datuma iz datepicker
            LocalDate pocetniDatum = odPicker.getValue();
            LocalDate zavrsniDatum = doPicker.getValue();
            String uslov = "WHERE datum>='" + pocetniDatum + "' AND datum<='" + zavrsniDatum + "'";
            if ((odPicker.getValue() != null) && (doPicker.getValue() != null)) {
                    kontroler.intervalniIzvestaj(tabela, uslov, sumaLabel, false);
            }
        });
        
        //povratak na prethodnu formu
        nazadDugme.setOnAction(e -> {
            primaryStage.close();
            new Izvestaji().start(primaryStage);
        });
        
    }

    
    
}