package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterVeteran extends Firefighter {
    @Override
    public String getTitle() {
        return "Veteran";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(8);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);

        return actions;
    }
}
