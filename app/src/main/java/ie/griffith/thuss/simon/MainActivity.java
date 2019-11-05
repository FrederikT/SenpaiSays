package ie.griffith.thuss.simon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ie.griffith.thuss.simon.Views.SimonCustomView;

public class MainActivity extends AppCompatActivity {
    SimonCustomView simon;
    private static TextView roundCounter;
    private static List<Highscore> highscore;
    private static ListView list;
    private static Context mContext;
    private static EditText playerName;
    private static ScoreManager manager;
    private static Switch twoPlayer;
    public static  GameMechanics mechanics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        simon = findViewById(R.id.custom);
        mechanics = new GameMechanics(simon, getApplicationContext());
        roundCounter = findViewById(R.id.roundNumber);
        list = findViewById(R.id.list);
        playerName = findViewById(R.id.playerName);
        highscore = new ArrayList<>();
        manager = new ScoreManager(getApplicationContext());
        showHighscore();
        playerName.setText(manager.getPlayername());
        twoPlayer=findViewById(R.id.switch1);
    }


    public void startGame(View view) {
        mechanics.playGame(twoPlayer.isChecked());
    }

    public static void setRound(int number){
        roundCounter.setText(String.valueOf(number));
    }

    public static void setHighscore(int score){
        String name = playerName.getText().toString();
        if(name.trim().equals("")){
            name = "Player 1";
        }
        manager.saveNewScore(name, score);
        showHighscore();

    }

    private static void showHighscore() {
        highscore = manager.getTopTen(mContext);
        ArrayAdapter<Highscore> arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_list_item_1,
                highscore);
        list.setAdapter(arrayAdapter);
    }

}
