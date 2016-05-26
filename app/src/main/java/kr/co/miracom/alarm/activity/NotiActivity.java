package kr.co.miracom.alarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import kr.co.miracom.alarm.R;

/**
 * Created by jiwoon-won on 2016-05-25.
 */
public class NotiActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_main);

        seekBar = (SeekBar) findViewById(R.id.notiSeekBar);
        textView = (TextView) findViewById(R.id.progressPosition);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                if (progress > 80) {
                    //    Toast.makeText(getApplicationContext(), "SeekBar End", Toast.LENGTH_SHORT).show();
                    textView.setText("Right Cancel");
                } else if (progress < 20) {
                    textView.setText("Left Cancel");
                } else {
                    textView.setText("Not QUIT");
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                seekBar.setProgress(50);
            }
        });
    }


}
