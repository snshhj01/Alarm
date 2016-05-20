package miracom.com.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

    private TabHost host;
    private ViewGroup sClockLayout;
    private View sClock = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
        initLayOutSimpleAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inflateClock();

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
        sClockLayout = (ViewGroup) findViewById(R.id.simple_clock_layout);
        sClockLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab1")){


        }
    }

    private void inflateClock(){
        if (sClock != null) {
            sClockLayout.removeView(sClock);
        }

        LayoutInflater.from(this).inflate(R.layout.digital_clock, sClockLayout);
        sClock = findViewById(R.id.clock);


    }
}
