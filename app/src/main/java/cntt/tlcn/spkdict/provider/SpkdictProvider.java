package cntt.tlcn.spkdict.provider;


import cntt.tlcn.spkdict.data.SpkdictEngine;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class SpkdictProvider extends ContentProvider {
    public static final String PROVIDER_TAG = "[SpkdictProvider]";
    public static final String PROVIDER_NAME = "cntt.tlcn.spkdict.SpkdictProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME);


    private static final int CODE_LIST_EMPTY = 0;
    private static final int CODE_LIST = 1;
    private static final int CODE_CONTENT_FROM_ID = 2;
    private static final int CODE_CONTENT_FROM_WORD = 3;


    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "dict/*/list/", CODE_LIST_EMPTY);
        uriMatcher.addURI(PROVIDER_NAME, "dict/*/list/*", CODE_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "dict/*/contentId/#", CODE_CONTENT_FROM_ID);
        uriMatcher.addURI(PROVIDER_NAME, "dict/*/contentWord/*", CODE_CONTENT_FROM_WORD);

    }

    private SpkdictEngine mDBEngine;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public boolean onCreate() {
        mDBEngine = new SpkdictEngine();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor c;
        int wordId;
        String word;
        switch (uriMatcher.match(uri)) {
            case CODE_LIST_EMPTY:
                if (mDBEngine.setDatabasefile() == false)
                    return null;
                c = mDBEngine.getCursorWordList("");
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            case CODE_CONTENT_FROM_ID:
                wordId = Integer.parseInt(uri.getPathSegments().get(3));
                c = mDBEngine.getCursorContentFromId(wordId);
                c.setNotificationUri(getContext().getContentResolver(), uri);

                return c;
            // khi go 1 tu vao textbox thi thuc hien cau lenh query
            case CODE_LIST:
                word = uri.getPathSegments().get(3);
                c = mDBEngine.getCursorWordList(word);
                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            case CODE_CONTENT_FROM_WORD:
                word = uri.getPathSegments().get(3);
                c = mDBEngine.getCursorContentFromWord(word);
                c.setNotificationUri(getContext().getContentResolver(), uri);

                return c;
            default:

                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
