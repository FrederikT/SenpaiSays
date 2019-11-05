package ie.griffith.thuss.simon;

import androidx.annotation.NonNull;

public class Highscore {
    private String playername;
    private int score;

    public Highscore(String playername, int score){
        this.playername = playername;
        this.score = score;
    }

    public String getPlayername() {
        return playername;
    }

    public int getScore() {
        return score;
    }

    @NonNull
    @Override
    public String toString() {
        return playername+"  -  "+score;
    }
}
