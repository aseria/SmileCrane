package controller.crane.ebay.smilecranejoystick;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

enum CRANE_STATUE {
    IDLE, COIN, UP, DOWN, LEFT, RIGHT, ZUP, ZDOWN
}
public class MainActivity extends IOIOActivity {

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


    class Looper extends BaseIOIOLooper {
        private DigitalOutput coin, up, down, left, right, zup, zdown;
        private DigitalOutput led;

        @Override
        protected void setup() throws ConnectionLostException {
            showVersions(ioio_, "IOIO connected!");

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
            switch(MainActivity.STATUS){
                case IDLE:
                    turnOn(false, false, false, false, false, false, false, false);
                    break;
                case COIN:
                    turnOn(true, true, false, false, false, false, false, false);
                    break;
                case UP:
                    turnOn(true, false, true, false, false, false, false, false);
                    break;
                case DOWN:
                    turnOn(true, false, false, true, false, false, false, false);
                    break;
                case LEFT:
                    turnOn(true, false, false, false, true, false, false, false);
                    break;
                case RIGHT:
                    turnOn(true, false, false, false, false, true, false, false);
                    break;
                case ZUP:
                    turnOn(true, false, false, false, false, false, true, false);
                    break;
                case ZDOWN:
                    turnOn(true, false, false, false, false, false, false, true);
                    break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        @Override
        public void disconnected() {
            toast("IOIO disconnected");
        }

        private void turnOn(boolean ledOn, boolean coinOn, boolean upOn, boolean downOn, boolean leftOn, boolean rightOn, boolean zupOn, boolean zdownOn){
            try {
                led.write(ledOn);
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

    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(VersionType.IOIOLIB_VER),
                ioio.getImplVersion(VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(VersionType.HARDWARE_VER)));
    }

    private void toast(final String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
