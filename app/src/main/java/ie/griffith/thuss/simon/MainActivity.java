package ie.griffith.thuss.simon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public static int deviceHeight;
    public static int deviceWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth= size.x;
        deviceHeight = size.y;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
