package com.example.sheeptracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterPredatorActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    Button btn_submitPred, btn_cancelPred;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_predator);

        btn_submitPred = (Button)findViewById(R.id.btn_submitPred);
        btn_cancelPred = (Button)findViewById(R.id.btn_cancelPred);
        radioGroup = (RadioGroup)findViewById(R.id.groupradio);

        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });

        // Add the Listener to the Submit Button
        btn_submitPred.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(RegisterPredatorActivity.this,
                            "No answer has been selected",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    RadioButton radioButton
                            = (RadioButton)radioGroup
                            .findViewById(selectedId);

                    Intent outIntent = new Intent();
                    outIntent.putExtra("PredatorType", radioButton.getText());
                    //retrieve data in OfflineMapActivity class in onActivityResult method
                    setResult(Activity.RESULT_OK, outIntent);
                    finish();

                    /*
                    RadioButton radioButton
                            = (RadioButton)radioGroup
                            .findViewById(selectedId);

                    Toast.makeText(RegisterPredatorActivity.this,
                            radioButton.getText(),
                            Toast.LENGTH_SHORT)
                            .show();

                     */
                }
            }
        });

        // Add the Listener to the Submit Button
        btn_cancelPred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                radioGroup.clearCheck();
            }
        });
    }
}