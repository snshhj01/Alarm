package kr.co.miracom.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.vo.ext.Alarms;

/**
 * Created by hjinhwang on 2016-05-19.
 */
public class AlarmListAdapter extends BaseAdapter {

    private ArrayList<Alarms> m_list;

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
                    Toast.makeText(context, "리스트 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // 터치 시 해당 아이템 이름 출력
                    Toast.makeText(context, "리스트 롱 클릭 : " + m_list.get(pos), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        return convertView;
    }

    public void add(Alarms vo) {
        m_list.add(vo);
    }

//    public void remove(int _position){
//        m_list.remove(_position);
//    }
}
