
package undp;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.Osoba;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NoviKupac {
    
    static Font font = new Font(25);
    TextField noviKupacNaziv = new TextField();
    TextField noviKupacUlica = new TextField();
    TextField noviKupacBroj = new TextField();
    TextField noviKupacGrad = new TextField();
    TextField noviKupacPostBroj = new TextField();
    TextField noviKupacDrzava = new TextField();
    TextField noviKupacTelefon = new TextField();   
    Button noviKupacPotvrda = new Button("Dodaj");
    Button noviKupacNazad = new Button("Nazad");
    Label noviKupacNaslov = new Label("Dodavanje komponente");
    
    
    public void pokreni(Osoba kupac, ComboBox kupacCB) {
        HBox headerDodajBox = new HBox();
        headerDodajBox.setId("headerBackground");
        headerDodajBox.setAlignment(Pos.CENTER);
        noviKupacNaslov.setFont(font);
        noviKupacNaslov.setId("headerLabel");
        headerDodajBox.getChildren().add(noviKupacNaslov);
        headerDodajBox.setMinSize(500, 80);
        HBox centerDodajBox = new HBox();
        centerDodajBox.setId("bottomStyle");
        centerDodajBox.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
        noviKupacPotvrda.setId("buttonStyle");
        noviKupacNazad.setId("buttonStyle");
        noviKupacPotvrda.setMinSize(100, 25);
        noviKupacNazad.setMinSize(100, 25);
        HBox.setMargin(grid, new Insets(40, 0, 0, 0));
        noviKupacNaziv.setPromptText("Naziv");
        noviKupacUlica.setPromptText("Ulica");
        noviKupacBroj.setPromptText("Broj");
        noviKupacGrad.setPromptText("Grad");
        noviKupacPostBroj.setPromptText("Poštanski Broj");
        noviKupacDrzava.setPromptText("Država");
        noviKupacTelefon.setPromptText("Telefon");
        grid.add(noviKupacNaziv, 0, 0);
        grid.add(noviKupacUlica, 0, 1);
        grid.add(noviKupacBroj, 0, 2);
        grid.add(noviKupacGrad, 1, 0);
        grid.add(noviKupacPostBroj, 1, 1);
        grid.add(noviKupacDrzava, 1, 2);
        grid.add(noviKupacTelefon, 0, 3);
        grid.add(noviKupacPotvrda, 1, 3);
        grid.add(noviKupacNazad, 2, 3);
        centerDodajBox.getChildren().addAll(grid);
        HBox footerDodajBox = new HBox();
        BorderPane dodajBox = new BorderPane();
        dodajBox.setTop(headerDodajBox);
        dodajBox.setCenter(centerDodajBox);
        dodajBox.setBottom(footerDodajBox);
        podesiFormatUnosa();
        
        //Kreiranje scene za novog kupaca
        Scene scena = new Scene(dodajBox, 500, 350); 
        Stage noviProzor=new Stage();
        noviProzor.setResizable(false);
        noviProzor.setTitle("Dodavanje novog kupaca");
        
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        
        noviProzor.setScene(scena);
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();
        
        //klik na dugme Potvrda
        noviKupacPotvrda.setOnAction(ev->{
            try {
                //ukoliko je neko od obaveznih polja nepopunjeno izbaci upozorenje
                if (noviKupacNaziv.getText().equals("") || noviKupacUlica.getText().equals("") ||
                        noviKupacBroj.getText().equals("") || noviKupacGrad.getText().equals("") ||
                        noviKupacPostBroj.getText().equals("") || noviKupacDrzava.getText().equals("")) 
                    throw new Exception();
                    
                //kreiranje nove komponente
                kupac.setNaziv(noviKupacNaziv.getText());
                kupac.setUlica(noviKupacUlica.getText());
                kupac.setBroj(noviKupacBroj.getText());
                kupac.setGrad(noviKupacGrad.getText());
                kupac.setPostBr(noviKupacPostBroj.getText());
                kupac.setDrzava(noviKupacDrzava.getText());
                kupac.setTelefon(noviKupacTelefon.getText());

                //proveri da ne postoji komponenta sa istim nazivom, istog tipa i proizvodjaca
                int duplikat=DBUtil.proveriDuplikatKupca(kupac.getNaziv(),
                        kupac.getUlica(), kupac.getBroj(), kupac.getGrad());

                //priprema obavestenja
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Obaveštenje");
                alert2.setHeaderText(null);

                int ubaceno=0;
                if (duplikat==0) {
                    //ubaci kupaca u bazu podataka
                    ubaceno=DBUtil.ubaciKupca(kupac);
                    if (ubaceno>0) {
                        alert2.setContentText("Kupac uspešno unet u bazu podataka!");
                        Platform.runLater(()->{
                            //zaustavi setOnAction handler da bi se dodao novi tip u combobox
                            EventHandler<ActionEvent> handler = kupacCB.getOnAction();
                            kupacCB.setOnAction(null);
                            //dodaj novi tip u cobobox
                            kupacCB.getItems().add(kupacCB.getItems().size()-1, kupac.getNaziv() + ", " + kupac.getGrad());
                            //selektuj novi tip u comboboxu
                            kupacCB.getSelectionModel().select(kupacCB.getItems().size()-2);
                            //ukljuci handler setOnAction eventa
                            kupacCB.setOnAction(handler);
                        });
                        //povratak na formu Nabavka
                        noviKupacNazad.fire();
                    }
                } 
                if (duplikat>0)
                    alert2.setContentText("Kupac sa istim nazivom, ulicom, brojem i gradom vec postoji u bazi podataka!");
                else if (ubaceno==0)
                    alert2.setContentText("Došlo je do greške pri upisu u bazu podataka!");
                Platform.runLater( () -> {
                    alert2.showAndWait();
                });
            } catch (Exception ex) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Obaveštenje");
                alert1.setHeaderText(null);
                alert1.setContentText("Sva polja osim telefona moraju biti popunjena!");
                Platform.runLater( () -> {
                    alert1.showAndWait();
                });
            }
            
        });

        //povratak na nabavku sa forme za unosenje nove komponente
        noviKupacNazad.setOnAction(ev ->{
            noviProzor.close();
        });
        
    }

    //podesava format unosa za cenu i kolicinu
    public void podesiFormatUnosa() {
        //naziv: eliminisati , zbog razdvajanja naziva i grada iz comboboxa
        Pattern patternN = Pattern.compile("^[a-zA-Z0-9 ()+_-]*$");
        TextFormatter formatterNaziv = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternN.matcher(change.getControlNewText()).matches() ? change : null;
        });
        noviKupacNaziv.setTextFormatter(formatterNaziv);
        Pattern patternG = Pattern.compile("^[a-zA-Z0-9 ()+_-]*$");
        TextFormatter formatterGrad = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternG.matcher(change.getControlNewText()).matches() ? change : null;
        });
        noviKupacGrad.setTextFormatter(formatterGrad);
        
        //kolicina se formatira kao int: samo cifre
        Pattern patternTel = Pattern.compile("^[0-9 ()+-]*$");
        TextFormatter formatterTel = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternTel.matcher(change.getControlNewText()).matches() ? change : null;
        });
        noviKupacTelefon.setTextFormatter(formatterTel);
    }
}
