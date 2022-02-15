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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class EinstellungenActivity extends AppCompatActivity {

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

    /**
     * changing the buttonsize for all buttons in this app
     * 0 = small
     * 1 = middle
     * 2 = big
     */
    public static int BUTTONSIZE = 0;

    private static int MUSIC_VOLUME = 20;
    private static int EFFECT_VOLUME = 20;

    public static AppManager APP_MANAGER;
    private int[] informationIDs = {
            R.string.einstellungen_hilfe_desc1_text,
            R.string.einstellungen_hilfe_desc2_text,
            R.string.einstellungen_hilfe_desc3_text,
    };

    /**
     * initializes the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init APP_MANAGER
        APP_MANAGER = new AppManager(this);
        // remove title from screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // change to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // show Layout
        setContentView(R.layout.activity_einstellungen);
        // Set Listeners on seekBars
        setListenerForSeekBars();

        displayOptions();
    }

    /**
     * Sets the listeners for SeekBars to show the percentage of their progress
     *
     */
    private void setListenerForSeekBars(){
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView textView;
                switch (seekBar.getId()){
                    case R.id.einstellungen_musiklautstaerke_seekBar:
                        textView = (TextView) findViewById(R.id.einstellungen_musiklautstaerke_prozent);
                        textView.setText(String.valueOf(seekBar.getProgress()) + "%");
                        APP_MANAGER.setMusic_volume(seekBar.getProgress());
                        break;
                    case R.id.einstellungen_effektlautstaerke_seekBar:
                        textView = (TextView) findViewById(R.id.einstellungen_effektlautstaerke_prozent);
                        textView.setText(String.valueOf(seekBar.getProgress()) + "%");
                        APP_MANAGER.setEffects_volume(seekBar.getProgress());
                        break;
                    default: break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        SeekBar seekBar = findViewById(R.id.einstellungen_musiklautstaerke_seekBar);
        seekBar.setOnSeekBarChangeListener(listener);
        seekBar = findViewById(R.id.einstellungen_effektlautstaerke_seekBar);
        seekBar.setOnSeekBarChangeListener(listener);
    }

    /**
     * Select the level by pressing a button
     * changes the static attribute LEVEL_SELECTED to either 1, 2 or 3
     *
     * @param view
     */
    public void onClickSelectButtongroesse(View view) {
        // Select level - must use String.valueOf, because getTag() returns an object
        APP_MANAGER.setButton_size(Integer.parseInt(String.valueOf(view.getTag())));
        // highlight button
        view.setBackground(getDrawable(R.drawable.button_background_with_selected_borders));
        // get all other buttons and change they're layout
        ArrayList<Button> buttons = getButtons();
        for (Button b : buttons){
            if (b.getId() != view.getId()) {
                b.setBackground(getDrawable(R.drawable.button_background_with_black_borders));
            }
            if (b.getId() == R.id.einstellungen_hilfeSchliessen) {
                b.setBackground(getDrawable(R.drawable.button_hilfe_background_with_grey_borders));
            }
        }

    }

    /**
     * When Hilfe-Button is pressed, the overlay for the help-Screen is shown
     * and every other button is disabled
     *
     * @param view
     */
    public void onClickHilfeDisplayHilfe(View view){
        // Set background to a grey
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_einstellungen);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        findViewById(R.id.buttongroesse_layout).setBackgroundResource(R.drawable.background_help_with_borders);
        findViewById(R.id.musiklautstaerke_layout).setBackgroundResource(R.drawable.background_help_with_borders);
        findViewById(R.id.effektlautstaerke_layout).setBackgroundResource(R.drawable.background_help_with_borders);

        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.einstellungen_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            if(b.getId() == R.id.einstellungen_zurueck) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }

        //todo disable die scrollbars und bei hilfe schließen wieder enablen

        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.einstellungen_hilfeSchliessen);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_einstellungen);
        menu.setBackgroundResource(R.drawable.background_menu);
        findViewById(R.id.buttongroesse_layout).setBackgroundResource(R.drawable.white_background_black_borders);
        findViewById(R.id.musiklautstaerke_layout).setBackgroundResource(R.drawable.white_background_black_borders);
        findViewById(R.id.effektlautstaerke_layout).setBackgroundResource(R.drawable.white_background_black_borders);

        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.einstellungen_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.einstellungen_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            if(b.getId() == R.id.einstellungen_zurueck) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }

        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.einstellungen_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.einstellungen_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.einstellungen_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.einstellungen_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    /**
     * displays the saved options from json-file options.json
     *
     */
    public void displayOptions(){
        // set button size:
        switch(APP_MANAGER.getButton_size()){
            case 0: onClickSelectButtongroesse(findViewById(R.id.einstellungen_buttongroesse_button_klein));
                break;
            case 1: onClickSelectButtongroesse(findViewById(R.id.einstellungen_buttongroesse_button_mittel));
                break;
            case 2: onClickSelectButtongroesse(findViewById(R.id.einstellungen_buttongroesse_button_gross));
                break;
            default: break;
        }

        // set seekBars
        SeekBar seekBar = (SeekBar) findViewById(R.id.einstellungen_musiklautstaerke_seekBar);
        seekBar.setProgress(APP_MANAGER.getMusic_volume());
        seekBar = (SeekBar) findViewById(R.id.einstellungen_effektlautstaerke_seekBar);
        seekBar.setProgress(APP_MANAGER.getEffects_volume());
    }

    /**
     * Get all Buttons from this Activity.
     * Works recursive.
     *
     * @return all buttons in activity Levelauswahl
     */
    public ArrayList<Button> getButtons(){
        ArrayList<Button> buttons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        findButtons(viewGroup, buttons);
        return buttons;
    }

    /**
     * Recursive algorithm to extract all Buttons within one ViewGroup
     *
     * @param viewGroup viewGroup in which the buttons are located in
     * @param buttons Arraylist which should be filled with buttons
     */
    private void findButtons(ViewGroup viewGroup, ArrayList<Button> buttons){
        for(int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View child = viewGroup.getChildAt(i);

            if(child instanceof ViewGroup)
                findButtons((ViewGroup) child, buttons);
            else if(child instanceof Button)
                buttons.add((Button) child);
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("EinstellungenActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("EinstellungenActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        APP_MANAGER.save(this);
        Log.d("EinstellungenActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        APP_MANAGER.save(this);
        Log.d("EinstellungenActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("EinstellungenActivity", "onDestroy");
    }
}
