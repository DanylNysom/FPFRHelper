package info.dylansymons.fpfrhelper.player;

import java.lang.*;
import java.util.ArrayList;

/**
 * Created by dylan on 12/28/16.
 */
public class PlayerList extends ArrayList<Player> {
    public void add(String name, String firefighter) {
        add(new Player(name, firefighter));
        System.err.println("Added " + name + " (" + firefighter + ")");
        System.err.println("Total: " + size());
        System.err.println();
    }
}
