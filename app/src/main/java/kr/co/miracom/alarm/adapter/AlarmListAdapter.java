package kr.co.miracom.alarm.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.activity.AlarmAddActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.service.AlarmReceiver;
import kr.co.miracom.alarm.util.AlarmUtils;
import kr.co.miracom.alarm.util.CommonUtils;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.vo.ext.Alarms;

import static kr.co.miracom.alarm.util.CommonUtils.setColorDOW;

/**
 * Created by hjinhwang on 2016-05-19.
 */
public class AlarmListAdapter extends BaseAdapter {

    private ArrayList<Alarms> m_list;
    protected DBHelper mDbHelper;

    private AlarmManager alarmManager;

    public AlarmListAdapter() {
        m_list = new ArrayList<Alarms>();
    }

    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);

            Alarms aVO = m_list.get(position);

            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
            TextView tTitle = (TextView) convertView.findViewById(R.id.title);
            TextView tAmPm = (TextView) convertView.findViewById(R.id.amPm);
            TextView tTimeFromTo = (TextView) convertView.findViewById(R.id.timeFromTo);
            TextView tLoc = (TextView) convertView.findViewById(R.id.loc);
            TextView tLocRange = (TextView) convertView.findViewById(R.id.locRange);
            TextView tBell = (TextView) convertView.findViewById(R.id.bell);
            tTitle.setText(aVO.getTitle());
            tAmPm.setText(aVO.getAmPm());
            tTimeFromTo.setText(aVO.getTimeFromTo());
            setColorDOW(convertView, aVO.getDayOfWeek());
            tLoc.setText(aVO.getLoc());
            tLocRange.setText(aVO.getLocRange());
            tBell.setText(aVO.getBell());

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "알람클로즈 클릭 : " + m_list.get(pos).get_Id() + "/" + pos, Toast.LENGTH_SHORT).show();

                    int active = m_list.get(pos).getActive();
                    int alarmUniqId = m_list.get(pos).getAlarmId();

                    if (active == 1) {
                        disableExistAlarm(alarmUniqId, context);
                    } else {
                        enableExistAlarm(m_list.get(pos), context);
                    }
                }
            });

            ImageView btnClose = (ImageView) convertView.findViewById(R.id.close);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "알람클로즈 클릭 : " + m_list.get(pos).get_Id() + "/" + pos, Toast.LENGTH_SHORT).show();
                    remove(pos, context);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    //Toast.makeText(context, "알람리스트 클릭 : " + m_list.get(pos).get_Id() + "/" + pos, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AlarmAddActivity.class);
                    intent.putExtra(Constants.ALARM_ID, m_list.get(pos).get_Id());
                    context.startActivity(intent);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    //Toast.makeText(context, "알람리스트 롱 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    remove(pos, context);
                    return true;
                }
            });

        }

        return convertView;
    }

    public void add(Alarms vo) {
        m_list.add(vo);
    }



    public void remove(int _position, Context context){

        int alarmUniqId = m_list.get(_position).getAlarmId();
        int _id = m_list.get(_position).get_Id();

        mDbHelper = new DBHelper(context);
        mDbHelper.open();
        //Toast.makeText(context, "없어질  : " + m_list.get(_position).getTitle()+"/"+ m_list.get(_position).get_Id() + "/" + _position, Toast.LENGTH_SHORT).show();
        mDbHelper.deleteAlarm(_id);

//        for(Alarms a : m_list){
//            Logger.d(this.getClass(), "%S", a.getTitle());
//        }
//        Logger.d(this.getClass(), "%s", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        m_list.remove(getItem(_position));

//        for(Alarms a : m_list){
//            Logger.d(this.getClass(), "%S", a.getTitle());
//        }

        disableExistAlarm(alarmUniqId, context) ;

        this.notifyDataSetChanged();

    }


//    public String korDayOfWeek(ArrayList<Integer> arrInt){
//        String korDOW = "";
//        String[] arrStr= {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
//        for(int i=0; i<arrInt.size(); i++){
//            korDOW += arrStr[arrInt.get(i)];
//        }
//
//        return korDOW;
//    }

    private void disableExistAlarm(int alartUniqId, Context context) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, alartUniqId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        pIntent.cancel();
    }

    private void enableExistAlarm(Alarms aVO, Context context) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        long triggerTime = 0;
        long intervalTime = 24 * 60 * 60 * 1000;// 24시간

        boolean isRepeat = aVO.getDayOfWeek().size() > 0;
        int alartUniqId = aVO.getAlarmId();

        intent.putExtra("one_time", isRepeat);
        intent.putExtra("alartUniqId", alartUniqId);
//        triggerTime = CommonUtils.setTriggerTime();
//        AlarmUtils.getInstance().startAlarm(context, intent, triggerTime, (isRepeat ? 1 : 0) );
    }


}
