package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter specializing in determining where Victims are.
 *
 * The Imaging Technician has a special Action "Identify" that can be used to flip over a POI
 * anywhere on the board for a cost of 1 Action Point.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterImaging extends Firefighter {
    @Override
    public String getTitle() {
        return "Imaging Technician";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        actions.add(new Action(1, "Identify",
                "Flip a POI marker anywhere on the board"));

        return actions;
    }
}
