package timerpack.timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import timerpack.timer.R;

import java.util.ArrayList;
import java.util.List;

import static timerpack.timer.R.id.listView;

public class MainActivity extends ActionBarActivity {

    EditText nameTxt, infoTxt;
    EditText hourTxt, minTxt, secTxt, hourTimerTxt, minTimerTxt, secTimerTxt;
    TimeDisplay timerTxt;
    List<TimerList> timerList = new ArrayList<TimerList>();
    ListView timerListView;

    DatabaseHandler dbHandler;

    CountDownTimer exit_timer;

    private TimerService ts;
    private Intent intent;
    private Context context = this;
    private boolean timerStarted = false;
    private Thread thread;

    int e_stunde = 0;
    int e_minute = 0;
    int e_sekunde = 0;

    int add_stunde = 0;
    int add_minute = 0;
    int add_sekunde = 0;

    boolean quitOnce;
    boolean bIsLong;

    TabHost tabHost;
    ArrayAdapter<TimerList> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BGService.class));

        nameTxt = (EditText) findViewById(R.id.editTextTimerName);
        infoTxt = (EditText) findViewById(R.id.editTextInfo);
        hourTxt = (EditText) findViewById(R.id.txtViewAddH);
        minTxt = (EditText) findViewById(R.id.txtViewAddM);
        secTxt = (EditText) findViewById(R.id.txtViewAddS);

        hourTimerTxt = (EditText) findViewById(R.id.txtViewH);
        minTimerTxt = (EditText) findViewById(R.id.txtViewM);
        secTimerTxt = (EditText) findViewById(R.id.editTextSekunde); //(TextView)findViewById(R.id.txtViewS);

        timerTxt = (TimeDisplay) findViewById(R.id.extViewTimer);

        timerListView = (ListView) findViewById(listView);

        intent = new Intent(context, TimerService.class);
        context.startService(intent);
        ts = new TimerService();

        dbHandler = new DatabaseHandler(getApplicationContext());

        double allTimeInMin;

        allTimeInMin = add_stunde * 60 + add_minute + add_sekunde / 60;

        final String allTimeInMinString;
        allTimeInMinString = Double.toString(allTimeInMin);

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


        tabHost = (TabHost) findViewById(R.id.tabHost);

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
                if (hourTxt.getText().toString().matches(""))
                {
                    hourTxt.setText("00");
                }
                if(minTxt.getText().toString().matches(""))
                {
                    minTxt.setText("00");
                }
                if(secTxt.getText().toString().matches(""))
                {
                    secTxt.setText("00");
                }

                String str1 = minTxt.getText().toString();
                String str2 = hourTxt.getText().toString();
                String str3 = secTxt.getText().toString();
                add_minute = Integer.parseInt(str1);
                add_stunde= Integer.parseInt(str2);
                add_sekunde = Integer.parseInt(str3);

                double allTimeInMin = add_stunde * 60 + add_minute + add_sekunde / 60;

                String allTimeInMinString = Double.toString(allTimeInMin);


                TimerList timer = new TimerList(dbHandler.getTimersCount(), String.valueOf(nameTxt.getText()), String.valueOf(allTimeInMinString + " min"), String.valueOf(infoTxt.getText()));

 //               addTimer(nameTxt.getText().toString(), "minutes: " + allTimeInMinString, infoTxt.getText().toString());

                Log.d("Timer4Everything", "START TEST");
                
                Log.d("Timer4Everything", "TEST COUNT-1: " + dbHandler.getTimersCount());

                dbHandler.createTimer(timer);
                timerList.add(timer);
                populateList();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();

                int tsda = dbHandler.getTimersCount();
                
                Log.d("Timer4Everything", "TEST COUNT-2: " + tsda);
                Log.d("Timer4Everything", "TEST COUNT-3: " + dbHandler.getTimersCount());
                
                Log.d("Timer4Everything", "END TEST");
            }
        });




        plusHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bIsLong = false;
                plusHBtn();
            }
        });

        plusMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusMBtn();
            }
        });

        plusSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusSBtn();
            }
        });

        minusHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusHBtn();
            }
        });

        minusMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusMBtn();
            }
        });

        minusSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusSBtn();
            }
        });



        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startTimer();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ts.isStop()) {
                    ts.timerStop();
                }
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
                plusAddHBtn();
            }
        });

        plusAddMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusAddMBtn();
            }
        });

        plusAddSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusAddSBtn();
            }
        });

        minusAddHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusAddHBtn();
            }
        });

        minusAddMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusAddMBtn();
            }
        });

        minusAddSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minusAddSBtn();
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
            if (timerStarted)
            {
                ts.timerStop();
            }
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
        adapter = new TimerListAdapter();
        timerListView.setAdapter(adapter);

        registerForContextMenu(timerListView);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Start");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //  info.position will give the index of selected item
        int intIndexSelected = info.position;
        if (item.getTitle() == "Start")
        {
            String time;
            TimerList currentTimer = timerList.get(intIndexSelected);
            time = currentTimer.getTimer();
            fromStringToDifTimer(time);
            startTimer();
            tabHost.setCurrentTab(0);
            Toast.makeText(getApplicationContext(),"Start " + currentTimer.getName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getTitle() == "Delete")
        {
            TimerList currentTimer = timerList.get(intIndexSelected);
            Toast.makeText(getApplicationContext(), "Delete " + currentTimer.getName(), Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            // geht nicht
            Toast.makeText(getApplicationContext(), "you pressed Nothing you FUCKING NOOB", Toast.LENGTH_SHORT).show();
            return false;
        }
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
    private void plusHBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = hourTimerTxt.getText().toString();
        String str2 = minTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();
            e_stunde = Integer.parseInt(str1);
            e_stunde++;
            if (e_stunde >= 24)
                e_stunde = 0;

            hourTimerTxt.setText(String.format("%02d", e_stunde));

    }
    private void plusMBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = minTimerTxt.getText().toString();
        String str2 = hourTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();

            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            e_minute++;
            if (e_minute >= 60) {
                e_minute = 0;
                e_stunde++;
                if (e_stunde >= 24) {
                    e_stunde = 0;
                }
            }

            secTimerTxt.setText(String.format("%02d", e_sekunde));
            minTimerTxt.setText(String.format("%02d", e_minute));
            hourTimerTxt.setText(String.format("%02d", e_stunde));
    }
    private void plusSBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = minTimerTxt.getText().toString();
        String str2 = hourTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();
            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            e_sekunde++;
            if (e_sekunde >= 60) {
                e_sekunde = 0;
                e_minute++;

                if (e_minute >= 60) {
                    e_minute = 0;
                    e_stunde++;

                    if (e_stunde >= 24) {
                        e_stunde = 0;
                    }
                }
            }

            secTimerTxt.setText(String.format("%02d", e_sekunde));
            minTimerTxt.setText(String.format("%02d", e_minute));
            hourTimerTxt.setText(String.format("%02d", e_stunde));
    }
    private void minusHBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = minTimerTxt.getText().toString();
        String str2 = hourTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();
            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            e_stunde--;

            if (e_stunde < 0) {
                e_stunde = 23;
            }

            secTimerTxt.setText(String.format("%02d", e_sekunde));
            minTimerTxt.setText(String.format("%02d", e_minute));
            hourTimerTxt.setText(String.format("%02d", e_stunde));
    }
    private void minusMBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = minTimerTxt.getText().toString();
        String str2 = hourTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();
            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            e_minute--;

            if (e_minute < 0) {
                e_minute = 59;
                e_stunde--;

                if (e_stunde < 0) {
                    e_stunde = 23;
                }
            }

            secTimerTxt.setText(String.format("%02d", e_sekunde));
            minTimerTxt.setText(String.format("%02d", e_minute));
            hourTimerTxt.setText(String.format("%02d", e_stunde));

    }
    private void minusSBtn()
    {
        if (hourTimerTxt.getText().toString().matches(""))
        {
            hourTimerTxt.setText("00");
        }
        if(minTimerTxt.getText().toString().matches(""))
        {
            minTimerTxt.setText("00");
        }
        if(secTimerTxt.getText().toString().matches(""))
        {
            secTimerTxt.setText("00");
        }

        String str1 = minTimerTxt.getText().toString();
        String str2 = hourTimerTxt.getText().toString();
        String str3 = secTimerTxt.getText().toString();
            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            e_sekunde--;

            if (e_sekunde < 0) {
                e_sekunde = 59;
                e_minute--;

                if (e_minute < 0) {
                    e_minute = 59;
                    e_stunde--;

                    if (e_stunde < 0) {
                        e_stunde = 23;
                    }
                }
            }

            secTimerTxt.setText(String.format("%02d", e_sekunde));
            minTimerTxt.setText(String.format("%02d", e_minute));
            hourTimerTxt.setText(String.format("%02d", e_stunde));
    }
    private void plusAddHBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
            hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }

        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_stunde++;
            if (add_stunde >= 24)
                add_stunde = 0;

            hourTxt.setText(String.format("%02d", add_stunde));

    }
    private void plusAddMBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
            hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }

        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_minute++;
            if (add_minute >= 60) {
                add_minute = 0;
                add_stunde++;
                if (add_stunde >= 24) {
                    add_stunde = 0;
                }
            }

            secTxt.setText(String.format("%02d", add_sekunde));
            minTxt.setText(String.format("%02d", add_minute));
            hourTxt.setText(String.format("%02d", add_stunde));
    }
    private void plusAddSBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
            hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }

        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_sekunde++;
            if (add_sekunde >= 60) {
                add_sekunde = 0;
                add_minute++;

                if (add_minute >= 60) {
                    add_minute = 0;
                    add_stunde++;

                    if (add_stunde >= 24) {
                        add_stunde = 0;
                    }
                }
            }

            secTxt.setText(String.format("%02d", add_sekunde));
            minTxt.setText(String.format("%02d", add_minute));
            hourTxt.setText(String.format("%02d", add_stunde));
    }
    private void minusAddHBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
        hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }
        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_stunde--;

            if (add_stunde < 0) {
                add_stunde = 23;
            }

            secTxt.setText(String.format("%02d", add_sekunde));
            minTxt.setText(String.format("%02d", add_minute));
            hourTxt.setText(String.format("%02d", add_stunde));
    }
    private void minusAddMBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
            hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }

        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_minute--;

            if (add_minute < 0) {
                add_minute = 59;
                add_stunde--;

                if (add_stunde < 0) {
                    add_stunde = 23;
                }
            }

            secTxt.setText(String.format("%02d", add_sekunde));
            minTxt.setText(String.format("%02d", add_minute));
            hourTxt.setText(String.format("%02d", add_stunde));
    }
    private void minusAddSBtn()
    {
        if (hourTxt.getText().toString().matches(""))
        {
            hourTxt.setText("00");
        }
        if(minTxt.getText().toString().matches(""))
        {
            minTxt.setText("00");
        }
        if(secTxt.getText().toString().matches(""))
        {
            secTxt.setText("00");
        }

        String str1 = minTxt.getText().toString();
        String str2 = hourTxt.getText().toString();
        String str3 = secTxt.getText().toString();
            add_minute = Integer.parseInt(str1);
            add_stunde = Integer.parseInt(str2);
            add_sekunde = Integer.parseInt(str3);

            add_sekunde--;

            if (add_sekunde < 0) {
                add_sekunde = 59;
                add_minute--;

                if (add_minute < 0) {
                    add_minute = 59;
                    add_stunde--;

                    if (add_stunde < 0) {
                        add_stunde = 23;
                    }
                }
            }

            secTxt.setText(String.format("%02d", add_sekunde));
            minTxt.setText(String.format("%02d", add_minute));
            hourTxt.setText(String.format("%02d", add_stunde));
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
    private void startTimer()
    {

        if(!ts.isRunning() && ts.isStop())
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            timerStarted = true;

            if (hourTimerTxt.getText().toString().matches("") || hourTimerTxt.getText().toString().matches("0"))
            {
                hourTimerTxt.setText("00");
            }
            if(minTimerTxt.getText().toString().matches("") || minTimerTxt.getText().toString().matches("0"))
            {
                minTimerTxt.setText("00");
            }
            if(secTimerTxt.getText().toString().matches("") || secTimerTxt.getText().toString().matches("0"))
            {
                secTimerTxt.setText("00");
            }

            String str1 = minTimerTxt.getText().toString();
            String str2 = hourTimerTxt.getText().toString();
            String str3 = secTimerTxt.getText().toString();
            e_minute = Integer.parseInt(str1);
            e_stunde = Integer.parseInt(str2);
            e_sekunde = Integer.parseInt(str3);

            ts.run(e_stunde, e_minute, e_sekunde, timerTxt,ringtone, vib);
        }
        else if(ts.isStop())
        {
            ts.timerStart();
        }
        else if(ts.isPlaying())
        {
            ts.timerStop();
        }
    }

    private void fromStringToDifTimer(String time)
    {
        int stunde, minute, sekunde;
        int alltimeInMin;
        stunde = 0;
        minute = 0;
        sekunde = 0;

        alltimeInMin = Integer.parseInt(time.replace(" min", ""));

        stunde = alltimeInMin / 60;
        double zwischenmin = alltimeInMin - (stunde * 60);
        minute = (int)zwischenmin;
        double zwischensek = zwischenmin - (double)minute;
        zwischensek = zwischensek * 60;
        sekunde = (int) zwischensek;

        hourTimerTxt.setText(Integer.toString(stunde));
        minTimerTxt.setText(Integer.toString(minute));
        secTimerTxt.setText(Integer.toString(sekunde));
    }
}
