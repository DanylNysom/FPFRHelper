package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in Extinguishing.
 * <p>
 * The CAFS Firefighter only receives 3 Action Points per turn, but receives 3 extra Action Points
 * that can only be used to Extinguish.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterPrevention extends Firefighter {
    @Override
    public String getTitle() {
        return "Fire Prevention Specialist";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);

        actions.add(new Action(1, "Prevent (Smoke)",
                "Move a smoke token one space towards the nearest exit"));
        actions.add(new Action(2, "Prevent (POI)",
                "Move a ? POI token one space towards the nearest exit"));
        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
