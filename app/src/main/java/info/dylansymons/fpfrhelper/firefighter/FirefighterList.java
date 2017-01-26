package info.dylansymons.fpfrhelper.firefighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

import info.dylansymons.fpfrhelper.R;

/**
 * Created by dylan on 1/4/17.
 */

public class FirefighterList {
    private static String[] firefighters;

    public static String[] getList(Context context) {
        ArrayList<String> list = new ArrayList<>(20);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        if (prefs.getBoolean("pref_base", true)) {
            list.addAll(Arrays.asList(res.getStringArray(R.array.firefighters_base_names)));
        }
        if (prefs.getBoolean("pref_urban", false)) {
            list.addAll(Arrays.asList(res.getStringArray(R.array.firefighters_urban_names)));
        }
        if (prefs.getBoolean("pref_veteran_dog", false)) {
            list.addAll(Arrays.asList(res.getStringArray(R.array.firefighters_veteran_dog_names)));
        }
        firefighters = list.toArray(new String[0]);
        Arrays.sort(firefighters);
        return firefighters;
    }
}
