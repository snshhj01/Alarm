package miracom.com.alarm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by kimsungmog on 2016-05-19.
 */
public class DigitalClock extends LinearLayout{

    private TextView mTimeDisplay;

    private Calendar mCalendar;
    private Context mContext;

    private String mFormat;

    private final static String M12 = "h:mm";

    public DigitalClock(Context context) {
        this(context, null);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
        mCalendar = Calendar.getInstance();
        mContext = getContext();
        setDateFormat();
    }

    private void setDateFormat() {

        mFormat = Alarms.get24HourMode(mContext) ? Alarms.M24 : M12;

    }
}
