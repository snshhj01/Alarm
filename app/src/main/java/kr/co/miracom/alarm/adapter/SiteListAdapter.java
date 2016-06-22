package kr.co.miracom.alarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.activity.AlarmAddActivity;
import kr.co.miracom.alarm.activity.SiteAddActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.view.SiteView;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;
import kr.co.miracom.alarm.vo.ext.Alarms;
import kr.co.miracom.alarm.vo.ext.SiteItem;

import static kr.co.miracom.alarm.util.CommonUtils.getHourAmPm;
import static kr.co.miracom.alarm.util.CommonUtils.getKorAmPm;
import static kr.co.miracom.alarm.util.CommonUtils.setColorDOW;

/**
 * Created by admin on 2016-05-31.
 */
public class SiteListAdapter extends BaseAdapter {


    //private final String TAG = "SiteListAdapter";

    //private Context mContext;

    //private List<SiteItem> items = new ArrayList<SiteItem>();

    private ArrayList<AlarmInfo> m_list;
    protected DBHelper mDbHelper;
    private Context mContext;
    private ListView mListView;

    public SiteListAdapter() {
        m_list = new ArrayList<AlarmInfo>();
    }

    public SiteListAdapter(Context context, ListView listView) {
        m_list = new ArrayList<AlarmInfo>();
        mContext = context;
        mListView = listView;
    }


    //public void setItem(SiteItem item) {
    //    items.add(item);
    //}

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


//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        SiteView itemView;
//        if (convertView == null) {
//            itemView = new SiteView(mContext, items.get(position));
//        } else {
//            itemView = (SiteView) convertView;
//
//            itemView.setText(items.get(position).getData(0));
//        }
//
//        return itemView;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);

            AlarmInfo aInfo = m_list.get(position);

            String amPm =  getKorAmPm(aInfo.getTime().get("hour"));
            String timeFromTo = getHourAmPm(aInfo.getTime().get("hour")) + ":" + (aInfo.getTime().get("minute")<10 ? "0": "") + aInfo.getTime().get("minute")
                    + "~" + getHourAmPm(aInfo.getTime().get("hour")) + ":" + (aInfo.getTime().get("minute") < 10 ? "0" : "") + aInfo.getTime().get("minute");

            String aType = "";
            if(aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND) {
                aType = "소리";
            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_VIBRATE) {
                aType = "진동";
            } else if (aInfo.getAlarmType() == Constants.ALARM_TYPE_SOUND_VIBRATE) {
                aType = "소리+진동";
            }

            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
            TextView tTitle = (TextView) convertView.findViewById(R.id.title);
            TextView tAmPm = (TextView) convertView.findViewById(R.id.amPm);
            TextView tTimeFromTo = (TextView) convertView.findViewById(R.id.timeFromTo);
            TextView tLoc = (TextView) convertView.findViewById(R.id.loc);
            TextView tLocRange = (TextView) convertView.findViewById(R.id.locRange);
            TextView tBell = (TextView) convertView.findViewById(R.id.bell);
            tTitle.setText(aInfo.getAlarmName());
            tAmPm.setText(amPm);
            tTimeFromTo.setText(timeFromTo);
            setColorDOW(convertView, aInfo.getDays());
            tLoc.setText(aInfo.getAddr());
            tLocRange.setText(aInfo.getRadius());
            tBell.setText(aType);

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
                    //Toast.makeText(context, "사이트리스트 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SiteAddActivity.class);
                    intent.putExtra(Constants.ALARM_ID, m_list.get(pos).get_id());
                    context.startActivity(intent);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
//                    Toast.makeText(context, "사이트리스트 롱 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    remove(pos, context);
                    return true;
                }
            });

        }

        return convertView;
    }

    public void add(AlarmInfo aInfo) {
        m_list.add(aInfo);
    }

    public void remove(int _position, Context context){

        mDbHelper = new DBHelper(context);
        mDbHelper.open();
        //Toast.makeText(context, "없어질  : " + m_list.get(_position).getTitle()+"/"+ m_list.get(_position).get_Id() + "/" + _position, Toast.LENGTH_SHORT).show();
        mDbHelper.deleteAlarm(m_list.get(_position).get_id());

//        for(Alarms a : m_list){
//            Logger.d(this.getClass(), "%S", a.getTitle());
//        }
//        Logger.d(this.getClass(), "%s", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        m_list.remove(getItem(_position));

//        for(Alarms a : m_list){
//            Logger.d(this.getClass(), "%S", a.getTitle());
//        }
        mListView.setAdapter(this);
        this.notifyDataSetChanged();

    }
}