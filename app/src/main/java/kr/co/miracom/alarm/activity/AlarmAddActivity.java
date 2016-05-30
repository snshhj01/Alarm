package kr.co.miracom.alarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import kr.co.miracom.alarm.R;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSettingsCancel;
    private Button btnSettingsOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_simple_alarm);

        btnSettingsCancel = (Button)findViewById(R.id.btnSettingsCancel);
        btnSettingsOk = (Button)findViewById(R.id.btnSettingsOk);

        btnSettingsCancel.setOnClickListener(this);
        btnSettingsOk.setOnClickListener(this);

        initLayout();

    }

    public void initLayout(){

    }

    @Override
    public void onClick(View v) {
        if(v == btnSettingsCancel){
            finish();
        }else if(v == btnSettingsOk){
            finish();
        }
    }
}
