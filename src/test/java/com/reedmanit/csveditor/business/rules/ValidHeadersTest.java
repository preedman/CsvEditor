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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
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
public class ValidHeadersTest {
    
    public ValidHeadersTest() {
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

   

    /**
     * Test of evaluate method, of class ValidHeaders.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        Facts facts = new Facts();
        
        String[] headers = new String[] { "name", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
       // List<String> listOfHeaders = new ArrayList<String>();
        
        facts.put("Headers", listOfHeaders);
        
        
        
        ValidHeaders validHeaders = new ValidHeaders();
        boolean expResult = false;
        boolean result = validHeaders.evaluate(facts);
        assertEquals(expResult, result);
        
       
        
    }

    
    
}
