package com.cvrahimis.costasv.icope.ReadingCode;

/**
 * Created by Costasv on 2/5/15.
 */
public class Quote {

    private long id;
    private String quote;
    private String author;

    public Quote(long qID, String q, String a){
        id = qID;
        quote = q;
        author = a;
    }

    public long getID(){return id;}
    public String getQuote(){return quote;}
    public String getAuthor(){return author;}

    public void setID(long value){ id = value; }
    public void setQuote(String value){ quote = value; }
    public void setAuthor(String value){ author = value; }
}
