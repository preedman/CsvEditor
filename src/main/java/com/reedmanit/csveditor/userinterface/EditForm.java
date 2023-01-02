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
package com.reedmanit.csveditor.userinterface;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author preed
 */
public class EditForm {

    private GridPane theGridPane;

    private HBox buttonPanel;

    private List<Label> formLabels;

    private List<TextField> formTextFields;

    public EditForm(List<Label> labels, List<TextField> textFields) throws Exception {
        
        formLabels = labels;
        formTextFields = textFields;
        
        if (labels.size() != textFields.size()) {
            throw new Exception("Must be equal number of labels and textfields");
        }
        
        buildGridPane();

    }

    private void buildGridPane() {

        theGridPane = new GridPane();

        theGridPane.setGridLinesVisible(true);

        for (int i = 0; i < formLabels.size(); i++) {

            theGridPane.add(formLabels.get(i), 0, i);
            theGridPane.add(formTextFields.get(i), 1, i);

        }

    }
    
    public GridPane getGridPane() {
        return theGridPane;
    }
    
    public List<TextField> getFormFields() {
        return formTextFields;
    }

}
