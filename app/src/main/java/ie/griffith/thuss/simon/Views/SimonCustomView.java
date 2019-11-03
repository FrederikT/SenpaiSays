package ie.griffith.thuss.simon.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import ie.griffith.thuss.simon.MainActivity;
import ie.griffith.thuss.simon.R;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonCustomView extends View {

    private static Context mContext;
    private final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    private final double width = displayMetrics.widthPixels;
    // Originally designed using the pixel 3 XL (resolution of 1440 x 2960 pixels and 522 ppi pixel density)
    // For responsive design the sizes will be calculated depending on the screen size of the pixel 3 XL and the screensize of the device that is used
    private final int SQUARE_SIZE = (int)(600*(width/1440));
    private final int[] TOP_LEFT = {(int)(100*(width/1440)),(int)(120*(width/1440))};
    private static  final int SPEED = 800;

    private List<Integer> sequenceList = new ArrayList<Integer>();
    private int sequenceCounter = 0;
    private int clickingCounter = 0;
    private int scoreCounter = 0;
    private boolean isPlaying;
    private Random r = new Random();
    final Handler handler = new Handler();
    private Rect yellowRectSquare;
    private Rect pinkRectSquare;
    private Rect blueRectSquare;
    private Rect greenRectSquare;
    private Paint yellowPaintSquare;
    private Paint pinkPaintSquare;
    private Paint bluePaintSquare;
    private Paint greenPaintSquare;




    public SimonCustomView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        yellowRectSquare = new Rect();
        pinkRectSquare = new Rect();
        blueRectSquare = new Rect();
        greenRectSquare = new Rect();
        yellowPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        pinkPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);

        yellowPaintSquare.setColor(getResources().getColor(R.color.yellow));
        pinkPaintSquare.setColor(getResources().getColor(R.color.pink));
        bluePaintSquare.setColor(getResources().getColor(R.color.blue));
        greenPaintSquare.setColor(getResources().getColor(R.color.green));
    }

    @Override
    protected void onDraw(Canvas canvas){

        yellowRectSquare.top = TOP_LEFT[0];
        yellowRectSquare.left = TOP_LEFT[1];
        yellowRectSquare.bottom = yellowRectSquare.top + SQUARE_SIZE;
        yellowRectSquare.right = yellowRectSquare.left + SQUARE_SIZE;

        pinkRectSquare.top = TOP_LEFT[0];
        pinkRectSquare.left = TOP_LEFT[1]+ SQUARE_SIZE;
        pinkRectSquare.bottom = pinkRectSquare.top + SQUARE_SIZE;
        pinkRectSquare.right = pinkRectSquare.left + SQUARE_SIZE;

        blueRectSquare.top = TOP_LEFT[0]+ SQUARE_SIZE;
        blueRectSquare.left = TOP_LEFT[1];
        blueRectSquare.bottom = blueRectSquare.top + SQUARE_SIZE;
        blueRectSquare.right = blueRectSquare.left + SQUARE_SIZE;

        greenRectSquare.top = TOP_LEFT[0]+ SQUARE_SIZE;
        greenRectSquare.left = TOP_LEFT[1]+ SQUARE_SIZE;
        greenRectSquare.bottom = greenRectSquare.top + SQUARE_SIZE;
        greenRectSquare.right = greenRectSquare.left + SQUARE_SIZE;


        canvas.drawRect(yellowRectSquare,yellowPaintSquare);
        canvas.drawRect(pinkRectSquare,pinkPaintSquare);
        canvas.drawRect(blueRectSquare,bluePaintSquare);
        canvas.drawRect(greenRectSquare,greenPaintSquare);


    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // for our use-case only the ACTION_DOWN event is necessary

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if(isPlaying) {
                if (sequenceCounter == sequenceList.size()) {
                    float x = e.getX();
                    float y = e.getY();

                    if ((x > TOP_LEFT[1] && x < TOP_LEFT[1] + SQUARE_SIZE) && (y > TOP_LEFT[0] && y < TOP_LEFT[0] + SQUARE_SIZE)) {
                        //within top left square
                        evaluateClick(1);
                    } else if ((x > TOP_LEFT[1] + SQUARE_SIZE && x < TOP_LEFT[1] + (SQUARE_SIZE * 2)) && (y > TOP_LEFT[0] && y < TOP_LEFT[0] + SQUARE_SIZE)) {
                        //within top right square
                        evaluateClick(2);
                    } else if ((x > TOP_LEFT[1] && x < TOP_LEFT[1] + SQUARE_SIZE) && (y > TOP_LEFT[0] + SQUARE_SIZE && y < TOP_LEFT[0] + (SQUARE_SIZE * 2))) {
                        //within bottom left square
                        evaluateClick(3);

                    } else if ((x > TOP_LEFT[1] + SQUARE_SIZE && x < TOP_LEFT[1] + (SQUARE_SIZE * 2)) && (y > TOP_LEFT[0] + SQUARE_SIZE && y < TOP_LEFT[0] + (SQUARE_SIZE * 2))) {
                        //within bottom right square
                        evaluateClick(4);
                    } else {
                        playSequence();
                    }
                } else {
                    Toast t = Toast.makeText(mContext, "Please wait for sequence to finish", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }

        return true;
    }

    private void evaluateClick(int squarenumber){
        blink(squarenumber);
        if(squarenumber == sequenceList.get(clickingCounter)){
            // correct square has been pressed
            clickingCounter++;
            if(clickingCounter == sequenceList.size()){
                // sequence has been completed
                scoreCounter++;
                MainActivity.setRound(scoreCounter);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSequence();

                    }
                }, 1000);
            }
        }else{
            //wrong square has been pressed
            gameOver();


        }

    }

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
        MainActivity.setHighscore(scoreCounter);
        //show score & Game over
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("GAME OVER \n Your Score: "+scoreCounter);
        builder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog myDialog = builder.create();
        myDialog.show();
        scoreCounter=0;
    }




    public void playSequence(){
        isPlaying = true;
        sequenceCounter = 0;
        clickingCounter = 0;
        sequenceList.add(r.nextInt(4)+1);
        for(int index=0;  index< sequenceList.size(); index++){
            play(index);

        }
    }


    private void play(final int playIndex) {
        //Function that clicks one place randomally on the view
        final Runnable r = new Runnable() {
            public void run() {
                blink(sequenceList.get(playIndex));
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

        handler.postDelayed(r, (SPEED) * playIndex); //playIndex is nessesary, so that blink wont be simultaneous.
    }


    public void blink(int squareNumber){
        switch (squareNumber){
            case 1:
                yellowPaintSquare.setColor(getResources().getColor(R.color.yellowBright));
                invalidate();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        yellowPaintSquare.setColor(getResources().getColor(R.color.yellow));
                        invalidate();
                    }
                }, SPEED);
                break;
            case 2:
                pinkPaintSquare.setColor(getResources().getColor(R.color.pinkBright));
                invalidate();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        pinkPaintSquare.setColor(getResources().getColor(R.color.pink));
                        invalidate();
                    }
                }, SPEED);
                break;
            case 3:
                bluePaintSquare.setColor(getResources().getColor(R.color.blueBright));
                invalidate();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        bluePaintSquare.setColor(getResources().getColor(R.color.blue));
                        invalidate();
                    }
                }, SPEED);
                break;
            case 4:
                greenPaintSquare.setColor(getResources().getColor(R.color.greenBright));
                invalidate();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        greenPaintSquare.setColor(getResources().getColor(R.color.green));
                        invalidate();
                    }
                }, SPEED);
                break;
            default:
                break;
        }

    }

}


