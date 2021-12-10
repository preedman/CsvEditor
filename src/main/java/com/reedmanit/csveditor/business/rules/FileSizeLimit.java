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
package com.reedmanit.csveditor.business.rules;

import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 *
 * @author preed
 */
public class FileSizeLimit implements Rule {
    
    private boolean showAlert;
    
    private static final Integer limit = 10280; 
    
    
   
    
    @Override
    public String getName() {
        return "File Size Lmit";
    }

    @Override
    public String getDescription() {
        return "CSV File Size can not exeed 10 MB";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean evaluate(Facts facts) {
        
        boolean b = false;
        
        Fact fileSize = facts.getFact("FileSize");
        
        Integer i = (Integer) fileSize.getValue();
        
        if (i > limit) {
            b = true;
        } else {
            b = false;
        }
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
