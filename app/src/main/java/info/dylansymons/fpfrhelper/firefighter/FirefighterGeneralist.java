package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

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
