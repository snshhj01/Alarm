package miracom.com.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by kimsungmog on 2016-05-19.
 */
public class DigitalClock extends LinearLayout{

    private TextView hTimeDisplay;
    private TextView mTimeDisplay;

    private Calendar mCalendar;
    private Context mContext;
    private AmPm mAmPm;
    private ContentObserver mFormatChangeObserver;


    private String mFormat;
    private boolean mAttached;
    private boolean mLive = true;

    private final static String M12 = "h:mm";

    private final Handler mHandler = new Handler();

    //실시간 현재 time update
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mLive && intent.getAction().equals(
                    Intent.ACTION_TIMEZONE_CHANGED)) {
                mCalendar = Calendar.getInstance();
            }
            updateTime();
        }
    };

    //AM PM 확인
    static class AmPm {

        private int mColorOn, mColorOff;

        private LinearLayout mAmPmLayout;
        private TextView mAm, mPm;

        AmPm(View parent) {
            mAmPmLayout = (LinearLayout) parent.findViewById(R.id.am_pm);
            mAm = (TextView)mAmPmLayout.findViewById(R.id.am);
            mPm = (TextView)mAmPmLayout.findViewById(R.id.pm);

            Resources r = parent.getResources();

            mColorOn = r.getColor(R.color.ampm_on);
            mColorOff = r.getColor(R.color.ampm_off);
        }

        void setShowAmPm(boolean show) {
            mAmPmLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        void setIsMorning(boolean isMorning) {
            Log.v(String.valueOf(isMorning));
            mAm.setTextColor(isMorning ? mColorOn : mColorOff);
            mPm.setTextColor(isMorning ? mColorOff : mColorOn);
        }

    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }
        @Override
        public void onChange(boolean selfChange) {
            setDateFormat();
            updateTime();
        }
    }

    public DigitalClock(Context context) {
        this(context, null);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        hTimeDisplay = (TextView) findViewById(R.id.hTimeDisplay);
        mTimeDisplay = (TextView) findViewById(R.id.mTimeDisplay);
        mAmPm = new AmPm(this);
        mCalendar = Calendar.getInstance();
        mContext = getContext();
        setDateFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("start onAttachedToWindow");

        if (mAttached)
            return;

        mAttached = true;

        /*if (mLive) {

            *//* monitor time ticks, time changed, timezone *//*
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
        }*/

        /* monitor 12/24-hour display preference */
        /*mFormatChangeObserver = new FormatChangeObserver();
        mContext.getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);*/

        updateTime();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (!mAttached) return;
        mAttached = false;

        Drawable background = getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).stop();
        }

        /*if (mLive) {
            mContext.unregisterReceiver(mIntentReceiver);
        }*/
        /*mContext.getContentResolver().unregisterContentObserver(
                mFormatChangeObserver);*/
    }

    private void updateTime() {
        Log.v("updateTime start");
        if (mLive) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        CharSequence newTime = DateFormat.format(mFormat, mCalendar);

        hTimeDisplay.setText(newTime.subSequence(0,1));
        mTimeDisplay.setText(newTime.subSequence(2,4));

        mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);

        Log.v("updateTime end");
    }

    private void setDateFormat() {

        mFormat = Alarms.get24HourMode(mContext) ? Alarms.M24 : M12;

    }
}
