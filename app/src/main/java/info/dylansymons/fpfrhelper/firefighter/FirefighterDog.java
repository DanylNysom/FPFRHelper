package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterDog extends Firefighter {
    @Override
    public String getTitle() {
        return "Rescue Dog";
    }

    @Override
    public int getAp() {
        return 12;
    }

    @Override
    public int getMaxSavedAp() {
        return 6;
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);

        return actions;
    }
}
