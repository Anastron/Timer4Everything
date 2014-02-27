package com.example.timer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends ActionBarActivity {

    EditText nameTxt, infoTxt;
    TextView hourTxt, minTxt, secTxt, hourTimerTxt, minTimerTxt, secTimerTxt;
    List<TimerList> timerList = new ArrayList<TimerList>();

 //   private timer_service ts;
    private Intent intent;
    private Context context = this;

    int e_stunde = 0;
    int e_minute = 0;
    int e_sekunde = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.editTextTimerName);
        infoTxt = (EditText) findViewById(R.id.editTextInfo);
        hourTxt = (TextView) findViewById(R.id.txtViewAddH);
        minTxt = (TextView) findViewById(R.id.txtViewAddM);
        secTxt = (TextView) findViewById(R.id.txtViewAddS);

        hourTimerTxt = (TextView) findViewById(R.id.txtViewH);
        minTimerTxt = (TextView) findViewById(R.id.txtViewM);
        secTimerTxt = (TextView)findViewById(R.id.txtViewS);

     //   intent = new Intent(context, timer_service.class);
        context.startService(intent);
     //   ts = new timer_service();


        int hour, min, sec, allTimeInMin;

        hour = Integer.parseInt(hourTxt.getText().toString());
        min = Integer.parseInt(minTxt.getText().toString());
        sec = Integer.parseInt(secTxt.getText().toString());

        allTimeInMin = hour * 60 + min + sec / 60;

        final String allTimeInMinString;
        allTimeInMinString = Integer.toString(allTimeInMin);


        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        final Button plusHBtn = (Button) findViewById(R.id.btnPlusH);
        final Button minusHBtn = (Button) findViewById(R.id.btnMinusH);
        final Button plusMBtn = (Button) findViewById(R.id.btnPlusM);
        final Button minusMBtn = (Button) findViewById(R.id.btnMinusM);
        final Button plusSBtn = (Button) findViewById(R.id.btnPlusS);
        final Button minusSBtn = (Button) findViewById(R.id.btnMinusS);
        final Button startBtn = (Button) findViewById(R.id.btnStart);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("timer");
        tabSpec.setContent(R.id.tabTimer);
        tabSpec.setIndicator("Timer");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("add");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Add");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

      nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
               addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTimer(nameTxt.getText().toString(), allTimeInMinString, infoTxt.getText().toString());
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();
            }
        });

        plusHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_stunde++;
                if(e_stunde == 24)
                    e_stunde = 0;

                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        plusMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_minute++;
                if(e_minute == 60)
                {
                    e_minute = 0;
                    e_stunde++;
                    if(e_stunde == 24)
                    {
                        e_stunde = 0;
                    }
                }

                secTimerTxt.setText(String.format("%02d", e_sekunde));
                minTimerTxt.setText(String.format("%02d", e_minute));
                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        plusSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_sekunde++;
                if(e_sekunde == 60)
                {
                    e_sekunde = 0;
                    e_minute ++;

                    if(e_minute == 60)
                    {
                        e_minute = 0;
                        e_stunde++;

                        if(e_stunde == 24)
                        {
                            e_stunde = 0;
                        }
                    }
                }

                secTimerTxt.setText(String.format("%02d", e_sekunde));
                minTimerTxt.setText(String.format("%02d", e_minute));
                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        minusHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_stunde--;

                if(e_stunde < 0)
                {
                    e_stunde = 23;
                }

                secTimerTxt.setText(String.format("%02d", e_sekunde));
                minTimerTxt.setText(String.format("%02d", e_minute));
                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        minusMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_minute--;

                if(e_minute < 0)
                {
                    e_minute = 59;
                    e_stunde--;

                    if(e_stunde < 0)
                    {
                        e_stunde = 23;
                    }
                }

                secTimerTxt.setText(String.format("%02d", e_sekunde));
                minTimerTxt.setText(String.format("%02d", e_minute));
                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        minusSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_sekunde--;

                if(e_sekunde < 0)
                {
                    e_sekunde = 59;
                    e_minute--;

                    if(e_minute < 0)
                    {
                        e_minute = 59;
                        e_stunde--;

                        if(e_stunde < 0)
                        {
                            e_stunde = 23;
                        }
                    }
                }

                secTimerTxt.setText(String.format("%02d", e_sekunde));
                minTimerTxt.setText(String.format("%02d", e_minute));
                hourTimerTxt.setText(String.format("%02d", e_stunde));
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//       if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }


    private void addTimer(String name, String time, String info)
    {
        timerList.add(new TimerList(name, info, time));
    }

   private class TimerListAdapter extends ArrayAdapter<TimerList>
    {
        public TimerListAdapter()
        {
            super(MainActivity.this, R.layout.listview_item, timerList);
        }

      @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            if(view == null)
                  view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            TimerList currentTimer = timerList.get(position);

            TextView name = (TextView) view.findViewById(R.id.textView);
            name.setText(currentTimer.getName());
            TextView time = (TextView) view.findViewById((R.id.txtListTime));
            time.setText(currentTimer.getTimer());
            TextView info = (TextView) view.findViewById((R.id.textListInfo));
            time.setText(currentTimer.getTimer());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
