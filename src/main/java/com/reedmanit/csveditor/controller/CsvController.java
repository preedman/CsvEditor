/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.controller;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.csveditor.business.rules.Data;
import com.reedmanit.csveditor.business.rules.FileSizeLimit;
import com.reedmanit.csveditor.business.rules.FileValidation;
import com.reedmanit.csveditor.data.CsvCache;
import com.reedmanit.csveditor.ui.rules.ExitWithOutSave;
import com.reedmanit.csveditor.data.SearchItem;
import com.reedmanit.csveditor.data.Util;
import com.reedmanit.csveditor.ui.rules.Edit;
import com.reedmanit.csveditor.ui.rules.ExitWithOutSaveRule;
import com.reedmanit.csveditor.ui.rules.FileOpen;

import com.reedmanit.csveditor.ui.rules.TurnOnSaveButton;
import com.reedmanit.csveditor.ui.rules.TurnOnSaveButtonRule;
import com.reedmanit.csveditor.ui.rules.TurnOnSearchButton;
import com.reedmanit.csveditor.ui.rules.UIEvent;
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
import java.util.ListIterator;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import static java.util.logging.Level.SEVERE;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    private Stage insertFormScreen;

    private Stage editFormScreen;

    private InsertController theInsertController;

    private static Properties prop;

    @FXML
    private BorderPane borderPaneLayout;

    @FXML
    private Button openFileBT;

    @FXML
    private Button searchButton;

    @FXML
    private Button insertRowBT;

    @FXML
    private Button editRowBT;

    @FXML
    private Button deleteRowBT;

    @FXML
    private Button aboutBT;

    @FXML
    private Button saveBT;

    @FXML
    private Button saveButton;

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

    private TurnOnSaveButton turnOnSaveButton;  // this is the old one, to be removed when the one below is working

    private TurnOnSaveButtonRule turnOnSaveButtonRule;

    private Rules rules;

    private RulesEngine rulesEngine;

    private ExitWithOutSaveRule exitWithOutSaveRule;

    private UIEvent aUIEvent;

    private Data dataRules;
    
    private FileSizeLimit fileSizeLimit;
    
    private FileValidation fileValidation;

    //private TurnOnSearchButton turnOnSearchButton;
    private TextInputDialog searchDialog;

    private ObservableList<String> cbData = FXCollections.<String>observableArrayList();

    private Stack<SearchItem> selectedItems;

    private static int pos = 0;

    private EditController theEditController;

    private ActionEvent mySaveFileEvent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        logger.info("Starting controller");

        saveBT.setDisable(true);
        saveButton.setDisable(true);
        searchButton.setDisable(true);
        findTF.setDisable(true);
        colCB.setDisable(true);
        insertRowBT.setDisable(true);
        deleteRowBT.setDisable(true);

        selectedItems = new Stack<SearchItem>();

        setUpRules();

        tsm = tableView.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);

        openFileBT.setOnAction((ActionEvent e) -> {
            try {
                boolean fileOpen = openFile();   // open the file
                if (fileOpen) {

                    tableView = dataCache.getTableView();  // get the table that was created by the cache

                    borderPaneLayout.setCenter(tableView);  // center it
                    insertRowBT.setDisable(false);
                    deleteRowBT.setDisable(false);

                }

            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());

            }
            catch (CsvValidationException ex) {
                System.out.println(ex.toString());
            }
        });

        logger.info("Exit Controller");
    }

    private void setUpRules() {

        logger.info("Entering Set Up Rules");

        readProperties();

        rulesEngine = new DefaultRulesEngine();

        facts = new Facts();

        facts.put("OpenButtonState", "DISABLE");

        turnOnSaveButton = new TurnOnSaveButton();  // old 
        turnOnSaveButton.setSaveButton(saveBT);   // old

        turnOnSaveButtonRule = new TurnOnSaveButtonRule();

        exitWithOutSaveRule = new ExitWithOutSaveRule();

        facts.put("OpenNewFile", "FALSE");
        facts.put("EditOccured", "FALSE");

        dataRules = new Data();
        
        fileSizeLimit = FileSizeLimit.getInstance();
        
        fileValidation = FileValidation.getInstance();

//        turnOnSearchButton = new TurnOnSearchButton();
        rules = new Rules();
        rules.register(turnOnSaveButton);   // old
        //  rules.register(exitwithOutSave);

//        rules.register(turnOnSearchButton);
        rulesEngine.fire(rules, facts);

        logger.info("Exit Set Up Rules");

    }

    public boolean openFile() throws IOException, CsvValidationException {

        logger.info("Enter Open File");

        boolean fileOpen = false;

        exitWithOutSaveRule.evaluateRule();
        if (exitWithOutSaveRule.getRuleState() == true) {
            ButtonType Yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType No = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert a = new Alert(AlertType.WARNING, "New File Opened but data has been modified. Save Data?", Yes, No);

            Optional<ButtonType> result = a.showAndWait();

            if (result.get() == Yes) {
                logger.info("Yes pushed");

                ActionEvent e = new ActionEvent(saveBT, saveBT);

                saveFileData(e);  // save the file 

            } else {
                logger.info("No pushed");
            }

            exitWithOutSaveRule.resetRuleEvents();
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
                
                setUpSearch();
                
                fileOpen = true;
                aUIEvent = new FileOpen();
                exitWithOutSaveRule.addRuleEvent(aUIEvent);
                exitWithOutSaveRule.evaluateRule();
                turnOnSaveButtonRule.addRuleEvent(aUIEvent);
            }
            catch (IOException ioe) {
                logger.error(ioe.getMessage());
                Alert a = new Alert(AlertType.ERROR, "There has been a data load exception with the CSV file. This might be that the file contains invalid data");
                a.showAndWait();
                theStage.setTitle("");
                fileOpen = false;
                throw ioe;
            }
            catch (CsvValidationException ve) {
                logger.error(ve.getMessage());
                Alert a = new Alert(AlertType.ERROR, "There has been a data load exception with the CSV file. This might be that the file contains invalid data");
                a.showAndWait();
                theStage.setTitle("");
                fileOpen = false;
                throw ve;
            }
            catch (Exception e) {
                logger.error(e.getMessage());
                Alert a = new Alert(AlertType.ERROR, "There has been a data load exception with the CSV file. This might be that the file contains invalid data");
                a.showAndWait();
                theStage.setTitle("");
                fileOpen = false;
                throw e;
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

        dataRules.setSourceFile(theCSVFile);  

      
        
        fileSizeLimit.setLimit(Integer.valueOf(prop.getProperty("filesizelimit")));

        if (fileSizeLimit.isFileSizeGreaterThanLimit(theCSVFile)) {
            Alert a = new Alert(AlertType.ERROR, "CSV File exceeds file size limit. Can not be used for processing");
            a.showAndWait();

            logger.error("Throwing an exception");
            throw new IOException("File Size Limit Exceeded");
        } else {

            facts.put("OpenButtonState", "DISABLE");
            
            if(!fileValidation.isRowDelimited(theCSVFile)) {
                   // if not commas then display a error message
                logger.error("Not a comman separated file");
                throw new IOException("Not a comma separate file");   // tell the caller that this was in error
            }

            dataCache = new CsvCache(theCSVFile);
            try {
                dataCache.buildData();
            } catch (Exception e) {
                throw e;
            }
            

           

            rulesEngine.fire(rules, facts);

        }

        logger.info("Exit Process CSV file");
    }

    @FXML
    private void about() {
        Alert a = new Alert(AlertType.INFORMATION, "Simple CSV Editor " + "Version " + prop.getProperty("version") + " BETA Release. By ReedmanIT");

        a.showAndWait();

    }

    @FXML
    private void editForm(ActionEvent event) throws IOException {
        logger.info("Entering edit form");

        tsm = tableView.getSelectionModel();

        if (tsm.isEmpty()) {
            Alert a = new Alert(AlertType.ERROR, "Please select a row to edit");
            a.showAndWait();

            return;   // stop
        }
        // lets go the user has selected a row
        //
        ArrayList<Label> labels = new ArrayList<Label>();   // build some data structures for the labels and text fields on the edit form
        ArrayList<TextField> textFields = new ArrayList<TextField>();

        ObservableList<TableColumn<ObservableList, ?>> tableColumns = tableView.getColumns();  // get the columns from the table view
        Iterator<TableColumn<ObservableList, ?>> i = tableColumns.iterator();

        while (i.hasNext()) {  // create the labels for the edit form
            TableColumn t = i.next();
            labels.add(new Label(t.getText()));

        }

        ObservableList<String> row = tableView.getItems().get(tsm.getSelectedIndex());  // get the row that the user has selected

        ListIterator<String> l = row.listIterator();

        while (l.hasNext()) {
            textFields.add(new TextField(l.next()));  // add text fields - supplying the data from the selected row

        }

        //Create Stage
        editFormScreen = new Stage();
        editFormScreen.setTitle("Edit Form");

        //Create view from FXML
        FXMLLoader loader = new FXMLLoader(CsvController.class.getResource("/com/reedmanit/csveditor/view/edit.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        theEditController = loader.<EditController>getController();

        theEditController.setCSVController(CsvController.this);

        try {
            theEditController.createEditForm(labels, textFields);
        }
        catch (Exception ex) {
            logger.equals(ex.toString());
        }

//Set view in window
        editFormScreen.setScene(scene);

//Launch
        editFormScreen.showAndWait();   // show the edit form

        if (theEditController.getAction().equals("submit")) {  // only want to do this if the data was changed
            extractFieldsFromEdit(tsm.getSelectedIndex());
            tableView.refresh();  // refresh the tableview - the new row will appear on the screen

            facts.put("EditOccured", "TRUE");
            aUIEvent = new Edit();
            exitWithOutSaveRule.addRuleEvent(aUIEvent);   // a event happened that this rule wants to know about
            turnOnSaveButtonRule.addRuleEvent(aUIEvent);  // edit event

            turnOnSaveButtonRule.evaluateRule();
            if (turnOnSaveButtonRule.getRuleState() == true) {
                if (saveBT.isDisabled()) {
                    saveBT.setDisable(false);
                }
                if (saveButton.isDisabled()) {
                    saveButton.setDisable(false);
                }

            }

        }

        logger.info("Exit Edit Form");
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

        facts.put("EditOccured", "TRUE");
        aUIEvent = new Edit();
        exitWithOutSaveRule.addRuleEvent(aUIEvent); // a event happened that this rule wants to know about
        turnOnSaveButtonRule.addRuleEvent(aUIEvent); // delete event

        turnOnSaveButtonRule.evaluateRule();
        if (turnOnSaveButtonRule.getRuleState() == true) {
            if (saveBT.isDisabled()) {
                saveBT.setDisable(false);
            }
            if (saveButton.isDisabled()) {
                saveButton.setDisable(false);
            }

        }

        logger.info("Exit delete row");

    }

    @FXML
    private void insertForm(ActionEvent event) throws IOException {

        //Create Stage
        insertFormScreen = new Stage();
        insertFormScreen.setTitle("Insert Form");

        ArrayList<Label> labels = new ArrayList<Label>();
        ArrayList<TextField> textFields = new ArrayList<TextField>();

        ObservableList<TableColumn<ObservableList, ?>> tableColumns = tableView.getColumns();
        Iterator<TableColumn<ObservableList, ?>> i = tableColumns.iterator();

        while (i.hasNext()) {
            TableColumn t = i.next();
            labels.add(new Label(t.getText()));
            textFields.add(new TextField());

        }

//Create view from FXML
        FXMLLoader loader = new FXMLLoader(CsvController.class.getResource("/com/reedmanit/csveditor/view/insert.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        theInsertController = loader.<InsertController>getController();

        theInsertController.setCSVController(CsvController.this);

        try {
            theInsertController.createInsertForm(labels, textFields);
        }
        catch (Exception ex) {
            logger.equals(ex.toString());
        }

//Set view in window
        insertFormScreen.setScene(scene);

//Launch
        insertFormScreen.showAndWait();

        if (theInsertController.getActionPerformed().equals("Submit")) {
            extractFieldsFromFormInsert(); // create the row

            aUIEvent = new Edit();
            exitWithOutSaveRule.addRuleEvent(aUIEvent); // a event happened that this rule wants to know about
            turnOnSaveButtonRule.addRuleEvent(aUIEvent); // insert event
            turnOnSaveButtonRule.evaluateRule();
            if (turnOnSaveButtonRule.getRuleState() == true) {
                if (saveBT.isDisabled()) {
                    saveBT.setDisable(false);
                }
                if (saveButton.isDisabled()) {
                    saveButton.setDisable(false);
                }

            }

        }

        logger.info("Exit Insert Form");
    }

    private void extractFieldsFromEdit(int rowIndex) {
        logger.info("Enter Extract Fieldas from Form Edit");

        List<TextField> changedRow = theEditController.getTheEditForm().getFormFields();  // get the form fields

        ObservableList<String> originalRow = FXCollections.<String>observableArrayList();  // create a new empty row of empty data

        originalRow = tableView.getItems().get(rowIndex);  // get the orginal row from the table view and populate the empty row

        for (int i = 0; i < changedRow.size(); i++) {  // set each TextField in the orginal row with the new data, this updates the underlying data model 

            TextField tf = changedRow.get(i);
            originalRow.set(i, tf.getText());

        }

        logger.info("Exit extract fields from form edit");
    }

    private void extractFieldsFromFormInsert() {

        logger.info("Enter Extract Fields from Form Insert");

        List<TextField> newRow = theInsertController.getTheInsertForm().getTextFields();

        var r = newRow.listIterator();

        int idx = tableView.getItems().size();     // find the last row

        ObservableList<String> aNewRow = FXCollections.<String>observableArrayList();  // create a new row of data

        while (r.hasNext()) {
            TextField tf = r.next();
            aNewRow.add(tf.getText());
        }

        tableView.getItems().add(idx, aNewRow);  // add the new row to the TableView

        tableView.getSelectionModel().select(idx);  // select the new row

        tableView.scrollTo(idx);  // scroll to the new row

        logger.info("Leave extract fields from Form Insert");

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

        tableView.scrollTo(idx);  // scroll to the new row

        facts.put("EditOccured", "TRUE");
        aUIEvent = new Edit();
        exitWithOutSaveRule.addRuleEvent(aUIEvent);
        turnOnSaveButtonRule.addRuleEvent(aUIEvent);
        turnOnSaveButtonRule.evaluateRule();
        if (turnOnSaveButtonRule.getRuleState() == true) {
            if (saveBT.isDisabled()) {
                saveBT.setDisable(false);
            }
            if (saveButton.isDisabled()) {
                saveButton.setDisable(false);
            }

        }

        logger.info("Exit insert row");

    }

    @FXML
    private void searchForData(ActionEvent event) throws IOException {

        logger.info("Enter Search Data");

        tableView.getSelectionModel().selectedItemProperty().addListener(
                new RowSelectChangeListener());

        Util u = new Util();

        Platform.runLater(new Runnable() {  // run in separate thread
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

    private void saveFileData(ActionEvent event) throws IOException {
        logger.info("Enter Save File Data");

        logger.info("Source " + event.getSource() + " Name " + event.getEventType().getName());

        // mySaveFileEvent = event;  // save it for later use.
        ObservableList<ObservableList> data = tableView.getItems();

        Iterator<ObservableList> i = data.iterator();
        CSVWriter writer = new CSVWriter(new FileWriter(theCSVFile.getAbsolutePath()));

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

        Alert a = new Alert(AlertType.INFORMATION, "File saved successfully");
        a.showAndWait();
        exitWithOutSaveRule.resetRuleEvents();
    //    turnOnSaveButtonRule.resetRuleEvents();
        saveBT.setDisable(true);

        saveButton.setDisable(true);

        logger.info("Exit Save File Data");

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
            exitWithOutSaveRule.resetRuleEvents();
      //      turnOnSaveButtonRule.resetRuleEvents();
            saveBT.setDisable(true);

            saveButton.setDisable(true);
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

    private static void readProperties() {

        logger.info("Enter read properties");

        try (InputStream input = CsvController.class.getClassLoader().getResourceAsStream("csveditor.properties")) {

            prop = new Properties();

            if (input == null) {
                logger.error("Unable to find properties file");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            logger.info("Software version " + prop.getProperty("version"));

        }
        catch (IOException ex) {
            logger.error(SEVERE + ex.getMessage());
        }
    }

    /**
     * @param theStage the theStage to set
     */
    public void setTheStage(Stage theStage) {
        this.theStage = theStage;
    }

    public Stage getInsertFormStage() {
        return insertFormScreen;
    }

    public Stage getEditFormStage() {
        return editFormScreen;
    }

}
