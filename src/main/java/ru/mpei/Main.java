package ru.mpei;
import java.util.ArrayList;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {

        ArrayList<String> str = new ArrayList<String>();
        str.add("one");
        str.add("two");
        str.add("six");
        str.add("four");
        str.add("five");
        str.add("six");
        str.add("seven");
        str.add("eight");
        str.add("nine");
        str.add("ten");
        str.add("eleven");
        str.add("twelve");
        TripletDeque<String> myDeque = new TripletDeque<String>(3, 4);
        myDeque.addAll(str);
        System.out.println(myDeque.removeLastOccurrence("six"));
    }
}
