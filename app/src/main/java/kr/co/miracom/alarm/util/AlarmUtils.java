package kr.co.miracom.alarm.util;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.miracom.alarm.common.Constants;
import kr.co.miracom.alarm.service.AlarmReceiver;
import kr.co.miracom.alarm.service.GpsReciever;
import kr.co.miracom.alarm.vo.ext.AlarmInfo;

/**
 * Created by Ch on 2016-05-13.
 */
public class AlarmUtils implements LocationListener {
    private final static int SIX_SECOND = 8 * 1000;
    private final static int FIVE_SECOND = 5 * 1000;
    private final static int FIVE_MINUTE = 5 * 60 * 1000;

    private static AlarmUtils _instance;

    private PendingIntent pendingIntent;

    public int alarmCountId = 0;
    private HashMap<String,Integer> timeMap;
    long intervalTime = 24 * 60 * 60 * 1000;// 24시간
    private AlarmManager alarmManager;

    //GPS 테스트
    //public LocationManager locManager;
    public Location myLocation = null;
    double latPoint = 0;
    double lngPoint = 0;

    public GpsReciever mGpsIntentReceiver;
    public LocationManager mLocationManger;
    ArrayList mPendingIntentList;

    public Context m_Activity;
    public boolean isGPSEnabled = false;
    public boolean isGPSInit = false;
    public boolean isWakeLock = false;
    private Intent saveIntent;

    public static AlarmUtils getInstance() {
        if (_instance == null) _instance = new AlarmUtils();
        return _instance;
    }

    public void gpsInit(Context context) {
        mLocationManger = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mPendingIntentList = new ArrayList();
        m_Activity = context;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // GPS로 부터 위치정보를 업데이트 요청, 1초마다 5km 이동시
        mLocationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5,  this);
        mLocationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, this);
        // LocationListener의 핸들을 얻음
        //AlarmUtils.getInstance().locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      //  enableGPSSetting();
        mGpsIntentReceiver = new GpsReciever(Constants.GPS_INENT_KEY);
        // mGpsIntentReceiver = new GpsIntentReciever(getApplicationContext(), GpsIntentReciever.class);
        m_Activity.getApplicationContext().registerReceiver(mGpsIntentReceiver, mGpsIntentReceiver.getFilter());
    }

    public void setAlarmIntent(Intent intent) {
        saveIntent = intent;
    }

    public Intent getAlarmIntent() {
        return saveIntent;
    }

    public void GetLocations(Intent intent) {
        if (myLocation != null) {
            isGPSEnabled = false;
            Log.d("location", "myLocation != null");
            //Toast.makeText(m_Activity, "myLocation != null GPS enabled  : 3개 지점 리스너 등록.", Toast.LENGTH_SHORT).show();

            latPoint = myLocation.getLatitude();
            lngPoint = myLocation.getLongitude();

            Log.e("Receiver GetLocations()", "latPoint: " + latPoint + ", lngPoint: " + lngPoint);
            //Toast.makeText(m_Activity, "latPoint: " + latPoint + ", lngPoint: " + lngPoint, Toast.LENGTH_SHORT).show();
           /* latTextView =(TextView) findViewById(R.id.lat);
            lngTextView =(TextView) findViewById(R.id.lng);

            latTextView.setText(String.valueOf(latPoint));
            lngTextView.setText(String.valueOf(lngPoint)  + " : " + getProvider());*/
            AlarmInfo alarm = (AlarmInfo)intent.getSerializableExtra("AlarmInfo");

            Double lat = Double.parseDouble(alarm.getLatitude());// intent.getDoubleExtra("latitude", 0.0);
            Double lng = Double.parseDouble(alarm.getLongitude());//intent.getDoubleExtra("longitude", 0.0);

            Log.e("Receiver GetLocations()", "latPoint2: " + lat + ", lngPoint2: " + lng);

            Toast.makeText(m_Activity, "latPoint: " + lat + ", lngPoint: " + lng, Toast.LENGTH_SHORT).show();

        //    locRegister(m_Activity, intent.getIntExtra("alartUniqId",0), 37.3680115, 127.1033245, 500, -1, intent); //정자
            locRegister(m_Activity, intent.getIntExtra(Constants.ALARM_ID, 0), lat, lng, 3000, -1, intent); //잠실1
       //    locRegister(m_Activity, intent.getIntExtra("alartUniqId",0), 37.5166423, 127.1010482, 100, -1, intent); //잠실2

        //    locRegister(m_Activity, intent.getIntExtra("alartUniqId",0), latPoint, lngPoint, 500, -1, intent);






            /*
            * Init에 넣어봄.
            * */
           /* mGpsIntentReceiver = new GpsReciever(Constants.GPS_INENT_KEY);
            // mGpsIntentReceiver = new GpsIntentReciever(getApplicationContext(), GpsIntentReciever.class);
            m_Activity.getApplicationContext().registerReceiver(mGpsIntentReceiver, mGpsIntentReceiver.getFilter());*/

        } else {
            Log.d("location", "myLocation == null");
        //    Toast.makeText(m_Activity, "myLocation == null GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLocationChanged(Location location) {

        // 위치정보가 업데이트 됨
        /*latTextView =(TextView) findViewById(R.id.lat);
        lngTextView =(TextView) findViewById(R.id.lng);
        latTextView.setText(String.valueOf(location.getLatitude()) + " : " + getProvider());
        lngTextView.setText(String.valueOf(location.getLongitude()));*/
     //   Toast.makeText(m_Activity, "onLocationChanged!!", Toast.LENGTH_SHORT).show();
        Log.d("location", "location changed");
       // Toast.makeText(m_Activity, "location changed!!", Toast.LENGTH_SHORT).show();
        myLocation = location;

        if (isGPSEnabled) {
            Log.d("location", "location isGPSEnabled");
            Toast.makeText(m_Activity, "location isGPSEnabled!!", Toast.LENGTH_SHORT).show();
            GetLocations(getAlarmIntent());
        }
    }

    public String getProvider () {
        // 최적의 위치제공자 조회
        Criteria criteria  = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String bestProvider = mLocationManger.getBestProvider(criteria, true);
        if (bestProvider == null) {
            bestProvider = "최적의 위치제공자를 찾을 수 없습니다.";
        } else {
            bestProvider = "Best provider=" + bestProvider;
        }

        return bestProvider;
    }

    public void locRegister(Context context, int id, double lat, double lng, float radius, long expiration, Intent intent) {
        Intent proximityIntent = new Intent(Constants.GPS_INENT_KEY);
        //Intent proximityIntent = new Intent(context, GpsIntentReciever.class);
        AlarmInfo alarm = (AlarmInfo)intent.getSerializableExtra("AlarmInfo");

        proximityIntent.putExtra(Constants.ALARM_ID, id);
        proximityIntent.putExtra("lat", lat);
        proximityIntent.putExtra("lng", lng);
       // proximityIntent.putExtra("alarmName", intent.getStringExtra("alarmName"));
        proximityIntent.putExtra("AlarmInfo",alarm);

        //Log.d(getClass().getName(), "OnCreate2 starGPStAlram startAlram : " + AlarmUtils.getInstance().alarmCountId);
        //PendingIntent pIntent = PendingIntent.getBroadcast(context, id, proximityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, proximityIntent, 0);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManger.addProximityAlert(lat, lng, radius, expiration, pendingIntent);
        mPendingIntentList.add(pendingIntent);
    }

    /*
    unregister
     */
    public void locUnRegister(Context context) {
        if (mPendingIntentList != null) {
           // Toast.makeText(context, "Del Gps List size: " + mPendingIntentList.size(), Toast.LENGTH_SHORT).show();
            Log.e("AlarmReceiver DelGpsLi", "onProviderEnabled : " + mPendingIntentList.size());
            for (int i = 0; i < mPendingIntentList.size(); i++) {
                PendingIntent curIntent = (PendingIntent) mPendingIntentList.get(i);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mLocationManger.removeProximityAlert(curIntent);
                mPendingIntentList.remove(i);
            }
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        // 프로바이더 상태가 변경됨
       // Toast.makeText(MainActivity.this, "onStatusChanged!!!!", Toast.LENGTH_SHORT).show();
        Log.d("Test", "test2");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
      //  Toast.makeText(m_Activity, "onProviderEnabled!!!!", Toast.LENGTH_SHORT).show();

        //myLocation = location;
        Log.e("AlarmReceiver connect", "onProviderEnabled ");
        //GetLocations(getAlarmIntent());
    //    isGPSEnabled = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
     //   Toast.makeText(m_Activity, "onProviderDisabled!!!!", Toast.LENGTH_SHORT).show();
        Log.e("AlarmReceiver connect", "onProviderDisabled ");
        isGPSEnabled = false;
        isGPSInit = false;

        locUnRegister(m_Activity);
        /*if (mGpsIntentReceiver != null) {
            m_Activity.unregisterReceiver(mGpsIntentReceiver);
            mGpsIntentReceiver = null;
        }*/
      //  Log.d("Test", "test4");
    }

    /*
    //일반 알람-----------------------------------------------------------------------------------------------------------------------
     */


    public void startInstantAlarm(Context context, Intent intent) {
        // AlarmManager 호출
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra(Constants.ALARM_ID, intent.getIntExtra(Constants.ALARM_ID, 0));
        //pendingIntent = PendingIntent.getBroadcast(context, intent.getIntExtra("alartUniqId", 0), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent = PendingIntent.getBroadcast(context, CommonUtils.getAlarmId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       // startAlram(context, pendingIntent, FIVE_SECOND);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + FIVE_SECOND, pendingIntent);
    }

    public void startAlarm(Context context, Intent intent, long triggerTime, int type) {
        // AlarmManager 호출
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /*
        * TODO Intent Setting.
        * */
        alarmIntent.putExtra(Constants.ALARM_ID, intent.getIntExtra(Constants.ALARM_ID, 0));

        pendingIntent =  PendingIntent.getBroadcast(context, intent.getIntExtra(Constants.ALARM_ID, 0), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // type 0 : 한번, 1 : repeat
        if (type == 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (type == 1) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pendingIntent);
        }
    }

    public void cancelAlram(Context context) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context, alarmCountId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // AlarmManager 호출
        //AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }
}