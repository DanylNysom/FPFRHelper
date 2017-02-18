package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in being cute.
 *
 * The Rescue Dog receives 12 Action Points per turn, and can save up to 6 at the end of the turn.
 * It can Squeeze through Damaged Walls at a cost of 2 Action Points, and reveal POIs in adjacent
 * spaces for free.
 * The Rescue Dog cannot move into fire at all, and pays 4 Action Points per square when carrying a
 * Victim. No Actions besides Move, Squeeze, Reveal, and Carry Victim are permitted.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterDog extends Firefighter {
    @Override
    public String getTitle() {
        return "Rescue Dog";
    }

    @Override
    public int getAp() {
        return 12;
    }

    @Override
    public int getMaxSavedAp() {
        return 6;
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(10);
        actions.add(Action.MOVE);
        Action specialCarry = new Action(Action.CARRY);
        specialCarry.setCost(4);
        actions.add(specialCarry);
        actions.add(new Action(2, "Squeeze", "Move through Damaged Wall"));
        actions.add(new Action(0, "Reveal", "Reveal Victim in adjacent space"));

        actions.add(Action.CREW_CHANGE);

        return actions;
    }
}
