package ie.griffith.thuss.simon.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import ie.griffith.thuss.simon.GameMechanics;
import ie.griffith.thuss.simon.MainActivity;
import ie.griffith.thuss.simon.R;

public class SimonCustomView extends View {


    private static Context mContext;
    private final Handler handler = new Handler();
    private final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    private final double width = displayMetrics.widthPixels;
    // Originally designed using the pixel 3 XL (resolution of 1440 x 2960 pixels and 522 ppi pixel density)
    // For responsive design the sizes will be calculated depending on the screen size of the pixel 3 XL and the screensize of the device that is used
    private final int SQUARE_SIZE = (int)(600*(width/1440));
    private final int[] TOP_LEFT = {(int)(100*(width/1440)),(int)(120*(width/1440))};

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
            if(MainActivity.mechanics.isPlaying) {
                if (MainActivity.mechanics.sequenceCounter == MainActivity.mechanics.sequenceList.size()) {
                    float x = e.getX();
                    float y = e.getY();

                    if ((x > TOP_LEFT[1] && x < TOP_LEFT[1] + SQUARE_SIZE) && (y > TOP_LEFT[0] && y < TOP_LEFT[0] + SQUARE_SIZE)) {
                        //within top left square
                        MainActivity.mechanics.evaluateClick(1);
                    } else if ((x > TOP_LEFT[1] + SQUARE_SIZE && x < TOP_LEFT[1] + (SQUARE_SIZE * 2)) && (y > TOP_LEFT[0] && y < TOP_LEFT[0] + SQUARE_SIZE)) {
                        //within top right square
                        MainActivity.mechanics.evaluateClick(2);
                    } else if ((x > TOP_LEFT[1] && x < TOP_LEFT[1] + SQUARE_SIZE) && (y > TOP_LEFT[0] + SQUARE_SIZE && y < TOP_LEFT[0] + (SQUARE_SIZE * 2))) {
                        //within bottom left square
                        MainActivity.mechanics.evaluateClick(3);

                    } else if ((x > TOP_LEFT[1] + SQUARE_SIZE && x < TOP_LEFT[1] + (SQUARE_SIZE * 2)) && (y > TOP_LEFT[0] + SQUARE_SIZE && y < TOP_LEFT[0] + (SQUARE_SIZE * 2))) {
                        //within bottom right square
                        MainActivity.mechanics.evaluateClick(4);
                    } else {
                        MainActivity.mechanics.playSequence();
                    }
                } else {
                    Toast t = Toast.makeText(mContext, "Please wait for sequence to finish", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }

        return true;
    }





    //redundant code within cases could be put in extra method e.g. using Paint and Color Arrays - readability would get worse.
    // Creating new class for this with Pain, two colors and creating a array of these would also be possible, but to much for this assignment
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
                }, GameMechanics.SPEED);
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
                }, GameMechanics.SPEED);
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
                }, GameMechanics.SPEED);
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
                }, GameMechanics.SPEED);
                break;
            default:
                break;
        }

    }

    public static void showGameOverDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("GAME OVER \nYour Score: "+score);
        builder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog myDialog = builder.create();
        myDialog.show();
    }

}


