package timerpack.timer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Armin on 02.05.2014.
 */
public class Prefs extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
