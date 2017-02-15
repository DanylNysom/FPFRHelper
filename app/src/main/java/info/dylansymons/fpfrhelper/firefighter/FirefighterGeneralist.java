package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterGeneralist extends Firefighter {
    @Override
    public String getTitle() {
        return "Generalist";
    }

    @Override
    public int getAp() {
        return 5;
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
