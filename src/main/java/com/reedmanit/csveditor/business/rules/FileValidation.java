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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class FileValidation {
    
    protected static final org.apache.log4j.Logger fileValidationLogger = LogManager.getLogger(FileValidation.class.getName());

    private static org.apache.log4j.Logger logger = fileValidationLogger;
    
    private File sourceFile;
    
    private FileValidation() {
        
    }
    
    private static class InstanceHolder {

        private static FileValidation instance = new FileValidation();
    }
    
    public static FileValidation getInstance() {
        return FileValidation.InstanceHolder.instance;
    }
    
    public Boolean isRowDelimited(File theSourceFile) throws FileNotFoundException, IOException {
        
        logger.info("Enter is row delimted");
        
        sourceFile = theSourceFile;
        
        Boolean commas = false;

        BufferedReader br = new BufferedReader(new FileReader(sourceFile.getAbsolutePath()));

        String line;

        line = br.readLine();  // read first row - assumption about structure - just check first row

        String[] values = line.split(",");   // try to split it on a comma

        if (values.length != 1 && values != null) {  
            commas = true;
        } else {
            commas = false;  // split did not work

        }
        br.close();
        
        logger.info("Exit is comma delimited");

        return commas;
        
        
        
    }
    
    
}
