/*
 * Copyright 2021 preed.
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
package com.reedmanit.csveditor.ui.rules;

import java.lang.annotation.Annotation;
import javafx.scene.control.Button;
import org.jeasy.rules.annotation.Action;
//import org.jeasy.rules.annotation.Fact;
//import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Fact;


/**
 *
 * @author preed
 */
public class TurnOnSaveButton implements Rule  {
    
    private Button saveButton;
    
   

    @Override
    public String getName() {
        return "Turn On Save Button";
    }

    @Override
    public String getDescription() {
        return "Save button is on when an edit has occured";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean evaluate(Facts facts) {
        
        System.out.println("evaluate");
        
        boolean b = false;
        
        Fact stateOfOpenButton = (Fact) facts.getFact("OpenButtonState");
        
        if (stateOfOpenButton.getValue().equals("ENABLE") && (saveButton.isDisabled())) {
           
            b = true;
         
        } 
        
        if (stateOfOpenButton.getValue().equals("DISABLE") && (!saveButton.isDisabled())) {
           b = true;
         
        } 
        
        
        
        return b;
        
        
        
    }

    @Override
    public void execute(Facts facts) throws Exception {
        
        System.out.println("execute");
        
        Fact stateOfOpenButton = (Fact) facts.getFact("OpenButtonState");
        
        if (stateOfOpenButton.getValue().equals("ENABLE")) {
            saveButton.setDisable(false);
        } 
        
        if (stateOfOpenButton.getValue().equals("DISABLE")) {
            saveButton.setDisable(true);
        } 
        
        
        
        
        
    }

    @Override
    public int compareTo(Rule o) {
        return 1;
    }

    public void setSaveButton(Button theSaveButton) {
        saveButton = theSaveButton;
    }
    
    

    
    
    
    

    
}
