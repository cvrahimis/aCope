package com.cvrahimis.costasv.icope.WritingCode;

/**
 * Created by Costasv on 2/4/15.
 */
public class Journal {

    private long id;
    private String title;
    private String entry;

    public Journal(long jID, String t, String e){
        id = jID;
        title = t;
        entry = e;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getEntry(){return entry;}

    public void setID(long value){ id = value; }
    public void setTitle(String value){ title = value; }
    public void setEntry(String value){ entry = value; }

}
