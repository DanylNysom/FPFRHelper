package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in helping the team.
 *
 * The Veteran grants an additional Action Point to any Firefighter that is within 3 spaces of the
 * Veteran at any point in the other Firefighter's turn. The Veteran also is able to take the Dodge
 * Action, using 1 Saved Action Point to move one space away from Fire created by an Explosion.
 * Nearby Firefighters also receive the Dodge Action, costing two Saved Action Points.
 *
 * The additional Action Points and the Dodge Action are not implemented in this system.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterVeteran extends Firefighter {
    @Override
    public String getTitle() {
        return "Veteran";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(8);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);
        actions.add(new Action(1, "Dodge", ""));
        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
