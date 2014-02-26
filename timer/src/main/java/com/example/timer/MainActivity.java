package com.example.timer;

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

    EditText nameTxt, infoTxt, hourTxt, minTxt, secTxt;
    List<TimerList> timerList = new ArrayList<TimerList>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.editTextTimerName);
        infoTxt = (EditText) findViewById(R.id.editTextInfo);
 /*       hourTxt = (EditText) findViewById(R.id.txtViewAddH);
        minTxt = (EditText) findViewById(R.id.txtViewAddM);
        secTxt = (EditText) findViewById(R.id.txtViewAddS);


       int hour, min, sec, allTimeInMin;

        hour = Integer.parseInt(hourTxt.getText().toString());
        min = Integer.parseInt(minTxt.getText().toString());
        sec = Integer.parseInt(secTxt.getText().toString());

        allTimeInMin = hour /60 + min + sec * 60;

        final String allTimeInMinString;

        allTimeInMinString = Integer.toString(allTimeInMin);

*/
        final Button addBtn = (Button) findViewById(R.id.btnAdd);
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
  //              addTimer(nameTxt.getText().toString(), allTimeInMinString, infoTxt.getText().toString());
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();
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
