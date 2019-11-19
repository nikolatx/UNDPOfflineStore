
package pomocne;

import com.grupa1.model.IzvestajPOJO;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Slika;
import java.io.File;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import konstante.Konst;

public class Tabela {

    static TableColumn kolonaCena = new TableColumn("Cena");
    static double sum = 0;
    public static int broj;
    
    //otvara se uvecana slika kada se klikne na sliku u prvoj koloni tabele
    private static void klikNaSliku(TableView tabela) {
        //ocitavanje odabrane komponente iz TableView-a
        try {
            KomponentaSaSlikom komponenta= (KomponentaSaSlikom) tabela.getSelectionModel().getSelectedItem();
            String nazivSlike=((Slika)komponenta.getSlika()).getNaziv();
            if (komponenta==null || nazivSlike.equals("")) return;
            //putanja do slike
            String imageFile=Konst.SLIKE_PATH + "\\" + nazivSlike;
            //inicijalizacija image objekta na osnovu putanje i naziva fajla
            Image image=new Image(new File(imageFile).toURI().toString());
            ImageView imageView=new ImageView(image);
            //podesavanje velicine uvecane slike
            if (image.getHeight()>image.getWidth())
                imageView.setFitHeight(Konst.VELICINA_SLIKE-10);
            else
                imageView.setFitWidth(Konst.VELICINA_SLIKE-10);
            imageView.setPreserveRatio(true);
            //slika se smesta u StackPane
            StackPane pane=new StackPane();
            pane.getChildren().add(imageView);
            //podesavanje border-a StackPane-a
            pane.setStyle("-fx-border-color: black;-fx-border-style: solid;-fx-border-width: 5;");

            Scene scena = new Scene(pane, Konst.VELICINA_SLIKE, Konst.VELICINA_SLIKE);
            //podesavanje novog prozora
            Stage noviProzor=new Stage();
            noviProzor.setTitle(nazivSlike+" "+komponenta.getProizvodjac());
            noviProzor.setScene(scena);

            scena.getStylesheets().addAll(Tabela.class.getResource("/resources/styles.css").toExternalForm());

            noviProzor.initStyle(StageStyle.UNDECORATED);
            noviProzor.initModality(Modality.APPLICATION_MODAL);
            //aktiviranje i prikaz novog prozora
            noviProzor.show();

            pane.setOnMouseClicked(ev->{
                noviProzor.close();
            });
        } catch (NullPointerException ex) {
            
        }
            
    }
    //tabela u pretrazi i azuriranju
    public static void kreirajTabelu(TableView<KomponentaSaSlikom> tabela, boolean editable) {
        
        //podesavanje kolone sa slikom komponente
        TableColumn<KomponentaSaSlikom, Slika> kolonaSlika = new TableColumn<>("Slika");
        //podesavanje zadavanja vrednosti celije
        kolonaSlika.setCellValueFactory(new PropertyValueFactory("slika"));
        //podesavanje prikaza celije (vrednost je string, a prikazuje se slika)
        kolonaSlika.setCellFactory(new Callback<TableColumn<KomponentaSaSlikom, Slika>, TableCell<KomponentaSaSlikom, Slika>>(){
            @Override
            public TableCell<KomponentaSaSlikom, Slika> call(TableColumn<KomponentaSaSlikom, Slika> param) {
                
                TableCell<KomponentaSaSlikom, Slika> cell = new TableCell<KomponentaSaSlikom, Slika>() {
                    
                    @Override
                    public void updateItem(Slika item, boolean empty) {
                        super.updateItem(item, empty); 

                        if (tabela.getItems()!=null) {
                            //priprema StackPane i ImageView koji ide u njega a sadrzace sliku
                            StackPane box=new StackPane();
                            ImageView imageview = new ImageView();
                            
                            if (item != null) {
                                box.setPadding(new Insets(4, 4, 4, 4));
                                try {
                                    //formiranje punog naziva i putanje do slike
                                    String nazivSlike=item.getNaziv();
                                    String slikaFajl=Konst.SLIKE_PATH+"\\"+nazivSlike;
                                    //umetanje slike u celiju u prvoj koloni
                                    if (!nazivSlike.equals("") && !nazivSlike.isEmpty() ) {
                                        //umetanje slike u ImageView
                                        Image img = new Image(new File(slikaFajl).toURI().toString());
                                        imageview.setImage(img);
                                        //podesavanje velicine slike
                                        imageview.setFitHeight(50.0); //50.0);
                                        imageview.setFitWidth(50.0); //50.0);
                                        //umetanje ImageView-a u StackPane
                                        if(!box.getChildren().contains(imageview)) {
                                            box.getChildren().add(imageview);
                                            box.setOnMouseClicked(e->{
                                               klikNaSliku(tabela);
                                            });
                                            //umetanje StackPane u celiju tabele
                                            setGraphic(box);
                                        }
                                    }
                                } catch (NullPointerException ex) { }
                            } else
                                setGraphic(null); //ovo mora da stoji ovde da se ne bi dobili nezeljeni podaci u tabeli (duplirane slike)
                        }
                    }
                };
                return cell;
            }
        });
        
        //kolonaSlika.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width       
        //kolonaSlika.setPrefWidth(1f * Integer.MAX_VALUE * 9); // 9% width       
        
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("id"));
        //definisanje procentualne sirine kolone
        //kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        //kolonaSifra.setPrefWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("naziv"));
        //kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 42); // 42% width
        //kolonaOpis.setPrefWidth(1f * Integer.MAX_VALUE * 42); // 42% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("proizvodjac"));
        //kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        //kolonaProizvodjac.setPrefWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("tip"));
        //kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 19); // 19% width
        //kolonaTip.setPrefWidth(1f * Integer.MAX_VALUE * 19); // 19% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("kolicina"));
        //kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 7); // 10% width
        //kolonaKolicina.setPrefWidth(1f * Integer.MAX_VALUE * 7); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");


        kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<>("cena"));
        //kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        //kolonaCena.setPrefWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        //kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 10 0 0;");
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT;");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<KomponentaSaSlikom, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value==null) {
                    setText("");
                } else {
                    setText(String.format("%,.2f", value.doubleValue()));
                    //setPadding(new Insets(10,10,10,10));
                }
            }
        });

        //dodavanje kolona u tabelu
        tabela.getColumns().addAll(kolonaSlika, kolonaSifra, kolonaOpis, 
                kolonaProizvodjac, kolonaTip, kolonaKolicina, kolonaCena);
        
        //podesavanje sirine kolona
        double[] widths = {9, 5, 42, 10, 19, 7, 10};//define the width of the columns
        sum = 0;
        for (double i : widths) 
            sum += i;

        kolonaSlika.prefWidthProperty().bind(tabela.widthProperty().multiply(9/sum));
        kolonaSifra.prefWidthProperty().bind(tabela.widthProperty().multiply(5/sum));
        kolonaOpis.prefWidthProperty().bind(tabela.widthProperty().multiply(40/sum));
        kolonaProizvodjac.prefWidthProperty().bind(tabela.widthProperty().multiply(10/sum));
        kolonaTip.prefWidthProperty().bind(tabela.widthProperty().multiply(19/sum));
        kolonaKolicina.prefWidthProperty().bind(tabela.widthProperty().multiply(7/sum));
        kolonaCena.prefWidthProperty().bind(tabela.widthProperty().multiply(10/sum));
        
        if (editable) {
            tabela.setEditable(true);
            kolonaOpis.setEditable(true);
            kolonaOpis.setCellFactory(TextFieldTableCell.forTableColumn());
            kolonaProizvodjac.setCellFactory(TextFieldTableCell.forTableColumn());
            kolonaTip.setCellFactory(TextFieldTableCell.forTableColumn());
            kolonaKolicina.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            kolonaCena.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            
            kolonaOpis.setOnEditCommit(e->{
               //((KomponentaSaSlikom) e.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
            });
            
            kolonaProizvodjac.setOnEditCommit(new EventHandler<CellEditEvent<KomponentaSaSlikom, String>>() {
                @Override
                public void handle(CellEditEvent<KomponentaSaSlikom, String> t) {
                    ((KomponentaSaSlikom) t.getTableView().getItems().get(t.getTablePosition().getRow())).setProizvodjac(t.getNewValue());
                } 
            });
        }
    }
    
    
    
    
    /*
    private static ScrollBar getVerticalScrollbar(TableView table) {
    ScrollBar result = null;
    for (Node n : table.lookupAll(".scroll-bar")) {
        if (n instanceof ScrollBar) {
            ScrollBar bar = (ScrollBar) n;
            if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                result = bar;
            }
        }
    }       
    return result;
}*/
    
    
    
    
    //tabela u izvestajima
    public static void kreirajTabeluIzvestaja(TableView tabela) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, Integer>("id"));
        //definisanje procentualne sirine kolone
        kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaNaziv = new TableColumn("Naziv komponente");
        kolonaNaziv.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, String>("naziv"));
        kolonaNaziv.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
        kolonaNaziv.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, String>("proizvodjac"));
        kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        //definisanje valueFactory-a za celije kolone
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, Integer>("kolicina"));
        //definisanje procentualne sirine kolone
        kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<IzvestajPOJO, Double>("cena"));
        kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        kolonaCena.setCellFactory(tc -> new TableCell<IzvestajPOJO, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty)
                    setText(null);
                else
                    setText(String.format("%,.2f", value.doubleValue()));
            }
        });
        tabela.getColumns().addAll(kolonaSifra, kolonaNaziv, kolonaProizvodjac, kolonaKolicina, kolonaCena);
    }
    
    
}
