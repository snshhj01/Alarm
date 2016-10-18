package kr.co.miracom.alarm.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.adapter.SiteListAdapter;
import kr.co.miracom.alarm.external.MyLocation;

/**
 * Created by admin on 2016-05-26.
 */
public class MapAddActivity extends FragmentActivity implements OnMapReadyCallback, PlaceSelectionListener, LocationListener {
    private static String TAG = "MapAddActivity";

    private GoogleMap mMap;
    EditText edit01;
    //TextView contentsText;
    Geocoder gc;
    //ListView listView;
    TextView tvDetail;
    SiteListAdapter adapter;
    Button btnSave;
    Button btnSearch;
    String address;
    double latitude;
    double longitude;
    double myLatitude;
    double myLongitude;

    private LocationManager locationManager;
    private String provider;
    private LatLng myLocation;
    double[] myGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site_map);

        Log.i(TAG, "onCreate");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gc = new Geocoder(this, Locale.KOREAN);

        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        /*
        edit01 = (EditText) findViewById(R.id.editCond);
        //contentsText = (TextView) findViewById(R.id.contentsText);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 사용자가 입력한 주소 정보 확인
                String searchStr = edit01.getText().toString();

                // 주소 정보를 이용해 위치 좌표 찾기 메소드 호출
                searchLocation(searchStr);
            }
        });
*/

        int googlePlayServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapAddActivity.this);
        if (googlePlayServiceResult != ConnectionResult.SUCCESS) { //구글 플레이 서비스를 활용하지 못할경우 <계정이 연결이 안되어 있는 경우
            //실패
            GooglePlayServicesUtil.getErrorDialog(googlePlayServiceResult, this, 0, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            }).show();
        } else { //구글 플레이가 활성화 된 경우
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);

            if (provider == null) {  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
                new AlertDialog.Builder(MapAddActivity.this)
                        .setTitle("위치서비스 동의")
                        .setNeutralButton("이동", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                        .show();
            } else {   //위치 정보 설정이 되어 있으면 현재위치를 받아옵니다
                locationManager.requestLocationUpdates(provider, 1, 1, MapAddActivity.this); //기본 위치 값 설정
                setUpMapIfNeeded(); //Map ReDraw
            }

            setMyLocation(); //내위치 정하는 함수
        }


        tvDetail = (TextView) findViewById(R.id.tvDetail);
        /*listView = (ListView) findViewById(R.id.listView);*/

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "onPlaceSelected");
        Log.i(TAG, "Place Selected: " + place.getName());

        searchLocation("" + place.getAddress());
        tvDetail.setText(place.getName() + " - " + place.getAddress());

/*
        place.getName(), place.getId()
        place.getAddress()
        place.getPhoneNumber()
        place.getWebsiteUri()
                */
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 주소를 이용해 위치 좌표를 찾는 메소드 정의
     */
    private void searchLocation(String searchStr) {
        Log.i(TAG, "searchLocation");
        // 결과값이 들어갈 리스트 선언
        List<Address> addressList = null;

        adapter = new SiteListAdapter();

        try {
            addressList = gc.getFromLocationName(searchStr, 3);

            if (addressList != null) {

                for (int i = 0; i < addressList.size(); i++) {
                    Address addr = addressList.get(i);
                    int addrCount = addr.getMaxAddressLineIndex() + 1;
                    StringBuffer buf = new StringBuffer();
                    for (int k = 0; k < addrCount; k++) {
                        buf.append(addr.getAddressLine(k));
                    }

                    address = buf.toString();
                    latitude = addr.getLatitude();
                    longitude = addr.getLongitude();

                    moveMap();

                    //adapter.setItem(new SiteItem(buf.toString(), ""+addr.getLatitude(), ""+addr.getLongitude()));
                }


                /*
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SiteItem curItem = (SiteItem) adapter.getItem(position);
                        String[] curData = curItem.getData();

                        Toast.makeText(getApplicationContext(), curData[0] + "||" + curData[1] + "||" + curData[2], Toast.LENGTH_LONG).show();

                        addr = curData[0];
                        latitude = Double.valueOf(curData[1]);
                        longitude = Double.valueOf(curData[2]);

                        moveMap();
                    }
                });
                */
            }

        } catch (IOException ex) {
            Log.d(TAG, "예외 : " + ex.toString());
        }
    }

    private void moveMap() {
        mMap.clear();
        LatLng curPoint = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(curPoint).title(""));
    }

    private void save() {
        Intent resultIntent = new Intent();

        if(latitude==0 && longitude==0){
            address = "도착지 하드코딩(올림픽공원)";
            latitude = Double.valueOf("37.520657");  //올림픽공원
            longitude = Double.valueOf("127.121423");
        }

        resultIntent.putExtra("address", address);
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);

        // 에뮬레이터 에러 방지. 테스트용 소스. 삭제요망!!!!!!!!!!!!!!!!!!
        if(myLatitude==0 && myLongitude==0){
            address = "출발지 하드코딩(봉은중학교) " + address;
            myLatitude = Double.valueOf("37.518889"); //봉은중학교
            myLongitude = Double.valueOf("127.062387");
        }

        resultIntent.putExtra("myLatitude", myLatitude);
        resultIntent.putExtra("myLongitude", myLongitude);

        // 응답을 전달하고 이 액티비티를 종료합니다.
            setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        LatLng curPoint = new LatLng(37.5200705, 127.0995);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 10));
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
    }

    private void setMyLocation(){
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }
    Marker mMarker;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d(TAG + " onMyLocationChange()","location.getLatitude(), location.getLongitude() -> " + location.getLatitude() +","+ location.getLongitude());
//            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

//            mMarker = mMap.addMarker(new MarkerOptions().position(loc));
//            if(mMap != null){
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//            }
        }
    };

    boolean locationTag=true;

    @Override
    public void onLocationChanged(Location location) {
        if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
            Log.d(TAG  , "onLocationChanged: !!");
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

            Toast.makeText(MapAddActivity.this, "위도  : " + myLatitude +  " 경도: "  + myLongitude ,  Toast.LENGTH_LONG).show();
            locationTag=false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG,"onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG,"onStatusChanged");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"onProviderEnabled");
    }
}
