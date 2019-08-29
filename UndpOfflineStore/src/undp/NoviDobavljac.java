
package undp;

import com.grupa1.dbconnection.DBUtil;
import com.grupa1.model.Osoba;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NoviDobavljac {
    
    static Font font = new Font(25);
    static TextField noviDobavljacNaziv = new TextField();
    static TextField noviDobavljacUlica = new TextField();
    static TextField noviDobavljacBroj = new TextField();
    static TextField noviDobavljacGrad = new TextField();
    static TextField noviDobavljacPostBroj = new TextField();
    static TextField noviDobavljacDrzava = new TextField();
    static TextField noviDobavljacTelefon = new TextField();   
    static Button noviDobavljacPotvrda = new Button("Dodaj");
    static Button noviDobavljacNazad = new Button("Nazad");
    static Label noviDobavljacNaslov = new Label("Dodavanje komponente");
    
    
    public static void pokreni(Osoba dobavljac, ComboBox dobavljacCB) {
        HBox headerDodajBox = new HBox();
        headerDodajBox.setId("headerBackground");
        headerDodajBox.setAlignment(Pos.CENTER);
        noviDobavljacNaslov.setFont(font);
        noviDobavljacNaslov.setId("headerLabel");
        headerDodajBox.getChildren().add(noviDobavljacNaslov);
        headerDodajBox.setMinSize(500, 80);
        HBox centerDodajBox = new HBox();
        centerDodajBox.setId("bottomStyle");
        centerDodajBox.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
        noviDobavljacPotvrda.setId("buttonStyle");
        noviDobavljacNazad.setId("buttonStyle");
        noviDobavljacPotvrda.setMinSize(100, 25);
        noviDobavljacNazad.setMinSize(100, 25);
        HBox.setMargin(grid, new Insets(40, 0, 0, 0));
        noviDobavljacNaziv.setPromptText("Naziv");
        noviDobavljacUlica.setPromptText("Ulica");
        noviDobavljacBroj.setPromptText("Broj");
        noviDobavljacGrad.setPromptText("Grad");
        noviDobavljacPostBroj.setPromptText("Poštanski Broj");
        noviDobavljacDrzava.setPromptText("Država");
        noviDobavljacTelefon.setPromptText("Telefon");
        grid.add(noviDobavljacNaziv, 0, 0);
        grid.add(noviDobavljacUlica, 0, 1);
        grid.add(noviDobavljacBroj, 0, 2);
        grid.add(noviDobavljacGrad, 1, 0);
        grid.add(noviDobavljacPostBroj, 1, 1);
        grid.add(noviDobavljacDrzava, 1, 2);
        grid.add(noviDobavljacTelefon, 0, 3);
        grid.add(noviDobavljacPotvrda, 1, 3);
        grid.add(noviDobavljacNazad, 2, 3);
        centerDodajBox.getChildren().addAll(grid);
        HBox footerDodajBox = new HBox();
        BorderPane dodajBox = new BorderPane();
        dodajBox.setTop(headerDodajBox);
        dodajBox.setCenter(centerDodajBox);
        dodajBox.setBottom(footerDodajBox);

        //Kreiranje scene za novog dobavljaca
        Scene scena = new Scene(dodajBox, 500, 350); 
        Stage noviProzor=new Stage();
        noviProzor.setResizable(false);
        noviProzor.setTitle("Dodavanje novog dobavljaca");
        
        scena.getStylesheets().add("styles.css");
        
        noviProzor.setScene(scena);
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();
        
        //klik na dugme Potvrda
        noviDobavljacPotvrda.setOnAction(ev->{
            try {
                //ukoliko je neko od obaveznih polja nepopunjeno izbaci upozorenje
                if (noviDobavljacNaziv.getText().equals("") || noviDobavljacUlica.getText().equals("") ||
                        noviDobavljacBroj.getText().equals("") || noviDobavljacGrad.getText().equals("") ||
                        noviDobavljacPostBroj.getText().equals("") || noviDobavljacDrzava.getText().equals("")) 
                    throw new Exception();
                    
                //kreiranje nove komponente
                dobavljac.setNaziv(noviDobavljacNaziv.getText());
                dobavljac.setUlica(noviDobavljacUlica.getText());
                dobavljac.setBroj(noviDobavljacBroj.getText());
                dobavljac.setGrad(noviDobavljacGrad.getText());
                dobavljac.setPostBr(noviDobavljacPostBroj.getText());
                dobavljac.setDrzava(noviDobavljacDrzava.getText());
                dobavljac.setTelefon(noviDobavljacTelefon.getText());

                //proveri da ne postoji komponenta sa istim nazivom, istog tipa i proizvodjaca
                int duplikat=DBUtil.proveriDuplikatDobavljaca(dobavljac.getNaziv(),
                        dobavljac.getUlica(), dobavljac.getBroj(), dobavljac.getGrad());

                //priprema obavestenja
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Obaveštenje");
                alert2.setHeaderText(null);

                int ubaceno=0;
                if (duplikat==0) {
                    //ubaci dobavljaca u bazu podataka
                    ubaceno=DBUtil.ubaciDobavljaca(dobavljac);
                    if (ubaceno>0) {
                        alert2.setContentText("Dobavljač uspešno unet u bazu podataka!");
                        Platform.runLater(()->{
                            //zaustavi setOnAction handler da bi se dodao novi tip u combobox
                            EventHandler<ActionEvent> handler = dobavljacCB.getOnAction();
                            dobavljacCB.setOnAction(null);
                            //dodaj novi tip u cobobox
                            dobavljacCB.getItems().add(dobavljacCB.getItems().size()-1, dobavljac.getNaziv() + ", " + dobavljac.getGrad());
                            //selektuj novi tip u comboboxu
                            dobavljacCB.getSelectionModel().select(dobavljacCB.getItems().size()-2);
                            //ukljuci handler setOnAction eventa
                            dobavljacCB.setOnAction(handler);
                        });
                        //povratak na formu Nabavka
                        noviDobavljacNazad.fire();
                    }
                } 
                if (duplikat>0)
                    alert2.setContentText("Dobavljač sa istim nazivom, ulicom, brojem i gradom vec postoji u bazi podataka!");
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
        noviDobavljacNazad.setOnAction(ev ->{
            noviProzor.close();
        });
        
    }

    
}
