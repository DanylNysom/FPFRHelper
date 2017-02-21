package info.dylansymons.fpfrhelper.firefighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.ArrayList;

import info.dylansymons.fpfrhelper.R;

/**
 * A list of Firefighters available to the current Game.
 *
 * The contents of the list depend on which Expansions have been selected in the Settings menu.
 *
 * @author dylan
 */

public class FirefighterList implements Serializable {
    private static final int BASE = 0;
    private static final int URBAN = 1;
    private static final int VETERAN_DOG = 2;
    private ArrayList<Firefighter> mFirefighters;
    private boolean mHasRandom;
    private boolean[] expansions = {false, false, false};

    private FirefighterList(Context context, boolean includeRandom) {
        mHasRandom = includeRandom;
        mFirefighters = new ArrayList<>(20);
        checkExpansions(context);
        if (mHasRandom) {
            mFirefighters.add(0, new FirefighterRandom());
        }
    }

    /**
     * Returns a list of the Firefighters available based on the currently selected Expansions.
     * <p>
     * No consideration is made to whether or not Firefighters have been selected by Players.
     *
     * @param context the context storing the SharedPreferences to retrieve the Expansion selection
     *                from
     * @return a list of the Firefighters currently available
     */
    public static FirefighterList getList(Context context, boolean includeRandom) {
        return new FirefighterList(context, includeRandom);
    }

    private void addFirefighters(String[] titles) {
        for(String title : titles) {
            try {
                mFirefighters.add((Firefighter)
                        Class.forName("info.dylansymons.fpfrhelper.firefighter." + title)
                                .newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int size() {
        int size = mFirefighters.size();
        return (mHasRandom) ? size - 1 : size;
    }

    public Firefighter get(int position) {
        return mFirefighters.get(position);
    }

    public Firefighter[] toArray() {
        return mFirefighters.toArray(new Firefighter[0]);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean remove(Firefighter firefighter) {
        return mFirefighters.remove(firefighter);
    }

    public boolean add(Firefighter firefighter) {
        return mFirefighters.add(firefighter);
    }

    public Firefighter getLast() {
        return get(mFirefighters.size() - 1);
    }

    public void checkExpansions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        if (!expansions[BASE] && prefs.getBoolean("pref_base", true)) {
            String[] titles = res.getStringArray(R.array.firefighters_base_names);
            addFirefighters(titles);
            expansions[BASE] = true;
        }
        if (!expansions[URBAN] && prefs.getBoolean("pref_urban", false)) {
            String[] titles = res.getStringArray(R.array.firefighters_urban_names);
            addFirefighters(titles);
            expansions[URBAN] = true;
        }
        if (!expansions[VETERAN_DOG] && prefs.getBoolean("pref_veteran_dog", false)) {
            String[] titles = res.getStringArray(R.array.firefighters_veteran_dog_names);
            addFirefighters(titles);
            expansions[VETERAN_DOG] = true;
        }
    }
}
