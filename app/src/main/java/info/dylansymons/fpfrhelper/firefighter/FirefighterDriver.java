package info.dylansymons.fpfrhelper.firefighter;

import java.util.ArrayList;

/**
 * A Firefighter that specializes in Firing the Deck Gun.
 *
 * The Driver/Operator role only pays 2 Action Points to Fire the Deck Gun. In addition, after
 * rolling the dice as part of the Fire the Deck Gun Action, each die may be rerolled once. The dice
 * can be rerolled together, or rolled one at a time with the option to not reroll the second one,
 * but in any case the second result for each die must be accepted.
 *
 * @author dylan
 */
@SuppressWarnings("unused")
public class FirefighterDriver extends Firefighter {
    @Override
    public String getTitle() {
        return "Driver/Operator";
    }

    @Override
    public ArrayList<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<>(9);
        actions.addAll(getDefaultMoveActions());
        actions.add(Action.EXTINGUISH);
        actions.addAll(getDefaultVehicleActions());
        actions.add(Action.CHOP);
        actions.add(Action.CREW_CHANGE);

        actions.get(actions.indexOf(Action.FIRE_DECK_GUN))
                .setCost(2);

        return actions;
    }
}
