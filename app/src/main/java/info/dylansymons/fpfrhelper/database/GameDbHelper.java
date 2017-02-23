package info.dylansymons.fpfrhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Game.db";

    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GameContract.SQL_CREATE);
        db.execSQL(FirefighterContract.SQL_CREATE);
        db.execSQL(PlayerContract.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GameContract.SQL_DELETE);
        db.execSQL(FirefighterContract.SQL_DELETE);
        db.execSQL(PlayerContract.SQL_DELETE);
        onCreate(db);
    }
}
