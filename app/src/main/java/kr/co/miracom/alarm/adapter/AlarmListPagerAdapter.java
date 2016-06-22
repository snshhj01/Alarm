package kr.co.miracom.alarm.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;
import kr.co.miracom.alarm.vo.ext.Alarms;

import static kr.co.miracom.alarm.util.CommonUtils.getHourAmPm;
import static kr.co.miracom.alarm.util.CommonUtils.getKorAmPm;


/**
 * Created by probyoo on 2016-05-23.
 */
public class AlarmListPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    private final int TAB_CNT = 2;

    protected DBHelper mDbHelper;



    @Override
    public int getCount() {
        return TAB_CNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public AlarmListPagerAdapter(Context context, LayoutInflater inflater) {
        this.context = context;this.inflater = inflater;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = null;

        switch (position) {
            case 0:
                view = inflater.inflate(R.layout.alarm_list, null);
                alarmListView(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.alarm_list, null);
                siteListView(view);
                break;
            default:
                break;
        }

        if (view != null) container.addView(view);

        return view;

    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.tab_alarm);
            case 1:
                return context.getString(R.string.tab_location);
        }

        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        super.destroyItem(container, position, object);
    }

    private void alarmListView(View v){
        ListView listView = (ListView) v.findViewById(R.id.alarm_list_view);
        AlarmListAdapter adapter = new AlarmListAdapter(context, listView);

        mDbHelper = new DBHelper(context);
        mDbHelper.open();

        List<AlarmInfo> alarmList = mDbHelper.selectAll("N");
        for(AlarmInfo aInfo : alarmList){

//            String amPm = getKorAmPm(aInfo.getTime().get("hour"));
//            String timeFromTo = getHourAmPm(aInfo.getTime().get("hour")) + ":" + (aInfo.getTime().get("minute")<10 ? "0": "") + aInfo.getTime().get("minute");
//
//            String aType = "";
//            if(aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND) {
//                aType = "소리";
//            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_VIBRATE) {
//                aType = "진동";
//            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND_VIBRATE) {
//                aType = "소리+진동";
//            }
//
//            Alarms aV = new Alarms();
//            aV.setTitle(aInfo.getAlarmName());
//            aV.setAmPm(amPm);
//            aV.setTimeFromTo(timeFromTo);
//            aV.setBell(aType);
//            aV.setDayOfWeek(aInfo.getDays());
//            aV.set_Id(aInfo.get_id());

            adapter.add(aInfo);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void siteListView(View v){
        ListView listView = (ListView) v.findViewById(R.id.alarm_list_view);
        SiteListAdapter adapter = new SiteListAdapter(context, listView);

        mDbHelper = new DBHelper(context);
        mDbHelper.open();

        List<AlarmInfo> siteList = mDbHelper.selectAll("Y");
        for(AlarmInfo aInfo : siteList){

//            String amPm =  getKorAmPm(aInfo.getTime().get("hour"));
//            String timeFromTo = getHourAmPm(aInfo.getTime().get("hour")) + ":" + (aInfo.getTime().get("minute")<10 ? "0": "") + aInfo.getTime().get("minute")
//                    + "~" + getHourAmPm(aInfo.getTime().get("hour")) + ":" + (aInfo.getTime().get("minute") < 10 ? "0" : "") + aInfo.getTime().get("minute");
//
//            String aType = "";
//            if(aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND) {
//                aType = "소리";
//            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_VIBRATE) {
//                aType = "진동";
//            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND_VIBRATE) {
//                aType = "소리+진동";
//            }
//
//            Alarms aV = new Alarms();
//            aV.setTitle(aInfo.getAlarmName());
//            aV.setAmPm(amPm);
//            aV.setTimeFromTo(timeFromTo);
//            aV.setBell(aType);
//            aV.setDayOfWeek(aInfo.getDays());
//            aV.setLoc(aInfo.getAddr());
//            aV.setLocRange(aInfo.getRadius());
//            aV.set_Id(aInfo.get_id());
//            aV.setAlarmId(aInfo.getAlarmId());

            adapter.add(aInfo);
        }


        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(context, AlarmAddActivity.class);
////                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }




}
