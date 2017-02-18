package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

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
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);

        actions.add(new Action(2, "Dispose",
                "Remove a Hazmat from the Firefighter's space and place in the Rescued spot"));
        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
