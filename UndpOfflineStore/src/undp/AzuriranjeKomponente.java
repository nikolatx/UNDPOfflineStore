package undp;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.KomponentaSvaPolja;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.grupa1.dbconnection.PomocneDAO;
import com.grupa1.model.Slika;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.FileChooser;
import konstante.Konst;

public class AzuriranjeKomponente {
    
    static Font font = new Font(25);
    
    //Liste za pracenje combobox-a
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);

    //Kreiranje TextFieldova
    TextField id = new TextField();
    TextField naziv = new TextField();
    TextField kolicina = new TextField();
    TextField cena = new TextField();
    TextField slika= new TextField();
    RadioButton aktuelno = new RadioButton();
    Label naslovForme = new Label("Ažuriranje komponente");
    Button potvrdiDugme = new Button("Potvrdi");
    Button nazadDugme = new Button("Nazad");
    Button slikaDugme = new Button("...");
    
    Label labId = new Label("Šifra:");
    Label labNaziv = new Label("Naziv:");
    Label labTip = new Label("Tip:");
    Label labProizvodjac = new Label("Proizvođač:");
    Label labKolicina = new Label("Količina:");
    Label labCena = new Label("Cena:");
    Label labSlika = new Label("Slika:");
    Label labAktuelna = new Label("Aktuelna:");
    
    public void pokreni(KomponentaSvaPolja komponenta, ObservableList<KomponentaSvaPolja> podaci) {
        //podesavanje header-a i dodavanje naslova
        HBox headerHB = new HBox();
        headerHB.setId("headerBackground");
        headerHB.setAlignment(Pos.CENTER);
        headerHB.setMinSize(600, 80);
        naslovForme.setId("headerLabel");
        naslovForme.setFont(font);
        headerHB.getChildren().add(naslovForme);
        
        //imamo posebnu labelu pa nam ne treba tekst uz kontrolu
        aktuelno.setText("");
        
        
        HBox srednjiHB = new HBox();
        srednjiHB.setAlignment(Pos.CENTER);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        
        //Podesavanje velicine,izgleda i dodavanje nodova na scenu
        potvrdiDugme.setId("buttonStyle");
        potvrdiDugme.setMinSize(100, 25);
        nazadDugme.setId("buttonStyle");
        nazadDugme.setMinSize(100, 25);
        
        //setovanje kontrola na vrednosti svojstava komponente
        id.setText(String.valueOf(komponenta.getId()));
        id.setEditable(false);
        naziv.setText(komponenta.getNaziv());
        tipCB.setMinSize(150, 25);
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, true);
        tipCB.getSelectionModel().select(komponenta.getTip());
        proizvodjacCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Proizvođač");
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, true);
        proizvodjacCB.getSelectionModel().select(komponenta.getProizvodjac());
        kolicina.setText(String.valueOf(komponenta.getKolicina()));
        String str2=String.format("%,.2f", komponenta.getCena());
        cena.setText(str2);
        slika.setText(((Slika)komponenta.getSlika()).getNaziv());
        aktuelno.setSelected(komponenta.getAktuelna());
        
        //ubacivanje kontrola u gridpane
        grid.add(labId, 0, 0);
        grid.add(id, 1, 0);
        grid.add(labNaziv, 0, 1);
        grid.add(naziv, 1, 1);
        grid.add(labProizvodjac, 0, 2);
        grid.add(proizvodjacCB, 1, 2);
        grid.add(labTip, 0, 3);
        grid.add(tipCB, 1, 3);
        grid.add(labKolicina, 0, 4);
        grid.add(kolicina, 1, 4);
        grid.add(labCena, 0, 5);
        grid.add(cena, 1, 5);
        grid.add(labSlika, 0, 6);
        grid.add(slika, 1, 6);
        grid.add(slikaDugme, 2, 6);
        grid.add(labAktuelna, 0, 7);
        grid.add(aktuelno, 1, 7);
        
        //podesavanje sirine kolona grida
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col1.setMinWidth(100);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        grid.getColumnConstraints().addAll(col1,col2);
        grid.setPadding(new Insets(10));
        srednjiHB.getChildren().add(grid);
        
        //podesavanje futera i dodavanje dugmica
        HBox footerHB = new HBox();
        footerHB.setAlignment(Pos.CENTER);
        footerHB.setSpacing(40);
        footerHB.setPadding(new Insets(10));
        footerHB.getChildren().addAll(potvrdiDugme, nazadDugme);
        footerHB.setMinWidth(600);
        
        //dodavanje kontrola u desni deo prozora
        BorderPane izmeniBP = new BorderPane();
        izmeniBP.setTop(headerHB);
        izmeniBP.setCenter(srednjiHB);
        izmeniBP.setBottom(footerHB);
        podesiFormatUnosa();
        
        //Kreiranje scene
        Scene scena = new Scene(izmeniBP, 600, 430);
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        Stage noviProzor=new Stage();
        noviProzor.getIcons().add(new Image("/resources/logo.jpg"));
        noviProzor.setResizable(false);
        noviProzor.setTitle("Dodavanje komponente");
        noviProzor.setScene(scena);
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();
        
        //dugme za izbor slike
        slikaDugme.setOnAction(ev->{
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(Konst.SLIKE_PATH));
            File file = fileChooser.showOpenDialog(noviProzor);
            if (file != null)
                slika.setText(file.getName());
        });
        
        //klik na dugme Potvrda
        potvrdiDugme.setOnAction(ev->{
            try {
                //ukoliko je neko od obaveznih polja nepopunjeno izbaci upozorenje
                if (naziv.getText().trim().equals("") || kolicina.getText().trim().equals("") ||
                        cena.getText().trim().equals("") || proizvodjacCB.getSelectionModel().getSelectedIndex()<0 ||
                        tipCB.getSelectionModel().getSelectedIndex()<0) 
                    throw new Exception();
                    
                //kreiranje nove komponente
                komponenta.setNaziv(naziv.getText().trim());
                komponenta.setTip((String)tipCB.getSelectionModel().getSelectedItem());
                komponenta.setProizvodjac((String)proizvodjacCB.getSelectionModel().getSelectedItem());
                int kolicinaInt=Integer.valueOf(kolicina.getText());
                komponenta.setKolicina(kolicinaInt);
                
                DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance();
                DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
                String decSep=String.valueOf(symbols.getDecimalSeparator());
                String thousandSep=(decSep.equals(".")?",":".");
                String cena1=cena.getText().replaceAll(thousandSep, "");
                cena.setText(cena1);
                komponenta.setCena(Double.valueOf(cena.getText()));
                komponenta.setSlika(new Slika(slika.getText().trim()));
                komponenta.setAktuelna(aktuelno.isSelected());
                komponenta.setId(Integer.valueOf(id.getText()));
                
                int indeks=podaci.indexOf(komponenta);
                podaci.set(indeks, komponenta);
                
                //ocitavanje proizvodjacID polja iz baze
                List<Integer> tempList=new ArrayList<>();
                String upit1="SELECT proizvodjac_id FROM proizvodjac WHERE naziv=?";
                tempList=DBUtil.prikupiPodatkeParam(upit1, komponenta.getProizvodjac(), "proizvodjac_id");
                int proizvodjacId=tempList.get(0);

                //ocitavanje tipID polja iz baze
                String upit2="SELECT tip_id FROM tip WHERE naziv=?";
                tempList=DBUtil.prikupiPodatkeParam(upit2, komponenta.getTip(), "tip_id");
                int tipId=tempList.get(0);

                //proveri da ne postoji komponenta sa istim nazivom, istog tipa i proizvodjaca
                int duplikat=DBUtil.proveriDuplikatKomponente(komponenta.getNaziv(), proizvodjacId, tipId);

                //priprema obavestenja
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setTitle("Obaveštenje");
                alert2.setHeaderText(null);

                int promenjeno=0;
                if (duplikat<=1) {
                    //promeni komponentu u bazi podataka
                    promenjeno=DBUtil.izmeniKomponentu(komponenta, proizvodjacId, tipId);
                    if (promenjeno>0) {
                        alert2.setContentText("Promena uspešno izvršena!");
                        
                        //povratak na formu Azuriranje
                        nazadDugme.fire();
                    }
                } 
                else if (duplikat>1)
                    alert2.setContentText("Komponenta sa istim nazivom, proizvođačem i tipom već postoji u bazi podataka!");
                else if (promenjeno==0)
                    alert2.setContentText("Došlo je do greške pri upisu u bazu podataka!");
                Platform.runLater( () -> {
                    alert2.showAndWait();
                });
                    
            } catch (Exception ex) {
                Alert alert1 = new Alert(AlertType.INFORMATION);
                alert1.setTitle("Obaveštenje");
                alert1.setHeaderText(null);
                alert1.setContentText("Sva polja osim naziva fajla slike moraju biti popunjena!");
                Platform.runLater( () -> {
                    alert1.showAndWait();
                });
            }
        });
        
        //dodavanje novog tipa
        tipCB.setOnAction(e->{
            if (tipCB.getSelectionModel().getSelectedItem().equals(">>dodaj novi<<")) {
                
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Novi tip");
                dialog.setHeaderText("Unošenje novog tipa komponente");
                dialog.setContentText("Tip:");
                
                Platform.runLater(() -> {
                    Optional<String> result = dialog.showAndWait();
                    //ako nije pritisnuto Cancel dugme
                    if (result.isPresent()){
                        //preuzmi naziv tipa
                        String naziv = result.get();
                        //upis u bazu podataka
                        if (!naziv.isEmpty()) {
                            //provera da li vec postoji
                            String upit2="SELECT tip_id FROM tip WHERE UPPER(naziv)=UPPER(?)";
                            List<Integer> tempList=new ArrayList<>();
                            tempList=DBUtil.prikupiPodatkeParam(upit2, naziv, "tip_id");
                            int dodatTip=0;
                            Alert alert1 = new Alert(AlertType.INFORMATION);
                            alert1.setTitle("Obaveštenje");
                            alert1.setHeaderText(null);
                            //ne sme biti prazan
                            if (!naziv.isEmpty())
                                //ne sme vec da postoji u bazi
                                if (tempList.isEmpty()) {
                                    dodatTip=DBUtil.dodajProizvodjacaIliTip("tip", naziv);
                                    //ako nije doslo do greske pri upisu nastavi
                                    if (dodatTip>0) {
                                        alert1.setContentText("Novi tip uspešno unet u bazu podataka!");
                                        Platform.runLater(()->{
                                            //zaustavi setOnAction handler da bi se dodao novi tip u combobox
                                            EventHandler<ActionEvent> handler = tipCB.getOnAction();
                                            tipCB.setOnAction(null);
                                            //dodaj novi tip u cobobox
                                            tipCB.getItems().add(tipCB.getItems().size()-1, naziv);
                                            //selektuj novi tip u comboboxu
                                            tipCB.getSelectionModel().select(tipCB.getItems().size()-2);
                                            //ukljuci handler setOnAction eventa
                                            tipCB.setOnAction(handler);
                                        });
                                    } else 
                                        alert1.setContentText("Došlo je do greške pri unosu tipa u bazu podataka!");
                                } else
                                    alert1.setContentText("Tip vec postoji u bazi podataka!");
                            else
                                alert1.setContentText("Naziv ne sme biti prazan string!");
                            alert1.showAndWait();
                        }
                    }
                });
            }
        });
        
        //dodavanje novog proizvodjaca
        proizvodjacCB.setOnAction(e->{
            if (proizvodjacCB.getSelectionModel().getSelectedItem().equals(">>dodaj novog<<")) {
                
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Novi proizvođač");
                dialog.setHeaderText("Unošenje novog proizvođača");
                dialog.setContentText("Proizvođač:");
                
                Platform.runLater(() -> {
                    Optional<String> result = dialog.showAndWait();
                    //ako nije pritisnuto Cancel dugme
                    if (result.isPresent()){
                        //preuzmi naziv tipa
                        String naziv = result.get();
                        //upis u bazu podataka
                        if (!naziv.isEmpty()) {
                            //provera da li vec postoji
                            String upit2="SELECT proizvodjac_id FROM proizvodjac WHERE UPPER(naziv)=UPPER(?)";
                            List<Integer> tempList=new ArrayList<>();
                            tempList=DBUtil.prikupiPodatkeParam(upit2, naziv, "proizvodjac_id");
                            int dodatProizvodjac=0;
                            Alert alert1 = new Alert(AlertType.INFORMATION);
                            alert1.setTitle("Obaveštenje");
                            alert1.setHeaderText(null);
                            //ne sme biti prazan
                            if (!naziv.isEmpty())
                                //ne sme vec da postoji u bazi
                                if (tempList.isEmpty()) {
                                    dodatProizvodjac=DBUtil.dodajProizvodjacaIliTip("proizvodjac", naziv);
                                    //ako nije doslo do greske pri upisu nastavi
                                    if (dodatProizvodjac>0) {
                                        alert1.setContentText("Novi proizvođač uspešno unet u bazu podataka!");
                                        Platform.runLater(()->{
                                            //zaustavi setOnAction handler da bi se dodao novi tip u combobox
                                            EventHandler<ActionEvent> handler = proizvodjacCB.getOnAction();
                                            proizvodjacCB.setOnAction(null);
                                            //dodaj novi tip u cobobox
                                            proizvodjacCB.getItems().add(proizvodjacCB.getItems().size()-1, naziv);
                                            //selektuj novi tip u comboboxu
                                            proizvodjacCB.getSelectionModel().select(proizvodjacCB.getItems().size()-2);
                                            //ukljuci handler setOnAction eventa
                                            proizvodjacCB.setOnAction(handler);
                                        });
                                    } else 
                                        alert1.setContentText("Došlo je do greške pri unosu proizvođača u bazu podataka!");
                                } else
                                    alert1.setContentText("Proizvođač vec postoji u bazi podataka!");
                            else
                                alert1.setContentText("Naziv ne sme biti prazan string!");
                            alert1.showAndWait();
                        }
                    }
                });
            }
        });
        
        //povratak na glavnu formu za azuriranje
        nazadDugme.setOnAction(ev ->{
            noviProzor.close();
        });
    }
    
    //podesava format unosa za cenu i kolicinu
    public void podesiFormatUnosa() {
        //cena se formatira kao double: cifre, decimalni zarez, 2 decimale
        Pattern patternDouble = Pattern.compile("-?\\d*(\\.\\d{0,2})?");
        TextFormatter formatterDouble = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternDouble.matcher(change.getControlNewText()).matches() ? change : null;
        });
        cena.setTextFormatter(formatterDouble);
        
        //kolicina se formatira kao int: samo cifre
        Pattern patternInt = Pattern.compile("[0-9]*");
        TextFormatter formatterInt = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternInt.matcher(change.getControlNewText()).matches() ? change : null;
        });
        kolicina.setTextFormatter(formatterInt);
    }
}