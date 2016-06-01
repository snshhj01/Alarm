package kr.co.miracom.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.sql.AlarmSql;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.vo.SimpleVo;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private AudioManager audio;
    private MediaPlayer mPlay;
    private AlarmSql sql;

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
    private SeekBar seekBarVolumeBar;

    private Ringtone rAlarm;
    private Ringtone defaultAlarm;
    private Uri rAlarmUri;

    private int alarmVolum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_simple_alarm);

        sql = new AlarmSql(this);

        mPlay = new MediaPlayer();


        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        rAlarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        defaultAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);


        toggleBtnSunday = (ToggleButton) findViewById(R.id.toggleBtnSunday);
        toggleBtnMonday = (ToggleButton) findViewById(R.id.toggleBtnMonday);
        toggleBtnTuesday = (ToggleButton) findViewById(R.id.toggleBtnTuesday);
        toggleBtnWednesday = (ToggleButton) findViewById(R.id.toggleBtnWednesday);
        toggleBtnThursday = (ToggleButton) findViewById(R.id.toggleBtnThursday);
        toggleBtnFriday = (ToggleButton) findViewById(R.id.toggleBtnFriday);
        toggleBtnSaturday = (ToggleButton) findViewById(R.id.toggleBtnSaturday);

        btnSettingsCancel = (Button) findViewById(R.id.btnSettingsCancel);
        btnSettingsCancel.setOnClickListener(this);

        btnSettingsOk = (Button) findViewById(R.id.btnSettingsOk);
        btnSettingsOk.setOnClickListener(this);

        seekBarVolumeBar = (SeekBar) findViewById(R.id.seekBarVolumeBar);
        seekBarVolumeBar.setOnSeekBarChangeListener(this);

        tvAlarmContent = (TextView) findViewById(R.id.tvAlarmContent);
        tvAlarmContent.setOnClickListener(this);

        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //화면 초기화...
    public void initLayout() {

        // 초기 시스템(alarm) volum을 가져와서 seekbar에 setprogress한다.
        alarmVolum = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        seekBarVolumeBar.setProgress(alarmVolum);

        //초기 default alarm path를 TextView 에 setting
        String alarmPath = defaultAlarm.getTitle(this);
        tvAlarmContent.setText(alarmPath);

    }

    //버튼 클릭 이벤트
    @Override
    public void onClick(View v) {

        if (v == btnSettingsCancel) {
            //취소 버튼 이벤트
            ringtoneStop();
            finish();
        } else if (v == btnSettingsOk) {
            //저장 버튼 이벤트
            SimpleVo simpleVo = new SimpleVo();
            //sql.insertSimpleAlarm(simpleVo);
            ringtoneStop();
            finish();
        } else if (v == tvAlarmContent) {
            //alarm textview click시 이벤트 alarm 선택 intent 실행
            ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select RingTone");
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);

            startActivityForResult(ringtoneIntent, 0);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        audio.setStreamVolume(AudioManager.STREAM_ALARM,progress,0);
        alarmVolum = audio.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        ringtoneStop();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ringtonePlay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        rAlarmUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        //alarm선택 액티비티에서 alarm을 선택안하고 액티비티를 종료 혹은 선택하고 종료
        //알람을 선택하고 종료하면 그대로
        //알람을 선택하지 않고 종료하면 rAlarm이 null(기존에 선택을 한번도 안했을경우)
        //defaultAlarm을 가진다.
        //rAlarm이 not null일경우 (기존에 선택을 한번이라도 했을 경우)
        //기존 alarm을 가진다.

        if(rAlarmUri != null){
            rAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);
            String alarmPath = rAlarm.getTitle(this);
            tvAlarmContent.setText(alarmPath);
        }else{
            if (rAlarm != null){
                String alarmPath = rAlarm.getTitle(this);
                tvAlarmContent.setText(alarmPath);
            }else{
                String alarmPath = defaultAlarm.getTitle(this);
                tvAlarmContent.setText(alarmPath);
            }
        }
    }

    public void ringtonePlay(){
        mPlay = MediaPlayer.create(this,rAlarmUri);
        mPlay.setVolume(alarmVolum,alarmVolum);


        if(rAlarm != null){

            rAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);
            String alarmPath = rAlarm.getTitle(this);

            mPlay.start();

        }else{
            rAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);
            String alarmPath = rAlarm.getTitle(this);

            mPlay.start();

        }
    }

    public void ringtoneStop(){
        if(mPlay != null){
            mPlay.stop();
        }
    }

}
