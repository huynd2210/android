package hda.nzse.tower_defense;

import android.content.Intent;
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

public class MenuActivity extends AppCompatActivity {

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

    public static String initialText= "";
    public static AppManager APP_MANAGER;
    private int[] informationIDs = {
            R.string.menu_hilfe_desc1_text,
            R.string.menu_hilfe_desc2_text,
            R.string.menu_hilfe_desc3_text,
            R.string.menu_hilfe_desc4_text,
            R.string.menu_hilfe_desc5_text
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
        setContentView(R.layout.activity_menu);
        APP_MANAGER = new AppManager(this);

        // Disable Continue-Button if there is no game
        // todo: Find saved game and create if-statement
        Button continueButton = (Button) findViewById(R.id.menu_spielFortsetzen);
        if(APP_MANAGER.getPlayedLevel() < 0)
            continueButton.setEnabled(false);
        else {
            continueButton.setEnabled(true);
            // Add information to button
            String level = getString(R.string.menu_button_spielFortsetzen_Level_text) + (APP_MANAGER.getPlayedLevel() + 1) + "\n";
            String wave = getString(R.string.menu_button_spielFortsetzen_wave_text) + APP_MANAGER.getWave() + "\n";
            String gold = getString(R.string.menu_button_spielFortsetzen_gold_text) + APP_MANAGER.getGold();
            continueButton.setText(getText(R.string.menu_button_spielFortsetzen_text) + "\n" + level + wave + gold);
            // Wrap Content
            ViewGroup.LayoutParams params = continueButton.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            continueButton.setLayoutParams(params);
        }

    }

    public void onClickSpielerklaerungSwtichToOnboarding(View view){
        APP_MANAGER.setOnboardingDisplayed(false);
        APP_MANAGER.save(this);
        startActivity(new Intent(this, OnboardingActivity.class));
    }

    /**
     * Swtiches to the Levelauswahl-Screen
     *
     * @param view
     */
    public void onClickSpielStartenSwitchToLevelauswahl(View view){
        Intent intent = new Intent(this, LevelauswahlActivity.class);
        startActivity(intent);
    }

    /**
     * Swtiches to the Extras-Screen
     *
     * @param view
     */
    public void onClickExtrasSwitchToExtras(View view){
        Intent intent = new Intent(this, ExtrasActivity.class);
        startActivity(intent);
    }

    /**
     * Switchtes to the Einstellungen-Screen
     *
     * @param view
     */
    public void onClickEinstellungenSwitchToEinstellungen(View view){
        Intent intent = new Intent(this, EinstellungenActivity.class);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_menu);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.menu_hilfe)
                b.setVisibility(View.INVISIBLE);
            b.setEnabled(false);
        }
        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.menu_hilfeSchliessen);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_menu);
        menu.setBackgroundResource(R.drawable.background_menu);
//        menu.setBackgroundColor(ContextCompat.getColor(this, R.drawable.green_grass_background));
        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.menu_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.menu_hilfe)
                b.setVisibility(View.VISIBLE);
            b.setEnabled(true);
        }

        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.menu_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.menu_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.menu_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.menu_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MenuActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MenutActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MenuActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MenuActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MenuActivity", "onDestroy");
    }
}
