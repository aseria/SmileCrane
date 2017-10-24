package controller.crane.ebay.smilecranejoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

enum CRANE_STATUE {
    IDLE, UP, DOWN, LEFT, RIGHT, ZUP, ZDOWN
}
public class MainActivity extends AppCompatActivity {

    public static CRANE_STATUE STATUS = CRANE_STATUE.IDLE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ba);

        ((Button)findViewById(R.id.up)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.down)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.left)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.right)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.idle)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.zup)).setOnTouchListener(buttonListener);
        ((Button)findViewById(R.id.zdown)).setOnTouchListener(buttonListener);

    }

    private View.OnTouchListener buttonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // PRESSED
                    switch (v.getId()) {
                        case R.id.idle:
                            onIDLE();
                            break;
                        case R.id.up:
                            onUP();
                            break;
                        case R.id.down:;
                            onDOWN();
                            break;
                        case R.id.left:
                            onLEFT();
                            break;
                        case R.id.right:
                            onRIGHT();
                            break;
                        case R.id.zup:
                            onZUP();
                            break;
                        case R.id.zdown:
                            onZDOWN();
                            break;
                    }
                    return true; // if you want to handle the touch event
                case MotionEvent.ACTION_UP:
                    // RELEASED
                    onIDLE();
                    return true; // if you want to handle the touch event
            }
            return false;
        }
    };

    //button click listener
    void onIDLE() { onTouchButton(CRANE_STATUE.IDLE);}
    void onUP() { onTouchButton(CRANE_STATUE.UP);}
    void onDOWN() { onTouchButton(CRANE_STATUE.DOWN);}
    void onLEFT() { onTouchButton(CRANE_STATUE.LEFT);}
    void onRIGHT() { onTouchButton(CRANE_STATUE.RIGHT);}
    void onZUP() { onTouchButton(CRANE_STATUE.ZUP);}
    void onZDOWN() { onTouchButton(CRANE_STATUE.ZDOWN);}



    private void onTouchButton(CRANE_STATUE status) {
        MainActivity.STATUS = status;
        ((TextView)findViewById(R.id.status_text)).setText(MainActivity.STATUS.toString());
        Log.d("STATUS", MainActivity.STATUS.toString());
    }
}
