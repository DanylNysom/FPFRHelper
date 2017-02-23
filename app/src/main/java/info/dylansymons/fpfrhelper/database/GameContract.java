package info.dylansymons.fpfrhelper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;
import info.dylansymons.fpfrhelper.game.Game;
import info.dylansymons.fpfrhelper.player.Player;

public final class GameContract {
    static final String SQL_CREATE =
            "CREATE TABLE " + GameEntry.TABLE_NAME + " (" +
                    GameEntry._ID + " INTEGER PRIMARY KEY," +
                    GameEntry.COLUMN_NAME_NAME + " TEXT," +
                    GameEntry.COLUMN_NAME_CURRENT_INDEX + " INTEGER," +
                    GameEntry.COLUMN_NAME_EXPANSIONS + " INTEGER)";
    static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME;

    private GameContract() {
    }

    public static Game create(SQLiteDatabase db, String name, Context context) {
        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_NAME_NAME, name);

        long id = db.insert(GameEntry.TABLE_NAME, null, values);
        Game game = restore(db, id);

        FirefighterList firefighters = FirefighterList.getList(context, game);
        FirefighterContract.createFirefighterList(db, firefighters, id);

        ArrayList<Player> players = new ArrayList<>();
        PlayerContract.createPlayerList(db, players, id);

        if (game != null) {
            game.setFirefighterList(firefighters);
            game.setPlayerList(players);
        }
        return game;
    }

    public static void save(SQLiteDatabase db, Game game) {
        PlayerContract.savePlayerList(db, game.getPlayerList(), game.getId());
        FirefighterContract.saveFirefighterList(db, game.getFirefighterList(), game.getId());
        ContentValues values = new ContentValues();
        System.err.println("name = " + game.getName());
        System.err.println("index = " + game.getCurrentPlayerIndex());
        System.err.println("id = " + game.getId());
        values.put(GameEntry.COLUMN_NAME_NAME, game.getName());
        values.put(GameEntry.COLUMN_NAME_CURRENT_INDEX, game.getCurrentPlayerIndex());

        boolean[] expansions = game.getExpansions();
        int expansionCode = 0;
        for (int i = 0; i < expansions.length; i++) {
            if (expansions[i]) {
                expansionCode += (1 << i);
            }
        }
        values.put(GameEntry.COLUMN_NAME_EXPANSIONS, expansionCode);
        String where = GameEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(game.getId())};
        db.update(GameEntry.TABLE_NAME, values, where, whereArgs);
    }

    public static Game restore(SQLiteDatabase db, long id) {
        String selection = GameEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor result = db.query(
                GameEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (result.moveToFirst()) {

            Game game = restoreFromCursor(result);
            ArrayList<Player> playerList = PlayerContract.restorePlayerList(db, id);
            ArrayList<Firefighter> firefighterList = FirefighterContract.restoreFirefighterList(db, id);

            game.setPlayerList(playerList);
            game.setFirefighterList(firefighterList);
            result.close();
            return game;
        } else {
            return null;
        }
    }

    private static Game restoreFromCursor(Cursor result) {
        Game game = new Game();
        game.setId(result.getInt(result.getColumnIndex(GameEntry._ID)));
        game.setName(result.getString(result.getColumnIndex(GameEntry.COLUMN_NAME_NAME)));
        game.setCurrentPlayerIndex(result.getInt(
                result.getColumnIndex(GameEntry.COLUMN_NAME_CURRENT_INDEX)
        ));

        boolean[] expansions = game.getExpansions();
        int expansionCode = result.getInt(result.getColumnIndex(GameEntry.COLUMN_NAME_EXPANSIONS));
        for (int i = 0; i < expansions.length; i++) {
            expansions[i] = (expansionCode & (1 << i)) > 0;
        }

        return game;
    }

    static class GameEntry implements BaseColumns {
        static final String TABLE_NAME = "game";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_CURRENT_INDEX = "current_index";
        static final String COLUMN_NAME_EXPANSIONS = "expansions";
    }
}
