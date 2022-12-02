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

import static java.lang.String.format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 *
 * @author preed
 * Simple valid tests - look for dates and numbers in headers
 * Suggests that this is data and not column names.
 */
public class ValidHeaders implements Rule {

    private boolean inValidData = false;

    @Override
    public String getName() {
        return "Valid Headers";
    }

    @Override
    public String getDescription() {
        return "CSV file must contain headers not data";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean evaluate(Facts facts) {

        boolean b = false;
        
       List<String> listOfHeaders = facts.get("Headers");

       Iterator<String> i = listOfHeaders.iterator();
       
       
       
       
        
                
        while(i.hasNext()) {
            String value = i.next();
            b = containsADateTime(value);
            if (!b) {   // so, the data does not contain a date
                b = NumberUtils.isParsable(value);   // if true, then the data contains a number
            }
            if (b) {  // so lets stop
                break;
            }
            
        }
        return b;
    }

    @Override
    public void execute(Facts facts) throws Exception {
        setInValidData(true);
    }

    @Override
    public int compareTo(Rule o) {
        return 1;
    }

    private boolean containsADateTime(String data) {

        boolean b = true;
        
        String[] parsePatterns = new String[] { "dd/MM/YY" , "dd/MM/yyyy" , "dd-MM-yyyy" , "dd-MM-yy", "MM/dd/YY", "MM/dd/yyyy",
                                              "MM-dd-yyyy", "MM-dd-yy", "dd-M-yyyy hh:mm:ss" , "dd MMMM yyyy" , "dd MMMM yyyy zzzz"  };
        
        try {
            DateUtils.parseDate(data, parsePatterns);
        } catch (ParseException ex) {
            b = false;  // data does not a contain a date 
        }
        
             
        return b;

    }
    
    

    /**
     * @return the inValidData
     */
    public boolean isInValidData() {
        return inValidData;
    }

    /**
     * @param inValidData the inValidData to set
     */
    public void setInValidData(boolean inValidData) {
        this.inValidData = inValidData;
    }

}
