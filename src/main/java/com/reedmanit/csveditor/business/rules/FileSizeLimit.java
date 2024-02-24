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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class FileSizeLimit {

    protected static final org.apache.log4j.Logger fileSizeLimitLogger = LogManager.getLogger(FileSizeLimit.class.getName());

    private static org.apache.log4j.Logger logger = fileSizeLimitLogger;

    private File sourceFile;

    private Integer limit = 10280;   // default is 10MB. But can be overwritten by the client of the API.
    
   

    private FileSizeLimit() {

    }

    private static class InstanceHolder {

        private static FileSizeLimit instance = new FileSizeLimit();
    }

    public static FileSizeLimit getInstance() {
        return InstanceHolder.instance;
    }

    public Boolean isFileSizeGreaterThanLimit(File theSourceFile) throws IOException {

        logger.info("Entering is file size greater than limit");

        sourceFile = theSourceFile;

        Path path = Paths.get(sourceFile.getAbsolutePath());

        long bytes = Files.size(path);

        logger.info("File size is " + bytes / 1024 + "kb");

        Long kb = bytes / 1024;

        Integer i = Integer.valueOf(kb.intValue());

        

        if (i > this.limit) {
            logger.info("File size exceeds limit " + this.limit);
            logger.info("Exit file size too big");
            return true;
        } else {
            logger.info("File size within limit " + this.limit);
            logger.info("Exit file size too big");
            return false;
        }

    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
