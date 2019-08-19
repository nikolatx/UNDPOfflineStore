package undp;

import javafx.application.Application;
import javafx.geometry.HPos;
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
import com.grupa1.dbconnection.*;
import com.grupa1.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;      
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class Pretraga extends Application {

    Connection conn;
    
    //Liste za pracenje combobox-a
    ObservableList opcijeCombo1 = FXCollections.observableArrayList();
    ObservableList opcijeCombo2 = FXCollections.observableArrayList();
    ObservableList opcijeCombo3 = FXCollections.observableArrayList();
    
    //Lista za pracenje informacija u tabeli
    ObservableList<ObservableList> podaci;
    
    //Scanner za izvlacenje podataka iz Stringa
    Scanner scanner;

    //Kreiranje 2 ComboBox polja 
    ComboBox tipCB = new ComboBox(opcijeCombo1);
    ComboBox proizvodjacCB = new ComboBox(opcijeCombo3);
    
    //Kreiranje jednog TextField polja
    TextField unosPretrage = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretrazi");
    Button dodavanjeDugme = new Button("Dodaj Komponentu");
    Button prihvatiDugme = new Button("Prihvati");
    Button prihvatiIObrazacDugme = new Button("PDF");
    Button nazad = new Button("Nazad");

    //Kreiranje opisa koji ce da stoji iznad combobox-eva
    Label dobrodosli = new Label("Pretraga");
    Label komponente = new Label("Dostupne komponente");
    Label komponenteZaIzvestaj = new Label("Komponente za izvestaj");

    //kreiranje 2 tabele koje ce da pokazuju podatke iz baze
    //prva tabela za prikaz artikala
    TableView prikazKomponenti;

    //druga tabela za prikaz odabranih artikala
    TableView odabraneStrane;
    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox opisiCB = new HBox();
    HBox comboboxHB = new HBox();
    HBox footerHB = new HBox();

    VBox desniVB = new VBox();
    VBox headerHB = new VBox();
    VBox prikaziTabele = new VBox();
    
    //Kreiranje Fonta
    Font font = new Font(25);

    @Override
    public void start(Stage primaryStage) throws SQLException {

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/undpofflinestore", "root", "");
            System.out.println("Uspesna konekcija");
        } catch (SQLException e) {
            System.out.println("Neuspesna konekcija");
        }
        
        //definisanje tabele
        prikazKomponenti = new TableView();
        odabraneStrane = new TableView();
        
        //testTabela = new TableView();
        
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvodjaca");
        proizvodjacCB.setMinSize(150, 25);
        
        
        //SREDI GA MALO, NEBOJSA
        //podesavanja za dugme za pretragu
        pretragaDugme.setMinSize(100, 25);
        pretragaDugme.setId("pretragaButton");
        

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        comboboxHB.setAlignment(Pos.BOTTOM_LEFT);
        comboboxHB.setId("headerBackground");
        comboboxHB.setPadding(new Insets(10));
        comboboxHB.setSpacing(30);
        comboboxHB.setMinSize(1000, 100);
        
        unosPretrage.setPromptText("Pretraga");
        
        dobrodosli.setTranslateY(-50);
        dobrodosli.setTranslateX(-300);
        dobrodosli.setFont(font);
        dobrodosli.setId("headerLabel");
        
        //dodavanje nodova u HBox
        comboboxHB.getChildren().addAll(tipCB, proizvodjacCB, unosPretrage, pretragaDugme,dobrodosli);

        //podesavanje velicine,pozicije i izgleda panela sa opisima i combobox-evima
        headerHB.getChildren().addAll(opisiCB, comboboxHB);

        //podesavanje velicine,pozicije i izgleda panela sa 2 tableView prozora
        prikazKomponenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        prikazKomponenti.setMaxSize(800, 250);
        prikaziTabele.setPadding(new Insets(10));
        
        //prikaziTabele.setSpacing(30);
        odabraneStrane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        odabraneStrane.setMaxSize(800, 250);
        prikaziTabele.getChildren().addAll(komponente,prikazKomponenti,komponenteZaIzvestaj, odabraneStrane);
         
        //podesavanje velicine,pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        prihvatiDugme.setId("buttonStyle");
        prihvatiIObrazacDugme.setId("buttonStyle");
        nazad.setId("buttonStyle");
        footerHB.setMargin(nazad, new Insets(0, 0, 0, 320));
        footerHB.setMargin(prihvatiDugme, new Insets(0, 0, 0, 250));
        footerHB.setMargin(prihvatiIObrazacDugme, new Insets(0, 0, 0, 20));
        footerHB.getChildren().addAll(prihvatiDugme, prihvatiIObrazacDugme,nazad);

        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        dodavanjeDugme.setId("dodavanjeDugme");
        desniVB.getChildren().add(dodavanjeDugme);
        desniVB.setAlignment(Pos.TOP_CENTER);
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(30);

        
        //combobox metode
        ispuniComboBoxZaTip();
        ispuniComboBoxZaNaziv();
        ispuniComboBoxZaProizvodjaca();
        
        //akcije za combobox 
        tipCB.setOnAction(e -> {
            try {
                resetujTabelu(prikazKomponenti);   
                povuciPodatke(comboAkcija((String)tipCB.getValue()), "tip");
            } catch (SQLException ex) {
                System.out.println("Problem sa combobox tipCB");
            }
        });
        
        proizvodjacCB.setOnAction(e -> {
           try {
                resetujTabelu(prikazKomponenti);   
                povuciPodatke(comboAkcija((String)proizvodjacCB.getValue()), "proizvodjac");
            } catch (SQLException ex) {
                System.out.println("Problem sa combobox tipCB");
            }    
        });
        
        pretragaDugme.setOnAction(e -> {
            try {
                resetujTabelu(prikazKomponenti);
                povuciPodatke(unosPretrage.textProperty().getValue());
                unosPretrage.clear();
            } catch (SQLException ex) {
                System.out.println("Greska sa TextField poljem");
            }
        
        });
        
        prikazKomponenti.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                    try {
                        String s = prikazKomponenti.getSelectionModel().getSelectedItem().toString();
                        scanner = new Scanner(s);
                        s = scanner.next();
                        povuciPodatkeTabelaDva(s.substring(1, 2));
                    } catch (SQLException ex) {
                        Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                        scanner.close();
                    }
                }
            }
        });
    
        
        //dugme za slanje na Nabavku komponenti
        dodavanjeDugme.setOnAction(e ->{
            
            
        });
        
        nazad.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });


        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(headerHB);
        root.setCenter(prikaziTabele);
        root.setBottom(footerHB);
        root.setRight(desniVB);

         povuciPodatke();
         
        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Pretraga - UNDP OfflineStore");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void ispuniComboBoxZaTip() {

        try {
            String upit = "SELECT naziv FROM tip";
            ResultSet rs = conn.createStatement().executeQuery(upit);
            while (rs.next()) {
                opcijeCombo1.add(rs.getString("naziv"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ispuniComboBoxZaNaziv() {

        try {
            String upit = "SELECT naziv FROM komponenta";
            ResultSet rs = conn.createStatement().executeQuery(upit);
            while (rs.next()) {
                opcijeCombo2.add(rs.getString("naziv"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ispuniComboBoxZaProizvodjaca() {

        try {
            String upit = "SELECT naziv FROM proizvodjac";
            ResultSet rs = conn.createStatement().executeQuery(upit);
            while (rs.next()) {
                opcijeCombo3.add(rs.getString("naziv"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String comboAkcija(String upit) {
            return upit;
    }
    
    public void povuciPodatke() throws SQLException {
      
        try {
            podaci = FXCollections.observableArrayList();
            String SQL = "SELECT komponenta_id,naziv,proizvodjac_id,tip_id,kolicina,cena FROM komponenta";
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                prikazKomponenti.getColumns().addAll(col);
            }
            
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                
                podaci.add(row);
            }
            prikazKomponenti.setItems(podaci);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public void povuciPodatke(String recKojaSeTrazi, String tabelaKojaSeTrazi) throws SQLException {
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            podaci = FXCollections.observableArrayList();
            String SQL = "SELECT komponenta_id, komponenta.naziv, komponenta.proizvodjac_id, komponenta.tip_id, kolicina, cena "
                    + "FROM komponenta JOIN " + tabelaKojaSeTrazi + " ON " + tabelaKojaSeTrazi + "."  + tabelaKojaSeTrazi + "_id = komponenta."  + tabelaKojaSeTrazi + "_id "
                    + "WHERE " + tabelaKojaSeTrazi + ".naziv='" + recKojaSeTrazi + "'";
            ps = conn.prepareStatement(SQL);
            rs = ps.executeQuery();
            
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                prikazKomponenti.getColumns().addAll(col);
            }
            
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                
                podaci.add(row);
            }
            prikazKomponenti.setItems(podaci);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ps.close();
            rs.close();
        }
    }
    
    public void povuciPodatke(String recKojaSeTrazi) throws SQLException {
      
        try {
            podaci = FXCollections.observableArrayList();
            String SQL = "SELECT komponenta_id,naziv,proizvodjac_id,tip_id,kolicina,cena "
                    + "FROM komponenta "
                    + "WHERE naziv LIKE '%" + recKojaSeTrazi + "%' OR naziv LIKE '"+ recKojaSeTrazi + "%' OR naziv LIKE '%"+ recKojaSeTrazi + "'";
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                prikazKomponenti.getColumns().addAll(col);
            }
            
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                
                podaci.add(row);
            }
            prikazKomponenti.setItems(podaci);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void povuciPodatkeTabelaDva(String id) throws SQLException {
      
        try {
            podaci = FXCollections.observableArrayList();
            String SQL = "SELECT komponenta_id,naziv,proizvodjac_id,tip_id,kolicina,cena FROM komponenta WHERE komponenta_id=" + id ;
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                odabraneStrane.getColumns().addAll(col);
            }
            
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                   
                    row.add(rs.getString(i));
                }
                
                podaci.add(row);
            }
            odabraneStrane.setItems(podaci);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void resetujTabelu(TableView tb) {
        tb.getItems().clear();
        tb.getColumns().clear();
    }
}
