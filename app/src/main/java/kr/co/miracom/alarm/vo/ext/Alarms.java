package kr.co.miracom.alarm.vo.ext;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kimsungmog on 2016-05-19.
 */
public class Alarms {
    private String title;
    private String amPm;
    private String timeFromTo;
    private ArrayList<Integer> dayOfWeek;
    private String loc;
    private String locRange;
    private String bell;
    private int _Id;
    private int alarmId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }

    public String getTimeFromTo() {
        return timeFromTo;
    }

    public void setTimeFromTo(String timeFromTo) {
        this.timeFromTo = timeFromTo;
    }

    public ArrayList<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(ArrayList<Integer> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLocRange() {
        return locRange;
    }

    public void setLocRange(String locRange) {
        this.locRange = locRange;
    }

    public String getBell() {
        return bell;
    }

    public void setBell(String bell) {
        this.bell = bell;
    }

    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public Alarms() {
    }

    public Alarms(String title, String amPm, String timeFromTo, ArrayList<Integer> dayOfWeek, String loc, String locRange, String bell, int _Id, int alarmId) {
        this.title = title;
        this.amPm = amPm;
        this.timeFromTo = timeFromTo;
        this.dayOfWeek = dayOfWeek;
        this.loc = loc;
        this.locRange = locRange;
        this.bell = bell;
        this._Id = _Id;
        this.alarmId = alarmId;
    }

    private final static String M12 = "h:mm aa";
    // Shared with DigitalClock
    final public static String M24 = "kk:mm";

    public static boolean get24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }


}
