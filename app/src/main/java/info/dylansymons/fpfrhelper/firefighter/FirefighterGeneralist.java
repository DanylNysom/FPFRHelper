package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter with no specialty.
 *
 * The Generalist receives an additional Action Point each turn, for a total of 5. Only 4 Action
 * Points can be saved per turn.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
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
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(9);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);
        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
