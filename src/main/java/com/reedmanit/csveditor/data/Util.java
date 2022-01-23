/*
 * Copyright 2022 preed.
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
package com.reedmanit.csveditor.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author preed
 */
public class Util {

    public Util() {

    }

    public int findPositionInCol(ObservableList<ObservableList> listOfLists, String target, int columnNumber) {

        System.out.println("Find position in col");

        Iterator<ObservableList> l = listOfLists.iterator();

        int index = 0;

        boolean found = false;

        List<String> row = new ArrayList<String>();

        while (l.hasNext()) {   // loop through the entire 2D table

            ObservableList o = l.next();
            index++;
            Iterator<String> r = o.iterator();

            row.clear();   // clear row
            while (r.hasNext()) {  // build row into an Arraylist
                String s = (String) r.next();
                row.add(s);

            }

            Iterator<String> rowIterator = row.iterator();
            int rowColIndex = 0;
            while (rowIterator.hasNext()) {   // search through the row
                String element = rowIterator.next();
                if ((element.contains(target) && (rowColIndex == columnNumber))) { // if the target is there and its in the requested column
                    found = true;
                    break;  // stop
                } else {
                    rowColIndex++;
                }
            }
            if (found) {
                break;  // we found the first occurance of the item - lets stop
            }
        }
        System.out.println("Index is " + index);
        
        if (!found) index = 0;

        return index;

    }

    public int findPositionInAllCols(ObservableList<ObservableList> listOfLists, String target) {

        System.out.println("Find position in all");

        Iterator<ObservableList> l = listOfLists.iterator();

        int index = 0;

        boolean found = false;

        while (l.hasNext()) {
            ObservableList o = l.next();
            index++;
            Iterator<String> r = o.iterator();

            while (r.hasNext()) {
                String s = (String) r.next();

                if (s.contains(target)) {
                    System.out.println("Found " + s);
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }

        }
        System.out.println("Index is " + index);
        
        if (!found) index = 0;
        
        return index;

    }

}
