package kr.co.miracom.alarm.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.co.miracom.alarm.R;
import kr.co.miracom.alarm.adapter.SiteListAdapter;

/**
 * Created by admin on 2016-05-26.
 */
public class MapAddActivity extends FragmentActivity implements OnMapReadyCallback, PlaceSelectionListener {
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
        tvDetail.setText(place.getName()+" - " + place.getAddress());

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

        adapter = new SiteListAdapter(this);

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

        } catch(IOException ex) {
            Log.d(TAG, "예외 : " + ex.toString());
        }
    }

    private void moveMap(){
        mMap.clear();
        LatLng curPoint = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(curPoint).title(""));
    }

    private void save(){
        Intent resultIntent = new Intent();

        resultIntent.putExtra("address", address);
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);

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
}
