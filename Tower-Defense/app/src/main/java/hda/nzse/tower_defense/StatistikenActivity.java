package hda.nzse.tower_defense;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatistikenActivity extends AppCompatActivity {

    /*
    Alternative to passing text by intents:
    Use static variables to be accessed where needed
    For lots of variables, use bundles:
        public static Bundle mMyAppsBundle = new Bundle():
    Later in code:
        ApplicationClass.mMyAppsBundle.putString("key","value");

    Now you can get these values from anywhere like this way:
        String str = ApplicationClass.mMyAppsBundle.getString("key");

     Obvious disadvantage: no real way to check if the static variables are current or out of date
     */

    public static AppManager APP_MANAGER;
    private ScrollView statistiken_allStatistics;

    private int[] informationIDs = {
            R.string.statistiken_hilfe_desc1_text
    };

    /**
     * initializes the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GameActivity", "oncreate");
        // remove title from screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // change to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // show Layout
        setContentView(R.layout.activity_statistiken);

        APP_MANAGER = new AppManager(this);

        Log.d("GameActivity", "displaystats 1 " + APP_MANAGER.getGoldEarned());
        APP_MANAGER.load(this);

        Log.d("GameActivity", "displaystats 2 " + APP_MANAGER.getGoldEarned());
        statistiken_allStatistics = findViewById(R.id.statistiken_allStatistics);
        displayStatistics();
    }

    /**
     * Swtiches to the Extras-Screen
     *
     * @param view
     */
    public void onClickZurueckSwitchToExtras(View view){
        Intent intent = new Intent(this, ExtrasActivity.class);
        startActivity(intent);
    }

    /**
     * When Hilfe-Button is pressed, the overlay for the help-Screen is shown
     * and every other button is disabled
     *
     * @param view
     */
    public void onClickHilfeDisplayHilfe(View view){
        // Set background to a grey
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_statistiken);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        statistiken_allStatistics.setBackgroundResource(R.drawable.background_help_with_borders);
        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.statistiken_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            if(b.getId() == R.id.statistiken_zurueck) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }
        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.statistiken_hilfeSchliessen);
        closeHelpButton.setVisibility(View.VISIBLE);
        closeHelpButton.setEnabled(true);
        // Enable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.VISIBLE);
        }
    }

    /**
     * When Hilfe Schließen-Button is pressed, the overlay for the help-Screen closes
     * and every other button is enabled
     *
     * @param view
     */
    public void onClickHilfeSchliessenCloseHilfe(View view){
        // Set background to normal background
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_statistiken);
        menu.setBackgroundResource(R.drawable.background_menu);
        statistiken_allStatistics.setBackgroundResource(R.drawable.white_background_black_borders);


        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.statistiken_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.statistiken_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            if(b.getId() == R.id.statistiken_zurueck) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }
        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.statistiken_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.statistiken_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.statistiken_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.statistiken_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    /**
     * Reads savegame-file to automatically display statistics
     */
    private void displayStatistics(){

        Log.d("GameActivity", "displaystats " + APP_MANAGER.getGoldEarned());
        TextView textView = (TextView) findViewById(R.id.statistiken_enemiesDefeated);
        textView.setText(getString(R.string.statistiken_enemiesDefeated_text) +   "" + APP_MANAGER.getTotalEnemies());

        textView = (TextView) findViewById(R.id.statistiken_goldEarned);
        textView.setText(getString(R.string.statistiken_goldEarned_text) + "" + APP_MANAGER.getGoldEarned());

        textView = (TextView) findViewById(R.id.statistiken_goldSpend);
        textView.setText(getString(R.string.statistiken_goldSpend_text) + "" + APP_MANAGER.getGoldSpend());

        textView = (TextView) findViewById(R.id.statistiken_totalGames);
        textView.setText(getString(R.string.statistiken_totalGames_text) + "" + APP_MANAGER.getTotalGames());

        textView = (TextView) findViewById(R.id.statistiken_wavesCleared);
        textView.setText(getString(R.string.statistiken_wavesCleared_text) + "" + APP_MANAGER.getTotalWaves());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("GameActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        APP_MANAGER.load(this);
        displayStatistics();
        Log.d("GameActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StatistikenActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("StatistikenActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("StatistikenActivity", "onDestroy");
    }
}
