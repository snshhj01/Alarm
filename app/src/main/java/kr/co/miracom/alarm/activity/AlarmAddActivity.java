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

import java.io.IOException;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.sql.AlarmSql;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.vo.SimpleVo;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private AudioManager audioManager;
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
    private Uri rAlarmUri;

    private int alarmVolum;

    int alarmId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_simple_alarm);

        //자신을 실행시킨 intent객체 획득
        Intent intent = getIntent();
        //넘어온 데이터 획득
        //alarmId = intent.getIntExtra("id",0);

        sql = new AlarmSql(this);

        mPlay = new MediaPlayer();


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        rAlarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        rAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);


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
        alarmVolum = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        seekBarVolumeBar.setProgress(alarmVolum);

        //초기 default alarm path를 TextView 에 setting
        String alarmPath = rAlarm.getTitle(this);
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
            ringtoneStop();
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
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,progress,0);

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
        rAlarm = RingtoneManager.getRingtone(getApplicationContext(), rAlarmUri);
        String alarmPath = rAlarm.getTitle(this);
        tvAlarmContent.setText(alarmPath);
    }

    public void ringtonePlay(){
        alarmVolum = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        Logger.v(this.getClass(),"%s","--------------------------");
        Logger.v(this.getClass(),"%s",alarmVolum);

        mPlay = new MediaPlayer();
        try {
            mPlay.setDataSource(getApplicationContext(),rAlarmUri);
            mPlay.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlay.setLooping(true);
            mPlay.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlay.start();

    }

    public void ringtoneStop(){
        //rAlarm.stop();
        if(mPlay != null){
            mPlay.stop();
        }
    }

}
