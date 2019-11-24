
package undp;

import com.grupa1.dbconnection.PodesavanjaDAO;
import com.grupa1.model.Podesavanja;
import java.io.File;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import konstante.Konst;
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
        noviProzor.setWidth(650);
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
        Button slPutanja = new Button("...");
        Button prijPutanja = new Button("...");
        Button prijTemplPutanja = new Button("...");
        Button faktPutanja = new Button("...");
        Button faktTemplPutanja = new Button("...");
        
        //dodavanje labela i polja za unos teksta u GridPane
        srednji.add(labela1, 0, 0);
        srednji.add(txtSlika, 1, 0);
        srednji.add(slPutanja, 2, 0);
        
        srednji.add(labela2, 0, 1);
        srednji.add(txtVelicinaSlike, 1, 1);
        
        srednji.add(labela3, 0, 2);
        srednji.add(rb, 1, 2);
        srednji.setHalignment(rb, HPos.CENTER);
        
        srednji.add(labela4, 0, 3);
        srednji.add(txtPrijemnice, 1, 3);
        srednji.add(prijPutanja, 2, 3);
        
        srednji.add(labela5, 0, 4);
        srednji.add(txtTemplatePr, 1, 4);
        srednji.add(prijTemplPutanja, 2, 4);
        
        srednji.add(labela6, 0, 5);
        srednji.add(txtFakture, 1, 5);
        srednji.add(faktPutanja, 2, 5);
        
        srednji.add(labela7, 0, 6);
        srednji.add(txtTemplateFa, 1, 6);
        srednji.add(faktTemplPutanja, 2, 6);
        
        //srednji.getChildren().addAll(labela1, labela2, labela3, labela4, labela5, labela6, labela7,
        //        txtSlika, txtVelicinaSlike, rb, txtPrijemnice, txtTemplatePr, txtFakture, txtTemplateFa);
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
        Scene scena = new Scene(root, 650, 420);
        scena.getStylesheets().addAll(this.getClass().getResource("/resources/styles.css").toExternalForm());
        noviProzor.getIcons().add(new Image("/resources/logo.jpg"));
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
        
        //odabir direktorijuma za slike
        slPutanja.setOnAction(e->{
            File directory = odaberiDirektorijum(Konst.SLIKE_PATH, noviProzor);
            if (directory != null)
                txtSlika.setText(directory.getAbsolutePath());
        });
        
        //odabir direktorijuma za prijemnice
        prijPutanja.setOnAction(e->{
            File directory = odaberiDirektorijum(Konst.PRIJEMNICE_PATH, noviProzor);
            if (directory != null)
                txtPrijemnice.setText(directory.getAbsolutePath());
        });
        
        //odabir template fajla za prijemnice
        prijTemplPutanja.setOnAction(e->{
            File fajl = odaberiFajl(Konst.PRIJEMNICA_TEMPLATE, noviProzor);
            if (fajl != null)
                txtTemplatePr.setText(fajl.getAbsolutePath());
        });
        
        //odabir direktorijuma za fakture
        faktPutanja.setOnAction(e->{
            File directory = odaberiDirektorijum(Konst.FAKTURE_PATH, noviProzor);
            if (directory != null)
                txtFakture.setText(directory.getAbsolutePath());
        });
        
        //odabir template fajla za prijemnice
        faktTemplPutanja.setOnAction(e->{
            File fajl = odaberiFajl(Konst.FAKTURA_TEMPLATE, noviProzor);
            if (fajl != null)
                txtTemplateFa.setText(fajl.getAbsolutePath());
        });
        
        noviProzor.showAndWait();
        return result;
    }
    
    private File odaberiDirektorijum(String pocetni, Stage prozor) {
        final DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(pocetni));
        File directory = chooser.showDialog(prozor);
        return directory;    
    }
    
    private File odaberiFajl(String pocetni, Stage prozor) {
        final FileChooser fileChooser = new FileChooser();
        File file=new File(pocetni);
        File dir=file.getParentFile();
        fileChooser.setInitialDirectory(dir);
        File fileCh = fileChooser.showOpenDialog(prozor);
        return fileCh;    
    }
}
