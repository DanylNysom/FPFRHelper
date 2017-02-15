package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * A Random, "dummy" Firefighter.
 *
 * This class has no Actions and should be replaced by an actual Firefighter role before starting
 * the game. It can be used as a placeholder for when a Firefighter role is not known yet.
 *
 * @author dylan
 */
public class FirefighterRandom extends Firefighter {
    @Override
    public String getTitle() {
        return "Random";
    }

    @Override
    public HashSet<Action> getActions() {
        return null;
    }
}
