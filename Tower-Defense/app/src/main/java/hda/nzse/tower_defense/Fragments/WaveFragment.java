package hda.nzse.tower_defense.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hda.nzse.tower_defense.AppManager;
import hda.nzse.tower_defense.GameActivity;
import hda.nzse.tower_defense.GameManager;
import hda.nzse.tower_defense.R;

public class WaveFragment extends Fragment implements View.OnClickListener {
    private View waveFragment;
    private GameManager gameManager;
    private Button nextWaveButton;
    private TextView welcomeMessage, waveFragment_goldTextView, waveFragment_waveTextView, waveFragment_healthTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        waveFragment = inflater.inflate(R.layout.fragment_wave, container, false);
        Log.d("Test", "wavefragment");

        init();
        initOnClickListeners();
        initValues();

        return waveFragment;
    }

    private void initValues() {
        waveFragment_goldTextView.setText("Momentanes Gold: "  +  gameManager.getGold());
        waveFragment_waveTextView.setText("Eben beendete Welle: "  +  gameManager.getWave());
        waveFragment_healthTextView.setText("Ihr momentanes Leben: "  +  gameManager.getPlayerHealth());

    }

    public void init(){
        nextWaveButton = (Button) waveFragment.findViewById(R.id.waveFragment_startWaveButton);
        welcomeMessage = waveFragment.findViewById(R.id.waveFragment_titleTextView);
        waveFragment_goldTextView = waveFragment.findViewById(R.id.waveFragment_goldTextView);
        waveFragment_waveTextView = waveFragment.findViewById(R.id.waveFragment_waveTextView);
        waveFragment_healthTextView = waveFragment.findViewById(R.id.waveFragment_healthTextView);
    }

    public void initOnClickListeners(){
        nextWaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId()) {
            case R.id.waveFragment_startWaveButton: {
                welcomeMessage.setText("drücken sie auf NÄCHSTE WELLE STARTEN");
                Log.d("Test", " " + welcomeMessage.getText());
                gameManager.startWave();
                gameManager.closeBuyOrUpgradeMenu();
                break;
            }
        }

    }

    public void setGameManager(GameManager gameManager){
        this.gameManager = gameManager;
    }
}
