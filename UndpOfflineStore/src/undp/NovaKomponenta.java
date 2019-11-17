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
import javafx.geometry.Insets;
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

public class NovaKomponenta {
    
    static Font font = new Font(25);
    //Kreiranje TextFiedla za novu komponentu i novog dobavljaca
    TextField novaKomponentaNaziv = new TextField();
    //Liste za pracenje combobox-a
    ObservableList opcijeTip = FXCollections.observableArrayList();
    ObservableList opcijeProizvodjac = FXCollections.observableArrayList();
    ComboBox tipCB = new ComboBox(opcijeTip);
    ComboBox proizvodjacCB = new ComboBox(opcijeProizvodjac);


    TextField novaKomponentaKolicina = new TextField();
    TextField novaKomponentaCena = new TextField();
    TextField novaKomponentaSlika= new TextField();
    RadioButton novaKomponentaAktuelno = new RadioButton();
    Label novaKomponentaLabel = new Label("Dodavanje komponente");
    Button novaKomponentaPotvrda = new Button("Potvrdi");
    Button novaKomponentaNazad = new Button("Nazad");
    
    public void pokreni(KomponentaSvaPolja komponenta) {
        HBox headerDodajBox = new HBox();
        headerDodajBox.setId("headerBackground");
        headerDodajBox.setAlignment(Pos.CENTER);
        novaKomponentaAktuelno.setText("Aktuelna?");
        novaKomponentaAktuelno.setSelected(true);
        novaKomponentaLabel.setFont(font);
        novaKomponentaLabel.setId("headerLabel");
        headerDodajBox.getChildren().add(novaKomponentaLabel);
        headerDodajBox.setMinSize(500, 80);
        HBox centerDodajBox = new HBox();
        centerDodajBox.setId("bottomStyle");
        centerDodajBox.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
        novaKomponentaPotvrda.setId("buttonStyle");
        novaKomponentaNazad.setId("buttonStyle");
        novaKomponentaPotvrda.setMinSize(100, 25);
        novaKomponentaNazad.setMinSize(100, 25);
        HBox.setMargin(grid, new Insets(40, 0, 0, 0));
        novaKomponentaNaziv.setPromptText("Naziv");
        tipCB.setMinSize(150, 25);
        tipCB.setPromptText("Tip");
        PomocneDAO.ispuniComboBoxZaTip(opcijeTip, true);
        proizvodjacCB.setMinSize(150, 25);
        proizvodjacCB.setPromptText("Proizvođač");
        PomocneDAO.ispuniComboBoxZaProizvodjaca(opcijeProizvodjac, true);
        novaKomponentaKolicina.setPromptText("Količina");
        novaKomponentaCena.setPromptText("Cena");
        novaKomponentaSlika.setPromptText("Naziv fajla slike");
        grid.add(novaKomponentaNaziv, 0, 0);
        grid.add(proizvodjacCB, 0, 1);
        grid.add(tipCB, 0, 2);
        grid.add(novaKomponentaKolicina, 1, 0);
        grid.add(novaKomponentaCena, 1, 1);
        grid.add(novaKomponentaSlika, 1, 2);
        grid.add(novaKomponentaAktuelno, 1, 3);
        grid.add(novaKomponentaPotvrda, 2, 2);
        grid.add(novaKomponentaNazad, 2, 3);
        centerDodajBox.getChildren().addAll(grid);
        HBox footerDodajBox = new HBox();
        BorderPane dodajBox = new BorderPane();
        dodajBox.setTop(headerDodajBox);
        dodajBox.setCenter(centerDodajBox);
        dodajBox.setBottom(footerDodajBox);
        podesiFormatUnosa();
        
        //Kreiranje scene za novu komponentu
        Scene scena = new Scene(dodajBox, 500, 350);
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        Stage noviProzor=new Stage();
        noviProzor.setResizable(false);
        noviProzor.setTitle("Dodavanje Komponente");
        noviProzor.setScene(scena);
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();
        
        //klik na dugme Potvrda
        novaKomponentaPotvrda.setOnAction(ev->{
            try {
                //ukoliko je neko od obaveznih polja nepopunjeno izbaci upozorenje
                if (novaKomponentaNaziv.getText().equals("") || novaKomponentaKolicina.getText().equals("") ||
                        novaKomponentaCena.getText().equals("") || proizvodjacCB.getSelectionModel().getSelectedIndex()<0 ||
                        tipCB.getSelectionModel().getSelectedIndex()<0) 
                    throw new Exception();
                    
                //kreiranje nove komponente
                komponenta.setNaziv(novaKomponentaNaziv.getText());
                komponenta.setTip((String)tipCB.getSelectionModel().getSelectedItem());
                komponenta.setProizvodjac((String)proizvodjacCB.getSelectionModel().getSelectedItem());
                komponenta.setKolicina(Integer.valueOf(novaKomponentaKolicina.getText()));
                komponenta.setCena(Double.valueOf(novaKomponentaCena.getText()));
                //komponenta.setSlika(novaKomponentaSlika.getText());
                komponenta.setSlika(new Slika(novaKomponentaSlika.getText()));
                komponenta.setAktuelna(novaKomponentaAktuelno.isSelected());

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

                int ubaceno=0;
                if (duplikat==0) {
                    //ubaci komponentu u bazu podataka
                    ubaceno=DBUtil.ubaciKomponentu(komponenta, proizvodjacId, tipId);
                    if (ubaceno>0) {
                        alert2.setContentText("Komponenta uspešno uneta u bazu podataka!");
                        //povratak na formu Nabavka
                        novaKomponentaNazad.fire();
                    }
                } 
                if (duplikat>0)
                    alert2.setContentText("Komponenta sa istim nazivom, proizvodjacem i tipom vec postoji u bazi podataka!");
                else if (ubaceno==0)
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
        
        
        //povratak na nabavku sa forme za unosenje nove komponente
        novaKomponentaNazad.setOnAction(ev ->{
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
        novaKomponentaCena.setTextFormatter(formatterDouble);
        
        //kolicina se formatira kao int: samo cifre
        Pattern patternInt = Pattern.compile("[0-9]*");
        TextFormatter formatterInt = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternInt.matcher(change.getControlNewText()).matches() ? change : null;
        });
        novaKomponentaKolicina.setTextFormatter(formatterInt);
    }
}