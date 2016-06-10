package kr.co.miracom.alarm.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import kr.co.miracom.alarm.activity.NotiActivity;

/**
 * Created by wjw on 2016-05-13.
 */
public class GpsReciever extends BroadcastReceiver {
    private String mExpectedAction;
    private Intent mLastReceivedIntent;

    public GpsReciever(String expectedAction) {
        mExpectedAction = expectedAction;
        mLastReceivedIntent = null;
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter(mExpectedAction);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            mLastReceivedIntent = intent;
            int id = intent.getIntExtra("id", 2000);
            double lat = intent.getDoubleExtra("lat", 0.0D);
            double lng = intent.getDoubleExtra("lng", 0.0D);

            Intent alarmIntent = new Intent(context, NotiActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            alarmIntent.putExtra("msg", "근처다." + id );
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, alarmIntent, 0);

            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    public Intent getLastRecievedIetent() {
        return mLastReceivedIntent;
    }

    public void clearReceivedIntents() {
        mLastReceivedIntent = null;
    }


}
