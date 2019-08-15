
package undp;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class UndpOfflineStore extends Application {
        //dugmici za realizaciju pojedinih funkcija
        Button btn1 = new Button("Prodaja");
        Button btn2 = new Button("Nabavka");
        Button btn3 = new Button("Izvestaji");
        Button btn4 = new Button("Azuriranje");
        
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
        hb1.setId("headerBackground");
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
        hb2.setId("centerPicture");
        
        //Podesavanje velicine , izgleda i dodavanje nodova na Hbox
        hb3.setMinSize(800, 130);
        hb3.setAlignment(Pos.CENTER);
        btn1.setId("buttonStyle");
        btn2.setId("buttonStyle");
        btn3.setId("buttonStyle");
        btn4.setId("buttonStyle");
        hb3.getChildren().addAll(btn1,btn2,btn3,btn4);
        hb3.setSpacing(30);
        hb3.setId("bottomStyle");
        
        //Kreiranje BorderPane-a za rasporedjivanje HBoc panela na sceni
        BorderPane root = new BorderPane();
        root.setTop(hb1);
        root.setCenter(hb2);
        root.setBottom(hb3);
        
        

        
                
        
        
        //Kreiranje scene,velicine,naziva i povezivanje sa css-om
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("UNDP Offline Store");
        scene.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    
}