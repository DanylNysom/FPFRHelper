package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

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
    public int getBonusAp() {
        return 3;
    }

    @Override
    public String getBonusApLabel() {
        return "Movement";
    }

    @Override
    public boolean hasBonusApFor(Action action) {
        String name = action.getShortDescription();
        return (name.equals(Action.MOVE.getShortDescription()) ||
                name.equals(Action.MOVE_FIRE.getShortDescription()) ||
                name.equals(Action.OPEN_CLOSE_DOOR.getShortDescription()) ||
                name.equals(Action.CARRY.getShortDescription()));
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(9);
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
