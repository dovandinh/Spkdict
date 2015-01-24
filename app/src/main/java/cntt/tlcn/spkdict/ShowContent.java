package cntt.tlcn.spkdict;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cntt.tlcn.spkdict.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ImageButton;

import android.widget.TextView;

import android.widget.Toast;

public class ShowContent extends Activity {

    static final private String CONTENT_TAG = "[Spkdict - Content]";
    private ArrayList<String> mWordHistory = null;
    private ArrayList<String> mWordBookmack = null;
    private SharedPreferences prefs;
    ImageButton btnBack;
    ImageButton btnlichsu;
    ImageButton btnaddbookmack;
    private TextView demuc;
    private ImageButton btnhienthiamthanh;
    private MediaPlayer media;
    private static String path = null;
    private String mCurrentWord;
    private String mSelectedDB;
    private int mCurrentWordId;
    private int mCurrentHistoryIndex = -1;
    private int mCurrentBookmackIndex = -1;

    static final private int SHOW_CONTENT_CODE = 1;

    private String noidungtu;
    private String luachon;

    private int mausac = 0;

    int vitri = 0;

    DB_Adapter mDb;


    private ProgressDialog pd = null;


    private static final String MIMETYPE = "text/html";
    private static final String ENCODING = "UTF-8";


    public String mContentStyle1 = "body {color:#050500;font-size:14px; font-family: Tahoma, Arial, Verdana, serif} * {margin:0px; padding:0px;} " +
            "ul {padding:1px; margin-left:20px;} " +
            "li{padding:0px;}" +
            " .type{font-weight:bold;font-size:18px;color:#0309fa;}" + // tu loai
            " .example {font-weight:bold;color:#FA8000;} " +
            ".title{font-weight:bold; font-size:18px; color:#a90120;}" +
            " .mexample{color:#535350; font-style:italic;} " +   // nghia cua vi du
            ".aexample{font-weight:bold;color:#FA8000;} " +
            ".aidiom{font-weight:bold;color:#CC0000;}";

    public String mContentStyle2 = "body {color:#FCFBDC;font-size:14px; font-family: Tahoma, Arial, Verdana, serif} * {margin:0px; padding:0px;} " +
            "ul {padding:1px; margin-left:20px;} " +
            "li{padding:0px;}" +
            " .type{font-weight:bold;font-size:18px;color:#56797F;}" +
            " .example {font-weight:bold;color:#FA8000;} " +
            ".title{font-weight:bold; font-size:18px; color:#87A0A4;}" +
            " .mexample{color:#B9Bf8E; font-style:italic;} " +
            ".aexample{font-weight:bold;color:#FA8000;} " +
            ".aidiom{font-weight:bold;color:#CC0000;}";

    private WebView wvContent = null;

    // menu
    //---only created once---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuChoice(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        CreateMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return MenuChoice(item);
    }

    private void CreateMenu(Menu menu) {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Delete word");
        {
            mnu1.setAlphabeticShortcut('a');
            mnu1.setIcon(R.drawable.btdelete);
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Edit word");
        {
            mnu2.setAlphabeticShortcut('b');
            mnu2.setIcon(R.drawable.btedit);
        }
        MenuItem mnu3 = menu.add(0, 2, 2, "Select color");
        {
            mnu3.setAlphabeticShortcut('c');
            mnu3.setIcon(R.drawable.btcolor);
        }

    }

    private boolean MenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:


                mDb = new DB_Adapter(this);
                mDb.openDB();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure delete: " + mCurrentWord)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // xu ly xo

                                mDb.remove(mCurrentWordId);
                                Toast msg = Toast.makeText(ShowContent.this, "Delete sussecfull", Toast.LENGTH_LONG);
                                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());
                                msg.show();

                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            case 1:

                Intent i = new Intent(this, EditWord.class);
                i.putExtra("word", mCurrentWord);
                i.putExtra("id", mCurrentWordId);
                i.putExtra("content", noidungtu);
                startActivityForResult(i, SHOW_CONTENT_CODE);
                return true;
            case 2:
                final String items[] = {"Black", "White"};


                AlertDialog.Builder ab = new AlertDialog.Builder(ShowContent.this);
                ab.setTitle("Select color");
                String luachon1 = prefs.getString("luachon", "").toString();
                if (luachon1.equals("White")) {
                    vitri = 1;
                } else {
                    vitri = 0;

                }

                ab.setSingleChoiceItems(items, vitri, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // onClick Action
                        luachon = items[item].toString();

                    }
                })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("luachon", luachon);
                                editor.commit();
                                Toast msg = Toast.makeText(ShowContent.this, "Color changed.", Toast.LENGTH_LONG);

                                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());

                                msg.show();

                                // on Ok button action
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // on cancel button action
                            }
                        });
                ab.show();
                return true;


        }
        return false;
    }
    // end menu

    ////------ luu lai khi chuong trinh thoat
    @Override
    public void onPause() {
        super.onPause();
        saveHistoryToPreferences();
        saveBookmackToPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hienthinghia);
        btnBack = (ImageButton) findViewById(R.id.btntroloai);
        btnlichsu = (ImageButton) findViewById(R.id.btnHistory);

        btnaddbookmack = (ImageButton) findViewById(R.id.btnaddbookmack);

        btnhienthiamthanh = (ImageButton) findViewById(R.id.btnamthanh);


        Intent i = this.getIntent();
        int wordId = i.getIntExtra("id", -1);
        mCurrentWord = i.getStringExtra("word");
        mSelectedDB = i.getStringExtra("db");


        demuc = (TextView) findViewById(R.id.hienthidemuc);
        demuc.setText(mCurrentWord + "  ");


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadHistoryFromPreferences();
        loadBookmackFromPreferences();


        wvContent = (WebView) findViewById(R.id.webView1);
        //	wvContent.setBackgroundColor(Color.argb(255, 0, 0, 0));

        String content = getContentById(wordId);
        showContent(content);


        // xac dinh mau sac webview

        String luachon1 = prefs.getString("luachon", "").toString();
        if (luachon1.equals("White")) {
            wvContent.setBackgroundColor(Color.argb(255, 255, 255, 255));
            mausac = 0;

            demuc.setBackgroundColor(R.color.maudemuc);
        } else {
            wvContent.setBackgroundColor(Color.argb(255, 0, 0, 0));
            mausac = 1;
        }


        wvContent.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (pd != null) {
                    pd.dismiss();
                    pd = null;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    String arrUrlPart[] = url.split("://");

                    if (arrUrlPart[0].equals("entry")) {
                        String content = getContentByWord(arrUrlPart[1]);
                        showContent(content);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }

        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SpkdictActivity.class);
                startActivity(intent);

            }
        });

        btnlichsu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), History.class);
                startActivity(intent);
                finish();


            }
        });

        btnaddbookmack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                saveBookmack();
                Toast msg = Toast.makeText(ShowContent.this, "Add favorites sussecfull", Toast.LENGTH_LONG);

                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());

                msg.show();

            }
        });

        btnhienthiamthanh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                media = new MediaPlayer();

                path = Environment.getExternalStorageDirectory().getAbsolutePath();
                path += "/Spkdict/sound/en/" + mCurrentWord + ".mp3";
                try {

                    media.setDataSource(path);
                    media.prepare();
                    media.start();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    Log.i(CONTENT_TAG, "File doesn't exist!!");
                    e1.printStackTrace();

                }

            }
        });
    }

    public String getContentById(int id) {
        Uri uri = Uri.parse("content://cntt.tlcn.spkdict.SpkdictProvider/dict/" + mSelectedDB + "/contentId/" + id);

        Cursor result = managedQuery(uri, null, null, null, null);

        String content = null;
        if (result != null) {
            result.moveToFirst();
            content = decodeContent(result.getString(result.getColumnIndex("content")));
            mCurrentWordId = result.getInt(result.getColumnIndex("id"));
            mCurrentWord = result.getString(result.getColumnIndex("word"));

            noidungtu = content;
        } else // Word not found
        {
            //  	content = getString(R.string.errorWordNotFound);
            mCurrentWordId = -1;
            mCurrentWord = "";
        }
        content = formatContent(content);

        return content;
    }

    //dinh trang html
    public String formatContent(String content) {
        String mContentStyle;
        String luachon1 = prefs.getString("luachon", "").toString();
        if (luachon1.equals("White")) {
            mContentStyle = mContentStyle1;
            demuc.setBackgroundColor(R.color.maudemuc);
        } else {
            mContentStyle = mContentStyle2;
        }

        StringBuilder htmlData = new StringBuilder();
        htmlData.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
        if (mContentStyle != null && !mContentStyle.equals("")) {
            htmlData.append("<head><style type=\"text/css\">" + mContentStyle + "</style></head>\n");
        }
        htmlData.append("<body><font face=\"Arial\">");

        htmlData.append(content);

        htmlData.append("</font></body></html>");

        return htmlData.toString();
    }

    /////////////////////////////
    public static String decodeContent(String content) {
        return content.replace('"', '\'');
    }

    public void showContent(String content) {

        if (content != null) {

            pd = ProgressDialog.show(this, "Working..", "Finding word", true, false);

            saveHistory();

            wvContent.loadDataWithBaseURL(null, content, MIMETYPE, ENCODING, "about:blank");
            demuc.setText(mCurrentWord + "  ");
        }
    }

    public String getContentByWord(String word) {
        Uri uri = Uri.parse("content://cntt.tlcn.spkdict.SpkdictProvider/dict/" + mSelectedDB + "/contentWord/" + word);


        Cursor result = managedQuery(uri, null, null, null, null);

        String content;
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            content = decodeContent(result.getString(result.getColumnIndex("content")));
            mCurrentWordId = result.getInt(result.getColumnIndex("id"));
            mCurrentWord = result.getString(result.getColumnIndex("word"));


        } else {

            content = getString(R.string.LoiKhongThayTu) + word;
            mCurrentWordId = -1;
            mCurrentWord = "";
        }
        content = formatContent(content);

        return content;
    }

    // ham luu lich su
    public void saveHistory() {
        String item = mCurrentWordId + "::" + mCurrentWord;
        if (mWordHistory.indexOf(item) == -1 && mCurrentWordId != -1) // new item
        {
            mWordHistory.add(item);
            mCurrentHistoryIndex = mWordHistory.size();
        }
    }

    // ham luu lich su
    public void saveBookmack() {
        String item = mCurrentWordId + "::" + mCurrentWord;
        if (mWordBookmack.indexOf(item) == -1 && mCurrentWordId != -1) // new item
        {
            mWordBookmack.add(item);
            mCurrentBookmackIndex = mWordBookmack.size();
        }
    }

    ///----- save--------luu khi thoat khoi activty
    public void saveHistoryToPreferences() {
        if (mWordHistory != null && mWordHistory.size() >= 1) {
            StringBuilder sbHistory = new StringBuilder();
            for (String item : mWordHistory) {
                sbHistory.append(item);
                sbHistory.append(",");
            }

            String strHistory = sbHistory.substring(0, sbHistory.length() - 1);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("history", strHistory);
            editor.commit();
            Log.i(CONTENT_TAG, "History saved!");
        }
    }

    ///----- save--------luu khi thoat khoi activty
    public void saveBookmackToPreferences() {
        if (mWordBookmack != null && mWordBookmack.size() >= 1) {
            StringBuilder sbHistory = new StringBuilder();
            for (String item : mWordBookmack) {
                sbHistory.append(item);
                sbHistory.append(",");
            }

            String strHistory = sbHistory.substring(0, sbHistory.length() - 1);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("bookmack", strHistory);
            editor.commit();
            Log.i(CONTENT_TAG, "Bookmack saved!");
        }
    }

    //- load History
    public void loadHistoryFromPreferences() {

        String strHistory = prefs.getString("history", "");
        Log.i(CONTENT_TAG, "History loaded");
        if (strHistory != null && !strHistory.equals("")) {
            mWordHistory = new ArrayList<String>(Arrays.asList(strHistory.split(",")));
        } else {
            if (mWordHistory == null) {
                mWordHistory = new ArrayList<String>();
            } else {
                mWordHistory.clear();
            }
        }
    }

    // load bookmack
    public void loadBookmackFromPreferences() {

        String strHistory = prefs.getString("bookmack", "");
        Log.i(CONTENT_TAG, "Bookmack loaded");
        if (strHistory != null && !strHistory.equals("")) {
            mWordBookmack = new ArrayList<String>(Arrays.asList(strHistory.split(",")));
        } else {
            if (mWordBookmack == null) {
                mWordBookmack = new ArrayList<String>();
            } else {
                mWordBookmack.clear();
            }
        }
    }
}
