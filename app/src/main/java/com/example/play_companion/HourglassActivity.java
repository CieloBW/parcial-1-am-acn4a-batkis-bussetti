package com.example.play_companion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HourglassActivity extends AppCompatActivity {

    ConstraintLayout hourglassCont;
    ImageView hourglassFull;
    ImageView hourglassTop;
    ImageView hourglassBottom;
    ImageView hourglassSream;
    TextView timer;
    Button startButton;
    EditText secondsInput;
    private static final long TIMER_INTERVAL = 50;
    private int minValue = 1;
    private int maxValue = 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourglass);

        hourglassCont = findViewById(R.id.pc_hourglass_cont);
        hourglassFull = findViewById(R.id.pc_hourglass_full_img);
        hourglassTop = findViewById(R.id.pc_hourglass_top_img);
        hourglassBottom = findViewById(R.id.pc_hourglass_bottom_img);
        hourglassSream = findViewById(R.id.pc_hourglass_stream_img);
        timer = findViewById(R.id.pc_timer);
        startButton = findViewById(R.id.pc_start_button);
        secondsInput = findViewById(R.id.editTextNumber);

        secondsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInput(s);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHourglass();
            }
        });
    }

    private void startHourglass() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.pc_rotate_180);
        hourglassCont.startAnimation(rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hourglassFull.setAlpha(0F);
                hourglassTop.setAlpha(1F);
                hourglassBottom.setAlpha(1F);
                hourglassSream.setAlpha(1F);
                startButton.setEnabled(false);
                startButton.setAlpha(0.6F);
                startTimer(Integer.parseInt(secondsInput.getText().toString()));
            }
        }, 500);
    }

    private void startTimer(int time) {
        long milliseconds = time * 1000L;

        new CountDownTimer(milliseconds, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining seconds
                long secondsRemaining = millisUntilFinished / 1000;
                long millisSpent = milliseconds - millisUntilFinished;
                timer.setText(secondsRemaining + "s");
                float percentage = (float) millisUntilFinished / milliseconds;
                float percentageSpent = (float) millisSpent / milliseconds;
                hourglassTop.setScaleY(percentage);
                hourglassBottom.setScaleY(percentageSpent);
            }

            @Override
            public void onFinish() {
                // When the countdown finishes (reaches 0 seconds), update the TextView
                timer.setText("0s");
                hourglassTop.setScaleY(0);
                hourglassBottom.setScaleY(1);
                hourglassTop.setAlpha(0F);
                hourglassBottom.setAlpha(0F);
                hourglassSream.setAlpha(0F);
                hourglassFull.setAlpha(1F);
                startButton.setEnabled(true);
                startButton.setAlpha(1F);
            }
        }.start();
    }

    private void validateInput(Editable s) {
        if (s.length() > 0) {
            int inputVal = Integer.parseInt(s.toString());
            if (inputVal < minValue || inputVal > maxValue) {
                if (inputVal < minValue) {
                    s.replace(0, s.length(), String.valueOf(minValue));
                } else {
                    s.replace(0, s.length(), String.valueOf(maxValue));
                }
            }
        } else {
            s.replace(0, s.length(), String.valueOf(minValue));
        }
    }

    public void returnMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}