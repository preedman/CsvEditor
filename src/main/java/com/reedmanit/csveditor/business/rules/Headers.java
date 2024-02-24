/*
 * Copyright 2024 preed.
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

import java.io.File;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class Headers {

    protected static final org.apache.log4j.Logger headersLogger = LogManager.getLogger(Headers.class.getName());

    private static org.apache.log4j.Logger logger = headersLogger;

    private File sourceFile;

    private List<String> listOfHeaders;

    private Headers() {

    }

    private static class InstanceHolder {

        private static Headers instance = new Headers();
    }

    public static Headers getInstance() {
        return Headers.InstanceHolder.instance;
    }

    public boolean isValidHeader() {

        Iterator<String> i = listOfHeaders.iterator();

        boolean b = true;
        boolean validHeader = false;

        while (i.hasNext()) {
            String value = i.next();
            b = containsADateTime(value);

            if (!b) {   // so, the data does not contain a date
                b = containsSpace(value);
                if(!b) {  // true if empty or null
                    b = NumberUtils.isParsable(value);  
                    if (!b) { // string is not a number
                        b = isLettersAndDigitsAndUnderScoreOnly(value);
                      //  b = isLettersAndDigitsOnly(value);   // if true, then the heading has letter or digit
                        if (b) {
                           validHeader = true; 
                        } else {
                            logger.info("Invalid string " + value);
                            validHeader = false;
                            break;
                        }
                    } else {
                        logger.info("Invalid string " + value);
                        validHeader = false;  // string was a number
                        break;
                    }
                } else {
                    logger.info("Invalid string " + value);
                    validHeader = false;   // string contained a space
                    break;
                }
            } else {
                logger.info("Invalid string " + value);
                validHeader = false;
                break;
            }
            

        }
        return validHeader;
    }
    
    private Boolean isLettersAndDigitsAndUnderScoreOnly(String s) {
        
        String pattern = "\\w+";  // letters a-z, A-Z, digits 0 – 9, and underscore.
        
        Boolean b = s.matches(pattern);
        
        return b;
        
        
    }

    private Boolean isLettersAndDigitsOnly(String s) {
        boolean isLettersAndNumbersOnly = false;
        
        for (char c : s.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
              isLettersAndNumbersOnly = false;
              return isLettersAndNumbersOnly;
             // break; 
            }
        }
        isLettersAndNumbersOnly = true;
        return isLettersAndNumbersOnly;

    }
    
    private Boolean containsSpace(String s) {
        boolean containsSpace = false;
        
        if (!s.trim().isEmpty()) { // if the string is not ""
            for (char c : s.toCharArray()) {
                if (Character.isSpaceChar(c)) {
                    containsSpace = true;
                    break;
                }
            
            }
        } else {
            containsSpace = true;
        }
        
        
        return containsSpace;
    }

    public void setListOfHeaders(List<String> listOfHeaders) {
        this.listOfHeaders = listOfHeaders;
    }

    private Boolean containsOnlyChars(String s) {
        return StringUtils.isAlphanumericSpace(s);
        
    }

    private boolean containsADateTime(String data) {

        boolean b = true; // assume worst case at start

        String[] parsePatterns = new String[]{"dd/MM/YY", "dd/MM/yyyy", "dd-MM-yyyy", "dd-MM-yy", "MM/dd/YY", "MM/dd/yyyy",
            "MM-dd-yyyy", "MM-dd-yy", "dd-M-yyyy hh:mm:ss", "dd MMMM yyyy", "dd MMMM yyyy zzzz"};

        try {
            DateUtils.parseDate(data, parsePatterns);
        }
        catch (ParseException ex) {
            b = false;  // data does not a contain a date, based on the patterns above 
        }

        return b;

    }

}
