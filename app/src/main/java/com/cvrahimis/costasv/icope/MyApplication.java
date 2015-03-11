package com.cvrahimis.costasv.icope;

import android.app.Application;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Costasv on 3/9/15.
 */
public class MyApplication extends Application {

    private Queue<ICopeActivity> activityQueue=new LinkedList<ICopeActivity>();

    public ICopeActivity pop(){
        if(activityQueue.size() > 0)
            return activityQueue.poll();
        return null;
    }
    public void push(ICopeActivity activity){
        activityQueue.add(activity);
    }
}
