
package undp;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Pretraga extends Application {
    
    //Kreiranje dugmica sa odredjenom funkcijom
    Button btn1 = new Button("Pretrazi");
    Button btn2 = new Button("Nazad");
    
    //Kreiranje Labela i  Fonta sa tekstom
    Label lab1 = new Label("Pretraga komponenti");
    Label lab2 = new Label("Tip komponente: ");
    Label lab3= new Label("Proizvodjac: ");
    Label lab4= new Label("Model komponente: ");
    Label lab5 = new Label("Rezultat pretrage");
    
    //TextArea i ScrollPane za umetanje texta pretrage
    TextArea ta= new TextArea();
    ScrollPane sp = new ScrollPane();
    

    
    //Kreiranje tekstualnih polja za unos teksta
    TextField tf1 = new TextField();
    TextField tf2 = new TextField();
    TextField tf3 = new TextField();
    Font font = new Font(25);
    
    //Kreiranje horizontalnih (HBox) i Vertikalnih (VBox) panela
    HBox hb1 = new HBox();
    HBox hb2 = new HBox();
    HBox hb3 = new HBox();
    
    
    
    
    
    @Override
    public void start(Stage primaryStage) {
        //podesavanje velicine,pozicije i izgleda hb1 - panela
        hb1.setMinSize(800, 120);
        hb1.setAlignment(Pos.CENTER);
        lab1.setFont(font);
        lab1.setId("headerLabel");    //Css - Style: podesavanje izgleda
        hb1.getChildren().add(lab1);
        hb1.setId("headerBackground");    //Css - Style: podesavanje izgleda
        
        
        //podesavanje velicine,pozicije i izgleda hb2 - panela sa GriPane-om
        hb2.setMinSize(800, 180);
        hb2.setAlignment(Pos.CENTER);
        hb3.setSpacing(15);
        hb2.setId("bottomStyle");    //Css - Style: podesavanje izgleda
        GridPane grid = new GridPane();  //Kreiranje GridPane-a za raspored nodova
        grid.setAlignment(Pos.CENTER);
        GridPane.setHalignment(lab2, HPos.CENTER);
        GridPane.setHalignment(lab3, HPos.CENTER);
        GridPane.setHalignment(lab4, HPos.CENTER);
        grid.setVgap(5);
        grid.setHgap(5);
        tf1.setAlignment(Pos.BASELINE_RIGHT);
        tf2.setAlignment(Pos.BASELINE_RIGHT);
        tf3.setAlignment(Pos.BASELINE_RIGHT);
        lab2.setId("serchLabel");
        lab3.setId("serchLabel");
        lab4.setId("serchLabel");
        btn1.setId("buttonStyle");    //Css - Style: podesavanje izgleda
        grid.add(lab2, 0, 0);
        grid.add(lab3, 1, 0);
        grid.add(lab4, 2, 0);
        grid.add(tf1, 0, 1);
        grid.add(tf2, 1, 1);
        grid.add(tf3, 2, 1);
        grid.add(btn1, 3, 1);
        hb2.getChildren().add(grid);
        
        //podesavanje velicine,pozicije i izgleda hb3 - panela
        hb3.setMinSize(800, 300);
        hb3.setAlignment(Pos.CENTER);
        ta.setPrefSize(590, 290);     
        sp=new ScrollPane(ta);          //Umetanje ScrollPane u Objekat TextArea
        sp.setPrefSize(600, 250);
        btn2.setId("buttonStyle");    //Css - Style: podesavanje izgleda
        hb3.setId("bottomStyle");    //Css - Style: podesavanje izgleda
        hb3.getChildren().addAll(sp,btn2);
        
        
        //Kreiranje BorderPane-a za raspored HBox i VBox panela
        BorderPane root = new BorderPane();
        root.setTop(hb1);
        root.setCenter(hb2);
        root.setBottom(hb3);
        
        //Akcija tastera za pretragu
        btn1.setOnAction(e ->{
            
        });
       
        
        
        //Akcija tastera za povratak nazad
        btn2.setOnAction(e ->{
            primaryStage.close();
            new UndpOfflineStore().start(primaryStage);
        });
        
        //Kreiranje scene ,velicine,naziva povezivanje sa Css-om
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Pretraga - UNDP OfflineStore");
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
