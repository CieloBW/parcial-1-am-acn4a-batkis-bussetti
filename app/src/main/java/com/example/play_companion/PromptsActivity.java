package com.example.play_companion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup;

public class PromptsActivity extends AppCompatActivity implements GetPrompts.AsyncResponse {

    Button rollButton;
    Spinner pcSpinner;
    TextView promptText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompts);

        checkConnection();

        rollButton = findViewById(R.id.pc_roll_button);
        promptText = findViewById(R.id.pc_prompt_text);
        pcSpinner = findViewById(R.id.pc_spinner);

        String[] options = {"Movies", "Animals", "Objects", "Places"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.pc_white));
                return view;
            }
        };

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrompt(pcSpinner.getSelectedItem().toString().toLowerCase());
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pcSpinner.setAdapter(adapter);
    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void getPrompt(String cat) {
        int diceSize = getResources().getDimensionPixelSize(R.dimen.pc_dice_size);
        promptText.setTextSize(TypedValue.COMPLEX_UNIT_PX, diceSize);
        promptText.setText(R.string.pc_question);

        GetPrompts getPrompts = new GetPrompts();
        getPrompts.delegate = this;
        getPrompts.execute("https://playcompanion.juanbatkis.repl.co/get-prompt?cat=" + cat);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.pc_rotate_infinite);
        promptText.startAnimation(rotate);
    }

    @Override
    public void processFinish(String result) {
        int titleSize = getResources().getDimensionPixelSize(R.dimen.pc_title_size);
        promptText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        promptText.setText(result);
        promptText.clearAnimation();
    }

    public void returnMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
