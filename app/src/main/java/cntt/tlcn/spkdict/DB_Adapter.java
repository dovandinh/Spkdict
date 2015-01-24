package cntt.tlcn.spkdict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DB_Adapter {
    public static final String SID = "id";
    public static final String TU = "word";
    public static final String NGHIA = "content";

    public static final String DB_NAME = "/sdcard/MyDict/db/anh_viet/anh_viet.db";
    public static final String DB_TABLE = "anh_viet";
    public static final int DB_VERSION = 1;

    private final Context mContext;
    private SQLiteDatabase mDB;
    private DBHelper mDBHelper;

    public DB_Adapter(Context c) {
        mContext = c;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            try {
                //             db.execSQL("CREATE TABLE Student(number integer PRIMARY KEY autoincrement, studentID text,name text);");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            Log.i("DBAdapter", "Updating database...");
            db.execSQL("DROP TABLE IF EXISTS Student");
            onCreate(db);
        }
    }

    public DB_Adapter openDB() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void closeDB() {
        mDBHelper.close();
    }

    public long insert(String _studentId, String _name) {
        ContentValues cv = new ContentValues();
        cv.put(TU, _studentId);
        cv.put(NGHIA, _name);
        return mDB.insert(DB_TABLE, null, cv);
    }

    public boolean edit(int _number, String _id, String _name) {
        ContentValues cv = new ContentValues();
        cv.put(SID, _number);
        cv.put(TU, _id);
        cv.put(NGHIA, _name);
        return mDB.update(DB_TABLE, cv, SID + "=" + _number, null) > 0;
    }

    public boolean remove(int _number) {
        return mDB.delete(DB_TABLE, SID + "=" + _number, null) > 0;
    }

}
