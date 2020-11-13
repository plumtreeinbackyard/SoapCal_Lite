package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;

public class OilProperties extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private TextView displayOilProperties;

    private ArrayList<String> oilItems = new ArrayList<>();
    private ArrayList<String> oilProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_properties);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_oil_properties);
        setSupportActionBar(toolbar);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btn_back_oil_properties);
        btnBack.setOnClickListener(this);

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        displayOilProperties = (TextView)findViewById(R.id.display_oil_properties);

        if (oilItems.size() == 0) {
            oilItems.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_name)));
        }
        if (oilProperties.size() == 0) {
            oilProperties.addAll(Arrays.asList(getResources().getStringArray(R.array.oil_properties)));
        }



        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = (AdView) findViewById(R.id.adView_oil_properties);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btn_back_oil_properties)) {
            onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner){
            int i = oilItems.indexOf(parent.getItemAtPosition(position).toString());
            displayOilProperties.setText(oilProperties.get(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
