package kr.co.miracom.alarm.vo.ext;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.miracom.alarm.vo.AbstractVO;

/**
 * @author kws
 * @date 2016-06-03
 * @since 0.1
 */
public class AlarmInfo extends AbstractVO {
    int _id;
    String alarmName;
    HashMap<String,Integer> time;
    int alarmId;
    ArrayList<Integer> days;
    int alarmType;
    HashMap<String,String> alarmSound;
    int volume;
    HashMap<String,Integer> snooze;
    int active;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public HashMap<String, Integer> getTime() {
        return time;
    }

    public void setTime(HashMap<String, Integer> time) {
        this.time = time;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public void setDays(ArrayList<Integer> days) {
        this.days = days;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public HashMap<String, String> getAlarmSound() {
        return alarmSound;
    }

    public void setAlarmSound(HashMap<String, String> alarmSound) {
        this.alarmSound = alarmSound;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public HashMap<String, Integer> getSnooze() {
        return snooze;
    }

    public void setSnooze(HashMap<String, Integer> snooze) {
        this.snooze = snooze;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
