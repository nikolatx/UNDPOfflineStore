
package kontroleri;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author nikolatimotijevic
 */
public class IzvestajKontroler {
    
    public static LineChart<String, Number> napraviGrafik() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> grafik = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Mesec");
        yAxis.setLabel("Dinara");
        grafik.setTitle("Obim prodaje i nabavke u poslednjih 12 meseci");
        
        XYChart.Series prodaja = new XYChart.Series();
        prodaja.setName("Prodaja");
        //XYChart.Series nabavka = new XYChart.Series();
        //prodaja.setName("Nabavka");
        
        
        prodaja.getData().add(new XYChart.Data("Jan", 23));
        prodaja.getData().add(new XYChart.Data("Feb", 14));
        prodaja.getData().add(new XYChart.Data("Mar", 15));
        prodaja.getData().add(new XYChart.Data("Apr", 24));
        prodaja.getData().add(new XYChart.Data("May", 34));
        prodaja.getData().add(new XYChart.Data("Jun", 36));
        prodaja.getData().add(new XYChart.Data("Jul", 22));
        prodaja.getData().add(new XYChart.Data("Aug", 45));
        prodaja.getData().add(new XYChart.Data("Sep", 43));
        prodaja.getData().add(new XYChart.Data("Oct", 17));
        prodaja.getData().add(new XYChart.Data("Nov", 29));
        prodaja.getData().add(new XYChart.Data("Dec", 25));
        
        grafik.getData().add(prodaja);
        
        return grafik;
    }
    
    
    
}
