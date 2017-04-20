package net.borkiss.looperdemo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LooperThread mLooperThread;
    private TextView text2;

    private class LooperThread extends Thread {

        private Handler mHandler;

        public void run() {
            Looper.prepare();
            mHandler = new Handler() {

                public void handleMessage(Message msg) {
                    if (msg.what == 0) {
                        doLongRunningOperation();
                    }
                }
            };
            Looper.loop();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        textView.setOnClickListener(this);

        mLooperThread = new LooperThread();
        mLooperThread.start();
    }

    @Override
    public void onClick(View v) {
        text2.setText("");
        if (mLooperThread.mHandler != null) {
            Message msg = mLooperThread.mHandler.obtainMessage(0);
            mLooperThread.mHandler.sendMessage(msg);
        }
    }

    private void doLongRunningOperation() {
        for (int i = 0; i < 100000; i++) {
            Log.d("LOOPER", "i = " + i);
            final String value = Integer.toString(i);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text2.setText(value);
                }
            });
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mLooperThread.mHandler.getLooper().quit();
    }

}
