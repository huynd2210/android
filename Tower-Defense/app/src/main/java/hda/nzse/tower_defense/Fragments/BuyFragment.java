package hda.nzse.tower_defense.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import hda.nzse.tower_defense.GameManager;
import hda.nzse.tower_defense.R;

public class BuyFragment extends Fragment implements View.OnClickListener{

    View buyFragmentv;
    Button kaufenBtn, abbrechenBtn, buttonStonetower, buttonIrontower, buttonFiretower;
    TextView textViewTowerDescription;
    ScrollView scrollViewTower;

    private GameManager gameManager;
    private int towerType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        buyFragmentv = inflater.inflate(R.layout.fragment_buy, container, false);
        Log.d("Test", " buyFragment");
        init();
        initOnClickListeners();
        return buyFragmentv;
    }

    private void init() {
        kaufenBtn = buyFragmentv.findViewById(R.id.imageButtonKaufen);
        abbrechenBtn = buyFragmentv.findViewById(R.id.imageButtonAbbrechen);
        buttonStonetower = buyFragmentv.findViewById(R.id.buttonStonetower);
        buttonIrontower = buyFragmentv.findViewById(R.id.buttonIrontower);
        buttonFiretower = buyFragmentv.findViewById(R.id.buttonFiretower);
        textViewTowerDescription = buyFragmentv.findViewById(R.id.textViewTowerDescription);
        //scrollViewTower = buyFragmentv.findViewById(R.id.scrollViewTower);
    }
    private void initOnClickListeners() {
        kaufenBtn.setOnClickListener(this);
        abbrechenBtn.setOnClickListener(this);
        buttonStonetower.setOnClickListener(this);
        buttonIrontower.setOnClickListener(this);
        buttonFiretower.setOnClickListener(this);
        //scrollViewTower = buyFragmentv.findViewById(R.id.scrollViewTower);
    }



    public void onClickSelectLevel(View view){

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imageButtonKaufen:{
                //todo Funktionalität kaufen: also Platzieren des Turms an der richtigen Stelle

                // Disable Buy GameButton and change button_selected_border to black_border
                kaufenBtn.setEnabled(false);
                buttonStonetower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonIrontower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonFiretower.setBackground(getResources().getDrawable(R.drawable.black_border));
                textViewTowerDescription.setText(""); //Wähle einen Turm aus!
                Log.d("Test", " " + textViewTowerDescription.getText());
                //scrollViewTower.fullScroll(ScrollView.FOCUS_LEFT);
                if(!gameManager.getSelectedTile().isHasTower()){
                    gameManager.placeTower2(towerType);
                }
                gameManager.closeBuyOrUpgradeMenu();
                break;
            }
            case R.id.imageButtonAbbrechen:{
                //todo Funktionalität abbrechen: Fragment soll vollständig ausgeblendet werden

                // Disable Buy GameButton and change button_selected_border to black_border
                kaufenBtn.setEnabled(false);
                buttonStonetower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonIrontower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonFiretower.setBackground(getResources().getDrawable(R.drawable.black_border));
                textViewTowerDescription.setText("Wählen einen Turm aus!");
                //scrollViewTower.fullScroll(ScrollView.FOCUS_RIGHT);
                gameManager.closeBuyOrUpgradeMenu();
                break;
            }
            case R.id.buttonStonetower:{
                buttonStonetower.setBackground(getResources().getDrawable(R.drawable.button_selected_border));
                buttonIrontower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonFiretower.setBackground(getResources().getDrawable(R.drawable.black_border));


                // Enable Buy Game button
                kaufenBtn.setEnabled(true);
                towerType = 1;

                //Show description of Tower
                textViewTowerDescription.setText(R.string.stonetower_description);

                break;
            }
            case R.id.buttonIrontower:{
                buttonIrontower.setBackground(getResources().getDrawable(R.drawable.button_selected_border));
                buttonStonetower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonFiretower.setBackground(getResources().getDrawable(R.drawable.black_border));
                // Enable Buy Game button
                kaufenBtn.setEnabled(true);
                towerType = 2;

                //Show description of Tower
                textViewTowerDescription.setText(R.string.irontower_description);

                break;
            }
            case R.id.buttonFiretower:{
                buttonFiretower.setBackground(getResources().getDrawable(R.drawable.button_selected_border));
                buttonIrontower.setBackground(getResources().getDrawable(R.drawable.black_border));
                buttonStonetower.setBackground(getResources().getDrawable(R.drawable.black_border));
                // Enable Buy Game button
                kaufenBtn.setEnabled(true);
                towerType = 3;

                //Show description of Tower
                textViewTowerDescription.setText(R.string.firetower_description);

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
}
