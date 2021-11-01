/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.cvseditor.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author preed
 */
public class CsvCache {

    TableView<ObservableList> table = new TableView<>();

    private static final ObservableList<ObservableList<StringProperty>> data = FXCollections.<ObservableList<StringProperty>>observableArrayList();

    private BufferedReader in;

    private String headers[];

    private final List<String> columnNames = new ArrayList<>();

    public CsvCache(File aDataFile) throws IOException {

        Path path = Paths.get(aDataFile.getAbsolutePath());

        long bytes = Files.size(path);
        System.out.println(String.format("%,d bytes", bytes));
        System.out.println(String.format("%,d kilobytes", bytes / 1024));

        long kb = bytes / 1024;

        if (kb > 10280) {
            throw new IOException("CSV File exceeds limit");
        }

        in = new BufferedReader(new FileReader(aDataFile));

    }

    /**
     * public void loadDataFile() throws IOException, CsvValidationException {
     *
     * // create csvReader object and skip first Line CSVReader csvReader = new
     * CSVReaderBuilder(in).build();
     *
     * headers = csvReader.readNextSilently();
     *
     * // csvReader.skip(1); ObservableList<StringProperty> m =
     * FXCollections.<StringProperty>observableArrayList();
     *
     * String[] records;
     *
     * while ((records = csvReader.readNext()) != null) {
     *
     * for (int i = 0; i < records.length; i++) {
     *
     * m.add(new SimpleStringProperty(records[i]));
     *
     * }
     * data.add(m);
     *
     * }
     *
     * }
     *
     */

    public void buildData() throws IOException, CsvValidationException {

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        CSVReader csvReader = new CSVReaderBuilder(in).build();

        headers = csvReader.readNextSilently();

        for (int i = 0; i < headers.length; i++) {

            final int j = i;
            TableColumn col = new TableColumn(headers[i]);
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                System.out.println("Cell Value");
                if (param.getValue().get(j) != null) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                } else {
                    return null;
                }
            });

            table.getColumns().addAll(col);
            this.columnNames.add(col.getText());
        }

        String[] records;

        while ((records = csvReader.readNext()) != null) {

            ObservableList<String> row = FXCollections.observableArrayList();

            for (int i = 0; i < records.length; i++) {
                row.add(new String(records[i]));
            }
            data.add(row);
        }

        //FINALLY ADDED TO TableView
        table.setItems(data);

    }

    public void setDataFile(BufferedReader aDataFile) {
        in = aDataFile;
    }

    public ObservableList<ObservableList<StringProperty>> getdata() {
        return data;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String getaHeader(int i) {
        return headers[i];
    }

    public TableView<ObservableList> getTableView() {
        return table;
    }

}
