/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undp;

import com.grupa1.model.Komponenta;
import java.sql.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class IzvestajNabavke extends Application {
    //Kreiranje HBox i VBox panela
    HBox headerBox = new HBox();
    HBox centerBox = new HBox();
    HBox footerBox = new HBox();
    VBox rightBox = new VBox();
    //Kreiranje tastera za odredjene funkcije
    Button dnevniButton = new Button("Dnevni izvestaj");
    Button nedelnjiButton = new Button("Nedeljni izvestaj");
    Button mesecniButton = new Button("Mesecni Izvestaj");
    Button godisnjiButton = new Button("Godisnji izvestaj");
    Button odDoButton = new Button("Odabrani datum");
    Button nazadButton = new Button("Nazad");
    Button exportButton = new Button("PDF");
    
    //Kreiranje DatePicker-a za izbor datuma za pretragu
    DatePicker odPicker = new DatePicker();
    DatePicker doPicker = new DatePicker();
    //Kreiranje labela sa izpisima
    Label sumaLabel = new Label("UKUPNO RSD : ");
    Label dnevnoLabel = new Label("");
    Label nedeljnoLabel = new Label("");
    Label mesecnoLabel = new Label("");
    Label godisnjiLabel = new Label("");
    Label odDoLabel = new Label("");
    Label nazivLabel = new Label("Izvestaji Nabavke");
    
    Font font = new Font(25);
    
    //Kreiranje TableView-a i liste za popunjavanje
    TableView<Komponenta> tabela = new TableView<Komponenta>();
    ObservableList<Komponenta> podaci;
    
    @Override
    public void start(Stage primaryStage) {
        
        
        TableColumn<Komponenta, String> tipCol = new TableColumn<Komponenta, String>("Tip");
        tipCol.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("tipProperty"));
        
        TableColumn<Komponenta, String> nazivCol = new TableColumn<Komponenta, String>("Naziv");
        nazivCol.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("nazivProperty"));
        
        TableColumn<Komponenta, String> proizvodjacCol = new TableColumn<Komponenta, String>("Proizvodjac");
        proizvodjacCol.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("proizvodjacProperty"));
        
        TableColumn<Komponenta, String> dobavljacCol = new TableColumn<Komponenta, String>("Dobavljac");
        dobavljacCol.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("dobavljacProperty"));
        
        TableColumn<Komponenta, Integer> kolicinaCol = new TableColumn<Komponenta, Integer>("Kolicina");
        kolicinaCol.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("kolicinaProperty"));
        
        TableColumn<Komponenta, Double> cenaCol = new TableColumn<Komponenta, Double>("Cena");
        cenaCol.setCellValueFactory(new PropertyValueFactory<Komponenta, Double>("cenaProperty"));
        
        TableColumn<Komponenta, Date> dateCol = new TableColumn<Komponenta, Date>("Datum");
        dateCol.setCellValueFactory(new PropertyValueFactory<Komponenta, Date>("datumProperty"));
        
        tabela.getColumns().addAll(tipCol,nazivCol,proizvodjacCol,dobavljacCol,kolicinaCol,cenaCol,dateCol);
        tabela.setMinSize(750, 400);
        
        tipCol.setMinWidth(100);
        nazivCol.setMinWidth(150);
        proizvodjacCol.setMinWidth(150);
        dobavljacCol.setMinWidth(150);
        kolicinaCol.setMinWidth(50);
        cenaCol.setMinWidth(75);
        dateCol.setMinWidth(75);
        
        
        
        
        
        
        //podesavanje velicine izgleda i dodavanje komponenti u headerBox
        headerBox.setMinSize(1000, 120);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setId("headerBackground");
        nazivLabel.setId("headerLabel");
        nazivLabel.setFont(font);
        headerBox.getChildren().add(nazivLabel);
        
        //podesavanje velicine izgleda i dodavanje komponenti u centerBox
        centerBox.setMinSize(810, 430);
        centerBox.setId("bottomStyle");
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(tabela);
        
        //podesavanje velicine izgleda i dodavanje komponenti u rightBox
        rightBox.setMinSize(210,430);
        rightBox.setId("bottomStyle");
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setSpacing(10);
        dnevniButton.setMinSize(150, 25);
        dnevniButton.setId("buttonStyle");
        nedelnjiButton.setMinSize(150, 25);
        nedelnjiButton.setId("buttonStyle");
        mesecniButton.setMinSize(150, 25);
        mesecniButton.setId("buttonStyle");
        godisnjiButton.setMinSize(150, 25);
        godisnjiButton.setId("buttonStyle");
        odDoButton.setMinSize(150, 25);
        odDoButton.setId("buttonStyle");
        rightBox.getChildren().addAll(dnevniButton,nedelnjiButton,mesecniButton,godisnjiButton,odPicker,doPicker,odDoButton);
        
        //podesavanje velicine izgleda i dodavanje komponenti u footerBox
        footerBox.setMinSize(1000, 100);
        footerBox.setId("bottomStyle");
        footerBox.setAlignment(Pos.BOTTOM_RIGHT);
        nazadButton.setMinSize(100, 25);
        nazadButton.setId("buttonStyle");
        exportButton.setMinSize(100, 25);
        exportButton.setId("buttonStyle");
        HBox.setMargin(nazadButton, new Insets(0, 20, 0, 0));
        HBox.setMargin(exportButton, new Insets(0, 0, 0, 50));
        HBox.setMargin(sumaLabel, new Insets(0, 30, 70, 0));
        
        footerBox.getChildren().addAll(sumaLabel,exportButton,nazadButton);
        
        //Kreiranje BorderPnaea za raspored po sceni
        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setRight(rightBox);
        root.setLeft(centerBox);
        root.setBottom(footerBox);
        
        
        //Funkcija tastera nazad i vracanje na izbor izvestaja 
        nazadButton.setOnAction(e ->{
            primaryStage.close();
            new Izvestaji().start(primaryStage);
        });
        
        
        
        
        //Kreiranje scene ,velicine i povezivanje sa CSS-om
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Izvestaji prodaje - UndpOfflineStore");
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
