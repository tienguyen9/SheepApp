package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterSheepActivity extends AppCompatActivity {

    Button btn_goBack, btn_confirm;
    EditText et_totalSheep, et_blackSheep, et_whiteSheep, et_redTies, et_yellowTies, et_greenTies, et_blueTies;
    TextView tv_header;
    CheckBox cb_redEar, cb_yellowEar, cb_greenEar;
    int totalSheep;
    int blackSheep;
    int whiteSheep;
    int redTies;
    int yellowTies;
    int greenTies;
    int blueTies;
    int redEar;
    int yellowEar;
    int greenEar;
    int existing;
    int sheepID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sheep);

        et_totalSheep = findViewById(R.id.et_numberTotal);
        et_blackSheep = findViewById(R.id.et_numberBlack);
        et_whiteSheep = findViewById(R.id.et_numberWhite);
        et_redTies = findViewById(R.id.et_redTies);
        et_yellowTies = findViewById(R.id.et_yellowTies);
        et_greenTies = findViewById(R.id.et_greenTies);
        et_blueTies = findViewById(R.id.et_blueTies);
        cb_redEar = findViewById(R.id.cb_redEar);
        cb_yellowEar = findViewById(R.id.cb_yellowEar);
        cb_greenEar = findViewById(R.id.cb_greenEar);
        tv_header = findViewById(R.id.tv_formHeader);

        Intent intent = getIntent();
        totalSheep = intent.getIntExtra("total_spotted", 0);
        blackSheep = intent.getIntExtra("blacks", 0);
        whiteSheep = intent.getIntExtra("whites", 0);
        redTies = intent.getIntExtra("red_ties", 0);
        yellowTies = intent.getIntExtra("yellow_ties", 0);
        greenTies = intent.getIntExtra("green_ties", 0);
        blueTies = intent.getIntExtra("blue_ties", 0);
        redEar = intent.getIntExtra("red_ear", 0);
        yellowEar = intent.getIntExtra("yellow_ear", 0);
        greenEar = intent.getIntExtra("green_ear", 0);
        sheepID = intent.getIntExtra("sheepID", -1);

        if(intent.getIntExtra("existing", 0) == 0){
            sheepID = -1;
            existing = 0;
        }else{
            tv_header.setText("Edit sheep entry " + sheepID);
            et_totalSheep.setText(String.valueOf(totalSheep));
            et_blackSheep.setText(String.valueOf(blackSheep));
            et_whiteSheep.setText(String.valueOf(whiteSheep));
            et_redTies.setText(String.valueOf(redTies));
            et_yellowTies.setText(String.valueOf(yellowTies));
            et_greenTies.setText(String.valueOf(greenTies));
            et_blueTies.setText(String.valueOf(blueTies));
            if(redEar == 1) {
                cb_redEar.setChecked(true);
            }
            if(yellowEar == 1) {
                cb_yellowEar.setChecked(true);
            }
            if(greenEar == 1) {
                cb_greenEar.setChecked(true);
            }
            existing = 1;

        }


        et_totalSheep.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                totalSheep = Integer.parseInt(s.toString());
            }
        });

        et_blackSheep.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                blackSheep = Integer.parseInt(s.toString());
            }
        });

        et_whiteSheep.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                whiteSheep = Integer.parseInt(s.toString());
            }
        });

        et_redTies.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                redTies = Integer.parseInt(s.toString());
            }
        });

        et_yellowTies.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                yellowTies = Integer.parseInt(s.toString());
            }
        });

        et_greenTies.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                greenTies = Integer.parseInt(s.toString());
            }
        });

        et_blueTies.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                blueTies = Integer.parseInt(s.toString());
            }
        });

        btn_goBack = findViewById(R.id.btn_goback);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int[] sheepNumbers = {totalSheep, blackSheep, whiteSheep};
                int[] tieNumbers = {redTies, yellowTies, greenTies, blueTies};
                boolean[] earMarks = {cb_redEar.isChecked(), cb_yellowEar.isChecked(), cb_greenEar.isChecked()};
                Intent outIntent = new Intent();
                outIntent.putExtra("SheepNumbers", sheepNumbers);
                outIntent.putExtra("TieNumbers", tieNumbers);
                outIntent.putExtra("EarMarks", earMarks);
                outIntent.putExtra("Existing", existing);
                outIntent.putExtra("SheepID", sheepID);
                //retrieve data in OfflineMapActivity class in onActivityResult method
                setResult(Activity.RESULT_OK, outIntent);
                finish();

            }
        });


    }


    public void increaseTotal(View v) {
        totalSheep++;
        et_totalSheep.setText(Integer.toString(totalSheep));
    }

    public void decreaseTotal(View v) {
        if (totalSheep > 0) {
            totalSheep--;
            et_totalSheep.setText(Integer.toString(totalSheep));
        }
    }

    public void increaseBlack(View v) {
        blackSheep++;
        et_blackSheep.setText(Integer.toString(blackSheep));
    }

    public void decreaseBlack(View v) {
        if (blackSheep > 0) {
            blackSheep--;
            et_blackSheep.setText(Integer.toString(blackSheep));
        }
    }

    public void increaseWhite(View v) {
        whiteSheep++;
        et_whiteSheep.setText(Integer.toString(whiteSheep));
    }

    public void decreaseWhite(View v) {
        if (whiteSheep > 0){
            whiteSheep--;
            et_whiteSheep.setText(Integer.toString(whiteSheep));
        }
    }

    public void increaseRedTies(View v) {
        redTies++;
        et_redTies.setText(Integer.toString(redTies));
    }

    public void decreaseRedTies(View v) {
        if (redTies >= 0) {
            redTies--;
            et_redTies.setText(Integer.toString(redTies));
        }
    }

    public void increaseYellowTies(View v) {
        yellowTies++;
        et_yellowTies.setText(Integer.toString(yellowTies));
    }

    public void decreaseYellowTies(View v) {
        if (yellowTies > 0) {
            yellowTies--;
            et_yellowTies.setText(Integer.toString(yellowTies));
        }

    }

    public void increaseGreenTies(View v) {
        greenTies++;
        et_greenTies.setText(Integer.toString(greenTies));
    }

    public void decreaseGreenTies(View v) {
        if (greenTies > 0) {
            greenTies--;
            et_greenTies.setText(Integer.toString(greenTies));
        }
    }

    public void increaseBlueTies(View v) {
        blueTies++;
        et_blueTies.setText(Integer.toString(blueTies));
    }

    public void decreaseBlueTies(View v) {
        if (blueTies > 0) {
            blueTies--;
            et_blueTies.setText(Integer.toString(blueTies));
        }
    }


}
