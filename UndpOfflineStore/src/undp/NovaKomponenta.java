package undp;

import com.grupa1.model.Komponenta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NovaKomponenta {
    
    private static boolean result=true;
    static Font font = new Font(25);
    //Kreiranje TextFiedla za novu komponentu i novog dobavljaca
    static TextField novaKomponentaNaziv = new TextField();
    static TextField novaKomponentaProizvodjac = new TextField();
    static TextField novaKomponentaTip = new TextField();
    static TextField novaKomponentaKolicina = new TextField();
    static TextField novaKomponentaCena = new TextField();
    static TextField novaKomponentaSlika= new TextField();
    static RadioButton novaKomponentaAktuletno = new RadioButton();
    //static Label novaKomponentaAktuelnoLabel = new Label("Aktuelno:");
    static Label novaKomponentaLabel = new Label("Dodavanje komponente");
    static Button novaKomponentaPotvrda = new Button("Potvrdi");
    static Button novaKomponentaNazad = new Button("Nazad");
    
    
    public static boolean display(Komponenta komponenta) {
        result=true;
        HBox headerDodajBox = new HBox();
        headerDodajBox.setId("headerBackground");
        headerDodajBox.setAlignment(Pos.CENTER);
        novaKomponentaAktuletno.setText("Aktuelna?");
        novaKomponentaLabel.setFont(font);
        novaKomponentaLabel.setId("headerLabel");
        headerDodajBox.getChildren().add(novaKomponentaLabel);
        headerDodajBox.setMinSize(500, 80);
        HBox centerDodajBox = new HBox();
        centerDodajBox.setId("bottomStyle");
        centerDodajBox.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        //Podesavanje velicine,izgleda i dodavanje nodova na scenu za Nova Komponenta
        novaKomponentaPotvrda.setId("buttonStyle");
        novaKomponentaNazad.setId("buttonStyle");
        novaKomponentaPotvrda.setMinSize(100, 25);
        novaKomponentaNazad.setMinSize(100, 25);
        HBox.setMargin(grid, new Insets(40, 0, 0, 0));
        novaKomponentaNaziv.setPromptText("Naziv");
        novaKomponentaProizvodjac.setPromptText("Novi proizvodjač");
        novaKomponentaTip.setPromptText("Tip");
        novaKomponentaKolicina.setPromptText("Količina");
        novaKomponentaCena.setPromptText("Cena");
        novaKomponentaSlika.setPromptText("Slika");
        grid.add(novaKomponentaNaziv, 0, 0);
        grid.add(novaKomponentaProizvodjac, 0, 1);
        grid.add(novaKomponentaTip, 0, 2);
        grid.add(novaKomponentaKolicina, 1, 0);
        grid.add(novaKomponentaCena, 1, 1);
        grid.add(novaKomponentaSlika, 1, 2);
        //grid.add(novaKomponentaAktuelnoLabel, 0, 3);
        grid.add(novaKomponentaAktuletno, 1, 3);
        grid.add(novaKomponentaPotvrda, 2, 2);
        grid.add(novaKomponentaNazad, 2, 3);
        centerDodajBox.getChildren().addAll(grid);
        HBox footerDodajBox = new HBox();
        BorderPane dodajBox = new BorderPane();
        dodajBox.setTop(headerDodajBox);
        dodajBox.setCenter(centerDodajBox);
        dodajBox.setBottom(footerDodajBox);

        //Kreiranje scene za novu komponentu
        Scene scena = new Scene(dodajBox, 500, 350); 
        Stage noviProzor=new Stage();
        noviProzor.setResizable(false);
        noviProzor.setTitle("Dodavanje Komponente");
        //scena.getStylesheets().addAll(this.getClass().getResource("styles.css").toExternalForm());
        scena.getStylesheets().add("styles.css");
        noviProzor.setScene(scena);
        noviProzor.initModality(Modality.WINDOW_MODAL);
        //noviProzor.initOwner(primaryStage);
        //aktiviranje i prikaz novog prozora
        noviProzor.show();
        
        
        
        //povratak na nabavku sa forme za unosenje nove komponente
        novaKomponentaNazad.setOnAction(ev ->{
            result=false;
            noviProzor.close();
        });
        return result;
    }
}