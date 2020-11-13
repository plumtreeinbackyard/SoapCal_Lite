package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EditSingleRecipe extends AppCompatActivity implements View.OnClickListener{

    private EditText ETRecipeTitle, ETRecipe;
    private String title, oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_single_recipe);
        setSupportActionBar(toolbar);

        ETRecipeTitle = (EditText)findViewById(R.id.et_recipe_title);
        ETRecipe = (EditText)findViewById(R.id.et_recipe);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btn_back_edit_single_recipe);
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        oldName = intent.getStringExtra("recipe_title");
        ETRecipeTitle.setText(oldName);
        title = intent.getStringExtra("recipe_title");
        ETRecipe.setText(intent.getStringExtra("recipe"));

        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = (AdView) findViewById(R.id.adView_edit_single_recipe);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btn_back_edit_single_recipe)) {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_single_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_recipe) {
            if (ETRecipeTitle.getText().toString().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_type_in_a_name), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (ETRecipeTitle.getText().toString().contains("\\.")) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.cannot_contain_symbol_dot), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                MainActivity.fileName.remove(title);
                if (MainActivity.fileName.contains(ETRecipeTitle.getText().toString())) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.this_name_is_already_used) + "\n" + getResources().getString(R.string.please_choose_another_name), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {

                    try {
                        File oldFileName = new File(EditSingleRecipe.this.getFilesDir(), oldName + ".txt");
                        oldFileName.delete();

                        FileOutputStream output = EditSingleRecipe.this.openFileOutput(ETRecipeTitle.getText().toString() + ".txt", Context.MODE_PRIVATE);
                        output.write(ETRecipe.getText().toString().getBytes());
                        output.close();

                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_was_saved_successfully), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_was_not_saved), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    Intent intent = new Intent(EditSingleRecipe.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
