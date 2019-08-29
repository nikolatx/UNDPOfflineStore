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
import com.grupa1.dbconnection.*;
import com.grupa1.model.Dokument;
import com.grupa1.model.Komponenta;
import com.grupa1.model.KomponentaSaSlikom;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;      
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;




public class Pretraga extends Application {
    
    //putanja do slika
    private final static String SLIKE_PATH = "D:\\UNDPOfflineStore\\Slike";
    //velicina uvecane slike
    private final static int VELICINA_SLIKE=400;
    
    
    //objekat za konekciju sa bazom podataka
    Connection conn;
  
    
    //Liste sa opcijama ComboBox-ova
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ObservableList opcijeDobavljac = FXCollections.observableArrayList();
    
    //Kreiranje 3 ComboBox-a na formi
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);

    
    CheckBox aktuelneCB = new CheckBox();
    
    //Liste za pracenje informacija u tabelama
    ObservableList<KomponentaSaSlikom> podaciFiltrirano=FXCollections.observableArrayList();;
    //ObservableList<Komponenta> podaciOdabrano=FXCollections.observableArrayList();;
    
    //tabela za prikaz komponenata koje zadovoljavaju zadate kriterijume pretrage
    TableView tabelaFiltrirano = new TableView();

    
    //polje za unos dela naziva komponente kao kriterijuma za pretragu
    TextField deoNaziva = new TextField();

    //Kreiranje dugmica
    Button pretragaDugme = new Button("Pretraži");
    Button nazadDugme = new Button("Nazad");
    
    
    //Kreiranje opisa koji ce da stoje na formi
    Label naslovForme = new Label("Pretraga");
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
    
    //kreiranje pomocnih promenljivih koje ce da se koriste u lambda izrazima
    private Statement st=null;
    private int prijemnicaId;
    
    @Override
    public void init() throws Exception {
        super.init();
        kreirajTabelu(tabelaFiltrirano);
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        
        
        //podesavanje CB-a
        tipCB.setPromptText("Izaberi tip");
        tipCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Izaberi proizvođača");
        proizvodjacCB.setMinSize(150, 25);

        
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
        deoNaziva.setPromptText("Pretraga");
        
        //podesavanje naslova forme
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
        

        tabelaFiltrirano.setMinSize(800, 500);
        boxZaTabele.setId("bottomStyle");
        boxZaTabele.setMaxSize(800, 530);
        boxZaTabele.setAlignment(Pos.CENTER_LEFT);
        boxZaTabele.getChildren().addAll(labelFiltriraneKomponente,tabelaFiltrirano);
        
        //podesavanje velicine, pozicije i izgleda panela sa dugmicima
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setPadding(new Insets(10));
        footerHB.setSpacing(30);
        nazadDugme.setId("buttonStyle");
        footerHB.setMargin(nazadDugme, new Insets(0, 0, 0, 860));
        //footerHB.getChildren().addAll(nazadDugme);
        
        //podesavanje checkbox-a za prikaz aktuelnih komponenata
        aktuelneCB.setText("Samo aktuelne");
        aktuelneCB.setSelected(true);
        //podesavanje velicine,pozicije i izgleda panela sa combobox-evima
        desniVB.setAlignment(Pos.CENTER);
        desniVB.setMinSize(200, 530);
        desniVB.setId("bottomStyle");
        desniVB.setAlignment(Pos.TOP_CENTER); //.TOP_LEFT);
        desniVB.setPadding(new Insets(10));
        desniVB.setSpacing(30);
        VBox.setMargin(nazadDugme, new Insets(430, 0, 0, 5));
        desniVB.getChildren().addAll(aktuelneCB, nazadDugme);
        
        //popunjavanje combobox-eva podacima
        ispuniComboBoxZaTip();
        ispuniComboBoxZaProizvodjaca();
        ispuniComboBoxZaDobavljaca();
        
        
        //pretrazivanje na osnovu zadatih kriterijuma
        pretragaDugme.setOnAction(e -> {
            try {
                tabelaFiltrirano.getItems().clear();
                tabelaFiltrirano.getColumns().clear();
                conn=DBUtil.napraviKonekciju();
                preuzmiPodatke();
            } catch (SQLException ex) {
                System.out.println("Problem sa očitavanjem tabele 'komponenta'!");
            }
        });
        
        
        nazadDugme.setOnAction(e ->{
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
        primaryStage.setTitle("Pretraga - UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
    private void poruka(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Greška");
        alert.setContentText(msg);
        Platform.runLater( ()-> alert.showAndWait() );
    }

    
    
    //ucitavanje vrednosti u ComboBox tipa
    public void ispuniComboBoxZaTip() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip");
                while (rs.next()) {
                    opcijeTip.add(rs.getString("naziv"));
                }
                //opcijeTip.addAll(DBUtil.prikupiPodatke(conn, "SELECT naziv FROM tip"));
            } catch (SQLException ex) {
                Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //ucitavanje vrednosti u ComboBox proizvodjaca
    public void ispuniComboBoxZaProizvodjaca() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM proizvodjac");
                while (rs.next()) {
                    opcijeProizvodjac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    //ucitavanje vrednosti u ComboBox dobavljaca
    public void ispuniComboBoxZaDobavljaca() {
        ResultSet rs=null;
        conn=DBUtil.napraviKonekciju();
        if (conn!=null) {
            try {
                rs=DBUtil.prikupiPodatke(conn, "SELECT naziv FROM dobavljac");
                while (rs.next()) {
                    opcijeDobavljac.add(rs.getString("naziv"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Pretraga.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void preuzmiPodatke() throws SQLException {
        if (conn!=null) {
            //uzimanje podataka iz combobox-eva i polja za unos teksta
            String tip=(String)tipCB.getSelectionModel().getSelectedItem();
            String proizvodjac=(String)proizvodjacCB.getSelectionModel().getSelectedItem();
            String komponenta=deoNaziva.getText();
            //formiranje SQL upita koji treba da vrati komponente koje zadovoljavaju
            //sve unete parametre (combobox-evi i uneti tekst za pretragu)
            if (tip==null) tip="";
            if (proizvodjac==null) proizvodjac="";
            String uslov="", uslovAktuelna="";
            if (aktuelneCB.isSelected())
                uslovAktuelna=" AND k.aktuelna<>0";
            if (tip.equals("") && proizvodjac.equals(""))
                uslov="WHERE k.naziv like '%" + komponenta + "%'";
            else if (tip.equals(""))
                uslov="WHERE p.naziv='" + proizvodjac + "' AND k.naziv like '%"+ komponenta + "%'";
            else if (proizvodjac.equals(""))
                uslov="WHERE t.naziv='" + tip + "' AND k.naziv like '%"+ komponenta + "%'";
            else
                uslov="WHERE t.naziv='" + tip + "' AND p.naziv='" + proizvodjac + "' AND k.naziv like '%"+ komponenta + "%'";

            String upit="SELECT k.komponenta_id, k.naziv, p.naziv, t.naziv, k.kolicina, k.cena, k.slika, k.aktuelna " +
                        "FROM komponenta as k " +
                        "INNER JOIN tip as t ON k.tip_id=t.tip_id " +
                        "INNER JOIN proizvodjac as p ON k.proizvodjac_id=p.proizvodjac_id " + uslov+uslovAktuelna;
            //postavljanje upita nad bazom podataka
            ResultSet rs=DBUtil.prikupiPodatke(conn, upit);
            //obrada rezultata upita
            if (rs!=null) {
                try {
                    podaciFiltrirano = FXCollections.observableArrayList();
                    //dodavanje kolona, naziva kolona i podesavanje sirine kolona tabele
                    if (tabelaFiltrirano.getColumns().size()==0)
                        kreirajTabelu(tabelaFiltrirano);
                    //ubacivanje podataka u listu
                    while (rs.next()) {
                        //dodavanje komponente u listu
                        podaciFiltrirano.add(new KomponentaSaSlikom(rs.getInt(1), rs.getString(2),
                                                            rs.getString(3), rs.getString(4), 
                                                            rs.getInt(5), rs.getDouble(6), rs.getString(7)));
                    }
                    //ubacivanje podataka u tabelu
                    tabelaFiltrirano.setItems(podaciFiltrirano);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn!=null) conn.close();
                }
            }
        }  
    }
    
    //otvara se uvecana slika kada se klikne na sliku u prvoj koloni tabele
    private void klikNaSliku() {
        //ocitavanje odabrane komponente iz TableView-a
        KomponentaSaSlikom komponenta = (KomponentaSaSlikom) tabelaFiltrirano.getSelectionModel().getSelectedItem();
        if (komponenta==null) return;
        //putanja do slike
        String imageFile=SLIKE_PATH + "\\" + komponenta.getSlika();
        //inicijalizacija image objekta na osnovu putanje i naziva fajla
        Image image=new Image(new File(imageFile).toURI().toString());
        ImageView imageView=new ImageView(image);
        //podesavanje velicine uvecane slike
        if (image.getHeight()>image.getWidth())
            imageView.setFitHeight(VELICINA_SLIKE-10);
        else
            imageView.setFitWidth(VELICINA_SLIKE-10);
        imageView.setPreserveRatio(true);
        //slika se smesta u StackPane
        StackPane pane=new StackPane();
        pane.getChildren().add(imageView);
        //podesavanje border-a StackPane-a
        pane.setStyle("-fx-border-color: black;-fx-border-style: solid;-fx-border-width: 5;");
        
        Scene scena = new Scene(pane, VELICINA_SLIKE, VELICINA_SLIKE);
        //podesavanje novog prozora
        Stage noviProzor=new Stage();
        noviProzor.setTitle(komponenta.getNaziv()+" "+komponenta.getProizvodjac());
        noviProzor.setScene(scena);

        scena.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        noviProzor.initStyle(StageStyle.UNDECORATED);
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();

        pane.setOnMouseClicked(f->{
            noviProzor.close();
        });
    }
    
    private void kreirajTabelu(TableView tabela) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //podesavanje kolone sa slikom komponente
        TableColumn<KomponentaSaSlikom, String> kolonaSlika = new TableColumn<>();
        Callback<TableColumn<KomponentaSaSlikom, String>, TableCell<KomponentaSaSlikom, String>> cellFactory;
        kolonaSlika.setCellValueFactory(new PropertyValueFactory("slika"));
        
        cellFactory= new Callback<TableColumn<KomponentaSaSlikom, String>, TableCell<KomponentaSaSlikom, String>>() {
            @Override
            public TableCell<KomponentaSaSlikom, String> call(TableColumn<KomponentaSaSlikom, String> param) {
                StackPane box= new StackPane();                         
                ImageView imageview = new ImageView();
                return new TableCell<KomponentaSaSlikom, String> () {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (item != null) {
                            box.setPadding(new Insets(4, 4, 4, 4));
                            Image img = null;
                            KomponentaSaSlikom k = getTableView().getItems().get(getIndex());
                            String slikaFajl=SLIKE_PATH+"\\"+k.getSlika();
                            img = new Image(new File(slikaFajl).toURI().toString());
 
                            imageview.setImage(img);
                            imageview.setFitHeight(50.0);
                            imageview.setFitWidth(50.0);
                        }
                        if(!box.getChildren().contains(imageview)) {
                        box.getChildren().add(imageview);
                        box.setOnMouseClicked(e->{
                            klikNaSliku();
                        });
                       setGraphic(box);
                       }
                   }
                };
            }
        };
        kolonaSlika.setCellFactory(cellFactory);
        kolonaSlika.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width       
        
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("id"));
        //definisanje procentualne sirine kolone
        kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("naziv"));
        kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 42); // 42% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("proizvodjac"));
        kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("tip"));
        kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 19); // 19% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("kolicina"));
        kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 7); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<Komponenta, Double>("cena"));
        kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<Komponenta, Number>() {
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

        //dodavanje kolona u tabelu
        tabela.getColumns().addAll(kolonaSlika, kolonaSifra, kolonaOpis, 
                kolonaProizvodjac, kolonaTip, kolonaKolicina, kolonaCena);
    }
    
    
}
