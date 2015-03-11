package com.cvrahimis.costasv.icope;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Costasv on 3/9/15.
 */
public class ICopeActivity {

        private String activityName;
        private int activityDuration;
        private int activityTime;

        public ICopeActivity(){
            activityName = "";
            activityDuration = 0;
            activityTime = 0;
        }

        public ICopeActivity(String name, int duration, int time)
        {
            activityName = name;
            activityDuration = duration;
            activityTime = time;
        }

        public ICopeActivity(String name)
        {
            activityName = name;
            activityDuration = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dMMyyyyHm");
            String str = sdf.format(new Date());
            activityTime = Integer.parseInt(str);
        }

        public ICopeActivity(String name, int time)
        {
            activityName = name;
            activityDuration = 0;
            activityTime = time;
        }

        //setters
        public void setActivityName(String value) {activityName = value;}
        public void setActivityDuration(int value) {activityDuration = value;}
        public void setActivityTime(int value) {activityTime = value;}

        //getters
        public String getActivityName() {return activityName;}
        public int getActivityDuration() {return activityDuration;}
        public int getActivityTime() {return activityTime;}



}
