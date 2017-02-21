package info.dylansymons.fpfrhelper.game.management;

import java.io.Serializable;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.player.Player;

/**
 * Created by dylan on 2/17/17.
 */
public interface NewPlayerDialogFragmentCallback extends Serializable {
    void addPlayer(String name, Firefighter firefighter, int color);

    void editPlayer(String name, Firefighter firefighter, int color, Player mPlayer);
}
