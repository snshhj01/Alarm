package kr.co.miracom.alarm.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.adapter.AlarmListPagerAdapter;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.Logger;

public class AlarmListActivity extends AppCompatActivity {

    ViewPager pager;
    FloatingActionButton addAlarmBtn;
    Integer posion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.alarm_list_layout);
        AlarmListPagerAdapter adapter = new AlarmListPagerAdapter(this, getLayoutInflater());
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);

        Intent intent = getIntent();

        Logger.d( this.getClass(), "%s",  "인텐트 뭐 받앗니 : " + intent.getIntExtra(Constants.PAGER, 0));
        posion = intent.getIntExtra(Constants.PAGER, 0);
        pager.setCurrentItem(posion);

        addAlarmBtn = (FloatingActionButton) findViewById(R.id.add_alarm);
        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Logger.d(this.getClass(), "%s", pager.getCurrentItem());
                if (pager.getCurrentItem() == 0) {
                    intent = new Intent(v.getContext(), AlarmAddActivity.class);

                } else {
                    intent = new Intent(v.getContext(), SiteAddActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
        enableGPSSetting();
       // AlarmUtils.getInstance().gpsInit(getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.PAGER, pager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        posion = savedInstanceState.getInt(Constants.PAGER, 0);
    }

    public void enableGPSSetting() {
        ContentResolver res = this.getContentResolver();
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(res,
                LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS 설정")
                    .setMessage("GPS가 필요한 서비스입니다. \nGPS를 켜시겠습니까?")
                    .setPositiveButton("GPS 켜기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("닫기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).show();
        }
    }

}
