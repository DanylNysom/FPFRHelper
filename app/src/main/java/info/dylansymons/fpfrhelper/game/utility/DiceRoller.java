package info.dylansymons.fpfrhelper.game.utility;

import java.io.Serializable;
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

    public class DiceRoll implements Serializable {
        public int redValue;
        public int blackValue;
    }
}
