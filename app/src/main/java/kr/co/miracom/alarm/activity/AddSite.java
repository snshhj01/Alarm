package kr.co.miracom.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.co.miracom.alarm.R;

/**
 * Created by admin on 2016-05-26.
 */
public class AddSite extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_MAP = 1001;

    Button btnMapSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site);

        btnMapSetting = (Button) findViewById(R.id.btnMapSetting);
        btnMapSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddSiteMap.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MAP);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);

        if(requestCode == REQUEST_CODE_ADD_MAP){
            Toast toast = Toast.makeText(this, Data.getStringExtra("addr")+"||"+Data.getDoubleExtra("latitude", 0.0)+"||"+Data.getDoubleExtra("longitude",0.0), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
