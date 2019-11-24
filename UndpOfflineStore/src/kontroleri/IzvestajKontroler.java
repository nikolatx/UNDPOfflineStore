
package kontroleri;

import com.grupa1.dbconnection.IzvestajDAO;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 *
 * @author nikolatimotijevic
 */
public class IzvestajKontroler {
    
    IzvestajDAO izvestajDAO = new IzvestajDAO();
    
    //pravljenje godisnjeg grafika nabavke/prodaje
    public LineChart<String, Number> napraviGrafik() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> grafik = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Mesec");
        yAxis.setLabel("Dinara");
        grafik.setTitle("Obim prodaje i nabavke u poslednjih 12 meseci");
        XYChart.Series prodaja = new XYChart.Series();
        prodaja.setName("Prodaja");
        XYChart.Series nabavka = new XYChart.Series();
        nabavka.setName("Nabavka");
        izvestajDAO.godisnjiGrafik(prodaja, true);
        izvestajDAO.godisnjiGrafik(nabavka, false);
        grafik.getData().addAll(prodaja, nabavka);
        return grafik;
    }
    
    //pravljenje godisnjeg grafika najisplativijih komponenata
    public BarChart<Number, String> napraviGrafikNajisplativije() {
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        BarChart<Number,String> grafik = new BarChart<>(xAxis, yAxis);
        yAxis.setLabel("Komponenta");
        xAxis.setLabel("Dinara");
        grafik.setTitle("5 najisplativijih komponenata");
        XYChart.Series komponente = new XYChart.Series();
        komponente.setName("");
        grafik.setLegendVisible(false);
        izvestajDAO.godisnjiGrafikNajisplativije(komponente);
        //izvestajDAO.godisnjiGrafik(nabavka, false);
        grafik.getData().add(komponente);
        return grafik;
    }
    
    //pravljenje godisnjeg grafika najisplativijih komponenata
    public BarChart<Number, String> napraviGrafikNajprodavanije() {
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        BarChart<Number, String> grafik = new BarChart<>(xAxis, yAxis);
        yAxis.setLabel("Komponenta");
        xAxis.setLabel("Komada");
        grafik.setTitle("5 najprodavanijih komponenata");
        XYChart.Series komponente = new XYChart.Series();
        komponente.setName("");
        grafik.setLegendVisible(false);
        izvestajDAO.godisnjiGrafikNajprodavanije(komponente);
        grafik.getData().add(komponente);
        return grafik;
    }
    
    public void dnevniIzvestaj(TableView tabela, Label sumaLabel, boolean prodaja) {
        String uslov="WHERE pr.datum=DATE(NOW())";
        izvestajDAO.kreirajIzvestaj(tabela, uslov, sumaLabel, prodaja);
    }

    public void nedeljniIzvestaj(TableView tabela, Label sumaLabel, boolean prodaja) {
        String uslov="WHERE YEARWEEK(pr.datum)=YEARWEEK(CURDATE())";
        izvestajDAO.kreirajIzvestaj(tabela, uslov, sumaLabel, prodaja);
    }
    
    public void mesecniIzvestaj(TableView tabela, Label sumaLabel, boolean prodaja) {
        String uslov="WHERE MONTH(CURDATE())=MONTH(pr.datum) AND YEAR(CURDATE())=YEAR(pr.datum)";
        izvestajDAO.kreirajIzvestaj(tabela, uslov, sumaLabel, prodaja);
    }
    
    public void godisnjiIzvestaj(TableView tabela, Label sumaLabel, boolean prodaja) {
        String uslov="WHERE YEAR(CURDATE())=YEAR(pr.datum)";
        izvestajDAO.kreirajIzvestaj(tabela, uslov, sumaLabel, prodaja);
    }
    
    public void intervalniIzvestaj(TableView tabela, String uslov, Label sumaLabel, boolean prodaja) {
        izvestajDAO.kreirajIzvestaj(tabela, uslov, sumaLabel, prodaja);
    }
}
