package com.thexabyte.simplemeditationapp;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView elapsedTimeTextView;
    EditText breathTimeEditText;
    EditText meditationTimeEditText;
    long startTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        elapsedTimeTextView = (TextView) findViewById(R.id.elapsedTimeTextView);
        breathTimeEditText = (EditText) findViewById(R.id.breathTimeEditText);
        meditationTimeEditText = (EditText) findViewById(R.id.meditationTimeEditText);

        final MediaPlayer chime = MediaPlayer.create(this, R.raw.chime2);

        //runs without a timer by reposting this handler at the end of the runnable
        final Handler timerHandler = new Handler();
        final Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
//                long millis = System.currentTimeMillis() - startTime;
//                int seconds = (int) (millis / 1000);
//                int minutes = seconds / 60;
//                seconds = seconds % 60;
//
//                elapsedTimeTextView.setText(String.format("%d:%02d", minutes, seconds));
                chime.start();


                final int breathTime = Integer.parseInt(breathTimeEditText.getText().toString());

                timerHandler.postDelayed(this, breathTime * 1000);
            }
        };

        final Handler elapsedTimeHandler = new Handler();
        final Runnable  elapsedTimerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                elapsedTimeTextView.setText(String.format("%d:%02d", minutes, seconds));
                elapsedTimeHandler.postDelayed(this, 1000);
            }
        };

        final ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startTime = System.currentTimeMillis();

                    meditationTimeEditText.setEnabled(false);
                    breathTimeEditText.setEnabled(false);
                    // The toggle is enabled
                    timerHandler.postDelayed(timerRunnable, 0);
                    elapsedTimeHandler.postDelayed(elapsedTimerRunnable, 0);

                    final int meditationTime = Integer.parseInt( meditationTimeEditText.getText().toString());
                    //turn the timer off
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toggle.setChecked(false);
                        }
                    }, meditationTime * 60 * 1000);
                } else {
                    meditationTimeEditText.setEnabled(true);
                    breathTimeEditText.setEnabled(true);

                    timerHandler.removeCallbacks(timerRunnable);
                    // The toggle is disabled
                }
            }
        });


    }
}
