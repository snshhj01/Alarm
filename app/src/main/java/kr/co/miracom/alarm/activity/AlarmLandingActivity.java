package kr.co.miracom.alarm.activity;

/**
 * Created by sunghan on 2016-05-19.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import kr.co.miracom.alarm.R;

public class AlarmLandingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_landing);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                finish();
                Intent intent = new Intent(AlarmLandingActivity.this, AlarmListActivity.class);
                startActivity(intent);
            }
        };
        // 2초 로딩 이후 종료
        handler.sendEmptyMessageDelayed(0, 3000);

    }
}
