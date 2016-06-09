package kr.co.miracom.alarm.dialog;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.util.Logger;

/**
 * @author kws
 * @date 2016-06-03
 * @since 0.1
 */
public class AlarmSoundSelectDialog extends DialogFragment implements AdapterView.OnItemClickListener{

    private SimpleAdapter internalSimpleAdapter;
    private SimpleAdapter userSimpleAdapter;
    private Context context;
    private View rootView;
    private TabHost host;
    private ArrayList<HashMap<String, String>> internalAlarmSoundList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> userAlarmSoundList = new ArrayList<HashMap<String, String>>();
    public SoundSelectFinish mSoundSelectListner = null;
    private ListView internalListView;
    private ListView userListView;

    public AlarmSoundSelectDialog(Context context) {
        this.context = context;
    }

    public interface SoundSelectFinish {
        void onSelect(HashMap<String,String> selectedData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.select_alarm_sound, container, false);
        initTabHost();
        initInternalList();
        initUserSoundList();
        return rootView;
    }

    private void initUserSoundList() {
        getUserAlarmSoundList();
        userListView = (ListView) rootView.findViewById(R.id.user_song_list);
        userSimpleAdapter = new SimpleAdapter(context, userAlarmSoundList, R.layout.sound_item_list, new String[]{Constants.TITLE, Constants.PATH}, new int[]{R.id.title, R.id.uri});
        userListView.setOnItemClickListener(this);
        userListView.setAdapter(userSimpleAdapter);
    }

    private void getUserAlarmSoundList() {
        if (userAlarmSoundList != null) {
            userAlarmSoundList.clear();
        }
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;
        if (cur != null) {
            count = cur.getCount();
            if (count > 0) {
                while (cur.moveToNext()) {
                    String title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String path = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Logger.d(this.getClass(), "%s,%s", "User song data : " + path,"Title : " + title);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constants.TITLE, title);
                    map.put(Constants.PATH, path);
                    userAlarmSoundList.add(map);
                }
            }
        }
        cur.close();
    }

    private void initInternalList() {
        internalListView = (ListView) rootView.findViewById(R.id.internal_list);
        getInternalAlarmSoundList();
        internalSimpleAdapter = new SimpleAdapter(context, internalAlarmSoundList, R.layout.sound_item_list, new String[]{Constants.TITLE, Constants.PATH}, new int[]{R.id.title, R.id.uri});
        internalListView.setOnItemClickListener(this);
        internalListView.setAdapter(internalSimpleAdapter);
    }

    private void getInternalAlarmSoundList() {
        if (internalAlarmSoundList != null) {
            internalAlarmSoundList.clear();
        }
        Logger.d(this.getClass(), "%s", "Loading list..");
        RingtoneManager ringtoneMgr = new RingtoneManager(context);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        if (alarmsCursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                String title = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(context);
                String path = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
                Logger.d(this.getClass(), "%s", "Titile : " + title);
                Logger.d(this.getClass(), "%s", "Uri : " + path);
                map.put(Constants.TITLE, title);
                map.put(Constants.PATH, path);
                internalAlarmSoundList.add(map);
            } while (alarmsCursor.moveToNext());
        }
        alarmsCursor.close();
    }

    private void initTabHost() {
        host = (TabHost) rootView.findViewById(R.id.host);
        host.setup();
        //기본 내장된 알람음 Tab
        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator("기본 알람음");
        spec.setContent(R.id.internal_list);
        host.addTab(spec);

        //사용자 음악 Tab
        spec = host.newTabSpec("tab2");
        spec.setIndicator("노래");
        spec.setContent(R.id.user_song_list);
        host.addTab(spec);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == internalListView.getId()) {
            String title =  internalAlarmSoundList.get(position).get(Constants.TITLE);
            Toast.makeText(context,"Title : "+ title,Toast.LENGTH_SHORT).show();
            if(mSoundSelectListner!=null) mSoundSelectListner.onSelect(internalAlarmSoundList.get(position));
            dismiss();
        } else {
            String title =  userAlarmSoundList.get(position).get(Constants.TITLE);
            Toast.makeText(context,"Title : "+ title,Toast.LENGTH_SHORT).show();
            if(mSoundSelectListner!=null) mSoundSelectListner.onSelect(userAlarmSoundList.get(position));
            dismiss();
        }
    }
}
