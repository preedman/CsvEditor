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

import java.io.File;
import java.util.Arrays;
import java.util.List;


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
public class DataTest {
    
    public DataTest() {
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
    public void testValidHeaders() throws Exception {
        System.out.println("test valid headers");
        Data instance = new Data();
        
        String[] headers = new String[] { "name", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(instance.invalidHeaders(), false);
    }
    
    @Test
    public void testInValidHeaders() throws Exception {
        System.out.println("test invalid headers");
        Data instance = new Data();
        
        String[] headers = new String[] { "1", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(instance.invalidHeaders(), true);
    }
    
}
