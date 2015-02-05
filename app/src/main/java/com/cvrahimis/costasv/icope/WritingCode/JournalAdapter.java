package com.cvrahimis.costasv.icope.WritingCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvrahimis.costasv.icope.R;

import java.util.ArrayList;

/**
 * Created by Costasv on 2/4/15.
 */
public class JournalAdapter extends BaseAdapter {

    //song list and layout
    private ArrayList<Journal> journals;
    private LayoutInflater journalInf;

    //constructor
    public JournalAdapter(Context c, ArrayList<Journal> allJournals){
        journals=allJournals;
        journalInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return journals.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout journalLay = (LinearLayout)journalInf.inflate(R.layout.journal, parent, false);
        //get title and artist views
        TextView titleView = (TextView)journalLay.findViewById(R.id.journal_title);
        TextView entryView = (TextView)journalLay.findViewById(R.id.journal_entry);
        //get song using position
        Journal currJournal = journals.get(position);
        //get title and artist strings
        titleView.setText(currJournal.getTitle());
        entryView.setText(currJournal.getEntry());
        //set position as tag
        journalLay.setTag(position);
        return journalLay;
    }

}

