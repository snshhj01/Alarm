package kr.co.miracom.alarm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.vo.ext.Alarms;


/**
 * Created by probyoo on 2016-05-23.
 */
public class AlarmListPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    private final int TAB_CNT = 2;

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
                initListView(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.alarm_list, null);
                initListView(view);
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
                return "Tab One";
            case 1:
                return "Tab Two";
        }

        return super.getPageTitle(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        super.destroyItem(container, position, object);
    }

    private void initListView(View v){
        ListView listView = (ListView) v.findViewById(R.id.alarm_list_view);
        AlarmListAdapter adapter = new AlarmListAdapter();

        Alarms aV = new Alarms("카메라 스티커 확인","오전","08:00~09:00","일월화수목금토","향군타워","50m", "소리");
        Alarms aV1 = new Alarms("가방 보안 체크","오후","06:00~07:00","일월화수목금토","잠실 동관","100m", "진동");


        adapter.add(aV);
        adapter.add(aV);
        adapter.add(aV1);
        adapter.add(aV);
        adapter.add(aV);
        adapter.add(aV1);
        adapter.add(aV);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
