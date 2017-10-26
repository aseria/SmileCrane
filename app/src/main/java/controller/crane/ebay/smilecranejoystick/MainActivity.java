package controller.crane.ebay.smilecranejoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import controller.crane.ebay.smilecranejoystick.status.CRANE_STATUS;
import controller.crane.ebay.smilecranejoystick.status.StatusUpdateThread;


public class MainActivity extends AppCompatActivity {
    boolean isRunning = true;
    public static CRANE_STATUS STATUS = CRANE_STATUS.IDLE;

    private StatusUpdateThread statusUpdateThread;
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
        ((Button)findViewById(R.id.coin)).setOnTouchListener(buttonListener);

        this.statusUpdateThread = new StatusUpdateThread(this);
        this.statusUpdateThread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
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
                        case R.id.coin:
                            onCOIN();
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
    void onIDLE() { onTouchButton(CRANE_STATUS.IDLE);}
    void onUP() { onTouchButton(CRANE_STATUS.UP);}
    void onDOWN() { onTouchButton(CRANE_STATUS.DOWN);}
    void onLEFT() { onTouchButton(CRANE_STATUS.LEFT);}
    void onRIGHT() { onTouchButton(CRANE_STATUS.RIGHT);}
    void onZUP() { onTouchButton(CRANE_STATUS.ZUP);}
    void onZDOWN() { onTouchButton(CRANE_STATUS.ZDOWN);}
    void onCOIN() { onTouchEvent(CRANE_STATUS.COIN;)}



    private void onTouchButton(CRANE_STATUS status) {
        MainActivity.STATUS = status;
        ((TextView)findViewById(R.id.status_text)).setText(MainActivity.STATUS.toString());
        Log.d("STATUS", MainActivity.STATUS.toString());
    }
}
