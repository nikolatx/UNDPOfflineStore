
package pomocne;

import javafx.application.Platform;
import javafx.scene.control.Alert;


public class Pomocne {
    
    
    public static void poruka(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Greška");
        alert.setContentText(msg);
        Platform.runLater( ()-> alert.showAndWait() );
    }
    
    
}