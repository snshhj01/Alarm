package kr.co.miracom.alarm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author kws
 * @date 2016-06-03
 * @since 0.1
 */
public class CommonUtils {

    /**
     * create Gson instance
     * @return
     */
    public static Gson getGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
    public static int getAlarmId() {
        int id = (int) (System.currentTimeMillis()/100);
        return id;
    }
}
