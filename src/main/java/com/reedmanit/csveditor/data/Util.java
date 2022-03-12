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
import java.util.ListIterator;
import java.util.Stack;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author preed
 */
public class Util {

    public Util() {

    }
    
    public int findIndexOfSearchItem(String theSearchItem, Stack<SearchItem> s) {
        
        int index = 0;
        
        if (s.empty()) {   // stack is empty so return 0
            index = 0;
        } else {
            SearchItem value = s.pop();   // get the most recent value of the stack
            
            if(value.getItem().equalsIgnoreCase(theSearchItem)) {  // if the most recent is the same as the one we are interested in - then return its index
                index = value.getIndex();
            }
        }
        
        return index;
        
    }

    public int findPositionInCol(ObservableList<ObservableList> listOfLists, String target, int columnNumber, int startPosition) {

        ListIterator<ObservableList> it = listOfLists.listIterator(startPosition);  // set up the iterator from a start position

        int index = startPosition;

        boolean found = false;

        List<String> row = new ArrayList<String>();

        while (it.hasNext()) {   // loop thru table from start position

            ObservableList o = it.next();
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
                if ((StringUtils.containsIgnoreCase(element, target) && (rowColIndex == columnNumber))) {
                    found = true;
                    break;  // stop
                } else {
                    rowColIndex++;
                }
            }

            if (found) {
                break;  // we found occurance of the item - lets stop
            }

        }
        return index;
    }
    
    public int findPositionInAllCols(ObservableList<ObservableList> listOfLists, String target, int startPosition) {
        
        System.out.println("Find position in all");

        ListIterator<ObservableList> it = listOfLists.listIterator(startPosition);  // set up the iterator from a start position

        int index = startPosition;

        boolean found = false;

        while (it.hasNext()) {
            ObservableList o = it.next();
            index++;
            Iterator<String> r = o.iterator();

            while (r.hasNext()) {
                String s = (String) r.next();

                if (StringUtils.containsIgnoreCase(s, target)) {
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

        if (!found) {
            index = 0;
        }

        return index;
        
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
                if ((StringUtils.containsIgnoreCase(element, target) && (rowColIndex == columnNumber))) {
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

        if (!found) {
            index = 0;
        }

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

                if (StringUtils.containsIgnoreCase(s, target)) {
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

        if (!found) {
            index = 0;
        }

        return index;

    }

    public int[] findAllPositionInAllCols(ObservableList<ObservableList> listOfLists, String target) {

        Iterator<ObservableList> l = listOfLists.iterator();

        int[] indexes = new int[listOfLists.size()]; // rows that contain the target

        int index = 0;  // ptr to the row

        int i = 0;  // ptr to the array that contains the indexes

        boolean found = false;

        while (l.hasNext()) {  // loop thru the rows
            ObservableList o = l.next();
            index++;
            Iterator<String> r = o.iterator();

            while (r.hasNext()) {   // loop through the columns
                String s = (String) r.next();

                if (StringUtils.containsIgnoreCase(s, target)) {
                    System.out.println("Found " + s);
                    found = true;
                    break;   // found the item in the row
                }
            }   // loop thru the row

            if (found) {   // found it
                indexes[i] = index;
                i++;
                found = false;   // reset found so we find in next row
            }

        }

        return indexes;

    }

}
