package info.dylansymons.fpfrhelper.player;

import java.io.Serializable;
import java.lang.*;
import java.util.ArrayList;

/**
 * Created by dylan on 12/28/16.
 */
public class PlayerList implements Serializable {
    private int currentIndex;
    private ArrayList<Player> mList;

    public PlayerList(int startCount) {
        mList = new ArrayList<>(startCount);
        currentIndex = -1;
    }

    private int getNextIndex() {
        return (currentIndex + 1) % mList.size();
    }

    public Player peekNext() {
        int nextIndex = getNextIndex();
        return mList.get(nextIndex);
    }

    public Player getNext() {
        mList.get(currentIndex).endTurn();
        currentIndex = getNextIndex();
        Player player = mList.get(currentIndex);
        player.startTurn();
        return player;
    }

    public Player getCurrent() {
        if (currentIndex >= 0) {
            return mList.get(currentIndex);
        } else {
            currentIndex = 0;
            Player player = mList.get(currentIndex);
            player.startTurn();
            return player;
        }
    }

    public void trim() {
        mList.trimToSize();
    }

    public void add(Player player) {
        mList.add(player);
    }

    public int size() {
        return mList.size();
    }

    public Player get(int position) {
        return mList.get(position);
    }

    public Player remove(int position) {
        return mList.remove(position);
    }

    public void add(int position, Player player) {
        mList.add(position, player);
    }
}
