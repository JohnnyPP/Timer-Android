package app.timer;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;
import android.os.Handler;

public class Timer extends ActionBarActivity {

    TextView textViewTimerValue;
    TextView textView;

    private long startTime = 0L;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private Runnable updateTimerThread = new Runnable()
    {
        public void run()
        {
            try
            {
                textViewTimerValue = (TextView) findViewById(R.id.timerValue);

                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updatedTime % 1000);
                textViewTimerValue.setText("" + mins + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
                customHandler.postDelayed(this, 0);
            }
            catch (Exception e)
            {
                textViewTimerValue.setText("Error" + e);
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
            return rootView;
        }
    }

    public void Start(View view)
    {
        new CountDownTimer(30000, 1000)
        {

            public void onTick(long millisUntilFinished)
            {
                textView = (TextView) findViewById(R.id.textViewTimer);
                textView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish()
            {
                textView = (TextView) findViewById(R.id.textViewTimer);
                textView.setText("done!");
            }
        }.start();

    }

    public void TimerStart(View view)
    {
        textView = (TextView) findViewById(R.id.timerValue);

        try
        {
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }
        catch (Exception e)
        {
            textView.setText("Error: " + e);
        }

    }

    public void TimerPause()
    {
        textView = (TextView) findViewById(R.id.timerValue);

        try
        {
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }
        catch (Exception e)
        {
            textView.setText("Error: " + e);
        }
    }

}
