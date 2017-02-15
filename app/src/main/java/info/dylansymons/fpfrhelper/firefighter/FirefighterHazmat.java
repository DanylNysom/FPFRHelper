package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * A Firefighter that specializes in disposing of Hazardous Materials.
 *
 * For a cost of 2 Action Points, the Hazmat Technician can remove a Hazmat token from the board.
 * The Hazmat must be in the same space as the Hazmat Technician.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterHazmat extends Firefighter {
    @Override
    public String getTitle() {
        return "Hazmat Technician";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        actions.add(new Action(2, "Dispose",
                "Remove a Hazmat from the Firefighter's space and place in the Rescued spot"));

        return actions;
    }
}
