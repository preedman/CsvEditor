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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author preed
 */
public class FileSizeLimitTest {
    
    public FileSizeLimitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    

    @Test
    public void testIsFileSizeGreaterThanLimit() throws Exception {
        System.out.println("isFileSizeGreaterThanLimit");
        File theSourceFile = (new File("src/main/resources/my-toilet-file-2.csv"));
        FileSizeLimit instance = FileSizeLimit.getInstance();
        Boolean expResult = false;
        Boolean result = instance.isFileSizeGreaterThanLimit(theSourceFile);
        assertEquals(expResult, result);
       
    }
    
    @Test
    public void testIsFileSizeExceedsLimit() throws Exception {
        System.out.println("isFileSizeExceedsLimit");
        File theSourceFile = (new File("src/main/resources/toiletmapexport_230201_074429.csv"));
        FileSizeLimit instance = FileSizeLimit.getInstance();
        Boolean expResult = true;
        Boolean result = instance.isFileSizeGreaterThanLimit(theSourceFile);
        assertEquals(expResult, result);
       
    }

    
    
}
