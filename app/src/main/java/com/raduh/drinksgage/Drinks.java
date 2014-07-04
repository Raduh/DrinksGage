package com.raduh.drinksgage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Drinks extends Activity {

    GlobalData globalState;
    private Vibrator vib;
    SharedPreferences otherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        globalState = (GlobalData) getApplication();

        otherData = this.getSharedPreferences("otherData", 0);


        final CustomAdapter my_awesome_adapter = new CustomAdapter(this, globalState.beverages);
        final ListView theList = (ListView) findViewById(R.id.lista_bauturi);

        LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View footVw = inf.inflate(R.layout.add_custom_button, null);

        footVw.findViewById(R.id.add_custom).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vib.vibrate(50);
                final Dialog dial = new Dialog(v.getContext());
                dial.setContentView(R.layout.new_drink_dialog);
                dial.setTitle("Add a new beverage");
                dial.show();
                dial.setCanceledOnTouchOutside(true);

                Button adder = (Button) dial.findViewById(R.id.dial_add);
                adder.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {

                        String new_name =
                                ((EditText) dial.findViewById(R.id.new_name)).getText().toString();

                        float new_alc = 0;
                        int new_vol = 0;
                        boolean rightData = true;

                        try {
                            new_alc = Float.parseFloat(
                                    ((EditText) dial.findViewById(R.id.new_alcohol_content)).getText().toString());

                            new_vol = Integer.parseInt(
                                    ((EditText) dial.findViewById(R.id.new_volume)).getText().toString());
                        } catch (Exception e) {
                            rightData = false;
                            Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < globalState.bev_nr; i++)
                            if (new_name.trim().compareTo(globalState.beverages[i].name) == 0) {
                                rightData = false;
                                Toast.makeText(getApplicationContext(), "Name of the beverage already exists", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        if (new_name.trim().equals("")) {
                            Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                            rightData = false;
                        }

                        if (rightData) {
                            Context ct = getApplicationContext();
                            SharedPreferences othDt = ct.getSharedPreferences("otherData", 0);
                            int bvNr = othDt.getInt("bev_nr", 9);
                            bvNr++;
                            othDt.edit().putInt("bev_nr", bvNr).commit();

                            ct.getSharedPreferences("bevNames", 0).edit().putString(("item" + (bvNr - 1)), new_name).commit();
                            ct.getSharedPreferences("bevAlc", 0).edit().putFloat(("item" + (bvNr - 1)), new_alc).commit();
                            ct.getSharedPreferences("bevVol", 0).edit().putInt(("item" + (bvNr - 1)), new_vol).commit();

                            globalState.initData(getApplicationContext());
                            my_awesome_adapter.updateContent(globalState.beverages);


                            dial.dismiss();
                        }
                    }
                });

            }
        });


        theList.addFooterView(footVw);
        theList.setAdapter(my_awesome_adapter);

        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drinks, menu);
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

            case R.id.done:
                NavUtils.navigateUpFromSameTask(this);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void increaseCount(View view) {

        vib.vibrate(50);

        View listRow = (View) view.getTag();
        TextView counter = (TextView) listRow.findViewById(R.id.drink_count);

        String title = (String) ((TextView) listRow.findViewById(R.id.beverage_name)).getText();

        int position = 0;

        for (int j = 0; j < globalState.beverages.length; j++)
            if (globalState.beverages[j].name.compareTo(title) == 0) {
                position = j;
                break;
            }

        globalState.beverages[position].count++;
        counter.setText("" + globalState.beverages[position].count);

        otherData.edit().putInt(("itemCount" + position), globalState.beverages[position].count).commit();

    }

    public void decreaseCount(View view) {

        vib.vibrate(50);

        View listRow = (View) view.getTag();
        TextView counter = (TextView) listRow.findViewById(R.id.drink_count);

        String title = (String) ((TextView) listRow.findViewById(R.id.beverage_name)).getText();

        int position = 0;

        for (int j = 0; j < globalState.beverages.length; j++)
            if (globalState.beverages[j].name.compareTo(title) == 0) {
                position = j;
                break;
            }
        if (globalState.beverages[position].count == 0) return;

        globalState.beverages[position].count--;
        counter.setText("" + globalState.beverages[position].count);

        otherData.edit().putInt(("itemCount" + position), globalState.beverages[position].count).commit();

    }


//	private void getOverflowMenu() {
//
//	     try {
//	        ViewConfiguration config = ViewConfiguration.get(this);
//	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//	        if(menuKeyField != null) {
//	            menuKeyField.setAccessible(true);
//	            menuKeyField.setBoolean(config, false);
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}

}
