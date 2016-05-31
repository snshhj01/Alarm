package kr.co.miracom.alarm.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016-05-31.
 */
public class SiteListAdapter extends BaseAdapter {
    private final String TAG = "SiteListAdapter";

    private Context mContext;

    private List<SiteItem> items = new ArrayList<SiteItem>();

    public SiteListAdapter(Context context){
        mContext = context;
    }


    public void setItem(SiteItem item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SiteView itemView;
        if (convertView == null) {
            itemView = new SiteView(mContext, items.get(position));
        } else {
            itemView = (SiteView) convertView;

            itemView.setText(items.get(position).getData(0));
        }

        return itemView;
    }
}
