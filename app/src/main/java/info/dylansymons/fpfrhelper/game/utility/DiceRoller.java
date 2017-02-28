package info.dylansymons.fpfrhelper.game.utility;

import java.util.Random;

public class DiceRoller {
    private int blackDie;
    private Random rng;

    public DiceRoller(boolean useD12) {
        rng = new Random();
        if (useD12) {
            blackDie = 12;
        } else {
            blackDie = 8;
        }
    }

    public DiceRoll roll() {
        DiceRoll roll = new DiceRoll();
        roll.redValue = rng.nextInt(6) + 1;
        roll.blackValue = rng.nextInt(blackDie) + 1;
        return roll;
    }

    private class DiceRoll {
        int redValue;
        int blackValue;
    }
}
