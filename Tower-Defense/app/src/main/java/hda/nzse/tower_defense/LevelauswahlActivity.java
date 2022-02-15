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

public class LevelauswahlActivity extends AppCompatActivity {

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
    private int selectedLevel = -1;

    public static String initialText= "";
    private int[] informationIDs = {
            R.string.levelauswahl_hilfe_desc1_text,
            R.string.levelauswahl_hilfe_desc2_text
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
        setContentView(R.layout.activity_levelauswahl);

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

    public void onClickSpielStartenStartGame(View view){
        APP_MANAGER.setPlayedLevel(selectedLevel);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /**
     * Select the level by pressing a button
     * changes the static attribute LEVEL_SELECTED to either 1, 2 or 3
     *
     * @param view
     */
    public void onClickSelectLevel(View view){
        // Select level - must use String.valueOf, because getTag() returns an object
        selectedLevel = Integer.parseInt(String.valueOf(view.getTag()));
        // highlight button
        view.setBackground(getDrawable(R.drawable.button_background_with_selected_borders));
        // Enable play Game button
        Button levelauswahl_button_spielStarten = findViewById(R.id.levelauswahl_button_spielStarten);
        levelauswahl_button_spielStarten.setEnabled(true);
        levelauswahl_button_spielStarten.setTextColor(getResources().getColor(R.color.black));

        // get all other buttons and change they're layout
        ArrayList<Button> buttons = APP_MANAGER.getButtons(this);
        for(Button b : buttons)
            if(b.getId() != view.getId())
                b.setBackground(getDrawable(R.drawable.button_background_with_black_borders));
    }

    /**
     * When Hilfe-Button is pressed, the overlay for the help-Screen is shown
     * and every other button is disabled
     *
     * @param view
     */
    public void onClickHilfeDisplayHilfe(View view){
        // Set background to a grey
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_levelauswahl);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.levelauswahl_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            if(b.getId() == R.id.levelauswahl_zurueck) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }
        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.levelauswahl_hilfeSchliessen);
        closeHelpButton.setVisibility(View.VISIBLE);
        closeHelpButton.setBackground(getDrawable(R.drawable.button_hilfe_background_with_grey_borders));
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_levelauswahl);
        menu.setBackgroundResource(R.drawable.background_menu);

        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.levelauswahl_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.levelauswahl_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            if(b.getId() == R.id.levelauswahl_zurueck) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }
        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.levelauswahl_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.levelauswahl_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.levelauswahl_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.levelauswahl_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LevelauswahlActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LevelauswahlActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LevelauswahlActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LevelauswahlActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LevelauswahlActivity", "onDestroy");
    }
}
