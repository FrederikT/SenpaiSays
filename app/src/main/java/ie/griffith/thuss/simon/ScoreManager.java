package ie.griffith.thuss.simon;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/***
 * ScoreManager manages the database.
 * this class is the connection between the app and the database
 */
public class ScoreManager {
    HighscoreDB db;
    private List<Highscore> highscore = new ArrayList();

    /**
     *  Constructor for the ScoreManager
     * @param context Application context
     */
    public ScoreManager(Context context){
        db = new HighscoreDB(context);
    }

    /**
     *  adds a new Highscore to the database
     *  calls {@link #repairString(String)} to prevent some SQL errors
     * @param playerName name of the Player
     * @param score new Score
     */
    public void saveNewScore(String playerName, int score){
        playerName = repairString(playerName);
        Highscore highscore1 = new Highscore(playerName,score);
        db.addScore(highscore1);
    }

    /**
     *  Gets List of all Highscores in Database
     * @param context Context
     * @return List of all Highscores in database
     */
    public List<Highscore> getHighscore(Context context){
        highscore = db.getHighscore();
        return  highscore;
    }

    /**
     *  Gets List of 10 best Scores in Database
     * @param context Context
     * @return List of 10 best Scores
     */
    public List<Highscore> getTopTen(Context context){
        highscore = db.getTopTen();
        return  highscore;
    }


    /**
     *
     * @return
     */
    public String getPlayername(){
        return db.getLastPlayerName();
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