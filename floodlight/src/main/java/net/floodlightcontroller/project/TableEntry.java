package net.floodlightcontroller.project;

import java.util.ArrayList;
import java.util.List;

public class TableEntry{
    public static int idxCounter = 0;
    public int index;
    public int linkCount;
    public List<Integer> bytesSentList; // num of bytes transmitted thru link l in this interval
    public List<Integer> queuedBytesList;
    public List<Integer> minRateList; // min data rate allocated to interactive queue
    public List<Integer> delayList; // physical delay at link i

    public TableEntry(){
        index = ++idxCounter; // when idx 0 queried, we must have higher than that num.
        linkCount = 6;
        bytesSentList = new ArrayList<>();
        queuedBytesList = new ArrayList<>();
        minRateList = new ArrayList<>();
        delayList = new ArrayList<>();
    }
}
