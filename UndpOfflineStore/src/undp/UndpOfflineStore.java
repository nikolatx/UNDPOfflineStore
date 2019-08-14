
package undp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class UndpOfflineStore extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        //dugmici za realizaciju pojedinih funkcija
        Button btn1 = new Button("Nabavka");
        Button btn2 = new Button("Prodaja");
        Button btn3 = new Button("Izvestaji");
        Button btn4 = new Button("Azuriranje");

        //Nabavka komponenata
        btn1.setOnAction(e->{
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "Jos uvek nije implementirano!");
            });
        });
        
        //Prodaja komponenata
        btn2.setOnAction(e->{
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "Jos uvek nije implementirano!");
            });
        });
        
        //Kreiranje izvestaja
        btn3.setOnAction(e->{
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "Jos uvek nije implementirano!");
            });
        });
        
        //Azuriranje komponenata
        btn4.setOnAction(e->{
            Platform.runLater(()->{
                JOptionPane.showMessageDialog(null, "Jos uvek nije implementirano!");
            });
        });
                
        
        GridPane root = new GridPane();
        root.add(btn1, 0, 0);
        root.add(btn2, 1, 0);
        root.add(btn3, 2, 0);
        root.add(btn4, 3, 0);
        root.setPadding(new Insets(50,50,50,50));
        root.setHgap(50);
        
        Scene scene = new Scene(root, 600, 500);
        
        primaryStage.setTitle("UNDP Offline Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    
}