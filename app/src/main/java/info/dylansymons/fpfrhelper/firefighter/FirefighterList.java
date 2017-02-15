package info.dylansymons.fpfrhelper.firefighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.R;

/**
 * A list of Firefighters available to the current Game.
 *
 * The contents of the list depend on which Expansions have been selected in the Settings menu.
 *
 * @author dylan
 */

public class FirefighterList {

    /**
     * Returns a list of the Firefighters available based on the currently selected Expansions.
     *
     * No consideration is made to whether or not Firefighters have been selected by Players.
     *
     * @param context the context storing the SharedPreferences to retrieve the Expansion selection
     *                from
     * @return a list of the Firefighters currently available
     */
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
        return list.toArray(new Firefighter[0]);
    }

    private static void addFirefighters(String[] titles, ArrayList<Firefighter> list) {
        for(String title : titles) {
            try {
                list.add((Firefighter) Class.forName("info.dylansymons.fpfrhelper.firefighter." + title).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
