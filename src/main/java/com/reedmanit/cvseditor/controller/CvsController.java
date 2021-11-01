/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.cvseditor.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.cvseditor.data.CsvCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author preed
 */
public class CvsController implements Initializable {

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

   

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            CsvCache dataCache = new CsvCache(new File("C:\\Dell\\TransferToNewLaptop\\Databases\\TolietMap\\TestData.csv"));

            dataCache.buildData();

            borderPaneLayout.setCenter(dataCache.getTableView());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CvsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CvsController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (CsvValidationException ex) {
            Logger.getLogger(CvsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
