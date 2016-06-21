package kr.co.miracom.alarm.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by kimsungmog on 2016-06-03.
 */
public class Player{

    Context mContext;
    MediaPlayer mediaPlayer;
    AudioManager mAudioManager;
    Uri mUri;

    public Player(Context context){
        this.mContext = context;
        init();
    }

    public void init(){
        mediaPlayer = new MediaPlayer();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void setUri(Uri uri){
        this.mUri = uri;
    }

    public void setMediaPlayerMode(){
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(mContext.getApplicationContext(),mUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void play(){
        mediaPlayer.start();
    }

    public void stop(){
        mediaPlayer.stop();
    }

}
