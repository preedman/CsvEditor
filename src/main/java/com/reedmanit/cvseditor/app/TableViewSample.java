/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.cvseditor.app;

import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.cvseditor.data.CsvCache;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author preed
 */
public class TableViewSample extends Application {
    
    

    @Override
    public void start(Stage primaryStage) throws IOException, FileNotFoundException, CsvValidationException {
        StackPane root = new StackPane();
        
  //      CsvCache myTable = new CsvCache();
        
         BufferedReader r = new BufferedReader(new FileReader("C:\\SoftwareDevelopment\\Projects\\CVSReaderTest\\src\\main\\java\\com\\reedmanit\\cvsreadertest\\cbd-bike-racks-2021-04-08.csv"));

    //    myTable.setDataFile(r);

     //   myTable.buildData();
        
      //  TableView<ObservableList<StringProperty>> table = new TableView<>();
        
       // table = populateTable(table);
        
        
//        root.getChildren().add(myTable.getTableView());
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
   /**
    private TableView<ObservableList<StringProperty>> populateTable(TableView<ObservableList<StringProperty>> aTable) throws FileNotFoundException, IOException, CsvValidationException {

        aTable.getItems().clear();
        aTable.getColumns().clear();
        CsvCache cache = CsvCache.getInstance();
        BufferedReader r;
        r = new BufferedReader(new FileReader("C:\\SoftwareDevelopment\\Projects\\CVSReaderTest\\src\\main\\java\\com\\reedmanit\\cvsreadertest\\cbd-bike-racks-2021-04-08.csv"));
        cache.setDataFile(r);
        cache.loadDataFile();

        for (int column = 0; column < cache.getHeaders().length; column++) {
            aTable.getColumns().add(createColumn(column, cache.getaHeader(column)));
        }
        
        aTable.setItems(cache.getdata());
        
        return aTable;
        
    }

    private TableColumn<ObservableList<StringProperty>, String> createColumn(final int columnIndex, String columnTitle) {

        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
        String title;
        if (columnTitle == null || columnTitle.trim().length() == 0) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
                        ObservableList<StringProperty> values = cellDataFeatures.getValue();
                        if (columnIndex >= values.size()) {
                            return new SimpleStringProperty("");
                        } else {
                            return cellDataFeatures.getValue().get(columnIndex);
                        }
                    }
                });
        return column;

    }
    * /

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
