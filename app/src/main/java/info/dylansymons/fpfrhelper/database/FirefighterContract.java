package info.dylansymons.fpfrhelper.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import info.dylansymons.fpfrhelper.firefighter.Firefighter;
import info.dylansymons.fpfrhelper.firefighter.FirefighterList;

final class FirefighterContract {
    static final String SQL_CREATE =
            "CREATE TABLE " + FirefighterEntry.TABLE_NAME + " (" +
                    FirefighterEntry._ID + " INTEGER PRIMARY KEY," +
                    FirefighterEntry.COLUMN_NAME_NAME + " TEXT," +
                    FirefighterEntry.COLUMN_NAME_GAME + " INTEGER REFERENCES " +
                    GameContract.GameEntry.TABLE_NAME + "(" + GameContract.GameEntry._ID + ")," +
                    FirefighterEntry.COLUMN_NAME_POSITION + " INTEGER)";
    static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + FirefighterEntry.TABLE_NAME;

    private FirefighterContract() {
    }

    static void createFirefighterList(SQLiteDatabase db, FirefighterList firefighters,
                                      long gameId) {
        for (Firefighter firefighter : firefighters) {
            create(db, firefighter, gameId, firefighters.indexOf(firefighter));
        }
    }

    private static void create(SQLiteDatabase db, Firefighter firefighter, long gameId, int position) {
        ContentValues values = new ContentValues();
        values.put(FirefighterEntry.COLUMN_NAME_NAME, firefighter.getTitle());
        values.put(FirefighterEntry.COLUMN_NAME_GAME, gameId);
        values.put(FirefighterEntry.COLUMN_NAME_POSITION, position);

        db.insert(FirefighterEntry.TABLE_NAME, null, values);
    }

    private static void save(SQLiteDatabase db, Firefighter firefighter, long gameId, int position) {
        ContentValues values = new ContentValues();
        values.put(FirefighterEntry.COLUMN_NAME_NAME, firefighter.getTitle());
        values.put(FirefighterEntry.COLUMN_NAME_GAME, gameId);
        values.put(FirefighterEntry.COLUMN_NAME_POSITION, position);

        String where = FirefighterEntry.COLUMN_NAME_NAME + " = ? AND " +
                FirefighterEntry.COLUMN_NAME_GAME + " = ?";
        String[] whereArgs = {firefighter.getTitle(), String.valueOf(gameId)};
        db.update(FirefighterEntry.TABLE_NAME, values, where, whereArgs);
    }

    static void saveFirefighterList(SQLiteDatabase db, FirefighterList firefighters,
                                    long gameId) {
        for (Firefighter firefighter : firefighters) {
            save(db, firefighter, gameId, firefighters.indexOf(firefighter));
        }
    }

    static FirefighterList restoreFirefighterList(SQLiteDatabase db, long gameId) {
        String selection = FirefighterEntry.COLUMN_NAME_GAME + " = ?";
        String[] selectionArgs = {String.valueOf(gameId)};
        String sortOrder = FirefighterEntry.COLUMN_NAME_POSITION + " ASC";
        Cursor result = db.query(
                FirefighterEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        FirefighterList firefighterList = new FirefighterList(result.getCount());
        while (result.moveToNext()) {
            firefighterList.add(restoreFromCursor(result));
        }
        result.close();
        return firefighterList;
    }

    private static Firefighter restoreFromCursor(Cursor result) {
        String name = result.getString(result.getColumnIndex(FirefighterEntry.COLUMN_NAME_NAME));
        return Firefighter.fromTitle(name);
    }

    private static class FirefighterEntry implements BaseColumns {
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_GAME = "game";
        static final String COLUMN_NAME_POSITION = "position";
        static final String TABLE_NAME = "firefighter";
    }
}
