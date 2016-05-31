package kr.co.miracom.alarm.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.miracom.alarm.R;

/**
 * Created by admin on 2016-05-31.
 */
public class SiteView extends LinearLayout {
    private final String TAG = "SiteView";

    private TextView mAddr;

    public SiteView(Context context) {
        super(context);
    }

    public  SiteView(Context context, SiteItem item){
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_site_view, this, true);

        mAddr = (TextView) findViewById(R.id.mAddr);
        mAddr.setText(item.getData(0));
    }

    public void setText(String data) {
        mAddr.setText(data);
    }
}
