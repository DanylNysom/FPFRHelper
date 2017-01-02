package info.dylansymons.fpfrhelper.player;

/**
 * Created by dylan on 12/28/16.
 */
public class Player {
    private String name;
    private String firefighter;

    public Player(String name, String firefighter) {
        setName(name);
        setFirefighter(firefighter);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirefighter() {
        return firefighter;
    }

    public void setFirefighter(String firefighter) {
        this.firefighter = firefighter;
    }

    @Override
    public String toString() {
        return getName() + " (" + getFirefighter() + ")";
    }
}
