/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Nebojsa
 */
public class Izvestaji extends Application {
    //Kreiranje HBox  i VBox - eva
    HBox headerHBox = new HBox();
    HBox footerHBox = new HBox();
    VBox rightBox = new VBox();
    VBox leftBox = new VBox();
    //Kreiranje tastera sa za pojedine funkcije
    Button prodajaButton = new Button("Prodaja");
    Button nabavkaButton = new Button("Nabavka");
    Button nazadButton = new Button("Nazad");
    //Kreiranje labela sa ispisima
    Label prodajaLabel = new Label("Izvestaji prodaje");
    Label nabavkaLabel = new Label("Izvestaji nabavke");
    Label nazivLabel = new Label("Izvestaji");
    Font font = new Font(25);
    
    @Override
    public void start(Stage primaryStage) {
        //Podesavanje vidljivosti labela
        prodajaLabel.setVisible(false);
        nabavkaLabel.setVisible(false);
        //podesavanje izgleda i velicine headerBox-a i dodavanje nodova
        headerHBox.setMinSize(800, 120);
        headerHBox.setStyle("-fx-background-position:center;");
        nazivLabel.setId("headerLabel");       
        headerHBox.setId("headerBackground");    //Css - Style: podesavanje izgleda
        headerHBox.setAlignment(Pos.CENTER);
        nazivLabel.setFont(font);
        HBox.setMargin(nazivLabel, new Insets(0, 0, 20, 0));
        headerHBox.getChildren().add(nazivLabel);;
        //podesavanje izgleda i velicine rightBox-a i dodavanje nodova
        rightBox.setMinSize(510, 430);
        rightBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.setSpacing(80);
        rightBox.setId("bottomStyle");
        prodajaLabel.setFont(font);
        nabavkaLabel.setFont(font);
        rightBox.getChildren().addAll(prodajaLabel,nabavkaLabel);
        //podesavanje izgleda i velicine leftBox-a i dodavanje nodova
        leftBox.setMinSize(510, 430);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setId("bottomStyle");
        prodajaButton.setId("buttonStyle");
        nabavkaButton.setId("buttonStyle");
        prodajaButton.setMinSize(150, 70);
        nabavkaButton.setMinSize(150,70); 
        leftBox.setSpacing(50);
        leftBox.getChildren().addAll(prodajaButton,nabavkaButton);
        //podesavanje izgleda i velicine footerHBox-a i dodavanje nodova
        footerHBox.setMinSize(1000, 100);
        footerHBox.setId("bottomStyle");
        nazadButton.setId("buttonStyle");
        nazadButton.setMinSize(100, 25);
        footerHBox.setAlignment(Pos.BASELINE_RIGHT);
        HBox.setMargin(nazadButton, new Insets(50, 20, 0, 0));
        footerHBox.getChildren().add(nazadButton);
        //Kreiranje borderpane-a za raspored po stage-a
        BorderPane root = new BorderPane();
        root.setTop(headerHBox);
        root.setRight(rightBox);
        root.setLeft(leftBox);
        root.setBottom(footerHBox);
        
        //Vidljivost labela prilikom prelaska misa na taster prodaja i nabavka
        prodajaButton.setOnMouseEntered(e ->{
            prodajaLabel.setVisible(true);
            nabavkaLabel.setVisible(false);
        });
        
        nabavkaButton.setOnMouseEntered(e ->{
            nabavkaLabel.setVisible(true);
            prodajaLabel.setVisible(false);
        });
        
        
         //funkcija tastera za prelazak na izvestaje prodaje
        prodajaButton.setOnAction(e ->{
            primaryStage.close();
            new IzvestajProdaje().start(primaryStage);
        });
        
        //funkcija tastera za prelazak na izvestaje nabavke
        nabavkaButton.setOnAction(e ->{
            primaryStage.close();
            new IzvestajNabavke().start(primaryStage);
        });
        
        //Funkcija tastera za povratak na pocetak alikacije
        nazadButton.setOnAction(e ->{
        primaryStage.close();
        new UndpOfflineStore().start(primaryStage);
    });
        
        //Kreiranje scene ,velicine i povezivanje sa CSS-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Izvestaji - UndpOfflineStore");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setResizable(false);
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
