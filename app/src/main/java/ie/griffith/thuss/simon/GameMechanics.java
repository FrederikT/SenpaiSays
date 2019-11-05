package ie.griffith.thuss.simon;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ie.griffith.thuss.simon.Views.SimonCustomView;

public class GameMechanics {
    private static final int DELAY_SPEED = 800;
    public static final int SPEED = (int)((double)DELAY_SPEED*0.9);
    private static final int ROUND_DELAY = 1000;
    private static Context mContext;
    private final Handler handler = new Handler();
    public int sequenceCounter = 0;
    public List<Integer> sequenceList = new ArrayList<>();
    public boolean isPlaying;
    private SimonCustomView simonCustomView;
    private Random r = new Random();
    private boolean isTwoPlayer;
    private boolean isAppending=false;
    private int scoreCounter = 0;
    private int clickingCounter = 0;

    /**
     *  Constructor for GameMechanics
     * @param simon reference of SimonCustomView used in Layout
     * @param context Context
     */
    GameMechanics(SimonCustomView simon, Context context) {
        simonCustomView = simon;
        mContext = context;
    }

    /**
     * will start the game by adding a random field to {@link #sequenceList} and calling {@link #playSequence()}
     * @param isTwoPlayerMode
     */
    void playGame(boolean isTwoPlayerMode){
        if(!isPlaying){
            isTwoPlayer=isTwoPlayerMode;
            sequenceList.add(r.nextInt(4)+1);
            playSequence();
        }
    }

    /**
     * Plays Sequence.
     * for each item of {@link #sequenceList} the {@link #play(int)} method will be called
     */
    public void playSequence(){
        isPlaying = true;
        sequenceCounter = 0;
        clickingCounter = 0;
        for(int index=0;  index< sequenceList.size(); index++){
            play(index);

        }
    }


    /**
     * will call {@link SimonCustomView#blink(int)} with reference to square bases on position in {@link #sequenceList}
     * furthermore plays the sound correlating to each square
     *
     * Both will be delayed by position in sequence times  @{link #DELAY_SPEED}
     * @param playIndex position in sequence
     */
    private void play(final int playIndex) {
        //Function that clicks one place randomly on the view
        final Runnable r = new Runnable() {
            public void run() {
                simonCustomView.blink(sequenceList.get(playIndex));
                int audioRes=0;
                switch (sequenceList.get(playIndex)){
                    case 1:
                        audioRes = R.raw.baka;
                        break;
                    case 2:
                        audioRes = R.raw.sugoi;
                        break;
                    case 3:
                        audioRes = R.raw.keno;
                        break;
                    case 4:
                        audioRes = R.raw.nani;
                        break;
                    default:
                        Log.e("Game Error", "sequenceList contains other than field number");

                }
                MediaPlayer p = MediaPlayer.create(mContext, audioRes);
                p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                p.start();
                sequenceCounter++;
            }
        };
        //playIndex is nessesary, so that blink wont be simultaneous.
        //DELAY_SPEED is 10% longer than SPEED (Blinking time) because a field wouldn't blink twice in a row
        // because it the setting to the brighter color will immediatly be overwritten with the previous call to set it dark again
        handler.postDelayed(r, (DELAY_SPEED) * playIndex);
    }

    /**
     * Will check if the clicked Square was the correct one by comparing it to the Position in the {@link #sequenceList}
     *  Also handles logic for last click
     *      Single Player: reset counter variables, add new item to sequence and call {@link #playSequence()}
     *      Multi Player: awaits one more click after sequence completed which will be added to sequence
     *                    reset counter variables, add new item to sequence and call {@link #playSequence()}
     * @param squarenumber Squarenumber
     */
    public void evaluateClick(final int squarenumber){
        simonCustomView.blink(squarenumber);
        if(isTwoPlayer && isAppending) {
            sequenceCounter = 0;
            clickingCounter = 0;
            isAppending = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sequenceList.add(squarenumber);
                    playSequence();
                }
            }, ROUND_DELAY);
        }else{
            if (squarenumber == sequenceList.get(clickingCounter)) {
                // correct square has been pressed
                clickingCounter++;
                if (clickingCounter == sequenceList.size()) {
                    // sequence has been completed
                    scoreCounter++;
                    MainActivity.setRound(scoreCounter);
                    //only in single player - next one is appending
                    if (isTwoPlayer) {
                        isAppending = true;
                    } else {
                        sequenceCounter = 0;
                        clickingCounter = 0;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sequenceList.add(r.nextInt(4)+1);
                                playSequence();
                            }
                        }, ROUND_DELAY);
                    }
                }
            } else {
                //wrong square has been pressed
                gameOver();

            }
        }
    }

    /**
     *  Game Over method
     *  Plays GameOver Sound, resets variables, calls {@link MainActivity#setHighscore(int)} and {@link SimonCustomView#showGameOverDialog(int)}
     */
    private void gameOver(){
        isPlaying=false;
        //GameOver music
        MediaPlayer p = MediaPlayer.create(mContext, R.raw.gameover);
        p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        p.start();
        sequenceList.clear();
        /*
         two player mode has no high score, even though the overall pressed fields is the same,
         the played rounds are only half as in Single player mode.
         furthermore it supports cheating (achieving a very good High Score by pressing only one field over and over again
         */

        if(!isTwoPlayer) {
            MainActivity.setHighscore(scoreCounter);
        }
        //show score & Game over

        SimonCustomView.showGameOverDialog(scoreCounter);

        scoreCounter=0;
    }


}
