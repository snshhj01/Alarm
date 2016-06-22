package kr.co.miracom.alarm.util;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.miracom.alarm.R;

/**
 * @author kws
 * @date 2016-06-03
 * @since 0.1
 */
public class CommonUtils {

    /**
     * create Gson instance
     * @return
     */
    public static Gson getGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
    public static int getAlarmId() {
        int id = (int) (System.currentTimeMillis()/100);
        return id;
    }

    public static String getKorAmPm(int hour){
        return hour<12 ? "오전": "오후";
    }

    public static String getHourAmPm(int hour){
        int rtnHour = hour;
        String rtnStr = "";
        String pre = "";

         if(hour < 13){
            rtnHour = hour;
        }else{  // 13 above
            rtnHour = hour - 12;
        }

        if(rtnHour < 10){
            pre = "0";
        }
        return pre + rtnHour;
    }

    public static void setColorDOW(View convertView,  ArrayList<Integer> arrInt){

        String strColor = "#17C300";

        int[] arrObj= {R.id.sun, R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat};

        for(int i=0; i<arrInt.size(); i++){
            TextView tv = (TextView) convertView.findViewById( arrObj[ arrInt.get(i)-1 ] );
            tv.setTextColor(Color.parseColor(strColor));
        }
    }
    public static long setTriggerTime(Integer hour, Integer minute) {
        // current Time
        long currentTime = System.currentTimeMillis();
        // timepicker
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, hour);
        curTime.set(Calendar.MINUTE, minute);
        curTime.set(Calendar.SECOND, 0);
        curTime.set(Calendar.MILLISECOND, 0);
        long settingTime = curTime.getTimeInMillis();
        long triggerTime = settingTime;
        if (currentTime > settingTime)
            triggerTime += 1000 * 60 * 60 * 24;
        return triggerTime;
    }
}
