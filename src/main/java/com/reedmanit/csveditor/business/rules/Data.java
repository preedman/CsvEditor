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
package com.reedmanit.csveditor.business.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed Two business rules The file size is too large, Set via
 * properties file and also hard coded Test for data in header The file first
 * row must be column names. And hence can not contain data.
 *
 * The test for this is rudimentary, currently.
 *
 * Look for numbers and dates in the first row data
 */
public class Data {

    protected static final org.apache.log4j.Logger dataLogger = LogManager.getLogger(Data.class.getName());

    private static org.apache.log4j.Logger logger = dataLogger;

    private File sourceFile;

    private Integer limit = 10280;

    private List<String> listOfHeaders;

    public Data() {

        listOfHeaders = new ArrayList<String>();

    }

    public boolean fileSizeTooBig() throws IOException {

        logger.info("Entering file size too big");

        Path path = Paths.get(sourceFile.getAbsolutePath());

        long bytes = Files.size(path);

        logger.info("File size is " + bytes / 1024 + "kb");

        Long kb = bytes / 1024;

        Integer i = Integer.valueOf(kb.intValue());

        logger.info("Exit file size too big");

        if (i > getLimit()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean invalidHeaders() {

        Iterator<String> i = listOfHeaders.iterator();

        boolean b = false;

        while (i.hasNext()) {
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

    public Boolean checkThatFileIsCommaDelimited() throws IOException {

        logger.info("Checking first line contains commas");

        Boolean commas = false;

        BufferedReader br = new BufferedReader(new FileReader(sourceFile.getAbsolutePath()));

        String line;

        line = br.readLine();

        String[] values = line.split(",");

        if (values.length != 1 && values != null) {
            commas = true;
        } else {
            commas = false;

        }
        br.close();

        return commas;

    }

    private boolean containsADateTime(String data) {

        boolean b = true; // assume worst case at start

        String[] parsePatterns = new String[]{"dd/MM/YY", "dd/MM/yyyy", "dd-MM-yyyy", "dd-MM-yy", "MM/dd/YY", "MM/dd/yyyy",
            "MM-dd-yyyy", "MM-dd-yy", "dd-M-yyyy hh:mm:ss", "dd MMMM yyyy", "dd MMMM yyyy zzzz"};

        try {
            DateUtils.parseDate(data, parsePatterns);
        } catch (ParseException ex) {
            b = false;  // data does not a contain a date, based on the patterns above 
        }

        return b;

    }

    /**
     * @param sourceFile the sourceFile to set
     */
    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @param listOfHeaders the listOfHeaders to set
     */
    public void setListOfHeaders(List<String> listOfHeaders) {
        this.listOfHeaders = listOfHeaders;
    }

}
