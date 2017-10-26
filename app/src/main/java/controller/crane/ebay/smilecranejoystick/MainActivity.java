package controller.crane.ebay.smilecranejoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;


import controller.crane.ebay.smilecranejoystick.status.CRANE_STATUS;
import controller.crane.ebay.smilecranejoystick.status.StatusUpdateThread;


public class MainActivity extends IOIOActivity {
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
    void onCOIN() { onTouchButton(CRANE_STATUS.COIN);}



    private void onTouchButton(CRANE_STATUS status) {
        MainActivity.STATUS = status;
        ((TextView)findViewById(R.id.status_text)).setText(MainActivity.STATUS.toString());
        Log.d("STATUS", MainActivity.STATUS.toString());
    }

    private  void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    class Looper extends BaseIOIOLooper {
        private DigitalOutput coin, up, down, left, right, zup, zdown;
        private DigitalOutput led;

        @Override
        protected void setup() throws ConnectionLostException {
            led = ioio_.openDigitalOutput(0, true);
            coin = ioio_.openDigitalOutput(40, false);
            up = ioio_.openDigitalOutput(41, false);
            down = ioio_.openDigitalOutput(42, false);
            left = ioio_.openDigitalOutput(43, false);
            right = ioio_.openDigitalOutput(44, false);
            zup = ioio_.openDigitalOutput(45, false);
            zdown = ioio_.openDigitalOutput(46, false);
        }

        @Override
        public void loop() throws ConnectionLostException {
            Log.d("status", ""+MainActivity.STATUS.toString());
            showToast("status"+MainActivity.STATUS.toString());

            switch(MainActivity.STATUS){
                case IDLE:
                    turnOn(false, false, false, false, false, false, false);
                    break;
                case COIN:
                    turnOn(true, false, false, false, false, false, false);
                    break;
                case UP:
                    turnOn(false, true, false, false, false, false, false);
                    break;
                case DOWN:
                    turnOn(false, false, true, false, false, false, false);
                    break;
                case LEFT:
                    turnOn(false, false, false, true, false, false, false);
                    break;
                case RIGHT:
                    turnOn(false, false, false, false, true, false, false);
                    break;
                case ZUP:
                    turnOn(false, false, false, false, false, true, false);
                    break;
                case ZDOWN:
                    turnOn(false, false, false, false, false, false, true);
                    break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }

        private void turnOn(boolean coinOn, boolean upOn, boolean downOn, boolean leftOn, boolean rightOn, boolean zupOn, boolean zdownOn){
            try {
                led.write(false);
                coin.write(coinOn);
                up.write(upOn);
                down.write(downOn);
                left.write(leftOn);
                right.write(rightOn);
                zup.write(zupOn);
                zdown.write(zdownOn);
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }
}
