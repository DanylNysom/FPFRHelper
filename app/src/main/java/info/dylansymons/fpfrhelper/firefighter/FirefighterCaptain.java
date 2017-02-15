package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * A Firefighter that specializes in moving team members.
 *
 * The Fire Captain can use his or her Action Points to Command other Firefighters to Move or open/
 * close doors at the cost that the target Firefighter would pay. Only 1 Action Point can be spent
 * in this way on the CAFS Firefighter. The Fire Captain also receives 2 free Action Points that may
 * only be used to Command other Firefighters.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterCaptain extends Firefighter {
    @Override
    public String getTitle() {
        return "Fire Captain";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(10);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        actions.add(new Action(1, "Command", "Spend AP to Move another player"));

        return actions;
    }
}
