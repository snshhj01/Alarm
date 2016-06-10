package kr.co.miracom.alarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import kr.co.miracom.alarm.R;

/**
 * Created by kimsungmog on 2016-06-09.
 */
public class AlarmRepeatActivity extends AppCompatActivity{

    RadioGroup radioInterval;
    RadioGroup radioRepeat;
    Button btnrepeat;
    int interval_min;
    int repeat_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat_list_layout);

        radioInterval = (RadioGroup)findViewById(R.id.radioInterval);
        radioInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.interval_five){
                    interval_min = 5;
                }else if (checkedId == R.id.interval_ten){
                    interval_min = 10;
                }else if (checkedId == R.id.interval_fifteen){
                    interval_min = 15;
                }else{
                    interval_min = 30;
                }
            }
        });
        radioRepeat = (RadioGroup)findViewById(R.id.radioRepeat);

        radioRepeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.repeat_three){
                    repeat_count = 3;
                }else if (checkedId == R.id.repeat_five){
                    repeat_count = 5;
                }else{
                    repeat_count = 10;
                }
            }
        });

        btnrepeat = (Button)findViewById(R.id.btnrepeat);
        btnrepeat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initLayout();

    }

    public void initLayout(){
        radioInterval.check(R.id.interval_five);
        radioRepeat.check(R.id.repeat_three);
    }
}
