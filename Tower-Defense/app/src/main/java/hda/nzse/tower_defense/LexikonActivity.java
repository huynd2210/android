package hda.nzse.tower_defense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class LexikonActivity extends AppCompatActivity {

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
    private int[] informationIDs = {
            R.string.lexikon_hilfe_desc1_text,
            R.string.lexikon_hilfe_desc2_text,
            R.string.lexikon_hilfe_desc3_text
    };

    private ScrollView lexikon_description_scrollbar;
    LinearLayout lexikon_frame;

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
        setContentView(R.layout.activity_lexikon);
        APP_MANAGER = new AppManager(this);
        lexikon_description_scrollbar = findViewById(R.id.lexikon_description_scrollview);
        lexikon_frame = findViewById(R.id.lexikon_frame);
    }

    /**
     * Swtiches to the Levelauswahl-Screen
     *
     * @param view
     */
    public void onClickZurueckSwitchToExtras(View view){
        Intent intent = new Intent(this, ExtrasActivity.class);
        startActivity(intent);
    }

    /**
     * Shows picture and information about an chosen entity.
     *
     * Receives the tag of a view to select information about an entity.
     *
     * @param view
     */
    public void onClickDiscplayEntity(View view){
        // Get imageView from entity description box
        ImageView entityPicture = findViewById(R.id.lexikon_entityPicture);
        // Get textView from entity description box
        TextView entityDescription = findViewById(R.id.lexikon_entityDescription);

        // Fill entity description box
        switch(view.getId())
        {
            case R.id.lexikon_enemy1_button:
                entityPicture.setImageResource(R.drawable.enemy1_a9);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                entityDescription.setText(R.string.lexikon_enemy1_details_text);
                break;
            case R.id.lexikon_enemy2_button:
                entityPicture.setImageResource(R.drawable.enemy2_a9);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                entityDescription.setText(R.string.lexikon_enemy2_details_text);
                break;
            case R.id.lexikon_turm1_button:
                entityPicture.setImageResource(R.drawable.stonetower_bild);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                entityDescription.setText(R.string.stonetower_details_text);
                break;
            case R.id.lexikon_turm2_button:
                entityPicture.setImageResource(R.drawable.irontower_bild);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                entityDescription.setText(R.string.irontower_details_text);
                break;
            case R.id.lexikon_turm3_button:
                entityPicture.setImageResource(R.drawable.firetower_bild);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                lexikon_description_scrollbar.fullScroll(ScrollView.FOCUS_UP);
                entityDescription.setText(R.string.firetower_details_text);
                break;
            default:
                break;
        }

        // Show LinearLayout
        findViewById(R.id.lexikon_entitiyInformation_container).setVisibility(View.VISIBLE);
    }

    /**
     * When Hilfe-Button is pressed, the overlay for the help-Screen is shown
     * and every other button is disabled
     *
     * @param view
     */
    public void onClickHilfeDisplayHilfe(View view){
        // Set background to a grey
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_lexikon);
        menu.setBackgroundResource(R.drawable.background_menu_help);
        lexikon_frame.setBackgroundResource(R.drawable.background_help_with_borders);

        //todo enable and later disable all scrollviews

        // Disable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.lexikon_hilfe) {
                b.setVisibility(View.INVISIBLE);
            }
            if(b.getId() == R.id.lexikon_zurueck) {
                b.setVisibility(View.INVISIBLE);
            }
            b.setEnabled(false);
        }
        // Disable all scrollViews
        findViewById(R.id.lexikon_description_scrollview).setEnabled(false);
        findViewById(R.id.lexikon_entitySelectior_container).setEnabled(false);

        // Enable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.lexikon_hilfeSchliessen);
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
        ConstraintLayout menu = (ConstraintLayout) findViewById(R.id.activity_lexikon);
        menu.setBackgroundResource(R.drawable.background_menu);
        lexikon_frame.setBackgroundResource(R.drawable.white_background_black_borders);
        // Disable "Hilfe schließen"-Button
        Button closeHelpButton = (Button) findViewById(R.id.lexikon_hilfeSchliessen);
        closeHelpButton.setVisibility(View.INVISIBLE);
        closeHelpButton.setEnabled(false);
        // Enable all buttons
        ArrayList<Button> allButtons = APP_MANAGER.getButtons(this);
        for(Button b : allButtons){
            if(b.getId() == R.id.lexikon_hilfe) {
                b.setVisibility(View.VISIBLE);
            }
            if(b.getId() == R.id.lexikon_zurueck) {
                b.setVisibility(View.VISIBLE);
            }
            b.setEnabled(true);
        }

        // Enable all scrollViews
        findViewById(R.id.lexikon_description_scrollview).setEnabled(true);
        findViewById(R.id.lexikon_entitySelectior_container).setEnabled(true);

        // Disable all Information-buttons
        ArrayList<ImageButton> imageButtons = APP_MANAGER.getImageButtons(this);
        for(ImageButton b : imageButtons){
            b.setVisibility(View.INVISIBLE);
        }
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.lexikon_informationLinearLayout);
        if(informationBox.getVisibility() == View.VISIBLE)
            informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationSchliessen(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.lexikon_informationLinearLayout);
        informationBox.setVisibility(View.INVISIBLE);
    }

    public void onClickInformationButton(View view){
        LinearLayout informationBox = (LinearLayout) findViewById(R.id.lexikon_informationLinearLayout);
        informationBox.setVisibility(View.VISIBLE);
        TextView informationText = (TextView) findViewById(R.id.lexikon_informationTextView);
        Log.d("MenuActivity", "tag of view " + view.getId() + ": " + view.getTag());
        informationText.setText(getString(informationIDs[Integer.parseInt(String.valueOf(view.getTag()))]));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LexikonActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LexikonActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LexikonActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LexikonActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LexikonActivity", "onDestroy");
    }
}
