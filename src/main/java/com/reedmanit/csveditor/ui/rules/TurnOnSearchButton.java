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

import javafx.scene.control.Button;
import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 *
 * @author preed
 */
public class TurnOnSearchButton implements Rule {
    
    private Button searchButton;
    private boolean turnOnSearchButton = false;

    @Override
    public String getName() {
        return "Turn On Search Button";
    }

    @Override
    public String getDescription() {
        return "Turn on the Search Button when a file has been loaded";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean evaluate(Facts facts) {
       
        System.out.println("Turn on Search Button");
        
        boolean b = false;
        
        Fact fileOpen = facts.getFact("OpenNewFile");
        
        if (fileOpen.getValue().equals("TRUE")) {
            b = true;
        } else {
            b = false;
        }
        return b;
        
       
    }

    @Override
    public void execute(Facts facts) throws Exception {
        setTurnOnSearchButton(true);
    }

    @Override
    public int compareTo(Rule o) {
        return 1;
    }

    /**
     * @return the searchButton
     */
    public Button getSearchButton() {
        return searchButton;
    }

    /**
     * @param searchButton the searchButton to set
     */
    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    /**
     * @return the turnOnSearchButton
     */
    public boolean isTurnOnSearchButton() {
        return turnOnSearchButton;
    }

    /**
     * @param turnOnSearchButton the turnOnSearchButton to set
     */
    public void setTurnOnSearchButton(boolean turnOnSearchButton) {
        this.turnOnSearchButton = turnOnSearchButton;
    }
    
    
    
}
