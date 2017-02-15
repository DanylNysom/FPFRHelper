package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterRescueSpecialist extends Firefighter {
    @Override
    public String getTitle() {
        return "Rescue Specialist";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(9);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.EXTINGUISH_DOUBLE);

        Action specialChop = new Action(Action.CHOP);
        specialChop.setCost(1);
        actions.add(specialChop);

        return actions;
    }
}
