package undp;

import com.grupa1.dbconnection.DBUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UndpOfflineStore extends Application {
        //dugmici za realizaciju pojedinih funkcija
        Button btn1 = new Button("Pretraga");
        Button btn2 = new Button("Nabavka");
        Button btn3 = new Button("Prodaja");
        Button btn4 = new Button("Izvestaji");
        Button btn5 = new Button("Azuriranje");
        
        //Kreiranje HBox panela za popunjvenje scene
        HBox hb1 = new HBox();
        HBox hb2 = new HBox();
        HBox hb3 = new HBox();
        
        //Kreiranje tekstualnih labela i fonta
        Label lab1 = new Label("UNDP OfflineStore");
        Font fotn = new Font(25);
       
        
    @Override
    public void start(Stage primaryStage) {
        
              
        //Podesavanje velicine , izgleda i dodavanje nodova na Hbox
        hb1.setMinSize(800, 120);
        lab1.setId("headerLabel");       
        hb1.setId("headerBackground");    //Css - Style: podesavanje izgleda
        hb1.setAlignment(Pos.CENTER);
        lab1.setFont(fotn);
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
        hb3.setSpacing(30);
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
            
        });
        
        
        //taster btn4 sa akcijom (prelak na stage Izvestaji)
        btn4.setOnAction(e ->{
            
        });
        
        
        //taster btn4 sa akcijom (prelak na stage Azuriranje)
        btn5.setOnAction(e ->{
            
        });
        
        
        //Kreiranje scene,velicine,naziva i povezivanje sa css-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    
}
