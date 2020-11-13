package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplaySingleRecipe extends AppCompatActivity implements View.OnClickListener{
    private TextView TVRecipeTitle, TVRecipe;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_single_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_display_single_recipe);
        setSupportActionBar(toolbar);

        TVRecipeTitle = (TextView)findViewById(R.id.textView_recipe_title);
        TVRecipe = (TextView)findViewById(R.id.tv_recipe);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btn_back_display_single_recipe);
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        TVRecipe.setText(intent.getStringExtra("recipe"));
        //TVRecipeTitle.setText(intent.getStringExtra("recipe_title"));

        title = intent.getStringExtra("recipe_title");
        if (title != null && !title.isEmpty()){
            TVRecipeTitle.setText(intent.getStringExtra("recipe_title"));
        } else {
            Date date = new Date();
            title = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
            TVRecipeTitle.setText(title);

            try {
                FileOutputStream output = DisplaySingleRecipe.this.openFileOutput(title + ".txt", Context.MODE_PRIVATE);
                output.write(TVRecipe.getText().toString().getBytes());
                output.close();
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_was_saved_successfully), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_was_not_saved), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        //ad.
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9005389628695120~2202441693");
        AdView mAdView = (AdView) findViewById(R.id.adView_display_single_recipe);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("2B71BD2A2993412FB98017B30DB2AD64").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btn_back_display_single_recipe)) {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_single_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_recipe) {
            StringBuilder message = new StringBuilder(TVRecipeTitle.getText().toString());
            message.append("\n\n");
            message.append(TVRecipe.getText().toString());

            Intent myShareIntent = new Intent(Intent.ACTION_SEND);
            myShareIntent.setType("text/plain");
            myShareIntent.putExtra(Intent.EXTRA_SUBJECT, TVRecipeTitle.getText().toString());
            myShareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
            startActivity(Intent.createChooser(myShareIntent, getResources().getString(R.string.share_via)));
            return true;
        }
        else if (id == R.id.edit_recipe) {
            Intent intent = new Intent(DisplaySingleRecipe.this, EditSingleRecipe.class);
            intent.putExtra("recipe_title", title);
            intent.putExtra("recipe", TVRecipe.getText());
            startActivity(intent);
            return true;
        }
        else if (id == R.id.delete_recipe) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DisplaySingleRecipe.this);
            builder1.setMessage(getResources().getString(R.string.delete_this_recipe));
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!TVRecipeTitle.getText().toString().equals("")) {
                                File file = new File(DisplaySingleRecipe.this.getFilesDir(), TVRecipeTitle.getText().toString() + ".txt");
                                file.delete();
                                Intent intent = new Intent(DisplaySingleRecipe.this, MainActivity.class);
                                startActivity(intent);
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recipe_was_deleted), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            else {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.this_recipe_is_never_saved), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    });

            builder1.setNegativeButton(
                    getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
