package com.example.play_companion;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class CoinActivity extends AppCompatActivity {

    Button flipButton;
    ImageView coinImg;
    ImageView coinTImg;
    TextView coinText;
    Random random = new Random();

    Boolean heads = true;
    int spins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        flipButton = findViewById(R.id.pc_flip_button);
        coinImg = findViewById(R.id.pc_coin_img);
        coinTImg = findViewById(R.id.pc_coint_img);
        coinText = findViewById(R.id.pc_coin);

        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spins = random.nextInt(2) + 3;
                Animation rotate = AnimationUtils.loadAnimation(CoinActivity.this, R.anim.pc_rotate);
                rotate.setDuration( (810 * spins ) - 10);
                coinImg.startAnimation(rotate);
                coinTImg.startAnimation(rotate);
                flipCoin();
            }
        });
    }

    private void flipCoin() {
        Log.d("TAG", String.valueOf(spins));
        Log.d("TAG", String.valueOf(heads));
        coinText.setText(R.string.pc_question);
        flipButton.setEnabled(false);
        flipButton.setAlpha(0.6F);

        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.pc_scale_x_axis);
        Animator animR = AnimatorInflater.loadAnimator(this, R.animator.pc_scale_x_axis_r);

        if (heads) {
            heads = false;
            Log.d("TAG", "start");
            Log.d("TAG", String.valueOf(heads));
            anim.setTarget(coinImg);
            animR.setTarget(coinTImg);
            anim.start();
            animR.start();
        } else {
            heads = true;
            Log.d("TAG", "start");
            Log.d("TAG", String.valueOf(heads));
            anim.setTarget(coinTImg);
            animR.setTarget(coinImg);
            anim.start();
            animR.start();
        }

        spins--;
        if (spins > 0) {
            Log.d("TAG", "inIf");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "flip again");
                    flipCoin();
                }
            }, 810);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (heads) {
                        coinText.setText(R.string.pc_heads);
                    } else {
                        coinText.setText(R.string.pc_tails);
                    }
                    flipButton.setEnabled(true);
                    flipButton.setAlpha(1F);
                }
            }, 810);
        }
    }

    public void returnMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}