package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * A Firefighter that specializes in rescuing Victims.
 *
 * The Rescue Specialist receives 3 extra Action Points per turn that can only be used for movement.
 * He or she also only pays 1 Action Point to enact the Chop Action.
 *
 * The Rescue Specialist pays double Action Points to extinguish Fire and Smoke.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
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
