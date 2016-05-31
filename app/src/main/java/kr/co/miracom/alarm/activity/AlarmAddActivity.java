package kr.co.miracom.alarm.activity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import kr.co.miracom.alarm.R;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSettingsCancel;
    private Button btnSettingsOk;
    private ToggleButton toggleBtnSunday;
    private ToggleButton toggleBtnMonday;
    private ToggleButton toggleBtnTuesday;
    private ToggleButton toggleBtnWednesday;
    private ToggleButton toggleBtnThursday;
    private ToggleButton toggleBtnFriday;
    private ToggleButton toggleBtnSaturday;
    private TextView tvAlarmContent;
    private Intent ringtoneIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_simple_alarm);

        btnSettingsCancel = (Button)findViewById(R.id.btnSettingsCancel);
        btnSettingsOk = (Button)findViewById(R.id.btnSettingsOk);
        toggleBtnSunday = (ToggleButton)findViewById(R.id.toggleBtnSunday);
        toggleBtnMonday = (ToggleButton)findViewById(R.id.toggleBtnMonday);
        toggleBtnTuesday = (ToggleButton)findViewById(R.id.toggleBtnTuesday);
        toggleBtnWednesday = (ToggleButton)findViewById(R.id.toggleBtnWednesday);
        toggleBtnThursday = (ToggleButton)findViewById(R.id.toggleBtnThursday);
        toggleBtnFriday = (ToggleButton)findViewById(R.id.toggleBtnFriday);
        toggleBtnSaturday = (ToggleButton)findViewById(R.id.toggleBtnSaturday);

        tvAlarmContent = (TextView) findViewById(R.id.tvAlarmContent);
        tvAlarmContent.setOnClickListener(this);

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
        }else if(v == tvAlarmContent){
            ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,"Select RingTone");
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,true);
            startActivityForResult(ringtoneIntent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        String ringTonePath = ringtone.getTitle(this);
        tvAlarmContent.setText(ringTonePath);

    }


}
