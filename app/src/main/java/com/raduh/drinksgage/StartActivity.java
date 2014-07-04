package com.raduh.drinksgage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

    GlobalData globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        globalState = (GlobalData) getApplication();


        //making sure preferences are in effect
        SharedPreferences otherData = this.getSharedPreferences("otherData", 0);
        otherData.edit().commit();

        SharedPreferences bevNames = this.getSharedPreferences("bevNames", 0);
        bevNames.edit().commit();

        SharedPreferences bevAlc = this.getSharedPreferences("bevAlc", 0);
        bevAlc.edit().commit();

        SharedPreferences bevVol = this.getSharedPreferences("bevVol", 0);
        bevVol.edit().commit();


        globalState.initData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    public void startDrinks(View parentView) {

        Intent drinksActivity = new Intent(this, Drinks.class);
        startActivity(drinksActivity);

    }

    public void clearData(View parentView) {
        SharedPreferences otherData = this.getSharedPreferences("otherData", 0);
        otherData.edit().clear().commit();

        SharedPreferences bevNames = this.getSharedPreferences("bevNames", 0);
        bevNames.edit().clear().commit();

        SharedPreferences bevAlc = this.getSharedPreferences("bevAlc", 0);
        bevAlc.edit().clear().commit();

        SharedPreferences bevVol = this.getSharedPreferences("bevVol", 0);
        bevVol.edit().clear().commit();

        globalState.initData(this);


        Toast.makeText(this, "Reset complete.", Toast.LENGTH_SHORT).show();
    }

    public void setBodyData(View parentView) {

        final Dialog dial = new Dialog(parentView.getContext());
        dial.setTitle("Body data");
        dial.setContentView(R.layout.body_data_dial);
        dial.show();
        dial.setCanceledOnTouchOutside(true);


        final SharedPreferences othData = this.getSharedPreferences("otherData", 0);
        int wgt = othData.getInt("userWeight", -1);
        boolean isM = othData.getBoolean("isMale", false);
        boolean isF = othData.getBoolean("isFemale", false);

        final SeekBar weight = (SeekBar) dial.findViewById(R.id.weight);
        final TextView weight_txt = (TextView) dial.findViewById(R.id.weight_txt);

        if (wgt != -1) {
            weight.setProgress(wgt - 20);
            weight_txt.setText(" " + wgt + " kg");
        }

        weight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar arg0,
                                          int progressNew,
                                          boolean fromUser) {
                weight_txt.setText(" " + (progressNew + 20) + " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        Button done = (Button) dial.findViewById(R.id.done_data);


        final RadioButton male = (RadioButton) dial.findViewById(R.id.male_radio);
        final RadioButton female = (RadioButton) dial.findViewById(R.id.female_radio);

        OnCheckedChangeListener radioListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton but, boolean isChecked) {

                int id = but.getId();

                if ((id == R.id.male_radio) && isChecked)
                    female.setChecked(false);

                if ((id == R.id.female_radio) && isChecked)
                    male.setChecked(false);
            }
        };//end of the listener

        male.setOnCheckedChangeListener(radioListener);
        female.setOnCheckedChangeListener(radioListener);

        if (isM) male.setChecked(true);
        if (isF) female.setChecked(true);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (male.isChecked())
                    othData.edit().putBoolean("isMale", true)
                            .putBoolean("isFemale", false).commit();
                if (female.isChecked())
                    othData.edit().putBoolean("isMale", false)
                            .putBoolean("isFemale", true).commit();

                othData.edit().putInt("userWeight",
                        (weight.getProgress() + 20)).commit();

                dial.dismiss();
            }
        });
    }//end of setBodyData()

    public void showResult(View view) {

        double r, beta;
        double initialBAC;

        SharedPreferences othData = getSharedPreferences("otherData", MODE_PRIVATE);

        int userWeight = othData.getInt("userWeight", -1);

        if (!othData.getBoolean("isMale", false) &&
                (!othData.getBoolean("isFemale", false))) {

            Toast.makeText(this, "Set body data first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userWeight == -1) {
            Toast.makeText(this, "Set body data first.", Toast.LENGTH_SHORT).show();
            return;
        }

        globalState.computeStandardDrinks();
        double stdDrinks = globalState.standardDrinks;

        if (stdDrinks == 0) {
            Toast.makeText(this, "You drank nothing.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (othData.getBoolean("isMale", false)) {
            r = 0.68;
            beta = 0.00015;
        } else {
            r = 0.55;
            beta = 0.00017;
        }

        initialBAC = 0.48 * stdDrinks / (userWeight * 35.274 * r);

        Intent resAct = new Intent(this, ResultActivity.class);
        resAct.putExtra("initialBAC", initialBAC);
        resAct.putExtra("beta", beta);
        startActivity(resAct);
    }//end of showResult
}
