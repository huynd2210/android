package hda.nzse.tower_defense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

public class PauseActivity extends AppCompatActivity {

    public AppManager APP_MANAGER;
    private int[] informationIDs = {
            R.string.pause_hilfe_desc1_text,
            R.string.pause_hilfe_desc2_text,
            R.string.pause_hilfe_desc3_text,
            R.string.pause_hilfe_desc4_text
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("PauseActivity", "onCreate");
        super.onCreate(savedInstanceState);
        // remove title from screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // change to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pause);
        APP_MANAGER = new AppManager(this);
        setListenerForSeekBars();
        loadSeekBars();
    }

    private void loadSeekBars(){
        SeekBar musicBar = (SeekBar) findViewById(R.id.pause_musik_bar);
        SeekBar effectBar = (SeekBar) findViewById(R.id.pause_effekt_bar);
        musicBar.setProgress(APP_MANAGER.getMusic_volume());
        effectBar.setProgress(APP_MANAGER.getEffects_volume());
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
                    case R.id.pause_musik_bar:
                        textView = (TextView) findViewById(R.id.pause_musik_percent);
                        textView.setText(String.valueOf(seekBar.getProgress()) + "%");
                        APP_MANAGER.setMusic_volume(seekBar.getProgress());
                        break;
                    case R.id.pause_effekt_bar:
                        textView = (TextView) findViewById(R.id.pause_effekt_percent);
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
        SeekBar seekBar = findViewById(R.id.pause_musik_bar);
        seekBar.setOnSeekBarChangeListener(listener);
        seekBar = findViewById(R.id.pause_effekt_bar);
        seekBar.setOnSeekBarChangeListener(listener);
    }

    public void onClickSpielFortsetzenSwitchToGame(View view){
        APP_MANAGER.save(this);
        onBackPressed();
        //Intent intent = new Intent(this, GameActivity.class);
        //startActivity(intent);
    }

    public void onClickSpeichernSwitchToMenu(View view){
        APP_MANAGER.save(this);
        Intent intent = new Intent(this, MenuActivity.class);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_pause);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        findViewById(R.id.musiklautstaerke_layout_pause).setBackgroundResource(R.drawable.background_help_with_borders);
        findViewById(R.id.effektlautstaerke_layout_pause).setBackgroundResource(R.drawable.background_help_with_borders);

        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.pause_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }

        //todo disable die scrollbars und bei hilfe schließen wieder enablen

        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.pause_hilfeSchliessen);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_pause);
        //todo anderer hintergrund screen allgemein für das pause-Menü, am besten ein ausgegrautet bild vom gamescreen, muss auch net dynamisch sein
        menu.setBackgroundResource(R.drawable.background_menu);
         findViewById(R.id.musiklautstaerke_layout_pause).setBackgroundResource(R.drawable.white_background_black_borders);
        findViewById(R.id.effektlautstaerke_layout_pause).setBackgroundResource(R.drawable.white_background_black_borders);

        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.pause_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.pause_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }
        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.pause_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.pause_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.pause_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.pause_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("PauseActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PauseActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PauseActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("PauseActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("PauseActivity", "onDestroy");
    }
}