package ie.griffith.thuss.simon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simon = findViewById(R.id.custom);
        roundCounter = findViewById(R.id.roundNumber);
        list = findViewById(R.id.list);
        playerName = findViewById(R.id.playerName);
        mContext = getApplicationContext();
        highscore = new ArrayList<>();
        manager = new ScoreManager(getApplicationContext());
        showHighscore();
        playerName.setText(manager.getPlayername());
        Toast t = Toast.makeText(getApplicationContext(),manager.getPlayername(),Toast.LENGTH_SHORT);
        t.show();
    }


    public void startGame(View view) {
        simon.playGame();
    }

    public static void setRound(int number){
        roundCounter.setText(""+number);
    }

    public static void setHighscore(int score){
        Toast t = Toast.makeText(mContext, ""+score, Toast.LENGTH_SHORT);
        t.show();
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
