package com.android.todoappshrishail.model;

import java.util.ArrayList;
import java.util.Collections;

public class ExampleTest {

    public void main(String a[]) {

    }

    interface MyList {
        void convert(String[] a);

        void replace(int idx);

        ArrayList<String> compact();
    }

    class ArrayToList implements MyList {

        public ArrayList<String> arrayToList;

        public ArrayToList() {
            arrayToList = new ArrayList<String>();
        }

        @Override
        public void convert(String[] a) {
            for (String str : a) {
                arrayToList.add(str);
                System.out.println("I have added the string " + str + " at the index " + arrayToList.indexOf(str));
            }

        }

        @Override
        public void replace(int idx) {
            arrayToList.set(idx, "");
            System.out.println("I have added the string " + arrayToList.get(idx) + " with a null string");
        }

        @Override
        public ArrayList<String> compact() {
            arrayToList.removeAll(Collections.singleton(""));
            return arrayToList;
        }
    }
}
