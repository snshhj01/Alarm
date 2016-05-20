package miracom.com.alarm;

import android.content.Context;

/**
 * Created by kimsungmog on 2016-05-19.
 */
public class Alarms {

    private final static String M12 = "h:mm aa";
    // Shared with DigitalClock
    final static String M24 = "kk:mm";


    static boolean get24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }
}
