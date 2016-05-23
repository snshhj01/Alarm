package miracom.com.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.text.DateFormatSymbols;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

    private TabHost host;
    private ViewGroup sClockLayout;
    private View sClock = null;

    private String mAm, mPm;

    static final boolean DEBUG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] ampm = new DateFormatSymbols().getAmPmStrings();
        mAm = ampm[0];
        mPm = ampm[1];

        initTab();
        //initLayOutSimpleAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.add_alram_default);
        //inflateClock();

    }

    private void initTab(){
        host = (TabHost)findViewById(R.id.host);
        host.setup();

        TabHost.TabSpec spac = host.newTabSpec("tab1");
        spac.setIndicator("Alarm"); //button
        spac.setContent(R.id.simple_alarm);
        host.addTab(spac);

        spac = host.newTabSpec("tab2");
        spac.setIndicator("Location"); //button
        spac.setContent(R.id.location_alarm);
        host.addTab(spac);
        host.setOnTabChangedListener(this);
    }

    private void initLayOutSimpleAlarm(){

        //simple_clock_layout은 alarmclock.xml에 있는 시계 화면과 list화면 중 시계화면 LinearLayout
        sClockLayout = (ViewGroup) findViewById(R.id.simple_clock_layout);
        sClockLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab1")){


        }
    }

    //custom digital_clock inflate
    private void inflateClock(){
        if (sClock != null) {
            sClockLayout.removeView(sClock);
        }
        //digital_clock digital_clock.xml miracom.com.alarm DigitalClock.java

        LayoutInflater.from(this).inflate(R.layout.digital_clock, sClockLayout);
        //위 코드가 불려지고 메모리 적재 시킨후..
        //DigitalClock.java protected void onFinishInflate()함수가 실행된다.

        //R.id.clock는 custom layout
        sClock = findViewById(R.id.clock);

        /*TextView am = (TextView) findViewById(R.id.am);
        TextView pm = (TextView) findViewById(R.id.pm);

       if (am != null) {
            am.setText(mAm);
        }
        if (pm != null) {
            pm.setText(mPm);
        }*/

        Log.v("inflateClock END!!");
    }
}
