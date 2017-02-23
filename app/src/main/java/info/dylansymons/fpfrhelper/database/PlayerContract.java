package info.dylansymons.fpfrhelper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.player.Player;

final class PlayerContract {
    static final String SQL_CREATE =
            "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                    PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                    PlayerEntry.COLUMN_NAME_NAME + " TEXT," +
                    PlayerEntry.COLUMN_NAME_GAME + " INTEGER REFERENCES " +
                    GameContract.GameEntry.TABLE_NAME + "(" + GameContract.GameEntry._ID + ")," +
                    PlayerEntry.COLUMN_NAME_COLOUR + " TEXT," +
                    PlayerEntry.COLUMN_NAME_FIREFIGHTER + " TEXT," +
                    PlayerEntry.COLUMN_NAME_PREVIOUS_FIREFIGHTER + " TEXT," +
                    PlayerEntry.COLUMN_NAME_AP + " TEXT," +
                    PlayerEntry.COLUMN_NAME_SAVED_AP + " TEXT," +
                    PlayerEntry.COLUMN_NAME_BONUS_AP + " TEXT," +
                    PlayerEntry.COLUMN_NAME_HAS_ACTED + " TEXT," +
                    PlayerEntry.COLUMN_NAME_POSITION + " INTEGER)";
    static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME;

    private PlayerContract() {
    }

    private static void create(SQLiteDatabase db, Player player, long gameId, int position) {
        ContentValues values = fillContentValues(player, gameId, position);

        db.insert(PlayerEntry.TABLE_NAME, null, values);
    }

    private static ContentValues fillContentValues(Player player, long gameId, int position) {
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_NAME_NAME, player.getName());
        values.put(PlayerEntry.COLUMN_NAME_COLOUR, player.getColour());
        values.put(PlayerEntry.COLUMN_NAME_FIREFIGHTER, player.getFirefighterTitle());
        if (player.getPreviousFirefighter() != null) {
            values.put(PlayerEntry.COLUMN_NAME_PREVIOUS_FIREFIGHTER,
                    player.getPreviousFirefighter().getTitle());
        }
        values.put(PlayerEntry.COLUMN_NAME_AP, player.getAp());
        values.put(PlayerEntry.COLUMN_NAME_SAVED_AP, player.getSavedAp());
        values.put(PlayerEntry.COLUMN_NAME_BONUS_AP, player.getBonusAp());
        values.put(PlayerEntry.COLUMN_NAME_HAS_ACTED, player.hasActed());
        values.put(PlayerEntry.COLUMN_NAME_GAME, gameId);
        values.put(PlayerEntry.COLUMN_NAME_POSITION, position);
        return values;
    }

    static void createPlayerList(SQLiteDatabase db, ArrayList<Player> players, long gameId) {
        for (Player player : players) {
            create(db, player, gameId, players.indexOf(player));
        }
    }

    static ArrayList<Player> restorePlayerList(SQLiteDatabase db, long gameId) {
        String selection = PlayerEntry.COLUMN_NAME_GAME + " = ?";
        String[] selectionArgs = {String.valueOf(gameId)};
        String sortOrder = PlayerEntry.COLUMN_NAME_POSITION + " ASC";
        Cursor result = db.query(
                PlayerEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Player> playerList = new ArrayList<>(result.getCount());
        while (result.moveToNext()) {
            playerList.add(restoreFromCursor(result));
        }
        result.close();
        return playerList;
    }

    private static Player restoreFromCursor(Cursor result) {
        Player player = new Player();
        player.setName(result.getString(result.getColumnIndex(PlayerEntry.COLUMN_NAME_NAME)));
        player.setColour(result.getInt(result.getColumnIndex(PlayerEntry.COLUMN_NAME_COLOUR)));
        player.setAp(result.getInt(result.getColumnIndex(PlayerEntry.COLUMN_NAME_AP)));
        player.setSavedAp(result.getInt(result.getColumnIndex(PlayerEntry.COLUMN_NAME_SAVED_AP)));
        player.setBonusAp(result.getInt(result.getColumnIndex(PlayerEntry.COLUMN_NAME_BONUS_AP)));

        String firefighterTitle = result.getString(
                result.getColumnIndex(PlayerEntry.COLUMN_NAME_FIREFIGHTER));
        String previousFirefighterTitle = result.getString(
                result.getColumnIndex(PlayerEntry.COLUMN_NAME_PREVIOUS_FIREFIGHTER));

        player.setFirefighter(Firefighter.fromTitle(firefighterTitle));
        player.setPreviousFirefighter(Firefighter.fromTitle(previousFirefighterTitle));

        int hasActed = result.getInt(result.getColumnIndex(PlayerEntry.COLUMN_NAME_HAS_ACTED));
        player.setHasActed(hasActed > 0);
        return player;
    }

    static void savePlayerList(SQLiteDatabase db, ArrayList<Player> players, long gameId) {
        String where = PlayerEntry.COLUMN_NAME_GAME + " = ?";
        String[] whereArgs = {String.valueOf(gameId)};
        db.delete(PlayerEntry.TABLE_NAME, where, whereArgs);
        for (Player player : players) {
            create(db, player, gameId, players.indexOf(player));
        }
    }

    private static class PlayerEntry implements BaseColumns {
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_GAME = "game";
        static final String TABLE_NAME = "player";
        static final String COLUMN_NAME_COLOUR = "colour";
        static final String COLUMN_NAME_FIREFIGHTER = "firefighter";
        static final String COLUMN_NAME_PREVIOUS_FIREFIGHTER = "previous_firefighter";
        static final String COLUMN_NAME_AP = "ap";
        static final String COLUMN_NAME_SAVED_AP = "saved_ap";
        static final String COLUMN_NAME_BONUS_AP = "bonus_ap";
        static final String COLUMN_NAME_HAS_ACTED = "has_acted";
        static final String COLUMN_NAME_POSITION = "position";
    }
}
