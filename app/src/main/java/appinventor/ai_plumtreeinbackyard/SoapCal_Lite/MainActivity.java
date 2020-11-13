package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences preferences;
    public static String method;
    public static String unitSettings;

    public static ArrayList<String> fileName = new ArrayList<>();

    private ListView recipeList;
    private ArrayList<File> savedFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recipeList = (ListView)findViewById(R.id.recipe_list);

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                StringBuilder sb = new StringBuilder();
                try {
                    FileInputStream input = MainActivity.this.openFileInput(fileName.get(position) + ".txt");
                    byte[] temp = new byte[1024];
                    int len;
                    //读取文件内容:
                    while ((len = input.read(temp)) > 0) {
                        sb.append(new String(temp, 0, len));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, DisplaySingleRecipe.class);
                intent.putExtra("recipe_title", fileName.get(position));
                intent.putExtra("recipe", sb.toString());
                startActivity(intent);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        method = preferences.getString("Method", "By weight");
        unitSettings = preferences.getString("Unit", "g");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method.equals("By weight")){
                    Intent intent = new Intent(MainActivity.this, CreateByWeight.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, CreateByPercentage.class);
                    startActivity(intent);
                }
            }
        });

        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = findViewById(R.id.adView_main);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        showListView();
    }

    public void showListView() {
        File dir = new File(MainActivity.this.getFilesDir().getPath());
        final File[] listFile = dir.listFiles();
        if (listFile.length == 0) {
            AlertDialog();
        } else {
            savedFiles.clear();
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().endsWith(".txt")) {
                    savedFiles.add(listFile[i]);
                }
            }
            if (savedFiles.size() == 0) {
                AlertDialog();
            } else {
                fileName.clear();
                for (int i = 0; i < savedFiles.size(); i++) {
                    String[] name = savedFiles.get(i).getName().split("\\.");
                    fileName.add(name[0]);
                }
                Collections.sort(fileName, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, fileName);
                recipeList.setAdapter(adapter);
            }
        }
    }

    public void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.no_recipe_has_been_saved_yet) + "\n" + getResources().getString(R.string.start_creating_a_recipe));
        builder.setCancelable(true);

        builder.setPositiveButton(
                getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (method == null || method.equals("By weight")) {
                            Intent intent = new Intent(MainActivity.this, CreateByWeight.class);
                            startActivity(intent);
                        } else if (method.equals("By percentage")) {
                            Intent intent = new Intent(MainActivity.this, CreateByPercentage.class);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveData() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Method", method);
        editor.putString("Unit", unitSettings);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            saveData();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            String version = "not available"; // initialize String
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.version) + " " + version + "\n\n"
                    + getResources().getString(R.string.more_functions));
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        } else if (id == R.id.privacy) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://interestingones.web.app/privacy.html"));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.create_by_weight) {
            method = "By weight";
            saveData();
            Intent intent = new Intent(MainActivity.this, CreateByWeight.class);
            startActivity(intent);
        }
        else if (id == R.id.create_by_percentage) {
            method = "By percentage";
            saveData();
            Intent intent = new Intent(MainActivity.this, CreateByPercentage.class);
            startActivity(intent);
        }
        else if (id == R.id.oil_properties) {
            Intent intent = new Intent(MainActivity.this, OilProperties.class);
            startActivity(intent);
        }
        else if (id == R.id.web_app) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.soapcal.info"));
            startActivity(i);
        }
        else if (id == R.id.feedback) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","plumtreeinbackyard@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.feedback_for_SoapCal));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
        else if (id == R.id.shareApp) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "SoapCal - Soap calculator");
            String sAux = "\nLet me recommend you this app,\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=appinventor.ai_plumtreeinbackyard.SoapCal_Lite \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        }
        else if (id == R.id.buy_me_a_coffee) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.buymeacoffee.com/soapcal"));
            startActivity(i);
        }
        else if (id == R.id.other_apps_from_developer) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://interestingones.web.app"));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
