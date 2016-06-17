package kr.co.miracom.alarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.activity.AlarmAddActivity;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.view.SiteView;
import kr.co.miracom.alarm.vo.ext.Alarms;
import kr.co.miracom.alarm.vo.ext.SiteItem;

/**
 * Created by admin on 2016-05-31.
 */
public class SiteListAdapter extends BaseAdapter {


    //private final String TAG = "SiteListAdapter";

    //private Context mContext;

    //private List<SiteItem> items = new ArrayList<SiteItem>();

    private ArrayList<Alarms> m_list;


    public SiteListAdapter() {
        m_list = new ArrayList<Alarms>();
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

            Alarms aVO = m_list.get(position);

            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
            TextView tTitle = (TextView) convertView.findViewById(R.id.title);
            TextView tAmPm = (TextView) convertView.findViewById(R.id.amPm);
            TextView tTimeFromTo = (TextView) convertView.findViewById(R.id.timeFromTo);
            TextView tDayOfWeek = (TextView) convertView.findViewById(R.id.dayOfWeek);
            TextView tLoc = (TextView) convertView.findViewById(R.id.loc);
            TextView tLocRange = (TextView) convertView.findViewById(R.id.locRange);
            TextView tBell = (TextView) convertView.findViewById(R.id.bell);
            tTitle.setText(aVO.getTitle());
            tAmPm.setText(aVO.getAmPm());
            tTimeFromTo.setText(aVO.getTimeFromTo());
            tDayOfWeek.setText(aVO.getDayOfWeek());
            tLoc.setText(aVO.getLoc());
            tLocRange.setText(aVO.getLocRange());
            tBell.setText(aVO.getBell());

//            Button btn = (Button) convertView.findViewById(R.id.btn_test);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, m_list.get(pos), Toast.LENGTH_SHORT).show();
//                }
//            });

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    //Toast.makeText(context, "사이트리스트 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AlarmAddActivity.class);
                    intent.putExtra(Constants.ALARM_ID, m_list.get(pos).get_Id());
                    context.startActivity(intent);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    Toast.makeText(context, "사이트리스트 롱 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        return convertView;
    }

    public void add(Alarms vo) {
        m_list.add(vo);
    }
}