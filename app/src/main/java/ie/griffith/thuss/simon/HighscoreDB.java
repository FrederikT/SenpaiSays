package ie.griffith.thuss.simon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HighscoreDB extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context context;

    public HighscoreDB(Context context){
        super(context, "highscore.db", null,1);
        this.context = context;
        this.db = getWritableDatabase();
        this.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        try {
            String sql = "CREATE TABLE score (player_name TEXT, score Number, Primary key(player_name,score))";
            db.execSQL(sql);
            Log.e("CorkDevelop", "onCreate: table successfully created");
        }catch (Exception e){
            Log.e("CorkDevelop", "onCreate: "+e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //No upgrade necessary in current version of the app
        Log.e("CorkDevelop", "Upgrade not yet implemented");
    }


    public void addScore(Highscore newScore){
        try {
            String sql = "Insert into score (player_name,score) Values('"+newScore.getPlayername()+"', "+newScore.getScore()+")";
            db.execSQL(sql);
            Log.e("CorkDevelop", "successfully added score entry to db ");

        }catch (Exception e){
            Log.e("CorkDevelop", "on Insert: "+e.getMessage());
        }
    }



    public List<Highscore> getHighscore(){
        List<Highscore> highscore = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("Select * from score Order by score desc", null);
            cursor.moveToNext();
            while(!cursor.isAfterLast()){
                highscore.add(new Highscore(cursor.getString(0),Integer.parseInt(cursor.getString(1))));
                cursor.moveToNext();
                Log.e("CorkDevelop", "on Select: getHighscore() successfully added score from db to list ");
            }
            cursor.close();
            return highscore;

        }catch (Exception e){
            Log.e("CorkDevelop", "on Select: getHighscore() "+e.getMessage());
        }

        return highscore;
    }
    public List<Highscore> getTopTen(){
        List<Highscore> highscore = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("Select * from score Order by score desc Limit 10", null);
            cursor.moveToNext();
            while(!cursor.isAfterLast()){
                highscore.add(new Highscore(cursor.getString(0),Integer.parseInt(cursor.getString(1))));
                cursor.moveToNext();
                Log.e("CorkDevelop", "on Select: getTopTen() successfully added score from db to list ");
            }
            cursor.close();
            return highscore;

        }catch (Exception e){
            Log.e("CorkDevelop", "on Select: getTopTen() "+e.getMessage());
        }

        return highscore;
    }

    public String getLastPlayerName(){
        String playerName = "";
        try{
            Cursor cursor = db.rawQuery("Select player_name from score Where player_name != 'Player 1'" +
                    "Order by rowid LIMIT 1", null);
            cursor.moveToNext();
            while(!cursor.isAfterLast()){
                playerName= cursor.getString(0);
                cursor.moveToNext();
                Log.e("CorkDevelop", "on Select: getLastPlayerName successfully read from db");
            }
            cursor.close();
            return playerName;

        }catch (Exception e){
            Log.e("CorkDevelop", "on Select: getTopTen() "+e.getMessage());
        }
        return playerName;
    }


}
