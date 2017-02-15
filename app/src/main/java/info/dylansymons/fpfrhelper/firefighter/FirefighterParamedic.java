package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterParamedic extends Firefighter {
    @Override
    public String getTitle() {
        return "Paramedic";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH_DOUBLE);
        actions.add(new Action(1, "Treat", "Resuscitate a Victim"));

        return actions;
    }
}
