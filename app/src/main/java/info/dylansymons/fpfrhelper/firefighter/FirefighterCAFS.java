package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * A Firefighter that specializes in Extinguishing.
 *
 * The CAFS Firefighter only receives 3 Action Points per turn, but receives 3 extra Action Points
 * that can only be used to Extinguish.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterCAFS extends Firefighter {
    @Override
    public String getTitle() {
        return "CAFS Firefighter";
    }

    @Override
    public int getAp() {
        return 3;
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(9);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        return actions;
    }
}
