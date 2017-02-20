package eu.yalacirodev.numberbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nico on 18/02/17.
 */
public class BestScores extends SQLiteOpenHelper {

    private static BestScores mInstance = null;

    private static final String DB_NAME = "NumberBox.db";
    private static final String SCORE_TABLE = "Score";
    private static final String L_NAME = "level_id";
    private static final String L_BEST = "level_best";

    public static final int NOT_COMPLETED = Integer.MAX_VALUE;

    public static BestScores getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new BestScores(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private BestScores(Context context) {
        super(context, DB_NAME, null, 1);
        // SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + SCORE_TABLE + " ( " +
                L_NAME + " TEXT PRIMARY KEY, " +
                L_BEST + " INTEGER ) ;";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + DB_NAME + " ;";
        db.execSQL(query);
        onCreate(db);
    }


    public Boolean insertData(String name, int best) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(L_NAME, name);
        contentValues.put(L_BEST, best);
        long result = db.insert(SCORE_TABLE, null, contentValues);

        return (result != -1);
    }


    public Boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = L_NAME + " = " + "'" + name + "'" + " ;";
        long result = db.delete(SCORE_TABLE, whereClause, null);

        // exactly one row has been deleted?
        return (result == 1);
    }

    public void updateData(String name, int best) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + SCORE_TABLE +
                " SET " + L_BEST + " = " + best +
                " WHERE " + L_NAME + " = " + "'" + name + "'" + " ;";

        db.execSQL(query);
    }

    public int getBest(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + L_BEST + " FROM " + SCORE_TABLE
                + " WHERE " + L_NAME + " = " + "'" + name + "'" + " ;";
        Cursor cursor = db.rawQuery(query, null);
        int best;
        if (cursor.moveToFirst()) { // first row of the cursor
            best = Integer.parseInt(cursor.getString(0)); // first column of the cursor
            cursor.close();
            return best;
        } else {
            cursor.close();
            return NOT_COMPLETED;
        }
    }

    public Cursor getAllData () {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + SCORE_TABLE;
        return db.rawQuery(query,null);
    }

}
