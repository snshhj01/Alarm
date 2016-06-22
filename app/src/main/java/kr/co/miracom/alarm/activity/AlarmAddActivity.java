package kr.co.miracom.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.service.AlarmReceiver;
import kr.co.miracom.alarm.util.AlarmUtils;
import kr.co.miracom.alarm.util.CommonUtils;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.util.Player;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String COLUMN_ID = "_id";

    //Layout variable
    private Button okBtn;
    private Button cancelBtn;
    private EditText alarmName;
    private ToggleButton tgBtnSun, thBtnMon, thBtnThe, tgBtnWed, tgBthThur, tgBtnFri, thBtnSat;
    private TimePicker timePicker;
    //private CheckBox repeatCheckBox;
    private RadioGroup alramTypeGroup;
    private LinearLayout alramSoundSelector;
    private SeekBar volSeekBar;
    private ImageView speakerImage;
    private TextView alramSoundName;


    //User define variable
    private DBHelper mDbHelper;
    private HashMap<String,Integer> timeMap;

    private boolean[] weekRepeatInfo;
    private boolean isRepeat = false;
    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;
    private boolean isModify;
    private int alarmId;
    private int _id;
    private int volume;
    private int alarmType = 1;

    private AudioManager audioManager;
    private Ringtone mRingtone;
    private Intent ringtoneIntent;
    private Uri mUri;
    private Player mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_simple_alarm);
        mDbHelper = new DBHelper(this);
        mDbHelper.open();

        mPlayer = new Player(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //intent에서 넘겨준 AlramId를 가져옴
        Intent intent = getIntent();
        initLayout();
        if(intent.getIntExtra(Constants.ALARM_ID,0) != 0) {
            isModify = true;
            _id = intent.getIntExtra(Constants.ALARM_ID,0);
            AlarmInfo alarm = mDbHelper.selectAlarm(_id, COLUMN_ID);
            //수정 시 기존 알람 정보를 세팅해 줌.
            setExistAlarmInfo(alarm);
        } else {
            alarmId = CommonUtils.getAlarmId();
            volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            volSeekBar.setProgress(volume);

            //초기 default alarm path를 TextView 에 setting
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
            alramSoundName.setText(mRingtone.getTitle(this));

            mPlayer.setUri(mUri);
        }
    }

    /**
     *알람 수정시 기존 알람의 정보를 Layout에 설정 해줌
     * @param alarm
     */
    private void setExistAlarmInfo(AlarmInfo alarm) {
        alarmId = alarm.getAlarmId();
        if(alarm.getAlarmName() != null){
            alarmName.setText(alarm.getAlarmName());
        }
        timePicker.setCurrentHour(alarm.getTime().get(Constants.TIME_HOUR));
        timePicker.setCurrentMinute(alarm.getTime().get(Constants.TIME_MINUTE));
        ArrayList<Integer> days = alarm.getDays();
        ToggleButton [] toogleButtons = new ToggleButton[]{tgBtnSun, thBtnMon, thBtnThe, tgBtnWed, tgBthThur, tgBtnFri, thBtnSat};
        for(Integer inx : days) {
            toogleButtons[inx-1].setChecked(true);
        }
        if(alarm.getAlarmType() == Constants.ALARM_TYPE_SOUND) {
            alramTypeGroup.check(R.id.radioBtnSound);
        } else if (alarm.getAlarmType() == Constants.ALARM_TYPE_VIBRATE) {
            alramTypeGroup.check(R.id.radioBtnVibrate);
        } else if (alarm.getAlarmType() == Constants.ALARM_TYPE_SOUND_VIBRATE) {
            alramTypeGroup.check(R.id.radioBtnSoundVibrate);
        }

        //mUri = alarm.getSoundUri();
        mUri = Uri.parse(alarm.getSoundUri());
        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
        alramSoundName.setText(mRingtone.getTitle(this));

        mPlayer.setUri(mUri);

        // seekbar progress = volum
        //audiomanager volum = volum
        volume = alarm.getVolume();
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,volume,0);
        volSeekBar.setProgress(volume);
    }

    /**
     * Layout 설정 및 데이터 초기화
     */
    private void initLayout() {
        //저장 / 취소
        okBtn = (Button) findViewById(R.id.btnSettingsOk);
        cancelBtn = (Button) findViewById(R.id.btnSettingsCancel);
        //알람이름
        alarmName = (EditText) findViewById(R.id.alarm_name);
        //시간설정
        timePicker = (TimePicker) findViewById(R.id.timePickerAddAlarm);
        timePicker.setIs24HourView(true);

        //알람 타입 선택(사운드,진동,사운드&진동)
        alramTypeGroup = (RadioGroup) findViewById(R.id.radioGroupAdd);
        alramTypeGroup.setOnClickListener(this);
        //반복요일 토글버튼
        tgBtnSun = (ToggleButton) findViewById(R.id.toggleBtnSunday);
        tgBtnSun.setOnClickListener(this);
        thBtnMon = (ToggleButton) findViewById(R.id.toggleBtnMonday);
        thBtnMon.setOnClickListener(this);
        thBtnThe = (ToggleButton) findViewById(R.id.toggleBtnTuesday);
        thBtnThe.setOnClickListener(this);
        tgBtnWed = (ToggleButton) findViewById(R.id.toggleBtnWednesday);
        tgBtnWed.setOnClickListener(this);
        tgBthThur = (ToggleButton) findViewById(R.id.toggleBtnThursday);
        tgBthThur.setOnClickListener(this);
        tgBtnFri = (ToggleButton) findViewById(R.id.toggleBtnFriday);
        tgBtnFri.setOnClickListener(this);
        thBtnSat = (ToggleButton) findViewById(R.id.toggleBtnSaturday);
        thBtnSat.setOnClickListener(this);

        //매주 반복 체크박스(
        //repeatCheckBox = (CheckBox) findViewById(R.id.repeatCheckBox);

        //알람 사운드 선택 버튼 및 제목표시
        alramSoundSelector = (LinearLayout) findViewById(R.id.addAlramContentBtn);
        alramSoundSelector.setOnClickListener(this);
        alramSoundName = (TextView) findViewById(R.id.alarmSoundContent);
        alramSoundName.setOnClickListener(this);
        //볼륨조절Bar
        volSeekBar = (SeekBar) findViewById(R.id.seekBarVolumeBar);
        speakerImage = (ImageView) findViewById(R.id.speakerImg);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeMap = new HashMap<String,Integer>();
                timeMap.put(Constants.TIME_HOUR, timePicker.getCurrentHour());
                timeMap.put(Constants.TIME_MINUTE, timePicker.getCurrentMinute());
                setAlarmType();
                registerAlram();
                ringtoneStop();
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtoneStop();
                Intent returnIntent = new Intent(AlarmAddActivity.this, AlarmListActivity.class);
                returnIntent.putExtra(Constants.PAGER, 0);
                startActivity(returnIntent);
                finish();
            }
        });
        alramSoundSelector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alarmSelectDialog();
            }
        });

        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,progress,0);
                volume = progress;
                if(progress ==0)
                    speakerImage.setImageResource(R.drawable.speaker_off);
                else
                    speakerImage.setImageResource(R.drawable.speaker_on_blue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ringtoneStop();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ringtonePlay();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ringtoneStop();
        Intent returnIntent = new Intent(AlarmAddActivity.this, AlarmListActivity.class);
        returnIntent.putExtra(Constants.PAGER, 0);
        startActivity(returnIntent);
        finish();
    }


    private void alarmSelectDialog(){
        ringtoneStop();
        ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

        ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select RingTone");
        ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);

        startActivityForResult(ringtoneIntent, 99);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Ringtone manager activtity
        if(requestCode == 99){
            mUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
            alramSoundName.setText(mRingtone.getTitle(this));
            mPlayer.setUri(mUri);
        }
    }

    @Override
    public void onClick(View v) {
        ringtoneStop();
    }

    /**
     * Radio버튼 항목에 따라 알람 타입 설정
     */
    private void setAlarmType() {
        int id = alramTypeGroup.getCheckedRadioButtonId();
        if(id == R.id.radioBtnSound) {
            alarmType = Constants.ALARM_TYPE_SOUND;
        } else if (id == R.id.radioBtnVibrate) {
            alarmType = Constants.ALARM_TYPE_VIBRATE;
        } else if (id == R.id.radioBtnSoundVibrate) {
            alarmType = Constants.ALARM_TYPE_SOUND_VIBRATE;
        }
    }

    /**
     * 새로운 알람에 대해서 Service 등록 및 DB 추가를 위한 데이터 세팅작업..
     */
    private void registerAlram() {
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //기존에 있는 알람이 있으면 취소하고 다시 등록함.
        if(isModify) {
            cancelExistAlarm();
        }
        weekRepeatInfo = new boolean[]{false, tgBtnSun.isChecked(), thBtnMon.isChecked(), thBtnThe.isChecked(), tgBtnWed.isChecked(), tgBthThur.isChecked(), tgBtnFri.isChecked(), thBtnSat.isChecked()};
        ArrayList<Integer> days = new ArrayList<Integer>();
        for(int i=1; i<weekRepeatInfo.length;i++) {
            if(weekRepeatInfo[i]) {
                days.add(i);
                isRepeat = true;
            }
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        long triggerTime = 0;
        triggerTime = CommonUtils.setTriggerTime(timeMap.get(Constants.TIME_HOUR), timeMap.get(Constants.TIME_HOUR));
        if (isRepeat) {
            Logger.d(this.getClass(), "%s", "Is repeat alarm!");
            intent.putExtra(Constants.ALARM_ID, alarmId);
            //pendingIntent = getPendingIntent(intent);
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pendingIntent);
            AlarmUtils.getInstance().startAlarm(getApplicationContext(), intent, triggerTime, 1);
        } else {
            intent.putExtra(Constants.ALARM_ID, alarmId);
            //pendingIntent = getPendingIntent(intent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            AlarmUtils.getInstance().startAlarm(getApplicationContext(), intent, triggerTime, 0);
        }
        if(isModify)
            alarmInfo.set_id(_id);
        alarmInfo.setAlarmName(alarmName.getText().toString());
        alarmInfo.setAlarmId(alarmId);
        alarmInfo.setActive(Constants.ALARM_ACTIVE);
        alarmInfo.setTime(timeMap);
        alarmInfo.setDays(days);
        alarmInfo.setAlarmType(alarmType);
        alarmInfo.setSoundUri(mUri.toString());
        alarmInfo.setVolume(volume);
        saveAlarmInfo(alarmInfo);
    }

    /**
     * 알람 정보 추가
     * @param alarmInfo
     */
    private void saveAlarmInfo(AlarmInfo alarmInfo) {
        if(!isModify)
            mDbHelper.insertAlarmInfo(alarmInfo);
        else
            mDbHelper.updateAlarm(alarmInfo);
    }

    /**
     * 선택 된 알람을 알람을 취소 함
     */
    private void cancelExistAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        pIntent.cancel();
    }

    /**
     * 알람 매니저에 등록 함 PendingIntent 생성
     * @param intent
     * @return
     */
    private PendingIntent getPendingIntent(Intent intent) {
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    public void ringtonePlay(){
        mPlayer.setMediaPlayerMode();
        mPlayer.play();
    }

    public void ringtoneStop(){
        if(mPlayer != null){
            mPlayer.stop();
        }
    }


}
