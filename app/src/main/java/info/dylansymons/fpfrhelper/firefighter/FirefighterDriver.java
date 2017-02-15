package info.dylansymons.fpfrhelper.firefighter;

import java.util.HashSet;

/**
 * Created by dylan on 1/26/17.
 */
public class FirefighterDriver extends Firefighter {
    @Override
    public String getTitle() {
        return "Driver/Operator";
    }

    @Override
    public HashSet<Action> getActions() {
        HashSet<Action> actions = new HashSet<>(9);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.DRIVE);
        actions.add(Action.CREW_CHANGE);
        actions.add(Action.CHOP);
        actions.add(Action.EXTINGUISH);

        Action specialDeckGun = new Action(Action.FIRE_DECK_GUN);
        specialDeckGun.setCost(2);

        return actions;
    }
}
