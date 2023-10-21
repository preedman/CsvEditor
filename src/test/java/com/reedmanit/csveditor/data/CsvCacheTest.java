/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.data;

import com.opencsv.exceptions.CsvValidationException;
import com.reedmanit.csveditor.data.CsvCache;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
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
public class CsvCacheTest {

    public CsvCacheTest() {
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
    public void testTableView() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        
        
        
        String resourceName = "cbd-bike-racks-2021-04-08.csv";

        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(resourceName).getFile());
        
        CsvCache cache = new CsvCache(testFile);
        

      

        cache.buildData();

        ObservableList<ObservableList> data = cache.getTableView().getItems();

        System.out.println(data.size());
        
        assertEquals(data.size(), 110);
        
        Platform.exit();

    }

  /**  
    @Test
    public void testInvalidSeparator() throws InterruptedException, IOException, CsvValidationException {
        
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        
         String resourceName = "my-toilet-file-invalid-sep.csv";

        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(resourceName).getFile());
        
        CsvCache cache = new CsvCache(testFile);
        

      

        cache.buildData();

        ObservableList<ObservableList> data = cache.getTableView().getItems();

        System.out.println(data.size());
        
        assertEquals(data.size(), 110);
        
        Platform.exit();
        
        
        
    }
**/

}
