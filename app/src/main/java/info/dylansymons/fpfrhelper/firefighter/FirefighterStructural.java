package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterStructural extends Firefighter {
    @Override
    public String getTitle() {
        return "Structural Engineer";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);

        actions.add(new Action(1, "Clear", "Return a Hot Spot"));
        actions.add(new Action(2, "Repair", "Return a Damage Marker"));

        return actions;
    }
}
