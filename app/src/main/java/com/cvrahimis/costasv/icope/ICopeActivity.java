package com.cvrahimis.costasv.icope;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Costasv on 3/9/15.
 */
public class ICopeActivity {

        private String activityName;
        private String activityDuration;
        private String activityTime;

        public ICopeActivity(){
            activityName = "";
            activityDuration = "";
            activityTime = "";
        }

        /*public ICopeActivity(String name, int duration, int time)
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
        }*/

        public ICopeActivity(String name, String time)
        {
            activityName = name;
            activityDuration = "";
            activityTime = time;
        }

        //setters
        public void setActivityName(String value) {activityName = value;}
        public void setActivityDuration(String value) {activityDuration = value;}
        public void setActivityTime(String value) {activityTime = value;}

        //getters
        public String getActivityName() {return activityName;}
        public String getActivityDuration() {return activityDuration;}
        public String getActivityTime() {return activityTime;}



}
