package hda.nzse.tower_defense;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AppManager {

    // Current activity for the manager
    private Activity activity;

    // Should onboarding be displayed?
    boolean onboardingDisplayed = false;

    // options
    private int music_volume = 20;
    private int effects_volume = 20;
    private int button_size = 0;

    // savestate
    private int playedLevel = -1;
    private int wave = -1;
    private int enemiesRemaining = -1;
    private int enemiesTotal = -1;
    private int gold = -1;
    private int health = -1;
    private int towersTotal = -1;
    // todo: Add Tower Objects as ArrayList

    // statistics
    private int totalGames = 0;
    private int totalWaves = 0;
    private int totalEnemies = 0;
    private int goldEarned = 0;

    private int goldSpend = 69;

    public AppManager(Activity activity){
        this.activity = activity;
        load(activity);
    }

    /**
     * Writes all attributes to according JSON-File
     *
     */
    public void save(AppCompatActivity activity){
        saveOnboarding(activity);
        saveOptions(activity);
        saveSavestate(activity);
        saveStatistics(activity);
    }

    private void saveOnboarding(Activity activity){
        try {
            JSONObject onboarding = new JSONObject();
            onboarding.put("onboardingDisplayed", onboardingDisplayed);

            File path = activity.getApplicationContext().getFilesDir();
            File file = new File(path, "onboarding.json");
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(onboarding.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveOptions(Activity activity){
        try {
            JSONObject options = new JSONObject();
            options.put("music_volume", music_volume);
            options.put("sound-effects_volume", effects_volume);
            options.put("buttongroesse", button_size);

            File path = activity.getApplicationContext().getFilesDir();
            File file = new File(path, "options.json");
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(options.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveSavestate(Activity activity){
        try {
            JSONObject options = new JSONObject();
            options.put("currentLevel", playedLevel);
            options.put("wave", wave);
            options.put("enemiesRemaining", enemiesRemaining);
            options.put("enemiesTotal", enemiesTotal);
            options.put("gold", gold);
            options.put("health", health);
            options.put("towersTotal", towersTotal);
            // todo: add tower objects if they are placed ingame

            File path = activity.getApplicationContext().getFilesDir();
            File file = new File(path, "savestate.json");
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(options.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveStatistics(Activity activity){
        try {
            JSONObject options = new JSONObject();
            options.put("gamesPlayedTotal", totalGames);
            options.put("wavesClearedTotal", totalWaves);
            options.put("enemiesDefeatedTotal", totalEnemies);
            options.put("goldEarnedTotal", goldEarned);
            options.put("goldSpendTotal", goldSpend);

            File path = activity.getApplicationContext().getFilesDir();
            File file = new File(path, "statistics.json");
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(options.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Extracts values out of JSON-Files and puts them into their according attributes
     *
     * @param activity the activity of which assets should be used
     */
    public void load(Activity activity){
        loadOnboarding(activity);
        loadOptions(activity);
        loadSavestate(activity);
        loadStatistics(activity);
    }

    private void loadOnboarding(Activity activity){
        try{
            String json_file = getJSONFile("onboarding.json", activity);
            if(json_file != null){
                JSONObject onboarding = new JSONObject(json_file);
                onboardingDisplayed = onboarding.getBoolean("onboardingDisplayed");
            } else {
                saveOnboarding(activity);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void loadOptions(Activity activity){
        try{
            String json_file = getJSONFile("options.json", activity);
            if(json_file != null){
                JSONObject options = new JSONObject(json_file);
                music_volume = options.getInt("music_volume");
                effects_volume = options.getInt("sound-effects_volume");
                button_size = options.getInt("buttongroesse");
            } else {
                saveOptions(activity);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void loadSavestate(Activity activity){
        try {
            String json_file = getJSONFile("savestate.json", activity);
            if(json_file != null){
                JSONObject savestate = new JSONObject(json_file);
                playedLevel = savestate.getInt("currentLevel");
                wave = savestate.getInt("wave");
                enemiesRemaining = savestate.getInt("enemiesRemaining");
                enemiesTotal = savestate.getInt("enemiesTotal");
                gold = savestate.getInt("gold");
                health = savestate.getInt("health");
                towersTotal = savestate.getInt("towersTotal");
                // todo: Add Tower objects to save file
            } else {
                saveSavestate(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadStatistics(Activity activity){
        JSONObject statistics = null;
        try {
            String json_file = getJSONFile("statistics.json", activity);
            if(json_file != null){
                statistics = new JSONObject(json_file);
                totalGames = statistics.getInt("gamesPlayedTotal");
                totalWaves = statistics.getInt("wavesClearedTotal");
                totalEnemies = statistics.getInt("enemiesDefeatedTotal");
                goldEarned = statistics.getInt("goldEarnedTotal");
                goldSpend = statistics.getInt("goldSpendTotal");
            } else {
                saveStatistics(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads through assets-directory and returns the json file as a String.
     *
     * @param filename name of the file which will be read
     * @return JSON file as a String
     */
    private String getJSONFile(String filename, Activity activity){
        try {
            File path = activity.getApplicationContext().getFilesDir();
            File file = new File(path, filename);
            if(file.exists()) {
                byte[] json_bytes = new byte[(int) file.length()];
                FileInputStream in = new FileInputStream(file);
                in.read(json_bytes);
                return new String(json_bytes);
            } else {
                return null;
            }
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Get all Buttons from an Activity.
     * Works recursive.
     *
     * @param activity The activity from which the buttons should be extracted
     * @return all buttons in activity
     */
    public ArrayList<Button> getButtons(Activity activity){
        ArrayList<Button> buttons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        findButtons(viewGroup, buttons);
        return buttons;
    }

    /**
     * Recursive algorithm to extract all Buttons within one ViewGroup
     *
     * @param viewGroup viewGroup in which the buttons are located in
     * @param buttons Arraylist which shall be filled with buttons
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

    public ArrayList<ImageButton> getImageButtons(Activity activity){
        ArrayList<ImageButton> buttons = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        findImageButtons(viewGroup, buttons);
        return buttons;
    }

    private void findImageButtons(ViewGroup viewGroup, ArrayList<ImageButton> buttons){
        for(int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View child = viewGroup.getChildAt(i);

            if(child instanceof ViewGroup)
                findImageButtons((ViewGroup) child, buttons);
            else if(child instanceof ImageButton)
                buttons.add((ImageButton) child);
        }
    }

    public String toString(){
        String res = "";
        try{
            JSONObject onboarding = new JSONObject();
            onboarding.put("onboardingDisplayed", onboardingDisplayed);

            JSONObject options = new JSONObject();
            options.put("music_volume", music_volume);
            options.put("sound-effect_volume", effects_volume);
            options.put("buttongroesse", button_size);

            JSONObject statistics = new JSONObject();
            statistics.put("gamesPlayedTotal", totalGames);
            statistics.put("wavesClearedTotal", totalWaves);
            statistics.put("enemiesDefeatedTotal", totalEnemies);
            statistics.put("goldEarnedTotal", goldEarned);
            statistics.put("goldSpendTotal", goldSpend);

            JSONObject savestate = new JSONObject();
            savestate.put("currentLevel", playedLevel);
            savestate.put("wave", wave);
            savestate.put("enemiesRemaining", enemiesRemaining);
            savestate.put("enemiesTotal", enemiesTotal);
            savestate.put("gold", gold);
            savestate.put("health", health);
            savestate.put("towersTotal", towersTotal);

            JSONObject information = new JSONObject();
            information.put("onboarding", onboarding);
            information.put("options", options);
            information.put("statistics", statistics);
            information.put("savestate", savestate);

            return information.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }

    // Setter
    public void setOnboardingDisplayed(boolean bool){
        this.onboardingDisplayed = bool;
    }

    public void setMusic_volume(int music_volume) {
        this.music_volume = music_volume;
        if(music_volume > 100)
            this.music_volume = 100;
        if(music_volume < 0)
            this.music_volume = 0;
    }

    public void setEffects_volume(int effects_volume) {
        this.effects_volume = effects_volume;
        if(effects_volume > 100)
            this.effects_volume = 100;
        if(effects_volume < 0)
            this.effects_volume = 0;
    }

    public void setButton_size(int button_size) {
        this.button_size = button_size;
        if(button_size > 2)
            this.button_size = 2;
        if(button_size < 0)
            this.button_size = 0;
    }

    public void setPlayedLevel(int playedLevel) {
        this.playedLevel = playedLevel;
        if(playedLevel < 0)
            this.playedLevel = 0;
    }

    public void setWave(int wave) {
        this.wave = wave;
        if(wave < 0)
            this.wave = 0;
    }

    public void setEnemiesRemaining(int enemiesRemaining) {
        this.enemiesRemaining = enemiesRemaining;
        if(enemiesRemaining < 0)
            this.enemiesRemaining = 0;
    }

    public void setEnemiesTotal(int enemiesTotal) {
        this.enemiesTotal = enemiesTotal;
        if(enemiesTotal < 0)
            this.enemiesTotal = 0;
    }

    public void setGold(int gold) {
        this.gold = gold;
        if(gold < 0)
            this.gold = 0;
    }

    public void setHealth(int health) {
        this.health = health;
        if(health > 100)
            this.health = 100;
        if(health < 0)
            this.health = 0;
    }

    public void setTowersTotal(int towersTotal) {
        this.towersTotal = towersTotal;
        if(towersTotal < 0)
            towersTotal = 0;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
        if(totalGames < 0)
            totalGames = 0;
    }

    public void setTotalWaves(int totalWaves) {
        this.totalWaves = totalWaves;
        if(totalWaves < 0)
            this.totalWaves = 0;
    }

    public void setTotalEnemies(int totalEnemies) {
        this.totalEnemies = totalEnemies;
        if(totalEnemies < 0)
            this.totalEnemies = 0;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
        if(goldEarned < 0)
            this.goldEarned = 0;
    }

    public void setGoldSpend(int goldSpend) {
        this.goldSpend = goldSpend;
        if(goldSpend < 0)
            this.goldSpend = 0;
    }

    // Getter
    public boolean getOnboardingDisplayed(){ return onboardingDisplayed; }

    public int getMusic_volume() {
        return music_volume;
    }

    public int getEffects_volume() {
        return effects_volume;
    }

    public int getButton_size() {
        return button_size;
    }

    /**
     *
     * @return Number from 0 to max(Levels). 0 is first level
     */
    public int getPlayedLevel() {
        return playedLevel;
    }

    public int getWave() {
        return wave;
    }

    public int getEnemiesRemaining() {
        return enemiesRemaining;
    }

    public int getEnemiesTotal() {
        return enemiesTotal;
    }

    public int getGold() {
        return gold;
    }

    public int getHealth() {
        return health;
    }

    public int getTowersTotal() {
        return towersTotal;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalWaves() {
        return totalWaves;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public int getGoldSpend() {
        return goldSpend;
    }
}
