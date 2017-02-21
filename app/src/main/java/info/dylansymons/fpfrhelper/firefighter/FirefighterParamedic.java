package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in rescuing Victims.
 *
 * The Paramedic has a special Action "Treat" that can be used to Treat a Victim on the current
 * space for 1 Action Point. A treated Victim can be led by a Firefighter, rather than being
 * carried. This removes the Action Point penalty for carrying the Victim, resulting in a usual cost
 * of only 1 Action Point per square moved with a Treated Victim. A Firefighter may only lead one
 * Treated Victim at a time, but may lead one Victim and carry another (both of which may be
 * Treated) at a default cost of 2 Action Points per square.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterParamedic extends Firefighter {
    @Override
    public String getTitle() {
        return "Paramedic";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH_DOUBLE);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);
        actions.add(new Action(1, "Treat", "Resuscitate a Victim"));
        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
