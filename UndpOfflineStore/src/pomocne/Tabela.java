
package pomocne;

import com.grupa1.model.IzvestajPOJO;
import com.grupa1.model.Komponenta;
import com.grupa1.model.KomponentaSaSlikom;
import com.grupa1.model.Slika;
import java.io.File;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
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
        
        //TableColumn<KomponentaSaSlikom, String> kolonaSlika = new TableColumn<>("Slika");
        TableColumn<KomponentaSaSlikom, Slika> kolonaSlika = new TableColumn<KomponentaSaSlikom, Slika>("Slika");
        
        //!!!!!!DBUtil.ubaciKomponentu slikaproperty
        
        kolonaSlika.setCellValueFactory(new PropertyValueFactory("slika"));
        
        kolonaSlika.setCellFactory(new Callback<TableColumn<KomponentaSaSlikom, Slika>, TableCell<KomponentaSaSlikom, Slika>>(){
            @Override
            public TableCell<KomponentaSaSlikom, Slika> call(TableColumn<KomponentaSaSlikom, Slika> param) {
                
                TableCell<KomponentaSaSlikom, Slika> cell = new TableCell<KomponentaSaSlikom, Slika>() {
                    
                    int a=0;
                    
                    @Override
                    public void updateItem(Slika item, boolean empty) {
                        //List<KomponentaSaSlikom> result = list.stream().filter(it -> item.getNaziv().equals( .getName()).collect(Collectors.toList());
                        /*
                        KomponentaSaSlikom kompo=new KomponentaSaSlikom(1, "", "", "", 1, 1, item);
                        
                        a=Collections.binarySearch(tabela.getItems(), kompo, new Comparator<KomponentaSaSlikom>() {
                        @Override
                        public int compare(KomponentaSaSlikom o1, KomponentaSaSlikom o2) {
                            String s1=((Slika)o1.getSlika()).getNaziv();
                            String s2=((Slika)o2.getSlika()).getNaziv();
                            return s1.compareTo(s2);
                            }
                        });
                        */
                        boolean exists = tabela.getItems().stream().anyMatch(o -> ((Slika)o.getSlika()).getNaziv().equals(item.getNaziv()));
                        
                        if (tabela.getItems()!=null) {
                        
                            StackPane box=new StackPane();
                            ImageView imageview = new ImageView();
                            //super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                            if (item != null) {
                                box.setPadding(new Insets(4, 4, 4, 4));

                                //Image img = null;
                                KomponentaSaSlikom k;
                                try {
                                    k = (KomponentaSaSlikom)tabela.getItems().get(getIndex());
                                    String nazivSlike=((Slika)k.getSlika()).getNaziv();
                                    String slikaFajl=Konst.SLIKE_PATH+"\\"+nazivSlike;
                                    if (!nazivSlike.equals("") && !nazivSlike.isEmpty() && k.getSlika()!=null) {

                                        Image img = new Image(new File(slikaFajl).toURI().toString());

                                        imageview.setImage(img);
                                        imageview.setFitHeight(50.0); //50.0);
                                        imageview.setFitWidth(50.0); //50.0);


                                        if(!box.getChildren().contains(imageview)) {
                                            box.getChildren().add(imageview);
                                            box.setOnMouseClicked(e->{
                                               klikNaSliku(tabela);
                                            });
                                            setGraphic(box);

                                        }
                                    }


                                } catch (NullPointerException ex) {

                                }

                            
                            
                            }
                        }
                    }
                    
                };
                return cell;
            }
        });
           
            
           
        /*
        kolonaSlika.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Slika>("slika"));
        //new PropertyValueFactory("slika"));
        
        Callback<TableColumn<KomponentaSaSlikom, Slika>, TableCell<KomponentaSaSlikom, Slika>> cellFactory;
        
        cellFactory = (TableColumn<KomponentaSaSlikom, Slika> param) -> {
            StackPane box= new StackPane();
            ImageView imageview = new ImageView();
            
            return new TableCell<KomponentaSaSlikom, Slika> () {
                @Override
                public void updateItem(Slika item, boolean empty) {
                    
                    if (item != null) {
                        box.setPadding(new Insets(4, 4, 4, 4));
                        //Image img = null;
                        KomponentaSaSlikom k = (KomponentaSaSlikom)tabela.getItems().get(getIndex()); // getTableView().getItems().get(getIndex());
                        //String slikaFajl=Konst.SLIKE_PATH+"\\"+k.getSlika();
                        String slikaFajl=Konst.SLIKE_PATH+"\\"+((Slika)k.getSlika()).getNaziv();
                        if (!((Slika)k.getSlika()).getNaziv().equals("") && !((Slika)k.getSlika()).getNaziv().isEmpty() && k.getSlika()!=null) {
                            
                            Image img = new Image(new File(slikaFajl).toURI().toString());
                        
                            imageview.setImage(img);
                            imageview.setFitHeight(50.0); //50.0);
                            imageview.setFitWidth(50.0); //50.0);
                        
                    
                        if(!box.getChildren().contains(imageview)) {
                            box.getChildren().add(imageview);
                            box.setOnMouseClicked(e->{
                               klikNaSliku(tabela);
                            });
                            setGraphic(box);

                        }
                        }
                    }
                }
            };
        };
        
        kolonaSlika.setCellFactory(cellFactory);
        */
        
        
        
        /*
        kolonaSlika.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("slika"));
        //new PropertyValueFactory("slika"));
        
        Callback<TableColumn<KomponentaSaSlikom, String>, TableCell<KomponentaSaSlikom, String>> cellFactory;
        
        cellFactory = (TableColumn<KomponentaSaSlikom, String> param) -> {
            StackPane box= new StackPane();
            ImageView imageview = new ImageView();
            
            return new TableCell<KomponentaSaSlikom, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    
                    if (item != null) {
                        box.setPadding(new Insets(4, 4, 4, 4));
                        //Image img = null;
                        KomponentaSaSlikom k = (KomponentaSaSlikom)tabela.getItems().get(getIndex()); // getTableView().getItems().get(getIndex());
                        String slikaFajl=Konst.SLIKE_PATH+"\\"+k.getSlika();
                        if (!k.getSlika().equals("") && !k.getSlika().isEmpty() && k.getSlika()!=null) {
                            
                            Image img = new Image(new File(slikaFajl).toURI().toString());
                        
                            imageview.setImage(img);
                            imageview.setFitHeight(50.0); //50.0);
                            imageview.setFitWidth(50.0); //50.0);
                        
                    
                        if(!box.getChildren().contains(imageview)) {
                            box.getChildren().add(imageview);
                            box.setOnMouseClicked(e->{
                               klikNaSliku(tabela);
                            });
                            setGraphic(box);

                        }
                        }
                    }
                }
            };
        };
        
        kolonaSlika.setCellFactory(cellFactory);
        
        */
        
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
        //kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Double>("cena"));
        //kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        //kolonaCena.setPrefWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        //kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 10 0 0;");
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT;");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<KomponentaSaSlikom, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty) ;
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
        
        double[] widths = {9, 5, 42, 10, 19, 7, 10};//define the width of the columns
        //calculate the sum of the width
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
    
    //tabela u pretrazi i azuriranju
    public static void kreirajCustomTabelu(CustomTableView tabela, boolean editable) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        //tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //podesavanje kolone sa slikom komponente
        CustomTableColumn<KomponentaSaSlikom, String> kolonaSlika = new CustomTableColumn<KomponentaSaSlikom, String>("Slika");
        Callback<CustomTableColumn<KomponentaSaSlikom, String>, TableCell<KomponentaSaSlikom, String>> cellFactory;
        kolonaSlika.setCellValueFactory(new PropertyValueFactory("slika"));
        
        /*
        cellFactory = (CustomTableColumn<KomponentaSaSlikom, String> param) -> {
            StackPane box= new StackPane();
            ImageView imageview = new ImageView();
            
            return new TableCell<KomponentaSaSlikom, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    if (item != null) {
                        box.setPadding(new Insets(4, 4, 4, 4));
                        //Image img = null;
                        KomponentaSaSlikom k = getTableView().getItems().get(getIndex());
                        String slikaFajl=Konst.SLIKE_PATH+"\\"+k.getSlika();
                        if (!k.getSlika().equals("") && !k.getSlika().isEmpty() && k.getSlika()!=null) {
                            
                            Image img = new Image(new File(slikaFajl).toURI().toString());
                        
                            imageview.setImage(img);
                            imageview.setFitHeight(50.0); //50.0);
                            imageview.setFitWidth(50.0); //50.0);
                        }
                    }
                    if(!box.getChildren().contains(imageview)) {
                        box.getChildren().add(imageview);
                        box.setOnMouseClicked(e->{
                            klikNaSliku(tabela);
                        });
                        setGraphic(box);
                    }
                }
            };
        };
        kolonaSlika.setCellFactory(cellFactory);
        */

        //kolonaSlika.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width       
        kolonaSlika.setPrefWidth(1f * Integer.MAX_VALUE * 9); // 9% width       
        
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("id"));
        //definisanje procentualne sirine kolone
        //kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        kolonaSifra.setPrefWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("naziv"));
        //kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 42); // 42% width
        kolonaOpis.setPrefWidth(1f * Integer.MAX_VALUE * 42); // 42% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("proizvodjac"));
        //kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaProizvodjac.setPrefWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, String>("tip"));
        //kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 19); // 19% width
        kolonaTip.setPrefWidth(1f * Integer.MAX_VALUE * 19); // 19% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Integer>("kolicina"));
        //kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 7); // 10% width
        kolonaKolicina.setPrefWidth(1f * Integer.MAX_VALUE * 7); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<KomponentaSaSlikom, Double>("cena"));
        //kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        kolonaCena.setPrefWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<KomponentaSaSlikom, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", value.doubleValue()));
                }
            }
        });

        //dodavanje kolona u tabelu
        
        tabela.getTableView().getColumns().addAll(kolonaSlika, kolonaSifra, kolonaOpis, 
                kolonaProizvodjac, kolonaTip, kolonaKolicina, kolonaCena);
        
        
        
        
        
        /*
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
        */
    }
    //tabela u nabavci i prodaji
    public static void kreirajTabeluBezSlike(TableView tabela, boolean editable) {
        //zabrana menjanja velicine da bi se zadale sirine kolona
        //tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //dodavanje nove kolone odgovarajuceg naziva
        TableColumn kolonaSifra = new TableColumn("Sifra");
        kolonaSifra.setStyle( "-fx-alignment: CENTER;");
        //definisanje valueFactory-a za celije kolone
        kolonaSifra.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("id"));
        //definisanje procentualne sirine kolone
        //kolonaSifra.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        TableColumn kolonaOpis = new TableColumn("Naziv komponente");
        kolonaOpis.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("naziv"));
        //kolonaOpis.setMaxWidth(1f * Integer.MAX_VALUE * 37); // 37% width
        kolonaOpis.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaProizvodjac = new TableColumn("Proizvodjac");
        kolonaProizvodjac.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("proizvodjac"));
        //kolonaProizvodjac.setMaxWidth(1f * Integer.MAX_VALUE * 23); // 23% width
        kolonaProizvodjac.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaTip = new TableColumn("Tip");
        kolonaTip.setCellValueFactory(new PropertyValueFactory<Komponenta, String>("tip"));
        //kolonaTip.setMaxWidth(1f * Integer.MAX_VALUE * 16); // 16% width
        kolonaTip.setStyle( "-fx-alignment: CENTER;");
        TableColumn kolonaKolicina = new TableColumn("Kolicina");
        kolonaKolicina.setCellValueFactory(new PropertyValueFactory<Komponenta, Integer>("kolicina"));
        //kolonaKolicina.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kolonaKolicina.setStyle( "-fx-alignment: CENTER;");
        
        TableColumn kolonaCena = new TableColumn("Cena");
        kolonaCena.setCellValueFactory(new PropertyValueFactory<Komponenta, Double>("cena"));
        //kolonaCena.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width
        kolonaCena.setStyle("-fx-alignment: CENTER-RIGHT");
        //podesavanje formata cene: 2 decimale i thousand separator
        kolonaCena.setCellFactory(tc -> new TableCell<Komponenta, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", value.doubleValue()));
                }
            }
        });

        //dodavanje kolona u tabelu
        tabela.getColumns().addAll(kolonaSifra, kolonaOpis, kolonaProizvodjac,
                kolonaTip, kolonaKolicina, kolonaCena);
    }
    
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
