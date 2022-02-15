package hda.nzse.tower_defense;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ExtrasActivity extends AppCompatActivity {

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

    public static String initialText= "";
    private int[] informationIDs = {
            R.string.extras_hilfe_desc1_text,
            R.string.extras_hilfe_desc2_text
    };

    /**
     * initializes the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title from screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // change to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // show Layout
        setContentView(R.layout.activity_extras);
        APP_MANAGER = new AppManager(this);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, LevelauswahlActivity.class);

        intent.putExtra(Intent.EXTRA_TEXT, "Hello World");

        // Alternative to passing text by intents:
        initialText = "Hallo Welt!";

        // Calls "constructor" for new activity (method onCreate)
        startActivity(intent);
    }

    /**
     * Swtiches to the Menu-Screen
     *
     * @param view
     */
    public void onClickZurueckSwitchToMenu(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Switches to the Statistiken-Screen
     *
     * @param view
     */
    public void onClickStatistikenSwtichToStatistiken(View view){
        Intent intent = new Intent(this, StatistikenActivity.class);
        startActivity(intent);
    }

    /**
     * Switches to the Lexikon-Screen
     *
     * @param view
     */
    public void onClickLexikonSwtichToLexikon(View view){
        Intent intent = new Intent(this, LexikonActivity.class);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_extras);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.extras_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            if(b.getId() == R.id.extras_zurueck) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }
        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.extras_hilfeSchliessen);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_extras);
        menu.setBackgroundResource(R.drawable.background_menu);
        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.extras_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.extras_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            if(b.getId() == R.id.extras_zurueck) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }
        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.extras_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.extras_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.extras_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.extras_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ExtrasActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ExtrasActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ExtrasActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ExtrasActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ExtrasActivity", "onDestroy");
    }
}
