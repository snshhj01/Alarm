package kr.co.miracom.alarm.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import kr.co.miracom.alarm.util.AlarmUtils;

/**
 * Created by jiwoon-won on 2016-05-27.
 */
public class PushWakeLock {
    private static PowerManager.WakeLock sCpuWakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static boolean isScreenLock;

    static void acquireCpuWakeLock(Context context, long timeout) {
        Log.e("PushWakeLock", "Acquiring cpu wake lock");
        Log.e("PushWakeLock", "wake sCpuWakeLock = " + sCpuWakeLock);

        if (sCpuWakeLock != null) {
            return;
        }

        if (AlarmUtils.getInstance().isWakeLock == true) {
            Log.e("PushWakeLock", "Acquiring cpu wake lock start.");
            AlarmUtils.getInstance().isWakeLock = false;
        } else {
            Log.e("PushWakeLock", "Acquiring cpu wake lock return.");
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hello");

        if(timeout > 0)
            sCpuWakeLock.acquire(timeout);
        else
            sCpuWakeLock.acquire();


    }

    static void releaseCpuLock() {
        Log.e("PushWakeLock", "Releasing cpu wake lock");
        Log.e("PushWakeLock", "relase sCpuWakeLock = " + sCpuWakeLock);

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }

}
