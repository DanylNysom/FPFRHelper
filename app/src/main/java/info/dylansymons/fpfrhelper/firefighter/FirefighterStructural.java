package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in rebuilding and slowing the spread of Fire.
 *
 * The Structural Engineer role has two Special Actions. It can repair damaged - but not destroyed -
 * adjacent walls at a cost of 2 Action Points per repair. It can also remove hot spots from the
 * current position of the Structural Engineer at a cost of 1 Action Point. Both of these Actions
 * require that there is no Smoke or Fire in any of the adjacent spaces.
 *
 * The Structural Engineer is unable to Extinguish Fire or Smoke.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterStructural extends Firefighter {
    @Override
    public String getTitle() {
        return "Structural Engineer";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);

        actions.add(new Action(1, "Clear", "Return a Hot Spot"));
        actions.add(new Action(2, "Repair", "Return a Damage Marker"));

        actions.add(Action.CREW_CHANGE);
        return actions;
    }
}
