/*
 * Copyright 2022 preed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reedmanit.csveditor.controller;

import com.reedmanit.csveditor.userinterface.EditForm;
import com.reedmanit.csveditor.userinterface.InsertForm;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class EditController implements Initializable {

    protected static final org.apache.log4j.Logger editControllerLogger = LogManager.getLogger(EditController.class.getName());

    private static org.apache.log4j.Logger logger = editControllerLogger;

    private CsvController csvController;
    
    private EditForm editForm;
    
    private String action;

    @FXML
    private BorderPane editBorderPane;
    
    @FXML
    private ScrollPane scrollPane;

    @FXML
    AnchorPane anchorPane;
    
    private Button submitBtn;
    
    private Button cancelBtn;
    
    private EditForm theEditForm;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Create the right child node
        submitBtn = new Button("Submit");
        cancelBtn = new Button("Cancel");

        // Make the submit and cancel buttons the same size
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setMaxWidth(Double.MAX_VALUE);

        action = "";
       

        submitBtn.setOnAction(((var e) -> {

           
            action = "submit";
            Stage editStage = csvController.getEditFormStage();

            editStage.close();

        }));

        cancelBtn.setOnAction(((var e) -> {

            action = "cancel";
            Stage editStage = csvController.getEditFormStage();
            editStage.close();
        }));

    }
    
    public void createEditForm(List<Label> labels, List<TextField> textFields) throws Exception {

        logger.info("Enter create Edit Form");

        try {
            theEditForm = new EditForm(labels, textFields);
            GridPane theGridPane = theEditForm.getGridPane();

            editBorderPane.setCenter(theGridPane);

            VBox left = new VBox(submitBtn, cancelBtn);
            editBorderPane.setLeft(left);
            left.setStyle("-fx-padding: 10;");

            editBorderPane.setStyle("-fx-background-color: lightgray;");

        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }

        logger.info("Exit create edit form");

    }
    
    public void setCSVController(CsvController theController) {
        csvController = theController;
    }
    
    public String getAction() {
        return action;
    }
    
    public EditForm getTheEditForm() {
        return theEditForm;
    }
}
