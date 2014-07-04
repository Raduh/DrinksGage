package com.raduh.drinksgage;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        Intent res_Act = getIntent();

        final GlobalData globalState = (GlobalData) getApplication();

        final double initialBAC = res_Act.getDoubleExtra("initialBAC", 0);
        final double beta = res_Act.getDoubleExtra("beta", 0.0015);


        final TextView res_bac = (TextView) findViewById(R.id.result_bac);
        final TextView consum = (TextView) findViewById(R.id.consumption_sum);
        final TextView bac_descr = (TextView) findViewById(R.id.bac_description);
        final ImageView bacIcon = (ImageView) findViewById(R.id.image_bac);
        final Button chron = (Button) findViewById(R.id.chronom);

        //bacIcon.setScaleType(ImageView.ScaleType.FIT_START);

        final Dialog hoursPassed = new Dialog(this);
        hoursPassed.setContentView(R.layout.when_started_drinking);
        hoursPassed.setTitle("How long ago?");
        hoursPassed.setCanceledOnTouchOutside(false);
        hoursPassed.setCancelable(false);
        hoursPassed.show();

        final WheelView startTime = (WheelView) hoursPassed.findViewById(R.id.wheel);
        startTime.setCyclic(true);
        final NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 24);
        hourAdapter.setItemResource(R.layout.wheel_item_hours);
        hourAdapter.setItemTextResource(R.id.time_item);
        startTime.setViewAdapter(hourAdapter);
        Button setHours = (Button) hoursPassed.findViewById(R.id.see_bac);
        setHours.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //SharedPreferences othData = getApplicationContext().getSharedPreferences("otherData", 0);
                int timePassed = Integer.parseInt((String) hourAdapter.getItemText(startTime.getCurrentItem()));
                //othData.edit().putInt("hourStarted", timePassed).commit();

                double currentBAC = initialBAC - (timePassed * beta);
                if (currentBAC < 0) currentBAC = 0;
                double bacToBeDisplayed = Math.floor(currentBAC * 100000) / 100;

                int hoursLeft = (int) (currentBAC / beta);
                int minutesLeft = (int) (((currentBAC - (beta * hoursLeft)) / beta) * 60);

                if (currentBAC < 0) {
                    hoursLeft = 0;
                    minutesLeft = 0;
                }

                res_bac.setText("" + bacToBeDisplayed);

                String myDrinks = "\u2022 Drinks you had: ";

                for (int i = 0; i < globalState.bev_nr; i++)
                    if (globalState.beverages[i].count != 0)
                        myDrinks = myDrinks + "\n\t\u2022 " + globalState.beverages[i].count + "x "
                                + globalState.beverages[i].volume + "ml of "
                                + globalState.beverages[i].alcohol_content + "% " + globalState.beverages[i].name;
                consum.setText(myDrinks);

                bac_descr.setText("\u2022 Symptoms: " + globalState.getBacEffectsString(bacToBeDisplayed / 10));
                bacIcon.setImageResource(globalState.getBacEffectsDrawable(bacToBeDisplayed / 10));

                new CountDownTimer((hoursLeft * 3600 + minutesLeft * 60) * 1000, 1000) {

                    @Override
                    public void onFinish() {
                        chron.setText("You are clean now!");

                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long sLeft = millisUntilFinished / 1000;
                        int mLeft = (int) ((sLeft % 3600) / 60);
                        int hLeft = (int) (sLeft / 3600);
                        sLeft = sLeft - (mLeft * 60) - (hLeft * 3600);
                        chron.setText("" + hLeft + " hours " + mLeft + " minutes " + sLeft + " seconds");

                    }

                }.start(); //end of the CountDownTimer


                hoursPassed.dismiss();

            }
        }); //end of the onClickListener


    } // end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeResult(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void shareIt(View view) {
        Vibrator vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(50);

        TextView res_bac = (TextView) findViewById(R.id.result_bac);
        TextView consum = (TextView) findViewById(R.id.consumption_sum);
        Button chron = (Button) findViewById(R.id.chronom);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        String content = (String) consum.getText();
        content = content.substring(17);
        content = "I had: " + content;
        content = content + "\n So now I have a BAC (blood alcohol level) of " + res_bac.getText()
                + ". I will be back to normal in " + chron.getText();
        content = content + "\n\nhttps://play.google.com/store/apps/details?id=com.raduh.drinksgage";
        String title = "Drinks gage results";

        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(share);

    }

}
