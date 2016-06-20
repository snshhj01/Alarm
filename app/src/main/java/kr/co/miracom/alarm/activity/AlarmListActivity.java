package kr.co.miracom.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
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

//        Logger.d( this.getClass(), "%s",  "인텐트 뭐 받앗니 : " + intent.getIntExtra(Constants.PAGER, 0));
        if(intent.getIntExtra(Constants.PAGER, 0) == 1){
            pager.setCurrentItem(1);  // SiteList로 이동
        }

        addAlarmBtn = (FloatingActionButton) findViewById(R.id.add_alarm);
        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Logger.d(this.getClass(), "%s", pager.getCurrentItem());
                if(pager.getCurrentItem() ==  0){
                    intent = new Intent(v.getContext(), AlarmAddActivity.class);

                }else{
                    intent = new Intent(v.getContext(), SiteAddActivity.class);
                }
                startActivity(intent);
            }
        });

    }

}
