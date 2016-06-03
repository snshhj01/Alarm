package kr.co.miracom.alarm.vo;

/**
 * Created by kimsungmog on 2016-06-01.
 */
public class SimpleVo {
    private int id;
    private String date;
    private String days;
    private int cycle;
    private String type;
    private String uri;
    private String sound;
    private int volum;
    private int used;
    private int repeat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getCycle(){
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getType() {
        return type;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getVolum() {
        return volum;
    }

    public void setVolum(int volum) {
        this.volum = volum;
    }

    public int getUsed(){
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getRepeat(){
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
