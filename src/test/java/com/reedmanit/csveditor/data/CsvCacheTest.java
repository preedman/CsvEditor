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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

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
public class CsvCacheTest {

    public CsvCacheTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            latch.countDown();
        });
        try {
            latch.await(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            System.out.println(ex.toString());
            Platform.exit();
            fail("System exception");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        Platform.exit();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTableView() throws Exception {
        
        System.out.println("Entering test table view");

        String resourceName = "cbd-bike-racks-2021-04-08.csv";

        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(resourceName).getFile());

        CsvCache cache = new CsvCache(testFile);

        cache.buildData();

        ObservableList<ObservableList> data = cache.getTableView().getItems();

        assertEquals(data.size(), 110);  // assert the size of the data set

        ObservableList<TableColumn<ObservableList, ?>> theCols = cache.getTableView().getColumns();

        assertEquals(theCols.size(), 7);  // assert the number of cols

        assertEquals("Suburb", theCols.get(0).getText());
        assertEquals("Longitude", theCols.get(6).getText());  // assert some of the cols
        
        ObservableList row = cache.getTableView().getItems().get(0);
        
        assertEquals("100 Adelaide Street", row.get(1)); // assert some data in the tableview
        assertEquals("153.024", row.get(6));
        
        
        

    }

    @Test
    public void testLargerFile() throws InterruptedException, IOException, CsvValidationException {
        
        System.out.println("Entering test larger file");

        String resourceName = "my-toilet-file-2.csv";

        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(resourceName).getFile());

        CsvCache cache = new CsvCache(testFile);

        cache.buildData();

        ObservableList<ObservableList> data = cache.getTableView().getItems();

        System.out.println("Size of table items " + data.size());
        
        ObservableList row = cache.getTableView().getItems().get(52);
        
        System.out.println(row.size());
        
        
        
        //assertEquals(data.size(), 110);

    }

}
