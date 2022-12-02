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

import com.reedmanit.csveditor.userinterface.InsertForm;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;

/**
 * FXML Controller class
 *
 * @author preed
 */
public class InsertController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private InsertForm theInsertForm;

    protected static final org.apache.log4j.Logger insertControllerLogger = LogManager.getLogger(InsertController.class.getName());

    private static org.apache.log4j.Logger logger = insertControllerLogger;

    private CsvController csvController;

    @FXML
    private BorderPane insertBorderPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    AnchorPane anchorPane;

    private Button submitBtn;

    private Button cancelBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        logger.info("Enter Initialise");

        // Create the right child node
        submitBtn = new Button("Submit");
        cancelBtn = new Button("Cancel");

        // Make the submit and cancel buttons the same size
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setMaxWidth(Double.MAX_VALUE);

        submitBtn.setOnAction(((ActionEvent e) -> {
            System.out.println("Submit");
            Stage insertForm = csvController.getInsertFormStage();
            insertForm.close();

        }));

        logger.info("Exit initialise");

    }

    public void createInsertForm(List<Label> labels, List<TextField> textFields) throws Exception {

        logger.info("Enter create Insert Form");

        try {
            theInsertForm = new InsertForm(labels, textFields);
            GridPane theGridPane = theInsertForm.getTheGridPane();

            insertBorderPane.setCenter(theGridPane);

            VBox left = new VBox(submitBtn, cancelBtn);
            insertBorderPane.setLeft(left);
            left.setStyle("-fx-padding: 10;");

            insertBorderPane.setStyle("-fx-background-color: lightgray;");

        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }

        logger.info("Exit create insert form");

    }

    public void setCSVController(CsvController theController) {
        csvController = theController;
    }
    
    public InsertForm getTheInsertForm() {
        return theInsertForm;
    }

}
