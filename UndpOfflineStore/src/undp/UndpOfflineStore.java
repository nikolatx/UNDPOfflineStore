package undp;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import pomocne.ServisKonekcija;

public class UndpOfflineStore extends Application {
    //dugmici za realizaciju pojedinih funkcija
    Button btn1 = new Button("Pretraga");
    Button btn2 = new Button("Nabavka");
    Button btn3 = new Button("Prodaja");
    Button btn4 = new Button("Izveštaji");
    Button btn5 = new Button("Ažuriranje");

    //Kreiranje HBox panela za popunjvenje scene
    HBox hb1 = new HBox();
    HBox hb2 = new HBox();
    HBox hb3 = new HBox();

    //Kreiranje tekstualnih labela i fonta
    Label lab1 = new Label("UNDP Offline Store");
    Font font = new Font(25);
    ServisKonekcija servisKonekcija=new ServisKonekcija();
    
        
    @Override
    public void start(Stage primaryStage) {
        
              
        //Podesavanje velicine , izgleda i dodavanje nodova na Hbox
        hb1.setMinSize(800, 120);
        lab1.setId("headerLabel");       
        hb1.setId("headerBackground");    //Css - Style: podesavanje izgleda
        hb1.setAlignment(Pos.CENTER);
        lab1.setFont(font);
        hb1.getChildren().add(lab1);
        
        
        ////Podesavanje velicine , izgleda i dodavanje nodova na Hbox;
        FadeTransition fade= new FadeTransition();
        fade.setDuration(Duration.millis(3500));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.setNode(hb2);
        fade.play();
        hb2.setMinSize(800, 350);
        hb2.setAlignment(Pos.CENTER);
        hb2.setId("centerPicture");      //Css - Style: podesavanje izgleda
        
        //Podesavanje velicine , izgleda i dodavanje nodova na Hbox
        hb3.setMinSize(800, 130);
        hb3.setAlignment(Pos.CENTER);
        btn1.setId("buttonStyle");
        btn2.setId("buttonStyle");
        btn3.setId("buttonStyle");       //Css - Style: podesavanje izgleda
        btn4.setId("buttonStyle");
        btn5.setId("buttonStyle");
        hb3.getChildren().addAll(btn1,btn2,btn3,btn4,btn5);
        hb3.setSpacing(40);
        hb3.setId("bottomStyle");       //Css - Style: podesavanje izgleda
        
        //Kreiranje BorderPane-a za rasporedjivanje HBoc panela na sceni
        BorderPane root = new BorderPane();
        root.setTop(hb1);
        root.setCenter(hb2);
        root.setBottom(hb3);
        
        
        //taster btn1 sa funkcijom (prelazak na Pretragu)
        btn1.setOnAction(e ->{
            primaryStage.close();
            try {
                new Pretraga().start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(UndpOfflineStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        //Taster btn2 sa akcijom (prelazak na nabavku)
        btn2.setOnAction(e ->{
            try {
                primaryStage.close();
                new Nabavka().start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(UndpOfflineStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        //Taster btn3 sa akcijom (prelazak na stage Prodaja)
        btn3.setOnAction(e ->{
            try {
                primaryStage.close();
                new Prodaja().start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(UndpOfflineStore.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        
        //taster btn4 sa akcijom (prelak na stage Izvestaji)
        btn4.setOnAction(e ->{
            primaryStage.close();
            new Izvestaji().start(primaryStage);
            
        });
        
        
        //taster btn4 sa akcijom (prelak na stage Azuriranje)
        btn5.setOnAction(e ->{
            try {
                primaryStage.close();
                new Azuriranje().start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(UndpOfflineStore.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        servisKonekcija.start();
        servisKonekcija.setOnSucceeded(e->{
            if (servisKonekcija.getValue()==null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Greška");
                alert.setHeaderText("MySQL server nije pokrenut! Pokreni server!");
                alert.setContentText("Izaberi opciju");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
                dialogPane.getStyleClass().add("dialogPane");
                ButtonType dugmePonovo = new ButtonType("Pokušaj ponovo");
                ButtonType dugmeOdustani = new ButtonType("Napusti aplikaciju", ButtonBar.ButtonData.CANCEL_CLOSE);
                //alert box za odabir opcije
                alert.getButtonTypes().setAll(dugmePonovo, dugmeOdustani);
                Platform.runLater(()->{
                    alert.showAndWait();
                    if (alert.getResult() == dugmePonovo) {
                        servisKonekcija.reset();
                        servisKonekcija.restart();
                    }
                    else 
                        Platform.exit();
                    alert.close();
                });
            }
        });
        
        
        //Kreiranje scene,velicine,naziva i povezivanje sa css-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    
}
