package com.example.timer;

/**
 * Created by cYa on 26.02.14.
 */
public class TimerList {

    private String _name, _info, _time;

    public TimerList(String name, String info, String time)
    {
        _name = name;
        _info = info;
        _time = time;
    }

    public String getName()
    {
        return _name;
    }

    public String getInfo()
    {
        return _info;
    }

    public String getTimer()
    {
        return _time;
    }
}
