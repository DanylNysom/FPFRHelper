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
    private static Firefighter[] firefighters;

    public static Firefighter[] getList(Context context) {
        ArrayList<Firefighter> list = new ArrayList<>(20);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        if (prefs.getBoolean("pref_base", true)) {
            String[] titles = res.getStringArray(R.array.firefighters_base_names);
            addFirefighters(titles, list);
        }
        if (prefs.getBoolean("pref_urban", false)) {
            String[] titles = res.getStringArray(R.array.firefighters_urban_names);
            addFirefighters(titles, list);
        }
        if (prefs.getBoolean("pref_veteran_dog", false)) {
            String[] titles = res.getStringArray(R.array.firefighters_veteran_dog_names);
            addFirefighters(titles, list);
        }
        firefighters = list.toArray(new Firefighter[0]);
//        Arrays.sort(firefighters);
        return firefighters;
    }

    private static void addFirefighters(String[] titles, ArrayList<Firefighter> list) {
        for(String title : titles) {
            try {
                list.add((Firefighter) Class.forName("info.dylansymons.fpfrhelper.firefighter." + title).newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
