package ie.griffith.thuss.simon;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import ie.griffith.thuss.simon.Views.SimonCustomView;

public class MainActivity extends AppCompatActivity implements GameMechanics.Callbacks {
    private GameMechanics mechanics;
    private TextView roundCounter;
    private ListView list;
    private EditText playerName;
    private ScoreManager manager;
    private Switch twoPlayer;
    private AlertDialog gameOverDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        playerName = findViewById(R.id.playerName);
        roundCounter = findViewById(R.id.roundNumber);
        SimonCustomView simon = findViewById(R.id.custom);
        twoPlayer=findViewById(R.id.switch1);
        mechanics = new GameMechanics(simon, this);
        simon.setSquareTapListener(mechanics);
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
    @Override
    public void onRoundChanged(int number) {
        roundCounter.setText(String.valueOf(number));
    }

    /**
     * Adds Score to database and displays it by calling {@link #showHighscore()}
     * Player name for Highscore will be read from the EditText playerName -if empty Player 1 will be used
     * @param score new Highscore
     */
    private void saveHighscore(int score) {
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
    private void showHighscore() {
        List<Highscore> highscore = manager.getTopTen(getApplicationContext());
        ArrayAdapter<Highscore> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                highscore);
        list.setAdapter(arrayAdapter);
    }


    @Override
    public void onGameOver(int score, boolean saveScore) {
        if (saveScore) {
            saveHighscore(score);
        }
        gameOverDialog = new AlertDialog.Builder(this)
                .setTitle("GAME OVER \nYour Score: " + score)
                .setPositiveButton("Okay", null)
                .show();
    }

    @Override
    public void onPlaybackBusy() {
        Toast.makeText(this, "Please wait for sequence to finish", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mechanics.close();
        manager.close();
        if (gameOverDialog != null) {
            gameOverDialog.dismiss();
        }
        super.onDestroy();
    }
}
