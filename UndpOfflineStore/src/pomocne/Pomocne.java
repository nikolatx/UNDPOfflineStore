
package pomocne;

import com.grupa1.dbconnection.PomocneDAO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class Pomocne {
    
    public static enum Izbor {NASTAVI, IZADJI, PONOVO};
    
    static boolean ponovo;
    static Izbor izbor;
    
    public static void poruka(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Greška");
        alert.setContentText(msg);
        Platform.runLater( ()-> alert.showAndWait() );
    }
    
    public static boolean ucitajProizvodjace(boolean proizvodjacOdabran, ObservableList opcijeProizvodjac, String item) {
        if (!proizvodjacOdabran) {
                opcijeProizvodjac.clear();
                PomocneDAO.ispuniComboBoxZaProizvodjacaPoTipu(opcijeProizvodjac, item, false);
                return true;
        }
        return false;
    }
    
    public static boolean ucitajTipove(boolean tipOdabran, ObservableList opcijeTip, String item) {
        if (!tipOdabran) {
                opcijeTip.clear();
                PomocneDAO.ispuniComboBoxZaTipProizvodjaca(opcijeTip, item, false);
                return true;
        }
        return false;
    }
}
