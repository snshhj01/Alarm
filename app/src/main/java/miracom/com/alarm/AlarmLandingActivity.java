package miracom.com.alarm;

/**
 * Created by sunghan on 2016-05-19.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AlarmLandingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_landing);
        /** set time to splash out */
        final int welcomeScreenDisplay = 3000;
        /** create a thread to show splash up to splash time */
        Thread welcomeThread = new Thread() {

            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    /**
                     * use while to get the splash time. Use sleep() to increase
                     * the wait variable for every 100L.
                     */
                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    /**
                     * Called after splash times up. Do some action after splash
                     * times up. Here we moved to another main activity class
                     */
                    startActivity(new Intent(AlarmLandingActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}
