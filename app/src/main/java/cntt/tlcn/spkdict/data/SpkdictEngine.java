package cntt.tlcn.spkdict.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/*duong dan file trong the nho
 *mo file voi duong dan do
 *Duong dan duoc mac dinh*/


public class SpkdictEngine {

    private SQLiteDatabase mDB = null;
    private String mDBName = "anh_viet";

    public boolean setDatabasefile() {
        String fullDbPath = "/sdcard/MyDict/db/anh_viet/anh_viet.db";
        try {
            mDB = SQLiteDatabase.openDatabase(fullDbPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException ex) {
            ex.printStackTrace();

            return false;
        }
/*		
        if (mDB == null)
		{
			return false;
		}		*/

        return true;

    }

    //----------Ham them--------------------------
    public static String encodeContent(String content) {
        return content.replace('\'', '"');
    }
    ///--------------End Ham them--------------

    // load data len luc khoi chay
    public Cursor getCursorWordList(String word) {
        String query;

        String wordEncode = encodeContent(word);
        if (word.equals("") || word == null) {

            query = "SELECT id,content,word FROM " + mDBName + " WHERE  word >= 'a' LIMIT 100";
        } else {
            query = "SELECT id,content,word FROM " + mDBName + " WHERE  word >= '" + wordEncode + "' LIMIT 100";
        }
        Cursor result = mDB.rawQuery(query, null);

        return result;
    }

    ///------------------ seach theo id--------------------------------
    public Cursor getCursorContentFromId(int wordId) {
        String query;

        if (wordId <= 0) {
            return null;
        } else {
            query = "SELECT id,content,word FROM " + mDBName + " WHERE Id = " + wordId;
        }
        Cursor result = mDB.rawQuery(query, null);

        return result;
    }

    //-------------- khi bat tu------------------------------------------
    public Cursor getCursorContentFromWord(String word) {
        String query;
        if (word == null || word.equals("")) {
            return null;
        } else {
            query = "SELECT id,content,word FROM " + mDBName + " WHERE word = '" + word + "' LIMIT 100";
        }


        Cursor result = mDB.rawQuery(query, null);

        return result;
    }

    //------------------------------------------
    public void closeDatabase() {
        mDB.close();
    }

    public boolean isOpen() {
        return mDB.isOpen();
    }

    public boolean isReadOnly() {
        return mDB.isReadOnly();
    }

}
