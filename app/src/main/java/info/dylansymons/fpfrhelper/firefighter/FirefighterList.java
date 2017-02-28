package info.dylansymons.fpfrhelper.firefighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.game.Game;

/**
 * A list of Firefighters available to the current Game.
 *
 * The contents of the list depend on which Expansions have been selected in the Settings menu.
 *
 * @author dylan
 */

public class FirefighterList extends ArrayList<Firefighter> {
    private ArrayList<Firefighter> chosenFirefighters = new ArrayList<>(6);

    private FirefighterList(Context context, Game game) {
        super(20);
        game.setFirefighterList(this);
        game.setExpansions(checkExpansions(context, game.getExpansions()));
        add(0, Firefighter.RANDOM);
    }

    public FirefighterList(ArrayList<Firefighter> arrayList) {
        super(arrayList);
    }

    public FirefighterList(int count) {
        super(count);
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
    public static FirefighterList getList(Context context, Game game) {
        return new FirefighterList(context, game);
    }

    public int size() {
        return super.size() - 1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Firefighter getLast() {
        return get(size() - 1);
    }

    public boolean[] checkExpansions(Context context, boolean[] expansions) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!expansions[Game.BASE] && prefs.getBoolean("pref_base", true)) {
            addFirefighters(Firefighter.FIREFIGHTERS_BASE);
            expansions[Game.BASE] = true;
        }
        if (!expansions[Game.PREVENTION] && prefs.getBoolean("pref_prevention", false)) {
            addFirefighters(Firefighter.FIREFIGHTERS_PREVENTION);
            expansions[Game.PREVENTION] = true;
        }
        if (!expansions[Game.URBAN] && prefs.getBoolean("pref_urban", false)) {
            addFirefighters(Firefighter.FIREFIGHTERS_URBAN);
            expansions[Game.URBAN] = true;
        }
        if (!expansions[Game.VETERAN_DOG] && prefs.getBoolean("pref_veteran_dog", false)) {
            addFirefighters(Firefighter.FIREFIGHTERS_VETERAN_DOG);
            expansions[Game.VETERAN_DOG] = true;
        }
        return expansions;
    }

    private void addFirefighters(Firefighter[] firefighters) {
        for (Firefighter f : firefighters) {
            add(f);
        }
    }

    public ArrayList<Firefighter> getChosen() {
        return chosenFirefighters;
    }

    public void setChosen(Firefighter firefighter, boolean isChosen) {
        if (isChosen) {
            remove(firefighter);
            chosenFirefighters.add(firefighter);
        } else {
            chosenFirefighters.remove(firefighter);
            add(firefighter);
        }
    }
}
