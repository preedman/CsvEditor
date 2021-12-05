/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.controller;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.csveditor.data.CsvCache;
import com.reedmanit.csveditor.rules.ExitWithOutSave;
import com.reedmanit.csveditor.rules.TurnOnSaveButton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    @FXML
    private BorderPane borderPaneLayout;

    @FXML
    private Button openFileBT;

    @FXML
    private Button insertRowBT;

    @FXML
    private Button deleteRowBT;

    @FXML
    private Button aboutBT;

    @FXML
    private Button saveBT;

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
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        saveBT.setDisable(true);

        rulesEngine = new DefaultRulesEngine();

        facts = new Facts();

        facts.put("OpenButtonState", "DISABLE");

        turnOnSaveButton = new TurnOnSaveButton();
        turnOnSaveButton.setSaveButton(saveBT);
        
        exitwithOutSave = new ExitWithOutSave();
        
        
        
        facts.put("OpenNewFile", "FALSE");
        facts.put("EditOccured", "FALSE");

        rules = new Rules();
        rules.register(turnOnSaveButton);
        rules.register(exitwithOutSave);
        
       

        rulesEngine.fire(rules, facts);

        tsm = tableView.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);
        tsm.setCellSelectionEnabled(true);

        openFileBT.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try {
                    openFile();
                    tableView = dataCache.getTableView();
                    tableView.setEditable(true);
                    borderPaneLayout.setCenter(tableView);

                    Iterator i = tableView.getColumns().iterator();

                    while (i.hasNext()) {
                        tc = (TableColumn) i.next();
                        setUp(tc);  // for every column on the screen, set up the on edit function. Pass in the Table Column
                    }

                } catch (IOException ex) {
                    Logger.getLogger(CsvController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CsvValidationException ex) {
                    Logger.getLogger(CsvController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        ObservableList<TablePosition> s = tsm.getSelectedCells();

        Iterator i = s.iterator();

        while (i.hasNext()) {
            TablePosition p = (TablePosition) i.next();
            System.out.println(p.getColumn());
        }

    }

    private void setUp(TableColumn t) {

        System.out.println(t.getText());

        t.setCellFactory(TextFieldTableCell.forTableColumn());

        t.setOnEditCommit(e -> OnEditCommit(e));  // set up the function to call for every table column when the user edits the data.

    }

    public void openFile() throws IOException, CsvValidationException {

        System.out.println("File Open");
        
        facts.put("OpenNewFile", "TRUE");
        rulesEngine.fire(rules, facts);
        
        if (exitwithOutSave.isShowAlert()) {
            Alert a = new Alert(AlertType.WARNING, "New File Opened but data has been modified - have you forgotten to save.?");
            a.showAndWait();
            facts.put("OpenNewFile", "FALSE");
            facts.put("EditOccured", "FALSE");
            exitwithOutSave.setShowAlert(false);
        }

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialFileName("*.csv");

        theCSVFile = fileChooser.showOpenDialog(new Stage());

        if (theCSVFile != null) {

            
            facts.put("OpenButtonState", "DISABLE");
            rulesEngine.fire(rules, facts);
            dataCache = new CsvCache(theCSVFile);
            dataCache.buildData();

        }

    }

    @FXML
    private void about() {
        Alert a = new Alert(AlertType.INFORMATION, "Simple CSV Editor. By ReedmanIT");

        a.showAndWait();

    }

    @FXML
    private void saveFile(ActionEvent event) throws IOException {

        System.out.println("Save file");

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

    }

    public void OnEditCommit(Event e) {

        System.out.println("Edit");

        TableColumn.CellEditEvent<String, String> ce;  // Set up the cell edit event

        ce = (TableColumn.CellEditEvent<String, String>) e; // cast the event from the parameter to the table column event

        System.out.println(ce.getOldValue() + " " + ce.getNewValue());

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

        

    }
}
