package timerpack.timer;

/**
 * Created by cYa on 26.02.14.
 */
public class TimerList {

    private String _name, _info, _time;
    private int _id;

    public TimerList(int id, String name, String time, String info)
    {
        _id = id;
        _name = name;
        _info = info;
        _time = time;
    }

    public int getId(){return _id;}

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
