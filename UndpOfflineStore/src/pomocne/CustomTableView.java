/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomocne;

import com.grupa1.model.KomponentaSaSlikom;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 *
 * @author NN
 */
public class CustomTableView<S> extends StackPane {
    
    private TableView<S> table;
    
    @SuppressWarnings("rawtypes")
    public CustomTableView() {
        this.table = new TableView<S>();
        final GridPane grid = new GridPane();
        this.table.getColumns().addListener(new ListChangeListener<TableColumn>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TableColumn> c) {
                grid.getColumnConstraints().clear();
                ColumnConstraints[] arr1=new ColumnConstraints[CustomTableView.this.table.getColumns().size()];
                StackPane[] arr2=new StackPane[CustomTableView.this.table.getColumns().size()];
                int i=0;
                for(TableColumn column:CustomTableView.this.table.getColumns()) {
                    CustomTableColumn col=(CustomTableColumn) column;
                    ColumnConstraints consta=new ColumnConstraints();
                    consta.setPercentWidth(col.getPercentWidth());
                    StackPane sp=new StackPane();
                    if (i==0) {
                        NumberBinding diff=sp.widthProperty().subtract(3.75);
                        column.prefWidthProperty().bind(diff);
                    } else {
                        column.prefWidthProperty().bind(sp.widthProperty());
                    }
                    arr1[i]=consta;
                    arr2[i]=sp;
                    i++;
                }
                grid.getColumnConstraints().addAll(arr1);
                grid.addRow(0, arr2);
            }
        });
        getChildren().addAll(grid, table);
    }
    
    public TableView<S> getTableView() {
        return this.table;
    }
}

class CustomTableColumn<S, T> extends TableColumn<S, T> {
    private SimpleDoubleProperty percentWidth = new SimpleDoubleProperty();
    public CustomTableColumn(String columnName) {
        super(columnName);
    }
    
    public double getPercentWidth() {
        return percentWidth.get();
    }

    public SimpleDoubleProperty percentWidth() {
        return percentWidth;
    }

    public void setPercentWidth(double percentWidth) {
        this.percentWidth.set(percentWidth);
    }

    
    
    
}