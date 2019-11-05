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
    public static  GameMechanics mechanics;
    private static TextView roundCounter;
    private static List<Highscore> highscore;
    private static ListView list;
    private static Context mContext;
    private static EditText playerName;
    private static ScoreManager manager;
    private static Switch twoPlayer;
    SimonCustomView simon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        playerName = findViewById(R.id.playerName);
        roundCounter = findViewById(R.id.roundNumber);
        simon = findViewById(R.id.custom);
        twoPlayer=findViewById(R.id.switch1);
        mContext = getApplicationContext();
        mechanics = new GameMechanics(simon, getApplicationContext());
        highscore = new ArrayList<>();
        manager = new ScoreManager(getApplicationContext());
        playerName.setText(manager.getPlayername());
        showHighscore();
    }

    /**
     * OnClickMethod for the startGame Button (id:start_btn)
     * @param view View
     */
    public void startGame(View view) {
        mechanics.playGame(twoPlayer.isChecked());
    }

    /**
     *  sets Text of TextView roundCounter (id:roundNumber)
     * @param number number of current round
     */
    public static void setRound(int number){
        roundCounter.setText(String.valueOf(number));
    }

    /**
     * Adds Score to database and displays it by calling {@link #setHighscore(int)}
     * Player name for Highscore will be read from the EditText playerName -if empty Player 1 will be used
     * @param score new Highscore
     */
    public static void setHighscore(int score){
        String name = playerName.getText().toString();
        if(name.trim().equals("")){
            name = "Player 1";
        }
        manager.saveNewScore(name, score);
        showHighscore();

    }

    /**
     * Reads the Top Ten Highscore from the database and sets that list for the ListView (id:list)
     * uses {@link ScoreManager#getTopTen(Context)}
     */
    private static void showHighscore() {
        highscore = manager.getTopTen(mContext);
        ArrayAdapter<Highscore> arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_list_item_1,
                highscore);
        list.setAdapter(arrayAdapter);
    }

}
