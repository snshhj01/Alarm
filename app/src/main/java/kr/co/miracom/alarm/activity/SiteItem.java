package kr.co.miracom.alarm.activity;

/**
 * Created by admin on 2016-05-31.
 */
public class SiteItem {
    private String[] data;

    public SiteItem(){

    }

    public SiteItem(String addr, String latitude, String longitude){
        data = new String[3];
        data[0] = addr;
        data[1] = latitude;
        data[2] = longitude;
    }

    public SiteItem(String[] obj){
        data = obj;
    }

    public String[] getData(){
        return data;
    }

    public void setData(String[] obj){
        data = obj;
    }

    public String getData(int index) {
        if (data == null || index >= data.length) {
            return null;
        }

        return data[index];
    }
}
