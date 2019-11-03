package ie.griffith.thuss.simon.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ie.griffith.thuss.simon.R;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SimonCustomView extends View {

    private static final int SQUARE_SIZE = 600;
    private static final int[] TOP_LEFT = {100,120};
    private static  final int SPEED = 800;
    private Context mContext;

    List<Integer> list;
    private Rect yellowRectSquare;
    private Rect pinkRectSquare;
    private Rect blueRectSquare;
    private Rect greenRectSquare;
    private Paint yellowPaintSquare;
    private Paint pinkPaintSquare;
    private Paint bluePaintSquare;
    private Paint greenPaintSquare;

    final Handler handler = new Handler();


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
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        if( (x > TOP_LEFT[1] && x < TOP_LEFT[1]+SQUARE_SIZE) && (y > TOP_LEFT[0] && y < TOP_LEFT[0]+SQUARE_SIZE)){
            //within top left square
            blink(1);
        }else if( (x > TOP_LEFT[1]+SQUARE_SIZE && x < TOP_LEFT[1]+(SQUARE_SIZE*2)) && (y > TOP_LEFT[0] && y < TOP_LEFT[0]+SQUARE_SIZE)){
            //within top right square
            blink(2);
        }else if( (x > TOP_LEFT[1] && x < TOP_LEFT[1]+SQUARE_SIZE) && (y > TOP_LEFT[0]+SQUARE_SIZE && y < TOP_LEFT[0]+(SQUARE_SIZE*2))){
            //within bottom left square
            blink(3);

        }else if( (x > TOP_LEFT[1]+SQUARE_SIZE && x < TOP_LEFT[1]+(SQUARE_SIZE*2)) && (y > TOP_LEFT[0]+SQUARE_SIZE && y < TOP_LEFT[0]+(SQUARE_SIZE*2))) {
            //within bottom right square
            blink(4);
        }else{
           list = new ArrayList<Integer>();
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(2);
            list.add(4);
            list.add(1);
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(2);
            list.add(4);
            list.add(1);
            playSequence(list);
        }



        return true;
    }


    public void playSequence(List<Integer> sequence){
        for(int i=0; i < sequence.size(); i++){
            click(i);
        }
    }

    public void click(final int clickIndex) {
        //Function that clicks one place randomally on the view
        final Runnable r = new Runnable() {
            public void run() {
                blink(list.get(clickIndex));
                int audioRes=0;
                switch (list.get(clickIndex)){
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
                        Log.e("Game Error", "list contains other than field number");

                }
                MediaPlayer p = MediaPlayer.create(mContext, audioRes);
                p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                p.start();
            }
        };

        handler.postDelayed(r, (SPEED) * clickIndex); //click_index is nessesary, so that blink wont be simultanious.
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


