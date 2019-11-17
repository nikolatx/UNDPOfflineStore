package undp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SpinnerDialog {
    
    private int result=-1;
    
    public int display(String title, String message, int maksKolicina, int pocetnaKolicina) {
        
        Stage noviProzor = new Stage();
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        noviProzor.setTitle(title);
        noviProzor.setWidth(350);
        noviProzor.setHeight(175);
        noviProzor.initStyle(StageStyle.UTILITY);
        Label label = new Label(message);

        //forma za dodavanje zeljene kolicine komponente
        Button dugmePotvrdi = new Button("Potvrdi");
        Button dugmeOdustani = new Button("Odustani");
        dugmePotvrdi.setId("buttonStyle");
        dugmeOdustani.setId("buttonStyle");
        //boxovi za smestaj kontrola na sceni
        VBox rootBox = new VBox();
        HBox gornjiHBox=new HBox();
        HBox donjiHBox=new HBox();
        Label labela1=new Label("Unesi željenu količinu:");
        labela1.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
        //spinner-om se vrsi odabir kolicine komponente koja se dodaje
        Spinner spinner=new Spinner();   
        spinner.setId("buttonStyleNabavka");

        //podesavanje granicnih vrednosti spinnera i podesene vrednosti
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maksKolicina, pocetnaKolicina));

        //dodavanje u gornji red labele i spinnera
        gornjiHBox.getChildren().addAll(labela1, spinner);
        gornjiHBox.setAlignment(Pos.CENTER);
        //dodavanje u donji red dugmica
        donjiHBox.getChildren().addAll(dugmePotvrdi, dugmeOdustani);
        donjiHBox.setAlignment(Pos.CENTER);
        //ubacivanje komponenti u glavni VBox a zatim i u novu scenu
        rootBox.getChildren().addAll(gornjiHBox, donjiHBox);
        
        Scene scena = new Scene(rootBox, 350, 130);
        noviProzor.setScene(scena);
        
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        //dugme potvrdi - promena kolicine komponente i ubacivanje u listu odabranih
        dugmePotvrdi.setOnAction( e -> {
            result=(Integer)spinner.getValue();
            noviProzor.close();
        });
        
        //zatvranje novog prozora ukoliko je odabrano dugme odustani
        dugmeOdustani.setOnAction( e -> { 
            result=-1; 
            noviProzor.close();
        });
        
        noviProzor.showAndWait();
        return result;
    }
}