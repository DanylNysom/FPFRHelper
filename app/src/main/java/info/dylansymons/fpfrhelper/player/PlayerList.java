package info.dylansymons.fpfrhelper.player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author dylan
 */
public class PlayerList implements Serializable {
    private static final int DEFAULT_PLAYER_COUNT = 6;
    private final ArrayList<Player> mList;
    private int currentIndex;

    public PlayerList() {
        mList = new ArrayList<>(DEFAULT_PLAYER_COUNT);
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

    Player remove(int position) {
        return mList.remove(position);
    }

    public void add(int position, Player player) {
        mList.add(position, player);
    }

    public int indexOf(Player player) {
        return mList.indexOf(player);
    }
}
