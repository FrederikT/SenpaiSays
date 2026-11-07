package ie.griffith.thuss.simon.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import ie.griffith.thuss.simon.R;

public class SimonCustomView extends View {


    public interface SquareTapListener {
        void onSquareTap(int square);
    }

    private SquareTapListener squareTapListener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    private final double width = displayMetrics.widthPixels;
    // Originally designed using the pixel 3 XL (resolution of 1440 x 2960 pixels and 522 ppi pixel density)
    // For responsive design the sizes will be calculated
    // depending on the screen size of the pixel 3 XL and the Screensize of the device that is used
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
        init(null);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SimonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    /**
     * initializes the Rects and Paints for this View
     * @param set
     */
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

    /**
     * draws the Squares depending on corresponding class variables
     * @param canvas Canvas
     */
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

    public void setSquareTapListener(SquareTapListener listener) {
        squareTapListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN || squareTapListener == null) {
            return true;
        }

        float x = event.getX();
        float y = event.getY();
        if (yellowRectSquare.contains((int) x, (int) y)) {
            squareTapListener.onSquareTap(1);
        } else if (pinkRectSquare.contains((int) x, (int) y)) {
            squareTapListener.onSquareTap(2);
        } else if (blueRectSquare.contains((int) x, (int) y)) {
            squareTapListener.onSquareTap(3);
        } else if (greenRectSquare.contains((int) x, (int) y)) {
            squareTapListener.onSquareTap(4);
        }
        return true;
    }

    //redundant code within cases could be put in extra method e.g. using Paint and Color Arrays - readability would get worse.
    // Creating new class for this with Pain, two colors and creating a array of these would also be possible, but to much for this assignment

    /**
     *  Highlights a field by setting the color to a brighter tone and setting it back after waiting for the highlight duration milliseconds
     * @param squareNumber number of square that should blink
     */
    public void blink(int squareNumber, int durationMs) {
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
                }, durationMs);
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
                }, durationMs);
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
                }, durationMs);
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
                }, durationMs);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        yellowPaintSquare.setColor(getResources().getColor(R.color.yellow));
        pinkPaintSquare.setColor(getResources().getColor(R.color.pink));
        bluePaintSquare.setColor(getResources().getColor(R.color.blue));
        greenPaintSquare.setColor(getResources().getColor(R.color.green));
        super.onDetachedFromWindow();
    }
}
