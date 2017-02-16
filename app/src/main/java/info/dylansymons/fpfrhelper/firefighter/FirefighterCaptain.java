package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

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
    private static final Action COMMAND1 =
            new Action(1, "Command", "Spend AP to Move another player");
    private static final Action COMMAND2 =
            new Action(2, "Command", "Spend AP to Move another player");
    private static final Action COMMAND4 =
            new Action(4, "Command", "Spend AP to Move another player");

    @Override
    public String getTitle() {
        return "Fire Captain";
    }

    @Override
    public int getBonusAp() {
        return 2;
    }

    @Override
    public String getBonusApLabel() {
        return "Command";
    }

    @Override
    public boolean hasBonusApFor(Action action) {
        String name = action.getShortDescription();
        return (name.equals(COMMAND1.getShortDescription()));
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(12);
        actions.addAll(getDefaultMoveActions());
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        actions.add(COMMAND1);
        actions.add(COMMAND2);
        actions.add(COMMAND4);

        return actions;
    }
}
