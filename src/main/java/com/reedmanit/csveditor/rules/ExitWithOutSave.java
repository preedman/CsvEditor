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
package com.reedmanit.csveditor.rules;

import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 *
 * @author preed
 */
public class ExitWithOutSave implements Rule {
    
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
        
        System.out.println("Exit with out saving");
        
        boolean b = false;
        
        Fact fileOpen = facts.getFact("OpenNewFile");
        Fact editOccured = facts.getFact("EditOccured");
        
    
        
        System.out.println("edit occurred " + editOccured.toString());
        System.out.println("file open " + fileOpen.toString());
        
        b = editOccured.getValue().equals("TRUE") && (fileOpen.getValue().equals("TRUE"));
        
    
        
        return b;
    }

    @Override
    public void execute(Facts facts) throws Exception {
        setShowAlert(true);
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
