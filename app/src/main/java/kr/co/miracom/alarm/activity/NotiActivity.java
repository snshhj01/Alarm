package kr.co.miracom.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by jiwoon-won on 2016-05-25.
 */
public class NotiActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView textView;
    Vibrator vib = null;
    MediaPlayer player = null;

    TextView alertMsgTextView;
    boolean progressSelectFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.noti_main);

        alertMsgTextView = (TextView) findViewById(R.id.alertTitle);
        seekBar = (SeekBar) findViewById(R.id.notiSeekBar);
        textView = (TextView) findViewById(R.id.progressPosition);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        //intent  받기.


        alertMsgTextView.setText(msg);

       // vib = startVibrate(this);
        AlarmInfo alarm = (AlarmInfo)intent.getSerializableExtra("AlarmInfo");
        startAlarm(2, null, alarm.getVolume());
        Log.d("test", "volume : " + alarm.getVolume());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                if (progress > 80 && progressSelectFlag == false) {
                    //    Toast.makeText(getApplicationContext(), "SeekBar End", Toast.LENGTH_SHORT).show();
                    progressSelectFlag = true;

                    //stopVibrate(vib);
                    stopAlarm();

                    // textView.setText("Right Cancel");

                    //    AlarmUtils.getInstance().locUnRegister(getApplicationContext());

                    finish();
                } else if (progress < 20 && progressSelectFlag == false) {
                    progressSelectFlag = true;

                    //stopVibrate(vib);
                    stopAlarm();
                    //  textView.setText("Left Cancel");

                    //AlarmUtils.getInstance().startInstantAlram(getApplicationContext(), getIntent());
                    finish();
                } else {
                    //  textView.setText("Not QUIT");
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                seekBar.setProgress(50);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        PushWakeLock.acquireCpuWakeLock(getApplicationContext(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        PushWakeLock.releaseCpuLock();
    }

    public void startAlarm(int alarmType, Uri uri, int volume) {
        if(alarmType == 2 || alarmType == 3) {
            startVibrate(this);
        }

        if(alarmType == 1 || alarmType == 3) {
            startPlayer(uri, volume);
        }

        //Timer timer = null;
        //1분뒤 알람 중지
      //  timer.schedule(new MyTimer(), 0, 60000);
    }

    public void stopAlarm() {
        if(vib != null) {
            stopVibrate();
            vib = null;
        }

        if(player != null) {
            stopPlayer();
            player = null;
        }
    }

    public void startVibrate(Context context) {
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 2000};
        vib.vibrate(pattern, 0);
    }

    public void stopVibrate() {
        vib.cancel();
    }

    public void  startPlayer(Uri uri, int volume) {
        player = new MediaPlayer();
        try {
            player.setDataSource(this, uri);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //set volume
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            try {
                player.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
        }
    }

    public void stopPlayer() {
        if(player.isPlaying()){
            player.stop();
            player.release();
        }
    }

    private class MyTimer extends TimerTask {
        public void run() {
            stopAlarm();
        }
    }
}
