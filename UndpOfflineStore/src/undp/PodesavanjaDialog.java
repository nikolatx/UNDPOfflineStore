
package undp;

import com.grupa1.dbconnection.PodesavanjaDAO;
import com.grupa1.model.Podesavanja;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pomocne.Pomocne;

/**
 *
 * @author nikolatimotijevic
 */
public class PodesavanjaDialog {
    
    private Podesavanja podesavanja;
    int result=-1;
    boolean prviUpis;
    
    public int display() {
        
        podesavanja = PodesavanjaDAO.ucitaj();
        if (podesavanja==null) {
            podesavanja=new Podesavanja();
            prviUpis=true;
        } else
            prviUpis=false;
        
        //podesavanje prozora
        Stage noviProzor = new Stage();
        noviProzor.initModality(Modality.APPLICATION_MODAL);
        noviProzor.setTitle("Podešavanja");
        noviProzor.setWidth(600);
        noviProzor.setHeight(400);
        noviProzor.initStyle(StageStyle.UTILITY);
        
        //boxevi za smestaj kontrola na sceni
        BorderPane root=new BorderPane();
        VBox headerVB=new VBox();
        GridPane srednji=new GridPane();
        HBox donji = new HBox();
        
        //podesavanje header boxa sa slikom
        headerVB.setAlignment(Pos.CENTER);
        headerVB.setId("headerBackground");
        headerVB.setPadding(new Insets(10));
        headerVB.setMinHeight(80);
        //dodavanje naslova u header dialoga
        Label dialogNaslov = new Label("Podešavanja");
        dialogNaslov.setFont(new Font(25));
        dialogNaslov.setId("headerLabel");
        headerVB.getChildren().add(dialogNaslov);
        //ubacivanje header boxa u root
        root.setTop(headerVB);
        
        //definisanje labela i polja za unos teksta
        Font font = Font.font("Verdana", FontWeight.BOLD, 11);
        Label labela1=new Label("Putanja do slika:");
        labela1.setFont(font);
        Label labela2=new Label("Velicina zumirane slike:");
        labela2.setFont(font);
        Label labela3=new Label("Brisanje bookmarka posle upisa:");
        labela3.setFont(font);
        Label labela4=new Label("Putanja do prijemnica:");
        labela4.setFont(font);
        Label labela5=new Label("Template fajl za prijemnice:");
        labela5.setFont(font);
        Label labela6=new Label("Putanja do faktura:");
        labela6.setFont(font);
        Label labela7=new Label("Template fajl za fakture:");
        labela7.setFont(font);
        
        TextField txtSlika = new TextField(podesavanja.getSlikePath());
        txtSlika.setMinWidth(350);
        TextField txtVelicinaSlike = new TextField(String.valueOf(podesavanja.getSlikeVelicina()));
        RadioButton rb = new RadioButton();
        rb.setSelected(podesavanja.isDeleteBookmark());
        TextField txtPrijemnice = new TextField(podesavanja.getPrijemnicePath());
        TextField txtTemplatePr = new TextField(podesavanja.getPrijemnicaTemplate());
        TextField txtFakture = new TextField(podesavanja.getFakturePath());
        TextField txtTemplateFa = new TextField(podesavanja.getFaktureTemplate());
        
        //dodavanje labela i polja za unos teksta u GridPane
        GridPane.setConstraints(labela1, 0, 0);
        GridPane.setConstraints(txtSlika, 1, 0);
        
        GridPane.setConstraints(labela2, 0, 1);
        GridPane.setConstraints(txtVelicinaSlike, 1, 1);
        
        GridPane.setConstraints(labela3, 0, 2);
        GridPane.setConstraints(rb, 1, 2);
        GridPane.setHalignment(rb, HPos.CENTER);
        
        GridPane.setConstraints(labela4, 0, 3);
        GridPane.setConstraints(txtPrijemnice, 1, 3);
        
        GridPane.setConstraints(labela5, 0, 4);
        GridPane.setConstraints(txtTemplatePr, 1, 4);
        
        GridPane.setConstraints(labela6, 0, 5);
        GridPane.setConstraints(txtFakture, 1, 5);
        
        GridPane.setConstraints(labela7, 0, 6);
        GridPane.setConstraints(txtTemplateFa, 1, 6);
        
        srednji.getChildren().addAll(labela1, labela2, labela3, labela4, labela5, labela6, labela7,
                txtSlika, txtVelicinaSlike, rb, txtPrijemnice, txtTemplatePr, txtFakture, txtTemplateFa);
        srednji.setPadding(new Insets(10,10,10,10));
        srednji.setAlignment(Pos.CENTER);
        srednji.setVgap(7);
        srednji.setHgap(10);
        //dodavanje GridPane u centar root-a
        root.setCenter(srednji);
        
        //podesavanje donjeg boxa i dodavanje u root
        Button dugmePotvrdi = new Button("Sačuvaj");
        Button dugmeOdustani = new Button("Odbaci");
        dugmePotvrdi.setId("buttonStyle");
        dugmeOdustani.setId("buttonStyle");
        donji.setMinHeight(60);
        donji.setAlignment(Pos.CENTER);
        donji.getChildren().addAll(dugmePotvrdi, dugmeOdustani);
        root.setBottom(donji);
        
        //podesavanje scene
        Scene scena = new Scene(root, 600, 420);
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        noviProzor.setResizable(false);
        noviProzor.setScene(scena);
        
        //dugme potvrdi - cuvanje novih podesavanja bazi podataka
        dugmePotvrdi.setOnAction( e -> {
            result=1;
            String slikaPath=txtSlika.getText().trim();
            String velicinaSlike = txtVelicinaSlike.getText().trim();
            boolean bookmarkDelete = rb.isSelected();
            String prijemnicePath = txtPrijemnice.getText().trim();
            String prijemnicaTemplate = txtTemplatePr.getText().trim();
            String fakturePath = txtFakture.getText().trim();
            String fakturaTemplate = txtTemplateFa.getText().trim();
            
            if (!(slikaPath.isEmpty() || velicinaSlike.isEmpty() || prijemnicePath.isEmpty() || prijemnicaTemplate.isEmpty() ||
                    fakturePath.isEmpty() || fakturaTemplate.isEmpty())) {
                int velicinaSlikeInt = Integer.parseInt(txtVelicinaSlike.getText().trim());
                podesavanja = new Podesavanja(slikaPath, velicinaSlikeInt, bookmarkDelete, prijemnicePath, prijemnicaTemplate,
                    fakturePath, fakturaTemplate);
                boolean uspesno=PodesavanjaDAO.sacuvaj(prviUpis, podesavanja); 
                
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Obaveštenje");
                alert1.setHeaderText(null);
                if (uspesno) {
                    alert1.setContentText("Podešavanja uspešno sačuvana!");
                } else
                    alert1.setContentText("Došlo je do greške pri upisu u bazu podataka!");
                Platform.runLater( () -> {
                    alert1.showAndWait();
                    //nazadDugme.fire();
                });
                
                
                noviProzor.close();
            } else {
                Pomocne.poruka("Sva polja moraju biti popunjena!");
            }
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
