package kr.co.miracom.alarm.activity;

import android.content.Intent;
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
    Intent intent;
    int interval;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat_list_layout);

        radioInterval = (RadioGroup)findViewById(R.id.radioInterval);
        radioInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.interval_five){
                    interval = 5;
                }else if (checkedId == R.id.interval_ten){
                    interval = 10;
                }else if (checkedId == R.id.interval_fifteen){
                    interval = 15;
                }else{
                    interval = 30;
                }
            }
        });
        radioRepeat = (RadioGroup)findViewById(R.id.radioRepeat);

        radioRepeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.repeat_three){
                    count = 3;
                }else if (checkedId == R.id.repeat_five){
                    count = 5;
                }else{
                    count = 10;
                }
            }
        });

        btnrepeat = (Button)findViewById(R.id.btnrepeat);
        btnrepeat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent.putExtra("interval",interval);
                intent.putExtra("count",count);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        initLayout();

    }

    public void initLayout(){
        intent = getIntent();
        interval = intent.getIntExtra("interval",5);
        count = intent.getIntExtra("count",3);

        if(interval == 5){
            radioInterval.check(R.id.interval_five);
        }else if( interval == 10){
            radioInterval.check(R.id.interval_ten);
        }else if( interval == 15){
            radioInterval.check(R.id.interval_fifteen);
        }else{
            radioInterval.check(R.id.interval_thirty);
        }

        if(count == 3){
            radioRepeat.check(R.id.repeat_three);
        }else if(count == 5){
            radioRepeat.check(R.id.repeat_five);
        }else{
            radioRepeat.check(R.id.repeat_continue);
        }

    }
}
