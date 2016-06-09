package kr.co.miracom.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.dialog.AlarmSoundSelectDialog;
import kr.co.miracom.alarm.service.AlarmReceiver;
import kr.co.miracom.alarm.util.CommonUtils;
import kr.co.miracom.alarm.util.DBHelper;
import kr.co.miracom.alarm.util.Logger;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by kimsungmog on 2016-05-26.
 */
public class AlarmAddActivity extends AppCompatActivity{
    //Layout variable
    private Button okBtn;
    private Button cancelBtn;
    private EditText alarmName;
    private ToggleButton tgBtnSun, thBtnMon, thBtnThe, tgBtnWed, tgBthThur, tgBtnFri, thBtnSat, thBtnSun;
    private TimePicker timePicker;
    //private CheckBox repeatCheckBox;
    private RadioGroup alramTypeGroup;
    private LinearLayout alramSoundSelector;
    private SeekBar volSeekBar;
    private Switch repeatSwich;
    private TextView alramSoundName;

    //User define variable
    private DBHelper mDbHelper;
    private boolean[] weekRepeatInfo;
    private boolean isRepeat = false;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private boolean isModify;
    private int alartUniqId;
    private int _id;

    private HashMap<String,Integer> timeMap;
    private HashMap<String,String> soundMap;
    private HashMap<String,Integer> snoozeMap;

    private int volume = 50;
    private int alarmType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_simple_alarm);
        mDbHelper = new DBHelper(this);
        mDbHelper.open();
        //intent에서 넘겨준 AlramId를 가져옴
        Intent intent = getIntent();
        initLayout();
        if(intent.getIntExtra(Constants.ALARM_ID,0) != 0) {
            isModify = true;
            _id = intent.getIntExtra(Constants.ALARM_ID,0);
            AlarmInfo alarm = mDbHelper.selectAlarm(_id);
            //수정 시 기존 알람 정보를 세팅해 줌.
            setExistAlarmInfo(alarm);
        } else {
            alartUniqId = CommonUtils.getAlarmId();
        }
    }

    /**
     *알람 수정시 기존 알람의 정보를 Layout에 설정 해줌
     * @param alarm
     */
    private void setExistAlarmInfo(AlarmInfo alarm) {
        alarmName.setText(alarm.getAlarmName());
        timePicker.setCurrentHour(alarm.getTime().get(Constants.TIME_HOUR));
        timePicker.setCurrentMinute(alarm.getTime().get(Constants.TIME_MINUTE));
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
        //반복요일 토글버튼
        tgBtnSun = (ToggleButton) findViewById(R.id.toggleBtnSunday);
        thBtnMon = (ToggleButton) findViewById(R.id.toggleBtnMonday);
        thBtnThe = (ToggleButton) findViewById(R.id.toggleBtnTuesday);
        tgBtnWed = (ToggleButton) findViewById(R.id.toggleBtnWednesday);
        tgBthThur = (ToggleButton) findViewById(R.id.toggleBtnThursday);
        tgBtnFri = (ToggleButton) findViewById(R.id.toggleBtnFriday);
        thBtnSat = (ToggleButton) findViewById(R.id.toggleBtnSaturday);

        //매주 반복 체크박스(
        //repeatCheckBox = (CheckBox) findViewById(R.id.repeatCheckBox);

        //알람 사운드 선택 버튼 및 제목표시
        alramSoundSelector = (LinearLayout) findViewById(R.id.addAlramContentBtn);
        alramSoundName = (TextView) findViewById(R.id.alarmSoundContent);
        //볼륨조절Bar
        volSeekBar = (SeekBar) findViewById(R.id.seekBarVolumeBar);
        //반복설정 스위치
        repeatSwich = (Switch) findViewById(R.id.switchRepeatSetting);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatSwich.isChecked()) {
                    //실제 Dialog에서 세팅해주나 샘플로 데이터 넣음
                    //5분간 3회
                    snoozeMap = new HashMap<String,Integer>();
                    snoozeMap.put(Constants.INTERVAL, 5);
                    snoozeMap.put(Constants.COUNT, 3);
                }
                setAlarmType();
                registerAlram();
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alramSoundSelector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlarmSoundSelectDialog soundSelectDialog = new AlarmSoundSelectDialog(AlarmAddActivity.this);
                soundSelectDialog.mSoundSelectListner = new AlarmSoundSelectDialog.SoundSelectFinish(){
                    @Override
                    public void onSelect(HashMap<String, String> selectedSound) {
                        alramSoundName.setText(selectedSound.get("title"));
                        soundMap = new HashMap<String,String>();
                        soundMap.put(Constants.TITLE, selectedSound.get(Constants.TITLE));
                        soundMap.put(Constants.PATH, selectedSound.get(Constants.PATH));
                    }
                };
                soundSelectDialog.show(getFragmentManager(), "Select sound");
            }
        });

        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
                Logger.d(this.getClass(), "%d", volume);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
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
        long intervalTime = 24 * 60 * 60 * 1000;// 24시간
        if (isRepeat) {
            Logger.d(this.getClass(), "%s", "Is repeat alarm!");
            intent.putExtra("one_time", false);
            intent.putExtra("day_of_week", weekRepeatInfo);
            pendingIntent = getPendingIntent(intent);
            triggerTime = setTriggerTime();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pendingIntent);
        } else {
            intent.putExtra("one_time", true);
            intent.putExtra("day_of_week", weekRepeatInfo);
            pendingIntent = getPendingIntent(intent);
            triggerTime = setTriggerTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
        alarmInfo.setAlarmName(alarmName.getText().toString());
        alarmInfo.setAlarmId(alartUniqId);
        alarmInfo.setActive(Constants.ALARM_ACTIVE);
        alarmInfo.setTime(timeMap);
        alarmInfo.setDays(days);
        alarmInfo.setAlarmType(alarmType);
        alarmInfo.setAlarmSound(soundMap);
        alarmInfo.setVolume(volume);
        alarmInfo.setSnooze(snoozeMap);
        saveAlarmInfo(alarmInfo);
        Intent returnIntent = new Intent(AlarmAddActivity.this, AlarmListActivity.class);
        returnIntent.putExtra(Constants.PAGER, 0);
        startActivity(returnIntent);
    }

    /**
     * 알람 정보 추가
     * @param alarmInfo
     */
    private void saveAlarmInfo(AlarmInfo alarmInfo) {
        mDbHelper.insertAlarmInfo(alarmInfo);
    }

    /**
     * 선택 된 알람을 알람을 취소 함
     */
    private void cancelExistAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alartUniqId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        pIntent.cancel();
    }

    /**
     * 알람 매니저에 등록 함 PendingIntent 생성
     * @param intent
     * @return
     */
    private PendingIntent getPendingIntent(Intent intent) {
        PendingIntent pIntent = PendingIntent.getBroadcast(this, alartUniqId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    /**
     * Trigger 시간을 설정 함
     * @return
     */
    private long setTriggerTime() {
        timeMap = new HashMap<String,Integer>();
        timeMap.put(Constants.TIME_HOUR,this.timePicker.getCurrentHour());
        timeMap.put(Constants.TIME_MINUTE,this.timePicker.getCurrentMinute());
        // current Time
        long currentTime = System.currentTimeMillis();
        // timepicker
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, this.timePicker.getCurrentHour());
        curTime.set(Calendar.MINUTE, this.timePicker.getCurrentMinute());
        curTime.set(Calendar.SECOND, 0);
        curTime.set(Calendar.MILLISECOND, 0);
        long settingTime = curTime.getTimeInMillis();
        long triggerTime = settingTime;
        if (currentTime > settingTime)
            triggerTime += 1000 * 60 * 60 * 24;
        return triggerTime;
    }

}
