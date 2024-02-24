/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.data;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.csveditor.business.rules.Headers;
import com.reedmanit.csveditor.business.rules.RowValidation;
import com.reedmanit.csveditor.utility.LoggingSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class CsvCache {

    protected static final org.apache.log4j.Logger cacheLogger = LogManager.getLogger(CsvCache.class.getName());

    private static org.apache.log4j.Logger logger = cacheLogger;

    TableView<ObservableList> table = new TableView<>();

    private static final ObservableList<ObservableList<StringProperty>> data = FXCollections.<ObservableList<StringProperty>>observableArrayList();

    private BufferedReader in;

    private String headers[];

    private final List<String> columnNames = new ArrayList<>();

    private LoggingSupport logSupport;

    private Headers theHeaders;

    public CsvCache(File aDataFile) throws IOException {

        in = new BufferedReader(new FileReader(aDataFile));

    }

    public void buildData() throws IOException, CsvValidationException {

        logger.info("Entering build csv cache");

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        RowValidation rowValid = RowValidation.getInstance();

        Headers theHeaders = Headers.getInstance();

        CSVParser parser
                = new CSVParserBuilder()
                        .withSeparator(',')
                        //   .withIgnoreQuotations(true)
                        .build();

        CSVReader csvReader = new CSVReaderBuilder(in).withCSVParser(parser).build();

        headers = csvReader.readNextSilently();  // pull out the headers from the file

        theHeaders.setListOfHeaders(Arrays.asList(headers));

        if (theHeaders.isValidHeader()) {   // are these valid headers

            for (int i = 0; i < headers.length; i++) {

                final int j = i;
                TableColumn col = new TableColumn(headers[i]);

                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> {
                    logger.info("Value set " + param.getValue().get(j).toString());
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                    //   logger.info("Entering set cell value");

                    //       if (param.getValue().get(j) != null) {
                    //           logger.info("enter set cell value");
                    //           logger.info("Value set " + param.getValue().get(j).toString());
                    //           return new SimpleStringProperty(param.getValue().get(j).toString());
                    //       } else {
                    //           logger.info("exit set cell value");
                    // return null;
                    //         return new SimpleStringProperty(" ");
                    //      }
                    // logger.info("exit set cell value");
                });

                table.getColumns().addAll(col);
                this.columnNames.add(col.getText());
            }
        } else {
            logger.info("Headers contain invalid data " + Arrays.toString(headers));
            throw new CsvValidationException("Headers contain data, invalid");
        }

        String[] records;

        while ((records = csvReader.readNext()) != null) {

            ObservableList<String> row = FXCollections.observableArrayList();

            for (int i = 0; i < records.length; i++) {
                row.add(new String(records[i]));
            }
            if (rowValid.doesHeadersEqualRow(columnNames, row)) {  // the number of items in the row should be equal to the headers
                data.add(row);
            } else {
                logger.info("Row record " + row);
                throw new CsvValidationException("row and column size not the same " + "row " + row.size() + "col " + columnNames.size());
            }
        }

        //FINALLY ADDED TO TableView
        table.setItems(data);

        logger.info("Leaving build cache");

    }

    public void OnEditCommit(Event e) {

        System.out.println(e.getEventType());

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

    public int indexOfHeader(String aHeader) {
        int index = 0;

        for (int i = 0; i < headers.length; i++) {

            if (headers[i].contains(aHeader)) {
                index = i;
                break;
            }
        }

        return index;
    }

}
