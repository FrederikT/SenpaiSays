package ie.griffith.thuss.simon;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/***
 * ScoreManager manages the database.
 * this class is the connection between the app and the database
 * all method names are self-explanatory
 */
public class ScoreManager {
    HighscoreDB db;

    private List<Highscore> highscore = new ArrayList();

    public ScoreManager(Context context){
        db = new HighscoreDB(context);
    }

    public void saveNewScore(String playerName, int score){
        playerName = repairString(playerName);
        Highscore highscore1 = new Highscore(playerName,score);
        db.addScore(highscore1);
    }

    public List<Highscore> getHighscore(Context context){
        highscore = db.getHighscore();
        return  highscore;
    }

    public List<Highscore> getTopTen(Context context){
        highscore = db.getTopTen();
        return  highscore;
    }


    /**
     * changes a string, so that strings can contain single '
     * without call of this method string cannot be used within database (sql) context
     *
     * @param s String which should be repaired
     * @return repaired string or null
     */
    private static String repairString(String s) {
        return s != null ? s.replaceAll("\'", "\'\'") : null;
    }
}