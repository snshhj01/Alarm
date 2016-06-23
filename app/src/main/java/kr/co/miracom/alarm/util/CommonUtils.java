package kr.co.miracom.alarm.util;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

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

        String enableColor = "#17C300";
        String disableColor="#9C9D9D";

        int[] arrObj= {R.id.sun, R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat};

        if(arrInt == null){ // disable
            for(int i=0; i<7; i++){
                TextView tv = (TextView) convertView.findViewById( arrObj[ i ] ); // All Disabled
                tv.setTextColor(Color.parseColor(disableColor));
            }
        }else { // enable
            for (int i = 0; i < arrInt.size(); i++) {
                TextView tv = (TextView) convertView.findViewById(arrObj[arrInt.get(i) - 1]);
                tv.setTextColor(Color.parseColor(enableColor));
            }
        }
    }

    public static void disabledViewColor(View convertView){
        ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        iv.setSelected(false);

        TextView tAmPm = (TextView) convertView.findViewById(R.id.amPm);
        tAmPm.setTextColor(Color.parseColor("#9C9D9D"));
        TextView tTimeFromTo = (TextView) convertView.findViewById(R.id.timeFromTo);
        tTimeFromTo.setTextColor(Color.parseColor("#9C9D9D"));

        setColorDOW(convertView, null);  // All disable
    }

    public static void enabledViewColor(View convertView, AlarmInfo alnfo){
        ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
        iv.setSelected(true);

        TextView tAmPm = (TextView) convertView.findViewById(R.id.amPm);
        tAmPm.setTextColor(Color.BLACK);
        TextView tTimeFromTo = (TextView) convertView.findViewById(R.id.timeFromTo);
        tTimeFromTo.setTextColor(Color.BLACK);

        setColorDOW(convertView, alnfo.getDays());  // All disable
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

    public static int addHour(int curHour, int addHour){
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, curHour);
        curTime.add(Calendar.HOUR, addHour);
        return curTime.get(Calendar.HOUR_OF_DAY);
    }
}
