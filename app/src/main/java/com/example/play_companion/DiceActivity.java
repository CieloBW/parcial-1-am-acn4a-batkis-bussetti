package com.example.play_companion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    Button rollButton;
    TextView diceNumber;
    ImageView diceImg;
    Random random = new Random();

    int diceSelected = 6;

    CheckBox d4Button;
    CheckBox d6Button;
    CheckBox d8Button;
    CheckBox d10Button;
    CheckBox d12Button;
    CheckBox d20Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        rollButton = findViewById(R.id.pc_roll_button);
        diceNumber = findViewById(R.id.pc_dice_number_1);
        diceImg = findViewById(R.id.pc_dice_img_1);



        d4Button = findViewById(R.id.pc_d4);
        d6Button = findViewById(R.id.pc_d6);
        d8Button = findViewById(R.id.pc_d8);
        d10Button = findViewById(R.id.pc_d10);
        d12Button = findViewById(R.id.pc_d12);
        d20Button = findViewById(R.id.pc_d20);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateDice();
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {changeDiceType(v);}
        };

        d4Button.setOnClickListener(onClickListener);
        d6Button.setOnClickListener(onClickListener);
        d8Button.setOnClickListener(onClickListener);
        d10Button.setOnClickListener(onClickListener);
        d12Button.setOnClickListener(onClickListener);
        d20Button.setOnClickListener(onClickListener);
    }

    private void rotateDice() {
        int i = random.nextInt(diceSelected)+1;

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.pc_rotate);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.pc_fade_out);

        diceNumber.startAnimation(fadeOut);
        diceImg.startAnimation(rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                diceNumber.setText(String.valueOf(i));
            }
        }, 500);
    }

    @SuppressLint("ResourceAsColor")
    private void changeDiceType(View view) {

        int id = view.getId();
        int d4Id = d4Button.getId();
        int d6Id = d6Button.getId();
        int d8Id = d8Button.getId();
        int d10Id = d10Button.getId();
        int d12Id = d12Button.getId();
        int d20Id = d20Button.getId();

        d4Button.setChecked(false);
        d6Button.setChecked(false);
        d8Button.setChecked(false);
        d10Button.setChecked(false);
        d12Button.setChecked(false);
        d20Button.setChecked(false);

        if (id == d4Id) {
            diceSelected = 4;
            d4Button.setChecked(true);
        } else if (id == d6Id) {
            diceSelected = 6;
            d6Button.setChecked(true);
        } else if (id == d8Id) {
            diceSelected = 8;
            d8Button.setChecked(true);
        } else if (id == d10Id) {
            diceSelected = 10;
            d10Button.setChecked(true);
        } else if (id == d12Id) {
            diceSelected = 12;
            d12Button.setChecked(true);
        } else if (id == d20Id) {
            diceSelected = 20;
            d20Button.setChecked(true);
        }

        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.pc_fade_out);
        diceImg.startAnimation(fadeOut);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) diceImg.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (diceSelected) {
                    case 4:
                    case 8:
                    case 20:
                        diceImg.setImageResource(R.drawable.triangle);
                        params.bottomMargin = (int) (40 * density);
                        break;
                    case 6:
                        diceImg.setImageResource(R.drawable.square);
                        params.bottomMargin = 0;
                        break;
                    case 10:
                        diceImg.setImageResource(R.drawable.kite);
                        params.bottomMargin = (int) (20 * density);
                        break;
                    case 12:
                        diceImg.setImageResource(R.drawable.pentagon);
                        params.bottomMargin = 0;
                        break;
                }
                diceImg.setLayoutParams(params);
            }
        }, 500);
    }

    public void returnMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}