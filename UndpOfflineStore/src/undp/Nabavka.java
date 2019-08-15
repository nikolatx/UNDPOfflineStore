package undp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Nebojsa
 */
public class Nabavka extends Application {
    //Kreiranje Labela i  Fonta sa tekstom
    Label lab1 = new Label("Nabavka komponenti");
    Label lab2 = new Label("Tip komponente: ");
    Label lab3 = new Label("Proizvodjac: ");
    Label lab4 = new Label("Model komponente: ");
    Label lab5 = new Label("Kolicina: ");
    Label lab6 = new Label("Datum: ");
    Font font = new Font(25);
    
    TextField tf1 = new TextField();
    TextField tf2 = new TextField();
    TextField tf3 = new TextField();
    TextField tf4 = new TextField();
    DatePicker dp = new DatePicker();
    
    
    
    
    //Kreiranje HBox panela za scenu
    HBox hb1 = new HBox();
    HBox hb2 = new HBox();
    HBox hb3 = new HBox();
    
    //kreiranje tastera za odredjene akcije
    Button btn1 = new Button("Potvrdi");
    Button btn2 = new Button("Nazad");
    Button btn3 = new Button("PDF");
    Button btn4 = new Button("Dodaj");
    
    
    
    
    
    
    @Override
    public void start(Stage primaryStage) {
        
        //podesavanje velicine,pozicije i izgleda hb1 - panela
        hb1.setMinSize(800, 120);
        hb1.setAlignment(Pos.CENTER);
        lab1.setFont(font);
        lab1.setId("headerLabel");    //Css - Style: podesavanje izgleda
        hb1.getChildren().add(lab1);
        hb1.setId("headerBackground");    //Css - Style: podesavanje izgleda
        
        
        
        
        hb2.setMinSize(800, 390);
        hb2.setAlignment(Pos.CENTER);
       
        
        
        
        //podesavanje velicine,pozicije i izgleda hb3 - panela
        hb3.setAlignment(Pos.BASELINE_RIGHT);
        hb3.setMinSize(800, 70);
        hb3.setId("bottomBack");
        btn1.setId("buttonNabavka");
        btn2.setId("buttonNabavka");
        btn3.setId("buttonNabavka");
        btn4.setId("buttonNabavka");
        hb3.setSpacing(20);
        hb3.getChildren().addAll(btn4,btn1,btn3,btn2);
        
        //Kreiranje BorderPane-a za raspored HBox panela na stage
        BorderPane root = new BorderPane();
        root.setTop(hb1);
        root.setCenter(hb2);
        root.setBottom(hb3);
        
        
        //Taster sa akcijom dodavanja komponente na listu
        btn4.setOnAction(e ->{
            
        });
        
        //Taster sa akcijom dodavanja komponente u bazu podataka
        btn1.setOnAction(e ->{
            
        });
        
        //Taster sa akcijom stampanja komponenti u PDF formatu
        btn3.setOnAction(e ->{
            
        });
        
        //Taster sa akcijom NAZAD na pocetni stage
        btn2.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
            
        });
        
       
        
        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Nabavka - UNDP OfflineStore");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
