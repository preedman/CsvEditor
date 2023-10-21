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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author preed
 */
public class DataTest {
    
    public DataTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testFileSizeOk() throws Exception {
        System.out.println("fileSize ok ");
        Data instance = new Data();
        instance.setSourceFile(new File("src/main/resources/my-toilet-file-2.csv"));
        boolean expResult = false;
        boolean result = instance.fileSizeTooBig();
        assertEquals(expResult, result);
       
    }
    
    @Test
    public void testFileSizeTooBig() throws Exception {
        System.out.println("fileSize Too Big");
        Data instance = new Data();
        instance.setSourceFile(new File("src/main/resources/toiletmapexport_230201_074429.csv"));
        boolean expResult = true;
        boolean result = instance.fileSizeTooBig();
        assertEquals(expResult, result);
       
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
