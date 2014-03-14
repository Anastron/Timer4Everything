package com.example.timer;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends ActionBarActivity {

    EditText nameTxt, infoTxt;
    TextView hourTxt, minTxt, secTxt, hourTimerTxt, minTimerTxt, secTimerTxt, timerTxt;
    List<TimerList> timerList = new ArrayList<TimerList>();
    ListView timerListView;

    DatabaseHandler dbHandler;

    CountDownTimer exit_timer;

    private TimerService ts;
    private Intent intent;
    private Context context = this;

    int e_stunde = 0;
    int e_minute = 0;
    int e_sekunde = 0;

    int add_stunde = 0;
    int add_minute = 0;
    int add_sekunde = 0;

    boolean quitOnce;





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
        timerTxt = (TextView) findViewById(R.id.txtViewTimer);

        timerListView = (ListView) findViewById(R.id.listView);

        intent = new Intent(context, TimerService.class);
        context.startService(intent);
        ts = new TimerService();

        dbHandler = new DatabaseHandler(getApplicationContext());

        int allTimeInMin;

        allTimeInMin = add_stunde * 60 + add_minute + add_sekunde / 60;

        final String allTimeInMinString;
        allTimeInMinString = Integer.toString(allTimeInMin);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        final Button plusAddHBtn = (Button) findViewById(R.id.btnAddPlusH);
        final Button minusAddHBtn = (Button) findViewById(R.id.btnAddMinusH);
        final Button plusAddMBtn = (Button) findViewById(R.id.btnAddPlusM);
        final Button minusAddMBtn = (Button) findViewById(R.id.btnAddMinusM);
        final Button plusAddSBtn = (Button) findViewById(R.id.btnAddPlusS);
        final Button minusAddSBtn = (Button) findViewById(R.id.btnAddMinusS);

        final Button plusHBtn = (Button) findViewById(R.id.btnPlusH);
        final Button minusHBtn = (Button) findViewById(R.id.btnMinusH);
        final Button plusMBtn = (Button) findViewById(R.id.btnPlusM);
        final Button minusMBtn = (Button) findViewById(R.id.btnMinusM);
        final Button plusSBtn = (Button) findViewById(R.id.btnPlusS);
        final Button minusSBtn = (Button) findViewById(R.id.btnMinusS);
        final Button startBtn = (Button) findViewById(R.id.btnStart);
        final Button resetBtn = (Button) findViewById(R.id.btnReset);
        final Button stopBtn = (Button) findViewById(R.id.btnStop);


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

        List<TimerList> addableTimers = dbHandler.getAllTimer();
        int timerCount = dbHandler.getTimersCount();

        for (int i = 0; i < timerCount; i++)
        {
            timerList.add(addableTimers.get(i));
        }

        if(!addableTimers.isEmpty())
        {
            populateList();
        }

      nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
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
                int allTimeInMin = add_stunde * 60 + add_minute + add_sekunde / 60;

                String allTimeInMinString = Integer.toString(allTimeInMin);

                TimerList timer = new TimerList(dbHandler.getTimersCount(), String.valueOf(nameTxt.getText()), String.valueOf(allTimeInMinString + " min"), String.valueOf(infoTxt.getText()));

//                addTimer(nameTxt.getText().toString(), "minutes: " + allTimeInMinString, infoTxt.getText().toString());
                
                Log.d("START TEST");
                
                Log.d("TEST COUNT-1: " + dbHandler.getTimersCount());
                
                dbHandler.createTimer(timer);
                timerList.add(timer);
                populateList();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();

                int tsda = dbHandler.getTimersCount();
                
                Log.d("TEST COUNT-2: " + tsda);
                Log.d("TEST COUNT-3: " + dbHandler.getTimersCount());
                
                Log.d("END TEST");
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
                if(!ts.isRunning())
                {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);


                    ts.run(e_stunde, e_minute, e_sekunde, timerTxt,ringtone);

                }
                else
                {
                    ts.timerStart();
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ts.timerStop();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ts.isRunning())
                {
                    ts.timerReset();
                }
            }
        });

        plusAddHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_stunde++;
                if(add_stunde == 24)
                    add_stunde = 0;

                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });

        plusAddMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_minute++;
                if(add_minute == 60)
                {
                    add_minute = 0;
                    add_stunde++;
                    if(add_stunde == 24)
                    {
                        add_stunde = 0;
                    }
                }

                secTxt.setText(String.format("%02d", add_sekunde));
                minTxt.setText(String.format("%02d", add_minute));
                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });

        plusAddSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_sekunde++;
                if(add_sekunde == 60)
                {
                    add_sekunde = 0;
                    add_minute ++;

                    if(add_minute == 60)
                    {
                        add_minute = 0;
                        add_stunde++;

                        if(add_stunde == 24)
                        {
                            add_stunde = 0;
                        }
                    }
                }

                secTxt.setText(String.format("%02d", add_sekunde));
                minTxt.setText(String.format("%02d", add_minute));
                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });

        minusAddHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_stunde--;

                if(add_stunde < 0)
                {
                   add_stunde = 23;
                }

                secTxt.setText(String.format("%02d", add_sekunde));
                minTxt.setText(String.format("%02d", add_minute));
                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });

        minusAddMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_minute--;

                if(add_minute < 0)
                {
                    add_minute = 59;
                    add_stunde--;

                    if(add_stunde < 0)
                    {
                        add_stunde = 23;
                    }
                }

                secTxt.setText(String.format("%02d", add_sekunde));
                minTxt.setText(String.format("%02d", add_minute));
                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });

        minusAddSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_sekunde--;

                if(add_sekunde < 0)
                {
                    add_sekunde = 59;
                    add_minute--;

                    if(add_minute < 0)
                    {
                        add_minute = 59;
                        add_stunde--;

                        if(add_stunde < 0)
                        {
                            add_stunde = 23;
                        }
                    }
                }

                secTxt.setText(String.format("%02d", add_sekunde));
                minTxt.setText(String.format("%02d", add_minute));
                hourTxt.setText(String.format("%02d", add_stunde));
            }
        });



//       if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }

    public void onBackPressed() {
        if (!quitOnce) {
            quitOnce = true;
            endTimer();
        } else {
            finish();
            exit_timer.cancel();
        }
    }

    private void endTimer() {
        exit_timer = new CountDownTimer(1000, 100) {

            @Override
            public void onFinish() {
                quitOnce = false;
                Toast.makeText(getApplicationContext(), "Do you realy want to leave? To leave press it two times", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTick(long arg0) {
            }

        }.start();
    }



    private void populateList()
    {
        ArrayAdapter<TimerList> adapter;
        adapter = new TimerListAdapter();
        timerListView.setAdapter(adapter);
    }

    private void addTimer(String name, String time, String info)
    {
        timerList.add(new TimerList(0, name, info, time));
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
            info.setText(currentTimer.getInfo());

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
