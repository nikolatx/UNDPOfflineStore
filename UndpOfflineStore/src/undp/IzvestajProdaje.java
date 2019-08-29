package undp;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.IzvestajPOJO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class IzvestajProdaje extends Application {

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
    Label naslovForme = new Label("Izveštaj prodaje");
    Label labelFiltriraneKomponente = new Label("Rezultat pretrage");

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

    //Formatiranje datuma za prikaz u TableView-u
    SimpleDateFormat prostDatum = new SimpleDateFormat("dd/MM/yyyy");

    //Kreiranje labela sa izpisima
    Label sumaLabel = new Label();
    Label odDoLabel = new Label("");
    Label nazivLabel = new Label("Izveštaji prodaje");

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
        kreirajTabelu(tabela);
    }

    public void start(Stage primaryStage) throws SQLException {

        
        naslovForme.setFont(font);
        naslovForme.setId("headerLabel");

        //dodavanje nodova u HBox
        comboboxHB.getChildren().addAll(naslovForme);

        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.setAlignment(Pos.CENTER);
        headerHB.setMinSize(1000, 120);
        headerHB.setId("headerBackground");
        nazivLabel.setFont(font);
        nazivLabel.setId("headerLabel");
        headerHB.getChildren().add(nazivLabel);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setMaxSize(800, 250);
        tabela.setId("my-table");
        boxZaTabele.setPadding(new Insets(10));

        tabela.setMaxSize(800, 400);
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setMaxSize(800, 450);
        boxZaTabele.setAlignment(Pos.CENTER);
        sumaLabel.setTranslateX(+200);
        labelFiltriraneKomponente.setTranslateX(-340);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente, tabela,sumaLabel);

        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        footerHB.setMinSize(1000, 100);
        footerHB.setId("bottomStyle");
        nazadDugme.setId("buttonStyle");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 860));
        footerHB.getChildren().addAll(nazadDugme);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        //podesavanje velicine izgleda i dodavanje komponenti u rightBox
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

        dnevniButton.setOnAction(e -> {
            try {
                String sql = "WHERE datum=DATE(NOW())";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'faktura'!");
            }
        });

        nedeljniButton.setOnAction(e -> {
            try {
                String sql = "WHERE datum >= DATE(NOW()) + INTERVAL -6 DAY AND datum < NOW() + INTERVAL 0 DAY";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'faktura'!");
            }
        });

        mesecniButton.setOnAction(e -> {
            try {
                String sql = "WHERE datum >= DATE(NOW()) + INTERVAL -30 DAY AND datum < NOW() + INTERVAL 0 DAY";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'faktura'!");
            }
        });

        godisnjiButton.setOnAction(e -> {
            try {
                String sql = "WHERE datum >= DATE(NOW()) + INTERVAL -355 DAY AND datum < NOW() + INTERVAL 0 DAY";
                tabela.getItems().clear();
                tabela.getColumns().clear();
                conn = DBUtil.napraviKonekciju();
                sumaLabel.setText("UKUPNO U RSD: " + String.format("%,.2f", izracunajSumu(sql)));
                preuzmiPodatke(sql);
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'faktura'!");
            }
        });

        odDoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //Preuzimanje datuma iz datapicker
                LocalDate pocetniDatum = odPicker.getValue();
                LocalDate zavrsniDatum = doPicker.getValue();
                String sql = "WHERE datum>='" + pocetniDatum + "' AND datum<='" + zavrsniDatum + "'";
                if ((odPicker.getValue() != null) && (doPicker.getValue() != null)) {
                    try {
                        tabela.getItems().clear();
                        tabela.getColumns().clear();
                        conn = DBUtil.napraviKonekciju();
                        sumaLabel.setText("UKUPNO U RSD: " + izracunajSumu(sql));
                        preuzmiPodatke(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(IzvestajProdaje.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        nazadDugme.setOnAction(e -> {
            primaryStage.close();
            new Izvestaji().start(primaryStage);
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

        primaryStage.setTitle("Izveštaj prodaje - UNDP Offline Store");

        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void preuzmiPodatke(String uslov) throws SQLException {

        if (conn != null) {
            //uzimanje podataka iz combobox-eva i polja za unos teksta

            String upit = "SELECT df.komponenta_id, k.naziv, df.cena, f.datum " 
                    + "FROM faktura as f "
                    + "INNER JOIN kupac as k ON f.kupac_id=k.kupac_id "
                    + "INNER JOIN detaljifakture as df ON f.faktura_id=df.faktura_id "
                    + uslov;

            //postavljanje upita nad bazom podataka
            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs != null) {
                try {
                    podaci = FXCollections.observableArrayList();
                    //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                    if (tabela.getColumns().size() == 0) {
                        kreirajTabelu(tabela);
                    }
                    //ubacivanje podataka u listu
                    while (rs.next()) {
                        //dodavanje komponente u listu
                        podaci.add(new IzvestajPOJO(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDate(4).toLocalDate()));
                    }
                    //ubacivanje podataka u tabelu
                    tabela.setItems(podaci);
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

    private double izracunajSumu(String uslov) throws SQLException {

        double suma = 0;
        if (conn != null) {
            String upit = "SELECT SUM(df.cena) "
                    + "FROM detaljifakture as df "
                    + "INNER JOIN faktura as f ON df.faktura_id=f.faktura_id "
                    + uslov;

            ResultSet rs = DBUtil.prikupiPodatke(conn, upit);

            if ((rs != null) && (rs.next())) {
                try {
                    suma += rs.getDouble(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
        }
        return suma;
    }

    private void kreirajTabelu(TableView tabela) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, Integer>("id"));
        //definisanje procentualne sirine kolone
        kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaOpis = new TableColumn("Naziv kupca");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, String>("naziv"));
        kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, Double>("cena"));
        kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width 
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        kolonaCena.setCellFactory(tc -> new TableCell<IzvestajPOJO, Number>() {
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
        TableColumn<IzvestajPOJO, LocalDate> kolonaDatum = new TableColumn("Datum");


        kolonaDatum.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, LocalDate>("datum"));
        kolonaDatum.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10 width
        kolonaDatum.setStyle( "-fx-alignment: CENTER;");
        //dodavanje kolona u tabelu
        tabela.getColumns().addAll(kolonaSifra, kolonaOpis, kolonaCena, kolonaDatum);
    }
}