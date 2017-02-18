package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in Extinguishing.
 *
 * The CAFS Firefighter only receives 3 Action Points per turn, but receives 3 extra Action Points
 * that can only be used to Extinguish.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterCAFS extends Firefighter {
    @Override
    public String getTitle() {
        return "CAFS Firefighter";
    }

    @Override
    public int getAp() {
        return 3;
    }

    @Override
    public int getBonusAp() {
        return 3;
    }

    @Override
    public String getBonusApLabel() {
        return "Extinguish";
    }

    @Override
    public boolean hasBonusApFor(Action action) {
        String name = action.getShortDescription();
        return (name.equals(Action.EXTINGUISH.getShortDescription()));
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
