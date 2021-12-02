/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reedmanit.csveditor.data;

import com.reedmanit.csveditor.data.CsvCache;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    /**
     * Test of loadDataFile method, of class CsvCache.
     *
     * /
     *
     *
     *
     * @Test public void testLoadDataFile() throws Exception {
     * System.out.println("loadDataFile"); CsvCache cache =
     * CsvCache.getInstance();
     *
     *
     * // InputStreamReader s = new
     * InputStreamReader(this.getClass().getResourceAsStream("/com/reedmanit/cvseditor/data/cbd-bike-racks-2021-04-08.csv"));
     *
     * BufferedReader r = new BufferedReader(new
     * FileReader("C:\\SoftwareDevelopment\\Projects\\CVSReaderTest\\src\\main\\java\\com\\reedmanit\\cvsreadertest\\cbd-bike-racks-2021-04-08.csv"));
     *
     * cache.setDataFile(r);
     *
     * cache.loadDataFile(); // TODO review the generated test code and remove
     * the default call to fail. //fail("The test case is a prototype.");
     *
     * ObservableList<ObservableList<StringProperty>> data = cache.getdata();
     *
     * System.out.println(data.size());
     *
     * for (int i = 0; i < data.size(); i++) {
     *
     * // Item m = data.get(i);
     *
     * ObservableList<StringProperty> item = data.get(i);
     *
     * System.out.println(item.size());
     *
     * for (int a = 0; a < item.size(); a++) { SimpleStringProperty p =
     * (SimpleStringProperty) item.get(a); System.out.println(p); }
     *
     * }
     *
     *
     * }
     */
    @Test
    public void testTableView() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        
        

        CsvCache cache = new CsvCache(new File("C:\\SoftwareDevelopment\\Projects\\CVSReaderTest\\src\\main\\java\\com\\reedmanit\\cvsreadertest\\cbd-bike-racks-2021-04-08.csv"));

        //   InputStreamReader s = new InputStreamReader(this.getClass().getResourceAsStream("/com/reedmanit/cvseditor/data/cbd-bike-racks-2021-04-08.csv"));
   //     BufferedReader r = new BufferedReader(new FileReader("C:\\SoftwareDevelopment\\Projects\\CVSReaderTest\\src\\main\\java\\com\\reedmanit\\cvsreadertest\\cbd-bike-racks-2021-04-08.csv"));

     //   cache.setDataFile(r);

        cache.buildData();

        ObservableList<ObservableList> data = cache.getTableView().getItems();

        System.out.println(data.size());
        
        assertEquals(data.size(), 110);
        
        Platform.exit();

    }

}
