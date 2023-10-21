/*
 * Copyright 2023 preed.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class ExitWithOutSaveRule {
    
    protected static final org.apache.log4j.Logger exitWithOutSaveLogger = LogManager.getLogger(ExitWithOutSave.class.getName());

    private static org.apache.log4j.Logger logger = exitWithOutSaveLogger;
    
    private final List<UIEvent> listOfUIEvents;
    
    private Boolean ruleState;
    
    private int openEvents;
    
    private int editEvents;
    
    public ExitWithOutSaveRule() {
        
        exitWithOutSaveLogger.info("Constructing rule");
        
        listOfUIEvents = new ArrayList<>();
        ruleState = false;
        
        openEvents = 0;
        
        editEvents = 0;
    }
    
    public void evaluateRule() {
        
        exitWithOutSaveLogger.info("Evaluting Rule Enter");
        
        if (openEvents ==1) {
            if (editEvents > 0) {
                this.setRuleState(true);
            }
        }
        
        exitWithOutSaveLogger.info("Open Events " + openEvents + " edit events " + editEvents);
        
        exitWithOutSaveLogger.info("Evaluting Rule Exit");
        
    }

    /**
     * @return the ruleState
     */
    public Boolean getRuleState() {
        return ruleState;
    }

    /**
     * @param ruleState the ruleState to set
     */
    public void setRuleState(Boolean ruleState) {
        this.ruleState = ruleState;
    }
    
    public void addRuleEvent(UIEvent aUIEvent) {
        listOfUIEvents.add(aUIEvent);
        if (aUIEvent.getEventName().equals("FileOpen")) openEvents++;
        if (aUIEvent.getEventName().equals("Edit")) editEvents++;
    }
    
    public void resetRuleEvents() {
        listOfUIEvents.clear();
        this.ruleState = false;
        openEvents = 0;
        editEvents = 0;
    }
    
    
    
    
}
