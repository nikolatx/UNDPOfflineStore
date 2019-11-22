package undp;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kontroleri.IzvestajKontroler;
import pomocne.Pomocne;


public class Izvestaji extends Application {
    //Kreiranje HBox  i VBox - eva
    HBox headerHBox = new HBox();
    HBox footerHBox = new HBox();
    BorderPane rightBox = new BorderPane();
    VBox gornjiBox = new VBox();
    VBox donjiBox = new VBox();
    VBox srednjiBox = new VBox();
    
    //Kreiranje tastera sa za pojedine funkcije
    Button prodajaButton = new Button("Prodaja");
    Button nabavkaButton = new Button("Nabavka");
    Button nazadButton = new Button("Nazad");
    //Kreiranje labela sa ispisima
    Label prodajaLabel = new Label("Izveštaji prodaje");
    Label nabavkaLabel = new Label("Izveštaji nabavke");
    Label nazivLabel = new Label("Izveštaji");
    Font font = new Font(25);
    IzvestajKontroler kontroler=new IzvestajKontroler();
    
    
    @Override
    public void start(Stage primaryStage) {
        //Podesavanje vidljivosti labela
        prodajaLabel.setVisible(false);
        nabavkaLabel.setVisible(false);
        //podesavanje izgleda i velicine headerBox-a i dodavanje nodova
        
        nazivLabel.setId("headerLabel");       
        nazivLabel.setFont(font);
        headerHBox.setMinSize(1000, 100);
        headerHBox.setId("headerBackground");    //Css - Style: podesavanje izgleda
        headerHBox.setAlignment(Pos.CENTER);
        headerHBox.getChildren().add(nazivLabel);
        
        
        //podesavanje izgleda i velicine rightBox-a i dodavanje nodova
        //rightBox.setMinSize(120, 550);
        //rightBox.setAlignment(Pos.CENTER);
        gornjiBox.setAlignment(Pos.CENTER);
        gornjiBox.setSpacing(20);
        gornjiBox.setPadding(new Insets(10));
        //gornjiBox.setId("bottomStyle");
        prodajaButton.setId("buttonStyleNabavka");
        nabavkaButton.setId("buttonStyleNabavka");
        gornjiBox.getChildren().addAll(nabavkaButton, prodajaButton);
        
        
        donjiBox.setAlignment(Pos.CENTER);
        donjiBox.setPadding(new Insets(10));
        donjiBox.setId("bottomStyle");
        nazadButton.setId("buttonStyleNabavka");
        donjiBox.setAlignment(Pos.BOTTOM_CENTER);
        //HBox.setMargin(nazadButton, new Insets(50, 20, 0, 0));
        donjiBox.getChildren().add(nazadButton);
        rightBox.setTop(gornjiBox);
        rightBox.setBottom(donjiBox);
        //rightBox.getChildren().addAll(gornjiBox, donjiBox);
        
        //podesavanje izgleda i velicine srednjeg box-a i dodavanje nodova
        srednjiBox.setMinWidth(800);
        srednjiBox.setAlignment(Pos.CENTER);
        srednjiBox.setId("bottomStyle");
        //srednjiBox.getChildren().addAll();
        srednjiBox.getChildren().add(IzvestajKontroler.napraviGrafik());
        
        
        //Kreiranje borderpane-a za raspored po stage-a
        BorderPane root = new BorderPane();
        root.setTop(headerHBox);
        root.setRight(rightBox);
        root.setCenter(srednjiBox);
        root.setBottom(footerHBox);
        
        //Kreiranje scene ,velicine i povezivanje sa CSS-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Izveštaji - UndpOfflineStore");
        scene.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
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
            try {
                primaryStage.close();
                new IzvestajProdaje().start(primaryStage);
            } catch (SQLException ex) {
                Pomocne.poruka(ex.getMessage());
            }
        });
        
        //funkcija tastera za prelazak na izvestaje nabavke
        nabavkaButton.setOnAction(e ->{
            try {
                primaryStage.close();
                new IzvestajNabavke().start(primaryStage);
            } catch (SQLException ex) {
                Pomocne.poruka(ex.getMessage());
            }
        });
        
        //Funkcija tastera za povratak na pocetak alikacije
        nazadButton.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });
        
        
    }
    
}