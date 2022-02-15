package hda.nzse.tower_defense;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//import android.app.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.widget.TextView;

import hda.nzse.tower_defense.Fragments.BuyFragment;
import hda.nzse.tower_defense.Fragments.UpgradeFragment;
import hda.nzse.tower_defense.Fragments.WaveFragment;

public class GameActivity extends AppCompatActivity {

    public static AppManager APP_MANAGER;

    public int maxWave = 10;

    // Necessary game objects
    public GameView gameView;
    private GameManager gameManager;

    // changeable attributes which are displayed on textViews
    public int gold = 0;
    public int wave = 0;
    public int enemiesDefeated = 0;
    public int health = 100;
    public double score = 0;
    public int shopMenu = 2;

    public boolean inUpgradeFragment = false;

    // textViews
    private TextView gold_textView;
    private TextView wave_enemy_textView;
    private TextView health_textView;

    //layouts
    private ConstraintLayout constraintLayout_gameover;
    private TextView textView_gameover;
    private TextView textView_information;

    // Shop
    BuyFragment buyFragment;
    UpgradeFragment upgradeFragment;
    WaveFragment waveFragment;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set layout
        setContentView(R.layout.activity_game);

        // init App_Manager who saves and loads data
        APP_MANAGER = new AppManager(this);

        // add TextViews
        gold_textView = findViewById(R.id.game_goldTextView);
        wave_enemy_textView = findViewById(R.id.game_welleFeindeTextView);
        health_textView = findViewById(R.id.game_lebenTextView);

        //add Game-Over-Layout
        constraintLayout_gameover = findViewById(R.id.gameover_layout);
        textView_gameover = findViewById(R.id.textView_gameover);
        textView_information = findViewById(R.id.textView_information);

        // add gameView to display game objects on canvas
        gameView = findViewById(R.id.game_gameView);
        gameManager = gameView.getGameManager();

        //todo darf erst aufgerufen werden wenn Tile/Feld ausgewählt wurde, welches grün/wiese ist oder upgradeFragment
        linearLayout = (LinearLayout) findViewById(R.id.game_fragmentLinearLayout);
        buyFragment = new BuyFragment();
        upgradeFragment = new UpgradeFragment();
        waveFragment = new WaveFragment();
        waveFragment.setGameManager(gameManager);
        buyFragment.setGameManager(gameManager);
        upgradeFragment.setGameManager(gameManager);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.buyAndUpgradeFragmentHolder, buyFragment, "buyFragmentTag");
        ft.replace(R.id.buyAndUpgradeFragmentHolder, waveFragment, "waveFragmentTag");
        ft.replace(R.id.buyAndUpgradeFragmentHolder, upgradeFragment, "upgradeFragmentTag");

        //getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder, waveFragment, "waveFragmentTag").addToBackStack("waveFragmentTag").commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder, upgradeFragment, "upgradeFragmentTag").addToBackStack("upgradeFragmentTag").commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder, buyFragment, "buyFragmentTag").addToBackStack("buyFragmentTag").commit();


//        frameLayout.setVisibility(View.INVISIBLE);

    }

    public void onClickPauseSwitchToPauseActivity(View view){
        Intent intent = new Intent(this, PauseActivity.class);
        startActivity(intent);
    }

    public void onClickHilfeDisplayGameOver(View view){

    }

    public void onClickZurueckSwitchToHauptmenu(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void update(double delta){
        // Updates on UI-Elements (like all Views) have to be done by the main UI-thread
        // Hence:
        // ** Update your game logic here:
        score += delta;

        // ** and then tell the UI-Thread to update the views whenever it pleases:
        runOnUiThread(() -> {
            toggleBuyOrUpgradeMenu(shopMenu);
            gold_textView.setText("Gold: " + gold);
            wave_enemy_textView.setText("Welle " + wave + " von " + maxWave + "\nbesiegte Feinde: " + enemiesDefeated);
            health_textView.setText("Leben: " + health + "/100");
            if(inUpgradeFragment){
                refresh();
            }
            if(!gameView.thread.isRunning()){
                constraintLayout_gameover.setVisibility(View.VISIBLE);
                if(!gameManager.getHatGewonnen()){
                    Log.d("Test", "ist in hat verloren");
                    textView_gameover.setText("Game Over");
                    textView_information.setText("Sie haben dieses Level leider nicht geschafft!");
                }
                if(gameManager.getHatGewonnen()){
                    Log.d("Test", "ist in hat gewonnen");
                    textView_gameover.setText("Gewonnen");
                    textView_information.setText("Super! Sie haben das Level geschafft!");
                }
            }

            upgradeFragment.showTowers();
        });



    }

    public void toggleBuyOrUpgradeMenu(int shop){
        switch (shop){
            case 0:
                inUpgradeFragment = false;
                linearLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder,buyFragment).commit();
                break;
            case 1:
                linearLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder, upgradeFragment).commit();
                inUpgradeFragment = true;
                break;
            case 2:
                inUpgradeFragment = false;
                linearLayout.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.buyAndUpgradeFragmentHolder, waveFragment).commit();
                break;
            case -1:
                linearLayout.setVisibility(View.INVISIBLE);
                break;
            default: break;
        }
    }

    @Override
    protected void onStart() {
        Log.d("GameActivity", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("GameActivity", "onResume");
        super.onResume();
        gameView.restart();
    }

    @Override
    protected void onPause() {
        Log.d("GameActivity", "onPause");
        super.onPause();
        gameView.thread.setRunning(false);
    }

    @Override
    protected void onStop() {
        Log.d("GameActivity", "onStop");
        super.onStop();
        APP_MANAGER.setGold(gold);
        APP_MANAGER.setGoldEarned(APP_MANAGER.getGold() + APP_MANAGER.getGoldEarned());

        Log.d("GameActivity", "gold earned " + APP_MANAGER.getGoldEarned());

        APP_MANAGER.setWave(wave);
        APP_MANAGER.setTotalWaves(APP_MANAGER.getWave() + APP_MANAGER.getTotalWaves());

        APP_MANAGER.setEnemiesTotal(enemiesDefeated);
        APP_MANAGER.setTotalEnemies(APP_MANAGER.getEnemiesTotal() + APP_MANAGER.getTotalEnemies());

        APP_MANAGER.setTotalGames(APP_MANAGER.getTotalGames() + 1);
        APP_MANAGER.save(this);
        APP_MANAGER.load(this);

        Log.d("GameActivity", "gold earned 2 " + APP_MANAGER.getGoldEarned());
    }

    @Override
    protected void onDestroy() {
        Log.d("GameActivity", "onDestroy");
        super.onDestroy();
    }


    public void refresh(){
        Fragment frag = upgradeFragment;
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(upgradeFragment);
        ft.attach(upgradeFragment);
        ft.commit();
    }
}