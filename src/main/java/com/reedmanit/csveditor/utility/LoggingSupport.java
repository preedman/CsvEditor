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
package com.reedmanit.csveditor.utility;

import com.reedmanit.csveditor.business.rules.FileSizeLimit;
import java.util.List;
import org.apache.log4j.LogManager;

/**
 *
 * @author preed
 */
public class LoggingSupport {
    
    protected static final org.apache.log4j.Logger logSupportLogger = LogManager.getLogger(LoggingSupport.class.getName());

    private static org.apache.log4j.Logger logger = logSupportLogger;
    
    private LoggingSupport() {
        
    }
    
    private static class InstanceHolder {

        private static LoggingSupport instance = new LoggingSupport();
    }

    public static LoggingSupport getInstance() {
        return InstanceHolder.instance;
    }
    
    public void printOutDataMapping(List<String> cols, List<String> row) {
        
        
        
        for (int i=0; i<cols.size(); i++) {
            logger.info(cols.get(i) + " " + row.get(i));
        }
        
    }
    
}
