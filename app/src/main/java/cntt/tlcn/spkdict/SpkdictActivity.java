package cntt.tlcn.spkdict;


import java.security.PublicKey;
import java.util.ArrayList;

import cntt.tlcn.spkdict.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class SpkdictActivity extends Activity {

    static final private String MAIN_TAG = "[SpkdictActivity]";
    static final private int SHOW_CONTENT_CODE = 1;

    private ListView lstWord = null;
    public ArrayList<String> mLSTCurrentWord = null;
    public ArrayList<Integer> mLSTCurrentWordId = null;

    public ArrayList<String> mLSTCurrentContent = null;    //--hien thi nghia ngay luc go tu

    private ArrayAdapter<String> mAdapter = null;

    private EditText edWord = null;


    //----------luong----
    private Handler mHandler;
    private Runnable mUpdateTimeTask;

    String text = "";

    ImageButton btnHelp;

    LinearLayout theme;

    ImageButton btnHienThiNghia;
    ImageButton btncaidat;
    ImageButton btnlichsu;
    ImageButton btnyeuthich;

    // menu

    final CharSequence[] danhmuc = {"Help", "About", "Exit"};

    public void Xulynutmenu() {

        AlertDialog.Builder builder11 = new AlertDialog.Builder(this);
        builder11.setTitle("Menu");
        builder11.setItems(danhmuc, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                //			Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();//Hiển thị item được lựa chọn
                //	txtDisplay.setText(items[item].toString());
                if (danhmuc[item].toString().equals("Help")) {
                    Intent intent1 = new Intent();
                    intent1.setClass(getApplicationContext(), Help.class);
                    startActivity(intent1);
                } else if (danhmuc[item].toString().equals("About")) {
                    Intent intent2 = new Intent();
                    intent2.setClass(getApplicationContext(), About.class);
                    startActivity(intent2);
                } else {
                    finish();
                }

            }
        });
        builder11.show();
    }

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
        MenuItem mnu1 = menu.add(0, 0, 0, "Add Word");
        {
            mnu1.setAlphabeticShortcut('a');
            mnu1.setIcon(R.drawable.btadd);
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "help");
        {
            mnu2.setAlphabeticShortcut('b');
            mnu2.setIcon(R.drawable.bthelp);
        }
        MenuItem mnu3 = menu.add(0, 2, 2, "About");
        {
            mnu3.setAlphabeticShortcut('c');
            mnu3.setIcon(R.drawable.btabout);

        }

    }

    private boolean MenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddWord.class);
                startActivity(intent);
                return true;
            case 1:
                Intent intent1 = new Intent();
                intent1.setClass(getApplicationContext(), Help.class);
                startActivity(intent1);
                return true;
            case 2:
                Intent intent2 = new Intent();
                intent2.setClass(getApplicationContext(), About.class);
                startActivity(intent2);
                return true;
        }
        return false;
    }
    // end menu

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // k hien title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create custom dialog object
        final Dialog dialog = new Dialog(SpkdictActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialoghello);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        TextView texthello = (TextView) dialog.findViewById(R.id.textDialog);
        texthello.setText("Custom dialog Android example.");
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(R.drawable.about);

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        menuMain();


        btnHienThiNghia = (ImageButton) findViewById(R.id.btnHienThiNghia);
       // btncaidat = (ImageButton) findViewById(R.id.btnSetting);
        //btnlichsu = (ImageButton) findViewById(R.id.btnHistory);
       // btnyeuthich = (ImageButton) findViewById(R.id.btnfavorites);

        theme = (LinearLayout) findViewById(R.id.linearLayouttheme);
        //   theme.setBackgroundResource(R.drawable.theme);

        btnHienThiNghia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                text = edWord.getText().toString();


            }
        });



      /*  btncaidat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Xulynutmenu();


            }
        });*/


       /* btnlichsu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), History.class);
                startActivity(intent);


            }
        });
        btnyeuthich.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Favorites.class);
                startActivity(intent);


            }
        });*/
    }

    public void menuMain() {
        edWord = (EditText) findViewById(R.id.edWord);


        lstWord = (ListView) findViewById(R.id.lstWord);

        mLSTCurrentWordId = new ArrayList<Integer>();
        mLSTCurrentWord = new ArrayList<String>();
        mLSTCurrentContent = new ArrayList<String>();//hien thi nghia ngay luc tra

        mAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);



        showWordlist();
        edWord.requestFocus();

        ///--xu ly luong de hien thi list khi go tu
        mHandler = new Handler();

        mUpdateTimeTask = new Runnable() {
            public void run() {
                Log.i(MAIN_TAG, "update word list now");
                //edWord.setEnabled(false);
                showWordlist();
                //edWord.setEnabled(true);
            }
        };
        edWord.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask, 50);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ;
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ;
            }
        });


        lstWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

                Intent i = new Intent(v.getContext(), ShowContent.class);
                i.putExtra("word", mAdapter.getItem(arg2));
                i.putExtra("id", mLSTCurrentWordId.get(arg2));
                i.putExtra("db", "anh_viet");
                startActivityForResult(i, SHOW_CONTENT_CODE);
                //
            }
        });

    }

    //----------------------------
    public static String decodeContent(String content) {
        return content.replace('"', '\'');
    }

    //-----------------------------
    public void showWordlist() {
        //edWord.setEnabled(false);
        String word = edWord.getText().toString();
        Uri uri = Uri.parse("content://cntt.tlcn.spkdict.SpkdictProvider/dict/" + "anh_viet" + "/list/" + word);
        try {
            Cursor result = managedQuery(uri, null, null, null, null);

            if (result != null) {
                int countRow = result.getCount();
                Log.i(MAIN_TAG, "countRow = " + countRow);
                mLSTCurrentWord.clear();
                //mLSTCurrentContent.clear();
                mLSTCurrentWordId.clear();
                mAdapter.clear();
                if (countRow >= 1) {
                    int indexWordColumn = result.getColumnIndex("word");
                    int indexIdColumn = result.getColumnIndex("id");
                    //          int indexContentColumn = result.getColumnIndex("Content");

                    result.moveToFirst();
                    String strWord;
                    int intId;
                    //---


                    int i = 0;
                    do {
                        strWord = decodeContent(result.getString(indexWordColumn));
                        intId = result.getInt(indexIdColumn);


                        mLSTCurrentWord.add(i, strWord);
                        mLSTCurrentWordId.add(i, intId);


                        mAdapter.add(strWord);
                        i++;
                    } while (result.moveToNext());
                }

                result.close();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("No data, please copy data to forder Sdcard/MyDict/db")
                        .setTitle("error")
                        .setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
            lstWord.setAdapter(mAdapter);
        } catch (Exception ex) {
            Log.e(MAIN_TAG, "Error = " + ex.toString());
        }
        edWord.setEnabled(true);
    }


}