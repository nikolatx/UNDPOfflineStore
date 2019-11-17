package undp;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.IzvestajPOJO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pomocne.Pomocne;
import pomocne.Tabela;

public class IzvestajNabavke extends Application {

    //objekat za konekciju sa bazom podataka
    Connection conn;

    //Liste sa opcijama ComboBox-ova
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ObservableList opcijeDobavljac = FXCollections.observableArrayList();

    //Liste za pracenje informacija u tabeli
    ObservableList<IzvestajPOJO> podaci = FXCollections.observableArrayList();
    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabela = new TableView();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button nazadDugme = new Button("Nazad");

    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Izveštaj nabavke");
    Label labelFiltriraneKomponente = new Label("Izveštaj nabavke");

    //Kreiranje tastera za odredjene funkcije
    Button dnevniButton = new Button("Dnevni izveštaj");
    Button nedeljniButton = new Button("Nedeljni izveštaj");
    Button mesecniButton = new Button("Mesečni izveštaj");
    Button godisnjiButton = new Button("Godišnji izveštaj");
    Button odDoButton = new Button("Odabrani datum");
    Button nazadButton = new Button("Nazad");
    Button exportButton = new Button("PDF");

    //Kreiranje DatePicker-a za izbor datuma za pretragu
    DatePicker odPicker = new DatePicker();
    DatePicker doPicker = new DatePicker();

    SimpleDateFormat prostDatum = new SimpleDateFormat("dd/MM/yyyy");

    //Kreiranje labela sa izpisima
    Label sumaLabel = new Label();
    Label odDoLabel = new Label("");
    Label nazivLabel = new Label("Izveštaji nabavke");

    //Kreiranje fonta
    Font font = new Font(25);

    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    HBox headerHB = new HBox();
    VBox boxZaTabele = new VBox();

    //kreiranje pomocnih promenljivih koje ce da se koriste u lambda izrazima
    private Statement st = null;
    private int prijemnicaId;

    @Override
    public void init() throws Exception {
        super.init();
        Tabela.kreirajTabeluIzvestaja(tabela);
    }

    public void start(Stage primaryStage) throws SQLException {

        //podesavanje velicine,pozicije i izgleda panela
        comboboxHB.setAlignment(Pos.BOTTOM_LEFT);
        comboboxHB.setId("headerBackground");
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.setMinSize(1000, 100);

        //podesavanje naslova forme
        naslovForme.setTranslateY(-30);
        naslovForme.setTranslateX(-230);
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");
        comboboxHB.getChildren().add(naslovForme);

        headerHB.setId("headerBackground");
        headerHB.setMinSize(1000, 120);
        nazivLabel.setId("headerLabel");
        headerHB.setAlignment(Pos.CENTER);
        nazivLabel.setFont(font);
        headerHB.getChildren().addAll(nazivLabel);

        //podesavanje velicine,pozicije i izgleda panela sa tableView 
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setMaxSize(800, 250);
        boxZaTabele.setPadding(new Insets(10));
        tabela.setPlaceholder(new Label(""));
        
        tabela.setMinSize(800, 430);
        tabela.setId("my-table");
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setMaxSize(800, 430);
        boxZaTabele.setAlignment(Pos.CENTER_LEFT);
        sumaLabel.setTranslateX(+550);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente, tabela,sumaLabel);

        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        footerHB.setId("bottomStyle");
        nazadDugme.setId("buttonStyle");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 860));
        footerHB.getChildren().addAll(nazadDugme);

        //podesavanje velicine izgleda i dodavanje komponenti u boxove
        desniVB.setMinSize(210, 430);
        desniVB.setId("bottomStyle");
        desniVB.setAlignment(Pos.CENTER);
        desniVB.setSpacing(10);
        dnevniButton.setMinSize(150, 25);
        dnevniButton.setId("buttonStyleNabavka");
        nedeljniButton.setMinSize(150, 25);
        nedeljniButton.setId("buttonStyleNabavka");
        mesecniButton.setMinSize(150, 25);
        mesecniButton.setId("buttonStyleNabavka");
        godisnjiButton.setMinSize(150, 25);
        godisnjiButton.setId("buttonStyleNabavka");
        odDoButton.setMinSize(150, 25);
        odDoButton.setId("buttonStyleNabavka");
        odPicker.setMaxSize(150, 25);
        doPicker.setMaxSize(150, 25);
        desniVB.getChildren().addAll(dnevniButton, nedeljniButton, mesecniButton, godisnjiButton, odPicker, doPicker, odDoButton);
        
        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(boxZaTabele);
        root.setBottom(footerHB);
        root.setRight(desniVB);

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Izveštaj nabavke - UNDP Offline Store");

        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        //izvestaj za danas
        dnevniButton.setOnAction(e -> {
            try {
                String sql = "WHERE pr.datum=DATE(NOW())";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke");
                if (conn!=null) try {
                    conn.close();
                } catch (SQLException ex1) {}
            }
        });

        //izvestaj za tekucu nedelju
        nedeljniButton.setOnAction(e -> {
            try {
                String sql = "WHERE YEARWEEK(pr.datum)=YEARWEEK(CURDATE())";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke");
                if (conn!=null) try {
                    conn.close();
                } catch (SQLException ex1) {}
            }
        });
        
        //izvestaj za tekuci mesec
        mesecniButton.setOnAction(e -> {
            try {
                String sql = "WHERE MONTH(CURDATE())=MONTH(pr.datum) AND YEAR(CURDATE())=YEAR(pr.datum)";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke");
                if (conn!=null) try {
                    conn.close();
                } catch (SQLException ex1) {}
            }
        });
        
        //izvestaj za tekucu godinu
        godisnjiButton.setOnAction(e -> {
            try {
                String sql = "WHERE YEAR(CURDATE())=YEAR(pr.datum)";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke!");
                if (conn!=null) try {
                    conn.close();
                } catch (SQLException ex1) {}
            }
        });
        
        //izvestaj za zadati interval
        odDoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //Preuzimanje datuma iz datepicker
                LocalDate pocetniDatum = odPicker.getValue();
                LocalDate zavrsniDatum = doPicker.getValue();
                String sql = "WHERE datum>='" + pocetniDatum + "' AND datum<='" + zavrsniDatum + "'";
                if ((odPicker.getValue() != null) && (doPicker.getValue() != null)) {
                    try {
                        tabela.getItems().clear();
                        tabela.getColumns().clear();
                        conn = DBUtil.napraviKonekciju();
                        sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                        preuzmiPodatke(sql);
                    } catch (SQLException ex) {
                        Pomocne.poruka("Problem sa očitavanjem podataka za izveštaj nabavke!");
                        if (conn!=null) try {
                            conn.close();
                        } catch (SQLException ex1) {}
                    }
                }
            }
        });
        
        //povratak na prethodnu formu
        nazadDugme.setOnAction(e -> {
            primaryStage.close();
            new Izvestaji().start(primaryStage);
        });
        
    }

    //preuzimanje podataka iz baze po zadatom upitu
    private void preuzmiPodatke(String uslov) throws SQLException {
        if (conn != null) {
            //formulisanje upita
            String upit = "SELECT k.komponenta_id, k.naziv, p.naziv, SUM(dp.kolicina), SUM(dp.cena) "
                    + "FROM prijemnica as pr "
                    + "INNER JOIN detaljiprijemnice as dp ON pr.prijemnica_id=dp.prijemnica_id "
                    + "INNER JOIN komponenta as k ON k.komponenta_id=dp.komponenta_id "
                    + "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id "
                    + uslov 
                    + "GROUP BY dp.komponenta_id";
            //postavljanje upita nad bazom podataka
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs != null) {
                podaci = FXCollections.observableArrayList();
                //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                if (tabela.getColumns().size() == 0) {
                    Tabela.kreirajTabeluIzvestaja(tabela);
                }
                //ubacivanje podataka u listu
                while (rs.next()) {
                    //dodavanje komponente u listu
                    podaci.add(new IzvestajPOJO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDouble(5)));
                }
                //ubacivanje podataka u tabelu
                tabela.setItems(podaci);
            }
        }
    }
    
    //racuna ukupnu sumu za zadati period
    private double izracunajSumu(String uslov) throws SQLException {
        double suma = 0;
        if (conn != null) {
            String upit = "SELECT SUM(dp.cena) "
                    + "FROM detaljiprijemnice as dp "
                    + "INNER JOIN prijemnica as pr ON dp.prijemnica_id=pr.prijemnica_id "
                    + uslov;
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            if ((rs != null) && (rs.next())) {
                    suma += rs.getDouble(1);
            }
        }
        return suma;
    }

}