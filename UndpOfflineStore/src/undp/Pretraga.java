package undp;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.grupa1.dbconnection.*;
import com.grupa1.model.*;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class Pretraga extends Application {

    //Kreiranje 3 ComboBox -a 
    ComboBox tipCB = new ComboBox();
    ComboBox nazivCB = new ComboBox();
    ComboBox proizvodjacCB = new ComboBox();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretrazi");
    Button dodavanjeDugme = new Button("Dodaj");
    Button prihvatiDugme = new Button("Prihvati");
    Button prihvatiIObrazacDugme = new Button("Prihvati i napravi obrazac");

    //Kreiranje opisa koji ce da stoji iznad combobox-eva
    Label tipOpis = new Label("Tip");
    Label nazivOpis = new Label("Naziv");
    Label proizvodjacOpis = new Label("Proizvodjac");

    //kreiranje 2 tabele koje ce da pokazuju podatke iz baze
    //prva tabela za prikaz artikala
    TableView prikazKomponenti = new TableView();

    //druga tabela za prikaz odabranih artikala
    TableView odabraneStrane = new TableView();

    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    VBox headerHB = new VBox();
    VBox prikaziTabele = new VBox();
    
Connection conn;
    @Override
    public void start(Stage primaryStage) {

        //podesavanje velicine,pozicije i izgleda panela sa opisima combobox-eva
        opisiCB.getChildren().addAll(tipOpis, nazivOpis, proizvodjacOpis);
        opisiCB.setAlignment(Pos.CENTER_LEFT);
        opisiCB.setPadding(new Insets(10));
        opisiCB.setSpacing(30);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        comboboxHB.setAlignment(Pos.CENTER_LEFT);
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.getChildren().addAll(tipCB, nazivCB, proizvodjacCB, pretragaDugme);

        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.getChildren().addAll(opisiCB, comboboxHB);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        prikaziTabele.setPadding(new Insets(10));
        prikaziTabele.setSpacing(30);
        prikaziTabele.getChildren().addAll(prikazKomponenti, odabraneStrane);

        //podesavanje velicine,pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        footerHB.getChildren().addAll(prihvatiDugme, prihvatiIObrazacDugme);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        desniVB.getChildren().add(dodavanjeDugme);
        desniVB.setAlignment(Pos.TOP_CENTER);
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(30);

        //Pravljenje kolona za prvu tabelu
        TableColumn<Komponente, Integer> kolona1 = new TableColumn("Id");
        kolona1.setMinWidth(80);
        kolona1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Komponente, String> kolona2 = new TableColumn("Naziv");
        kolona2.setMinWidth(80);
        kolona2.setCellValueFactory(new PropertyValueFactory<>("naziv"));

        TableColumn<Komponente, Integer> kolona3 = new TableColumn("Tip");
        kolona3.setMinWidth(80);
        kolona3.setCellValueFactory(new PropertyValueFactory<>("tip"));

        TableColumn <Komponente, Integer>kolona4 = new TableColumn("Kolicina");
        kolona4.setMinWidth(80);
        kolona4.setCellValueFactory(new PropertyValueFactory<>("kolicina"));

        TableColumn<Komponente, Double> kolona5 = new TableColumn("Cena");
        kolona5.setMinWidth(80);
        kolona5.setCellValueFactory(new PropertyValueFactory<>("cena"));
       
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/undpofflinestore", "root", "");
            System.out.println("Uspesna konekcija");
        } catch (SQLException e) {
            System.out.println("Neuspesna konekcija");
        }
        
        try {
            prikazKomponenti.setItems(povuciPodatke());
            prikazKomponenti.getColumns().addAll(kolona1, kolona2, kolona3, kolona4, kolona5);
            System.out.println("Uspesno povlacenje iz baze");
        } catch (SQLException e) {
            System.out.println("Neuspesno povlacenje iz baze");
        }
        

        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(prikaziTabele);
        root.setBottom(footerHB);
        root.setRight(desniVB);

        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Pretraga - UNDP OfflineStore");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public ObservableList<Komponente> povuciPodatke() throws SQLException{
        ObservableList<Komponente> podaci = FXCollections.observableArrayList();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM komponenta"); 
        while (rs.next()) {
               podaci.add(new Komponente(
                       rs.getInt("komponenta_id"),
                       rs.getString("naziv"),
                       rs.getInt("tip_id"),
                       rs.getInt("kolicina"),
                       rs.getDouble("cena")));
           }
        return podaci;
    }
    
    public ObservableList<Komponente> podaciZaCB() throws SQLException {
        ObservableList<Komponente> podaci = FXCollections.observableArrayList();
        ResultSet rs = conn.createStatement().executeQuery("SELECT naziv FROM tip");
        return
    }
}
