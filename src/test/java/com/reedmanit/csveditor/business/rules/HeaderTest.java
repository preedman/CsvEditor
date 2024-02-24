package com.reedmanit.csveditor.business.rules;

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

import com.reedmanit.csveditor.business.rules.Data;
import com.reedmanit.csveditor.business.rules.Headers;
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
public class HeaderTest {
    
    public HeaderTest() {
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
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "name", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(true, instance.isValidHeader());
    }
    
     @Test
    public void testInValidHeadersNumber() throws Exception {
        System.out.println("test invalid headers number");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "1", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    
    @Test
    public void testInValidHeadersDate() throws Exception {
        System.out.println("test invalid headers date");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "20/11/2025", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    
    @Test
    public void testInValidHeadersEmptyString() throws Exception {
        System.out.println("test invalid headers empty string");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    @Test
    public void testInValidHeadersSpace() throws Exception {
        System.out.println("test invalid headers space");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { " ", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    
    @Test
    public void testInValidHeadersDashChar() throws Exception {
        System.out.println("test headers dash char");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "pasta-go", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    
    @Test
    public void testInValidHeadersSpaceChar() throws Exception {
        System.out.println("test headers space char");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "pasta go", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(false, instance.isValidHeader());
    }
    @Test
    public void testInValidHeaderLetterNumber() throws Exception {
        System.out.println("test headers letter number");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "address1", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(true, instance.isValidHeader());
    }
    
    @Test
    public void testValidHeadersUnderScore() throws Exception {
        System.out.println("test headers valid underscore");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "pasta_go", "address" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(true, instance.isValidHeader());
    }
    
    @Test
    public void testValidSingleChar() throws Exception {
        System.out.println("test headers valid single char");
        Headers instance = Headers.getInstance();
        
        String[] headers = new String[] { "p", "a" };
        
        List<String> listOfHeaders = Arrays.asList(headers);
        
        instance.setListOfHeaders(listOfHeaders);
        
        assertEquals(true, instance.isValidHeader());
    }
}
