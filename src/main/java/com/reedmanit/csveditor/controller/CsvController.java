/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.controller;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.csveditor.data.CsvCache;
import com.reedmanit.csveditor.ui.rules.ExitWithOutSave;
import com.reedmanit.csveditor.business.rules.FileSizeLimit;
import com.reedmanit.csveditor.business.rules.ValidHeaders;
import com.reedmanit.csveditor.data.SearchItem;
import com.reedmanit.csveditor.data.Util;

import com.reedmanit.csveditor.ui.rules.TurnOnSaveButton;
import com.reedmanit.csveditor.ui.rules.TurnOnSearchButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

/**
 * FXML Controller class
 *
 * @author preed
 */
public class CsvController implements Initializable {

    /**
     * Initializes the controller class.
     */
    protected static final org.apache.log4j.Logger csvControllerLogger = LogManager.getLogger(CsvController.class.getName());

    private static org.apache.log4j.Logger logger = csvControllerLogger;

    private Stage theStage; // see main for setting - used for file name

    @FXML
    private BorderPane borderPaneLayout;

    @FXML
    private Button openFileBT;

    @FXML
    private Button searchButton;

    @FXML
    private Button insertRowBT;

    @FXML
    private Button deleteRowBT;

    @FXML
    private Button aboutBT;

    @FXML
    private Button saveBT;

    @FXML
    private TextField findTF;

    @FXML
    private ComboBox colCB;

    private File theCSVFile;

    private CsvCache dataCache;

    private TableView<ObservableList> tableView = new TableView<>();

    private TableViewSelectionModel<ObservableList> tsm;

    private TableColumn tc = new TableColumn<String, String>();

    private Facts facts;

    private TurnOnSaveButton turnOnSaveButton;

    private Rules rules;

    private RulesEngine rulesEngine;

    private ExitWithOutSave exitwithOutSave;

    private FileSizeLimit fileSizeLimit;

    private ValidHeaders validHeaders;

    private TurnOnSearchButton turnOnSearchButton;

    private TextInputDialog searchDialog;

    private ObservableList<String> cbData = FXCollections.<String>observableArrayList();

    private Stack<SearchItem> selectedItems;

    private static int pos = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        logger.info("Starting controller");

        saveBT.setDisable(true);
        searchButton.setDisable(true);
        findTF.setDisable(true);
        colCB.setDisable(true);
        insertRowBT.setDisable(true);
        deleteRowBT.setDisable(true);

        selectedItems = new Stack<SearchItem>();

        setUpRules();
        

        tsm = tableView.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);
        tsm.setCellSelectionEnabled(true);

        
        openFileBT.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try {
                    boolean fileOpen = openFile();   // open the file 
                    if (fileOpen) {
                        tableView = dataCache.getTableView();  // get the table that was created by the cache
                        tableView.setEditable(true);
                        borderPaneLayout.setCenter(tableView);  // center it
                        insertRowBT.setDisable(false);
                        deleteRowBT.setDisable(false);
                        

                        Iterator i = tableView.getColumns().iterator();

                        while (i.hasNext()) {
                            tc = (TableColumn) i.next();
                            setUp(tc);  // for every column on the screen, set up the on edit function. Pass in the Table Column
                        }
                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());

                } catch (CsvValidationException ex) {
                    System.out.println(ex.toString());
                }
            }
        });

        logger.info("Exit Controller");
    }

    

    private void setUpRules() {

        logger.info("Entering Set Up Rules");

        rulesEngine = new DefaultRulesEngine();

        facts = new Facts();

        facts.put("OpenButtonState", "DISABLE");

        turnOnSaveButton = new TurnOnSaveButton();
        turnOnSaveButton.setSaveButton(saveBT);

        exitwithOutSave = new ExitWithOutSave();

        facts.put("OpenNewFile", "FALSE");
        facts.put("EditOccured", "FALSE");

        fileSizeLimit = new FileSizeLimit();

        facts.put("FileSize", 0);   // set up file size rule with inital size

        validHeaders = new ValidHeaders();
        List<String> listOfHeaders = new ArrayList<String>();  // create an empty array for initial values

        facts.put("Headers", listOfHeaders);

        turnOnSearchButton = new TurnOnSearchButton();

        rules = new Rules();
        rules.register(turnOnSaveButton);
        rules.register(exitwithOutSave);
        rules.register(fileSizeLimit);
        rules.register(validHeaders);
        rules.register(turnOnSearchButton);

        rulesEngine.fire(rules, facts);

        logger.info("Exit Set Up Rules");

    }

    private void setUp(TableColumn t) {

        logger.info("Enter SetUp " + t.getText());

        t.setCellFactory(TextFieldTableCell.forTableColumn());

        t.setOnEditCommit(e -> OnEditCommit(e));  // set up the function to call for every table column when the user edits the data.

        logger.info("Exit SetUp");
    }

    public boolean openFile() throws IOException, CsvValidationException {

        logger.info("Enter Open File");

        boolean fileOpen = false;

        facts.put("OpenNewFile", "TRUE");
        rulesEngine.fire(rules, facts);

        if (exitwithOutSave.isShowAlert()) {
            Alert a = new Alert(AlertType.WARNING, "New File Opened but data has been modified - have you forgotten to save.?");
            a.showAndWait();
            facts.put("OpenNewFile", "FALSE");
            facts.put("EditOccured", "FALSE");
            rulesEngine.fire(rules, facts);
            exitwithOutSave.setShowAlert(false);
        }
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialFileName("*.csv");

        theCSVFile = fileChooser.showOpenDialog(new Stage());

        if (theCSVFile != null) {

            try {
                theStage.setTitle(theCSVFile.getName());
                processCSVFile();
                if (turnOnSearchButton.isTurnOnSearchButton()) {
                    setUpSearch();
                }
                fileOpen = true;
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
                throw ioe;
            } catch (CsvValidationException ve) {
                logger.error(ve.getMessage());
                throw ve;
            }

        }
        logger.info("Exit Open File");
        return fileOpen;

    }

    private void setUpSearch() {

        logger.info("Enter set up search");

        searchButton.setDisable(false);
        findTF.setDisable(false);
        colCB.setDisable(false);
        cbData.clear();

        cbData.add("Any Column");

        for (int i = 0; i < dataCache.getHeaders().length; i++) {
            cbData.add(dataCache.getaHeader(i));
        }

        colCB.setItems(cbData);

        colCB.getSelectionModel().selectFirst();

        logger.info("Exit set up search");
    }

    private void processCSVFile() throws IOException, CsvValidationException {

        logger.info("Enter process csv file");

        if (fileSizeExceedsLimit()) {
            Alert a = new Alert(AlertType.ERROR, "CSV File exceeds file size limit. Can not be used for processing");
            a.showAndWait();
            facts.put("FileSize", 0);  // reset rule variables for rule processing
            fileSizeLimit.setShowAlert(false);
            logger.error("Throwing an exception");
            throw new IOException("File Size Limit Exceeded");
        } else {
            fileSizeLimit.setShowAlert(false);
            facts.put("OpenButtonState", "DISABLE");

            dataCache = new CsvCache(theCSVFile);
            dataCache.buildData();
            facts.put("Headers", Arrays.asList(dataCache.getHeaders()));  // put the headers in the rule for validation

            rulesEngine.fire(rules, facts);

            if (validHeaders.isInValidData()) {
                Alert a = new Alert(AlertType.ERROR, "CSV File has a header record which contains data. This is invalid.");
                a.showAndWait();
                facts.put("Headers", new ArrayList<String>());  // reset the headers
                validHeaders.setInValidData(false);  // set the valid headers back to false
                logger.error("Throw an exception");
                throw new IOException("Invalid Data in Headers");   // tell the caller that this was in error

            }
        }

        logger.info("Exit Process CSV file");
    }

    private boolean fileSizeExceedsLimit() throws IOException {

        logger.info("Enter file size exceeds limit");

        Path path = Paths.get(theCSVFile.getAbsolutePath());

        long bytes = Files.size(path);

        logger.info(bytes / 1024 + "kb");

        //System.out.println(String.format("%,d kilobytes", bytes / 1024));
        Long kb = bytes / 1024;

        Integer i = Integer.valueOf(kb.intValue());

        facts.put("FileSize", i);

        rulesEngine.fire(rules, facts);

        logger.info("Exit file size exceeds limit");

        return fileSizeLimit.isShowAlert();
    }

    @FXML
    private void about() {
        Alert a = new Alert(AlertType.INFORMATION, "Simple CSV Editor. Version 0.8. BETA Release. By ReedmanIT");

        a.showAndWait();

    }

    @FXML
    private void deleteRow(ActionEvent event) {

        logger.info("Entering delete row");

        tsm = tableView.getSelectionModel();

        if (tsm.isEmpty()) {
            Alert a = new Alert(AlertType.ERROR, "Please select a row to delete");
            a.showAndWait();

            return;
        }

        tableView.getItems().remove(tsm.getSelectedIndex());

        if (saveBT.isDisabled()) {
            saveBT.setDisable(false);
        }
        facts.put("EditOccured", "TRUE");

        logger.info("Exit delete row");

    }

    @FXML
    private void insertRow(ActionEvent event) {

        logger.info("Enter insert row");

        int idx = tableView.getItems().size();     // find the last row

        ObservableList<String> aNewRow = FXCollections.<String>observableArrayList();  // create a new row of data

        for (int i = 0; i < dataCache.getHeaders().length; i++) {  // fill the row of data with NEW
            aNewRow.add("NEW");

        }

        tableView.getItems().add(idx, aNewRow);  // add the new row to the TableView

        tableView.getSelectionModel().select(idx);  // select the new row
        tableView.edit(idx, tableView.getColumns().get(0));  // set as edit
        tableView.scrollTo(idx);  // scroll to the new row

        if (saveBT.isDisabled()) {
            saveBT.setDisable(false);
        }
        facts.put("EditOccured", "TRUE");

        logger.info("Exit insert row");

    }

    @FXML
    private void searchForData(ActionEvent event) throws IOException {

        logger.info("Enter Search Data");

        tableView.getSelectionModel().selectedItemProperty().addListener(
                new RowSelectChangeListener());

        Util u = new Util();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                logger.info("Enter thread for search");
                int indexOfHeader = 0;
                String selectedItem = null;

                boolean allColumns = false;

                selectedItem = (String) colCB.getSelectionModel().getSelectedItem();  // get the selected item from the combo box

                boolean emptySearchTerm = StringUtils.isAllEmpty(findTF.getText());

                if (!emptySearchTerm) {  // user needs to enter something to find

                    if (selectedItem.equals("Any Column")) {
                        allColumns = true;
                    }

                    if (allColumns) {
                        pos = u.findPositionInAllCols(tableView.getItems(), findTF.getText(), pos);
                    } else {
                        indexOfHeader = dataCache.indexOfHeader(selectedItem);   // which header
                        pos = u.findPositionInCol(tableView.getItems(), findTF.getText(), indexOfHeader, pos);
                    }

                    if (pos > 0) {  // search item found
                        tableView.requestFocus();
                        tableView.getSelectionModel().select(pos - 1);  // the header is first in row, compensate

                        tableView.scrollTo(pos - 1);
                        tableView.getFocusModel().focus(pos - 1);

                        selectedItems.push(new SearchItem(findTF.getText(), pos));

                        logger.info("Audit " + findTF.getText() + " " + pos);

                    } else {

                        Alert a = new Alert(AlertType.WARNING, "Search item " + findTF.getText() + " not found");
                        a.showAndWait();

                        tableView.requestFocus();
                        tableView.getSelectionModel().select(0);  // sit the focus at the first row

                        tableView.scrollTo(0);
                        tableView.getFocusModel().focus(0);
                    }

                } else {
                    Alert a = new Alert(AlertType.WARNING, "Must enter value for search");
                    a.showAndWait();
                }
                logger.info("Exit thread for search");
            }
        });
        logger.info("Exit Search Data");

    }

    @FXML

    private void saveFile(ActionEvent event) throws IOException {

        logger.info("Enter Save File");

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File theDataFile = fileChooser.showSaveDialog(new Stage());

        if (theDataFile != null) {

            ObservableList<ObservableList> data = tableView.getItems();

            Iterator<ObservableList> i = data.iterator();
            CSVWriter writer = new CSVWriter(new FileWriter(theDataFile.getAbsolutePath()));

            writer.writeNext(dataCache.getHeaders());  // write the headers to the new file

            while (i.hasNext()) {

                ObservableList tokens = i.next();

                String s[] = new String[tokens.size()];

                for (int a = 0; a < tokens.size(); a++) {   // get from the observable list a string array which does not remove any quote chars

                    s[a] = (String) tokens.get(a);
                }

                writer.writeNext(s);

            }

            writer.close();
        }
        logger.info("Exit save File");

    }

    public void OnEditCommit(Event e) {

        logger.info("Enter On Edit Commit");

        TableColumn.CellEditEvent<String, String> ce;  // Set up the cell edit event

        ce = (TableColumn.CellEditEvent<String, String>) e; // cast the event from the parameter to the table column event

        logger.info(ce.getOldValue() + " " + ce.getNewValue());

        ObservableList<ObservableList> data = tableView.getItems();  // get the data from the tableview - thats on screen

        //
        // So, set the data in the table view. By getting from the table cell event the row and the column index
        // This index is the position of the cell that has been changed on the screen
        // Set the data item using the index from the new value
        // That is the value that has been entered on the screen
        //
        data.get(ce.getTablePosition().getRow()).set(ce.getTablePosition().getColumn(), ce.getNewValue());

        facts.put("OpenButtonState", "ENABLE");
         facts.put("EditOccured", "TRUE");
        rulesEngine.fire(rules, facts);

        //editOccuredCondition.setState(true);
      //  listOfConditions.add(editOccuredCondition);

        logger.info("Edit On Edit Commit");

    }

    private class RowSelectChangeListener implements ChangeListener {

        //public WebView theWebView;
        public RowSelectChangeListener() {

        }

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            System.out.println("Row selected");

            //     selectedBicycleRack = (BikeRack) observable.getValue();
            //     if (selectedBicycleRack != null) {
            //         bicycleRackLocationTF.setText(selectedBicycleRack.getLocation());
            //     }
        }

    }

    /**
     * @param theStage the theStage to set
     */
    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

}
