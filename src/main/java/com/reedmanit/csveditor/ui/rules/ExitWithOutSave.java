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

import com.reedmanit.csveditor.controller.CsvController;
import org.apache.log4j.LogManager;
import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 *
 * @author preed
 */
public class ExitWithOutSave implements Rule {
    
    protected static final org.apache.log4j.Logger exitWithOutSaveLogger = LogManager.getLogger(ExitWithOutSave.class.getName());

    private static org.apache.log4j.Logger logger = exitWithOutSaveLogger;
    
    private boolean showAlert = false;

    @Override
    public String getName() {
        return "Exit without Save";
    }

    @Override
    public String getDescription() {
        return "Data has been edited but not saved";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean evaluate(Facts facts) {
        
        exitWithOutSaveLogger.info("Entering evaluate");
        
       
        
        boolean b = false;
        
        Fact fileOpen = facts.getFact("OpenNewFile");
        Fact editOccured = facts.getFact("EditOccured");
        
    
        exitWithOutSaveLogger.info("edit occured " + editOccured.toString());
        exitWithOutSaveLogger.info("file open " + fileOpen.toString());
        
        
        
        b = editOccured.getValue().equals("TRUE") && (fileOpen.getValue().equals("TRUE"));
        
        exitWithOutSaveLogger.info("Exit evaluate");
    
        
        return b;
    }

    @Override
    public void execute(Facts facts) throws Exception {
        exitWithOutSaveLogger.info("Entering execute");
        setShowAlert(true);
        exitWithOutSaveLogger.info("Exit execute");
    }

    @Override
    public int compareTo(Rule o) {
        return 1;
    }

    /**
     * @return the showAlert
     */
    public boolean isShowAlert() {
        return showAlert;
    }

    /**
     * @param showAlert the showAlert to set
     */
    public void setShowAlert(boolean showAlert) {
        this.showAlert = showAlert;
    }
    
}
