package hda.nzse.tower_defense;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class OnboardingActivity extends AppCompatActivity {

    public static AppManager APP_MANAGER;

    // contains every screen of the onboarding screen
    private ViewPager slideViewPager;
    // Add new Listener for when slide is changed:
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            currentPage = position;

            if(currentPage == 0){
                nextButton.setEnabled(true);
                nextButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(false); // can't go back if first page
                previousButton.setVisibility(View.INVISIBLE);

                nextButton.setText(getText(R.string.onboarding_button_next_text));
                previousButton.setText("");

            } else if(currentPage == dots.length - 1){
                nextButton.setEnabled(true);
                nextButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);

                nextButton.setText(getText(R.string.onboarding_button_next_finish_text));
                previousButton.setText(getText(R.string.onboarding_button_previous_text));

            } else {
                nextButton.setEnabled(true);
                nextButton.setVisibility(View.VISIBLE);
                previousButton.setEnabled(true);
                previousButton.setVisibility(View.VISIBLE);

                nextButton.setText(getText(R.string.onboarding_button_next_text));
                previousButton.setText(getText(R.string.onboarding_button_previous_text));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // navigation menu for the onboarding screen
    private LinearLayout dotsLayout;
    // dots to show in navigation menu on which slide one is
    private TextView[] dots;
    // Buttons for navigation
    private Button nextButton;
    private Button previousButton;

    // Dynamically constructing a slide and handling slide animation
    private SlideAdapter slideAdapter;

    // Saving current page to change button attributes
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        Log.d("OnboardingActivity", "onCreate");
        APP_MANAGER = new AppManager(this);
        // onboard already shown?
        if(APP_MANAGER.getOnboardingDisplayed())
            startActivity(new Intent(this, MenuActivity.class));

        setContentView(R.layout.activity_onboarding);

        slideViewPager = (ViewPager) findViewById(R.id.onboarding_slideViewPager);

        dotsLayout = (LinearLayout) findViewById(R.id.onboarding_dotsLayout);

        nextButton = (Button) findViewById(R.id.onboarding_button_next);
        previousButton = (Button) findViewById(R.id.onboarding_button_previous);

        slideAdapter = new SlideAdapter(this);

        // Displaying the dynamically created slides
        slideViewPager.setAdapter(slideAdapter);

        // Adding dots to navigation menu, starting at page 0
        addDotsIndicator(0);
        // Adding new listener so the dots color can change
        slideViewPager.addOnPageChangeListener(viewListener);
    }

    /**
     * Either raises the currentPage or decreases the current page when
     * nextButton or previousButton is clicked.
     *
     * @param view The button which is clicked
     */
    public void onClick(View view){
        switch(view.getId()){
            case R.id.onboarding_button_next:
                if(currentPage == dots.length-1) {
                    // close onboarding and go to main menu
                    APP_MANAGER.setOnboardingDisplayed(true);
                    APP_MANAGER.save(this);
                    startActivity(new Intent(this, MenuActivity.class));
                }
                else
                    // raise page
                    slideViewPager.setCurrentItem(currentPage + 1);
                break;
            case R.id.onboarding_button_previous:
                slideViewPager.setCurrentItem(currentPage - 1);
                break;
            default: break;
        }
    }

    /**
     * Count number of items you need to display as dots
     *
     * @param position position of the dot which shows the current page
     */
    private void addDotsIndicator(int position){
        dots = new TextView[slideViewPager.getAdapter().getCount()];
        dotsLayout.removeAllViews();

        // Add a dot for each slide
        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            // HTML-Code for dot symbol
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.white));

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OnboardingActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OnboardingActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("OnboardingActivity", "onPause");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("OnboardingActivity", "onStop");
        APP_MANAGER.save(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("OnboardingActivity", "onDestroy");
    }
}
