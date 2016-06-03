package kr.co.miracom.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
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
import kr.co.miracom.alarm.util.Player;
import kr.co.miracom.alarm.vo.SimpleVo;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private AudioManager audioManager;
    //private MediaPlayer mPlay;
    private AlarmSql sql;

    private Player mPlayer;

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

    private Ringtone mRingtone;
    private Uri mUri;
    private SimpleVo simpleVo;

    private int alarmVolum;

    int alarmId = 999999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_simple_alarm);

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

        //자신을 실행시킨 intent객체 획득
        Intent intent = getIntent();
        //넘어온 데이터 획득
        alarmId = intent.getIntExtra("id",999999);

        Logger.v(this.getClass(),"%s",intent.getIntExtra("id",99999));

        sql = new AlarmSql(this);
        mPlayer = new Player(this);

        //audiomanager 초기화
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        simpleVo = new SimpleVo();
        // 초기 시스템(alarm) volum을 가져와서 seekbar에 setprogress한다.

        if(alarmId == 999999){
            alarmVolum = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            seekBarVolumeBar.setProgress(alarmVolum);

            //초기 default alarm path를 TextView 에 setting
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
            tvAlarmContent.setText(mRingtone.getTitle(this));

            mPlayer.setUri(mUri);

        }else{
            Cursor cursor = sql.selectSimpleAlarm(alarmId);

            while (cursor.moveToNext()){
                simpleVo.setId(cursor.getInt(0));
                simpleVo.setDate(cursor.getString(1));
                simpleVo.setDays(cursor.getString(2));
                simpleVo.setCycle(cursor.getInt(3));
                simpleVo.setType(cursor.getString(4));
                simpleVo.setUri(cursor.getString(5));
                simpleVo.setSound(cursor.getString(6));
                simpleVo.setVolum(cursor.getInt(7));
                simpleVo.setUsed(cursor.getInt(8));
                simpleVo.setRepeat(cursor.getInt(9));
            }

        }
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

        mUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
        tvAlarmContent.setText(mRingtone.getTitle(this));

        mPlayer.setUri(mUri);
    }

    public void ringtonePlay(){
        mPlayer.setMediaPlayerMode();
        mPlayer.play();
    }

    public void ringtoneStop(){
        mPlayer.stop();
    }

}
