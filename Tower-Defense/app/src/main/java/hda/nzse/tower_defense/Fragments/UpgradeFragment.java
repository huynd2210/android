package hda.nzse.tower_defense.Fragments;



import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hda.nzse.tower_defense.GameManager;
import hda.nzse.tower_defense.R;

public class UpgradeFragment extends Fragment implements View.OnClickListener{

    View upgradeFragmentv;
    Button kaufenBtn, abbrechenBtn, verkaufeBtn;
    TextView tower_old_text_view, tower_new_text_view;
    ImageView tower_old_image_view, tower_new_image_view;

    private GameManager gameManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        upgradeFragmentv = inflater.inflate(R.layout.fragment_upgrade, container, false);
        init();
        initOnClickListeners();

        showTowers();
        return upgradeFragmentv;
    }

    public void showTowers() {
        if(gameManager.getSelectedTile() == null){
            return;
        }
        if(gameManager.getSelectedTile().isHasTower()){
            if (gameManager.getSelectedTile().getTower().getLevel() == 1){
                switch (gameManager.getSelectedTile().getTower().getTowerType()){
                    case 1:{
                        tower_old_image_view.setBackgroundResource(R.drawable.stonetower_lvl1_a1);
                        tower_new_image_view.setBackgroundResource(R.drawable.stonetower_lvl2_a1);
                        tower_old_text_view.setText(R.string.stonetower_description);
                        tower_new_text_view.setText(R.string.stonetower_description_level2);
                        break;
                    }
                    case 2:{
                        tower_old_image_view.setBackgroundResource(R.drawable.irontower_lvl1_a1);
                        tower_new_image_view.setBackgroundResource(R.drawable.irontower_lvl2_a1);
                        tower_old_text_view.setText(R.string.irontower_description);
                        tower_new_text_view.setText(R.string.irontower_description_level2);
                        break;
                    }
                    case 3:{
                        tower_old_image_view.setBackgroundResource(R.drawable.firetower_lvl1_a1);
                        tower_new_image_view.setBackgroundResource(R.drawable.firetower_lvl2_a1);
                        tower_old_text_view.setText(R.string.firetower_description);
                        tower_new_text_view.setText(R.string.firetower_description_level2);
                        break;
                    }
                }

            }
            if (gameManager.getSelectedTile().getTower().getLevel() == 2){
                switch (gameManager.getSelectedTile().getTower().getTowerType()){
                    case 1:{
                        tower_old_image_view.setBackgroundResource(R.drawable.stonetower_lvl3_a1);
                        tower_old_text_view.setText(R.string.stonetower_description_level2);
                        tower_new_image_view.setBackgroundResource(R.drawable.close);
                        tower_new_text_view.setText("Keine weiteren Upgrades mehr verfügbar");
                        break;
                    }
                    case 2:{
                        tower_old_image_view.setBackgroundResource(R.drawable.irontower_lvl3_a1);
                        tower_old_text_view.setText(R.string.irontower_description_level2);
                        tower_new_image_view.setBackgroundResource(R.drawable.close);
                        tower_new_text_view.setText("Keine weiteren Upgrades mehr verfügbar");
                        break;
                    }
                    case 3:{
                        tower_old_image_view.setBackgroundResource(R.drawable.firetower_lvl3_a1);
                        tower_old_text_view.setText(R.string.firetower_description_level2);
                        tower_new_image_view.setBackgroundResource(R.drawable.close);
                        tower_new_text_view.setText("Keine weiteren Upgrades mehr verfügbar");
                        break;
                    }
                }
            }
        }
    }

    private void init() {
        tower_old_image_view = upgradeFragmentv.findViewById(R.id.tower_old_image_view);
        tower_new_image_view = upgradeFragmentv.findViewById(R.id.tower_new_image_view);
        tower_old_text_view = upgradeFragmentv.findViewById(R.id.tower_old_text_view);
        tower_new_text_view = upgradeFragmentv.findViewById(R.id.tower_new_text_view);

        kaufenBtn = upgradeFragmentv.findViewById(R.id.imageButtonKaufeUpgrade);
        abbrechenBtn = upgradeFragmentv.findViewById(R.id.imageButtonAbbrechenUpgrade);
        verkaufeBtn = upgradeFragmentv.findViewById(R.id.imageButtonVerkaufen);
    }

    private void initOnClickListeners() {
        kaufenBtn.setOnClickListener(this);
        abbrechenBtn.setOnClickListener(this);
        verkaufeBtn.setOnClickListener(this);
    }



    public void onClickSelectLevel(View view){

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imageButtonKaufeUpgrade:{
                //kaufenBtn.setEnabled(true);

                gameManager.upgradeTower();
                gameManager.closeBuyOrUpgradeMenu();
                break;
            }
            case R.id.imageButtonAbbrechenUpgrade:{
                gameManager.closeBuyOrUpgradeMenu();
                break;
            }
            case R.id.imageButtonVerkaufen:{

                    gameManager.getSelectedTile().getTower().sell();
                    gameManager.closeBuyOrUpgradeMenu();

               break;
            }
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void onResume() {
        Log.d("Test", "Hallo");
        showTowers();
        super.onResume();
    }
}
