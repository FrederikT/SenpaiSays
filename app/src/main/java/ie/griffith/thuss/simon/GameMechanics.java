package ie.griffith.thuss.simon;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ie.griffith.thuss.simon.Views.SimonCustomView;

final class GameMechanics implements SimonCustomView.SquareTapListener {
    interface Callbacks {
        void onRoundChanged(int round);
        void onGameOver(int score, boolean saveScore);
        void onPlaybackBusy();
    }

    private static final int DELAY_SPEED = 800;
    private static final int HIGHLIGHT_DURATION_MS = DELAY_SPEED * 9 / 10;
    private static final int ROUND_DELAY = 1000;
    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Callbacks callbacks;
    private final List<MediaPlayer> playingSounds = new ArrayList<>();
    private int sequenceCounter;
    private final List<Integer> sequenceList = new ArrayList<>();
    private boolean isPlaying;
    private final SimonCustomView simonCustomView;
    private Random r = new Random();
    private boolean isTwoPlayer;
    private boolean isAppending=false;
    private int scoreCounter = 0;
    private int clickingCounter = 0;

    /**
     *  Constructor for GameMechanics
     * @param simon reference of SimonCustomView used in Layout
     * @param callbacks callbacks for the current game screen
     */
    GameMechanics(SimonCustomView simon, Callbacks callbacks) {
        simonCustomView = simon;
        context = simon.getContext().getApplicationContext();
        this.callbacks = callbacks;
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
    private void playSequence() {
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
                simonCustomView.blink(sequenceList.get(playIndex), HIGHLIGHT_DURATION_MS);
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
                playSound(audioRes);
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
    @Override
    public void onSquareTap(final int squarenumber) {
        if (!isPlaying) {
            return;
        }
        if (sequenceCounter != sequenceList.size()) {
            callbacks.onPlaybackBusy();
            return;
        }
        simonCustomView.blink(squarenumber, HIGHLIGHT_DURATION_MS);
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
                    callbacks.onRoundChanged(scoreCounter);
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
     * Plays the game-over sound and reports the final score.
     */
    private void gameOver(){
        isPlaying=false;
        playSound(R.raw.gameover);
        sequenceList.clear();
        /*
         two player mode has no high score, even though the overall pressed fields is the same,
         the played rounds are only half as in Single player mode.
         furthermore it supports cheating (achieving a very good High Score by pressing only one field over and over again
         */

        callbacks.onGameOver(scoreCounter, !isTwoPlayer);

        scoreCounter=0;
    }



    private void playSound(int audioResource) {
        MediaPlayer player = MediaPlayer.create(context, audioResource);
        if (player == null) {
            return;
        }
        playingSounds.add(player);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer finished) {
                if (playingSounds.remove(finished)) {
                    finished.release();
                }
            }
        });
        player.start();
    }

    void close() {
        handler.removeCallbacksAndMessages(null);
        simonCustomView.setSquareTapListener(null);
        for (MediaPlayer player : playingSounds) {
            player.release();
        }
        playingSounds.clear();
    }
}
