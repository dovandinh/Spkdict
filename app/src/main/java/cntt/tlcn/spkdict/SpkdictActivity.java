package cntt.tlcn.spkdict;


import java.security.PublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import cntt.tlcn.spkdict.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

    private SharedPreferences prefs;
    int chudedangchon = 0;
    String chuDeDuocClick = "";

    String mauTextTheoChuDe = "";

    private ListView lstWord = null;
    public ArrayList<String> mLSTCurrentWord = null;
    public ArrayList<Integer> mLSTCurrentWordId = null;

    public ArrayList<String> mLSTCurrentContent = null;    //--hien thi nghia ngay luc go tu

    private ArrayAdapter<String> mAdapter = null;

    private EditText edWord = null;

    ListView lv;
    Context context;

    //----------luong----
    private Handler mHandler;
    private Runnable mUpdateTimeTask;

    String text = "";

    ImageButton btnHelp;

    LinearLayout theme;

    ImageButton btnConChuot;
    ImageButton btnThayDoiTheme;
    ImageButton btncaidat;
    ImageButton btnlichsu;
    ImageButton btnyeuthich;

    TextView workText;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // k hien title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btnConChuot = (ImageButton)findViewById(R.id.btnConChuot);
        btnThayDoiTheme = (ImageButton) findViewById(R.id.btnthaydoitheme);
        btnlichsu = (ImageButton) findViewById(R.id.btnHistory);
        btnyeuthich = (ImageButton) findViewById(R.id.btnfavorites);
        theme = (LinearLayout) findViewById(R.id.linearLayouttheme);

        String loaichudetheme = prefs.getString("loaichude", "").toString();
        if (loaichudetheme.equals("White")) {
            mauTextTheoChuDe = "WHITE";
            theme.setBackgroundResource(R.drawable.bg_white);
        } else if(loaichudetheme.equals("Pink")){

            mauTextTheoChuDe = "PINK";
            theme.setBackgroundResource(R.drawable.bg_pink);
        }else if(loaichudetheme.equals("Love")){

            mauTextTheoChuDe = "LOVE";
            theme.setBackgroundResource(R.drawable.bg_love);
        }
        else {
            mauTextTheoChuDe = "BLACK";
            theme.setBackgroundResource(R.drawable.bg);
        }

        // Show diglog on first time on day.

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str_ngaydaluu = prefs.getString("ngaydaluu", "").toString();
        if(str_ngaydaluu == ""){
            str_ngaydaluu = "14/01/2015";
        }

        Date date = new Date();
        String str_ngayhientai = formatter.format(date);

        Date ngaydaluu = null;
        Date ngayhientai = null;

        try {
            ngaydaluu = formatter.parse(str_ngaydaluu);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            ngayhientai = formatter.parse(str_ngayhientai);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ngaydaluu.compareTo(ngayhientai)<0 || str_ngayhientai == "14/02/2015") {

            Log.i(MAIN_TAG, "Hien thi dialog trong ngay dau");
            //Luu lai
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ngaydaluu", str_ngayhientai);
            editor.commit();

            // Create custom dialog object
            final Dialog dialog = new Dialog(SpkdictActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Include dialog.xml file
            dialog.setContentView(R.layout.dialoghello);
            // Set dialog title

            // set values for custom dialog components - text, image and button
            TextView texthello = (TextView) dialog.findViewById(R.id.textDialog);
            ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
            Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

            if(str_ngayhientai == "14/02/2015"){
                texthello.setText("Happy Valentine's Day, You are always in my heart");
                image.setImageResource(R.drawable.valentines_day);
                declineButton.setBackgroundColor(Color.parseColor("#FF99CC"));
                declineButton.setTextColor(Color.parseColor("#006600"));
                declineButton.setText("THANK YOU!");
            }
            else {

                Random r = new Random();
                int random = r.nextInt(10); // 0<= random < 10

                switch(random) {
                    case 1:
                        texthello.setText("You look so beautiful. Have a nice day!");
                        image.setImageResource(R.drawable.bg_em_1);
                        declineButton.setBackgroundColor(Color.parseColor("#FF0066"));
                        declineButton.setTextColor(Color.parseColor("#0000CC"));
                        declineButton.setText("OK (^.^)");
                        break;
                    case 2:
                        texthello.setText("Azure blue sea water. Have a good day.");
                        image.setImageResource(R.drawable.bg_em_2);
                        declineButton.setBackgroundColor(Color.parseColor("#99FF99"));
                        declineButton.setTextColor(Color.parseColor("#990000"));
                        declineButton.setText("I see (*.*)");

                        break;
                    case 3:
                        texthello.setText("The harmonious nature view. Wish you are always happy and beautiful.");
                        image.setImageResource(R.drawable.bg_em_3);
                        declineButton.setBackgroundColor(Color.parseColor("#99FF99"));
                        declineButton.setTextColor(Color.parseColor("#FF0000"));
                        declineButton.setText("THANKS!");
                        break;
                    case 4:
                        texthello.setText("The mountain as some kind of fairyland. Have a good day!");
                        image.setImageResource(R.drawable.bg_em_4);
                        declineButton.setBackgroundColor(Color.parseColor("#CCFFCC"));
                        declineButton.setTextColor(Color.parseColor("#CC0066"));
                        declineButton.setText("OK");

                        break;
                    case 5:
                        texthello.setText("I wish you will study well.");
                        image.setImageResource(R.drawable.bg_em_5);
                        declineButton.setBackgroundColor(Color.parseColor("#99CCFF"));
                        declineButton.setTextColor(Color.parseColor("#FF3300"));
                        declineButton.setText("OK");
                        break;
                    case 6:
                        texthello.setText("I hope you get what you wished for.");
                        image.setImageResource(R.drawable.bg_em_6);
                        declineButton.setBackgroundColor(Color.parseColor("#FFFF99"));
                        declineButton.setTextColor(Color.parseColor("#006600"));
                        declineButton.setText("THANKS");

                        break;
                    case 7:
                        texthello.setText("Wish you are always happy and beautiful.");
                        image.setImageResource(R.drawable.bg_em_7);
                        declineButton.setBackgroundColor(Color.parseColor("#FF9999"));
                        declineButton.setTextColor(Color.parseColor("#3333FF"));
                        declineButton.setText("OK");

                        break;
                    case 8:
                        texthello.setText("May good times always smile on you.");
                        image.setImageResource(R.drawable.bg_em_8);
                        declineButton.setBackgroundColor(Color.parseColor("#FFCC66"));
                        declineButton.setTextColor(Color.parseColor("#333399"));
                        declineButton.setText("OK");

                        break;
                    case 9:
                        texthello.setText("Wish you everything to your liking.");
                        image.setImageResource(R.drawable.bg_em_9);
                        declineButton.setBackgroundColor(Color.parseColor("#FF99CC"));
                        declineButton.setTextColor(Color.parseColor("#00CC00"));
                        declineButton.setText("OK");

                        break;
                    default:
                        texthello.setText("Wish you are always healthy and successful in your life.");
                        image.setImageResource(R.drawable.bg_em_10);
                        declineButton.setBackgroundColor(Color.parseColor("#FF99CC"));
                        declineButton.setTextColor(Color.parseColor("#006600"));
                        declineButton.setText("OK (^.^");
                }
            }

            dialog.show();


            // if decline button is clicked, close the custom dialog
            declineButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    dialog.dismiss();
                }
            });

        }

        menuMain();

        context=this;





        btnConChuot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create custom dialog object
                final Dialog dialogConChuot = new Dialog(SpkdictActivity.this);
                dialogConChuot.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // Include dialog.xml file
                dialogConChuot.setContentView(R.layout.khibanconchuot);
                // Set dialog title
                dialogConChuot.show();

                ImageView image = (ImageView) dialogConChuot.findViewById(R.id.imageConChuot);

                // if decline button is clicked, close the custom dialog
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialogConChuot.dismiss();
                    }
                });
            }
        });

        btnThayDoiTheme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final String items[] = {"White", "Pink", "Love", "Black"};
                AlertDialog.Builder dialogTheme = new AlertDialog.Builder(SpkdictActivity.this);

                dialogTheme.setTitle("Selecting theme");
                final String loaichude = prefs.getString("loaichude", "").toString();

                if (loaichude.equals("White")) {
                    chudedangchon = 0;
                } else if(loaichude.equals("Pink")){
                    chudedangchon = 1;
                }else if(loaichude.equals("Love")){
                    chudedangchon = 2;
                }
                else {
                    chudedangchon = 3;
                }

                dialogTheme.setSingleChoiceItems(items, chudedangchon, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // onClick Action
                        chuDeDuocClick = items[item].toString();

                    }
                })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (!chuDeDuocClick.equals(loaichude)) {

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("loaichude", chuDeDuocClick);
                                    editor.commit();


                                    if (chuDeDuocClick.equals("White")) {
                                        mauTextTheoChuDe = "WHITE";
                                        theme.setBackgroundResource(R.drawable.bg_white);
                                    } else if (chuDeDuocClick.equals("Pink")) {
                                        mauTextTheoChuDe = "PINK";
                                        theme.setBackgroundResource(R.drawable.bg_pink);
                                    }
                                    else if (chuDeDuocClick.equals("Love")) {
                                        mauTextTheoChuDe = "LOVE";
                                        theme.setBackgroundResource(R.drawable.bg_love);
                                    }
                                    else {
                                        mauTextTheoChuDe = "BLACK";
                                        theme.setBackgroundResource(R.drawable.bg);
                                    }

                                    Toast msg = Toast.makeText(SpkdictActivity.this, "Theme changed.", Toast.LENGTH_LONG);
                                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());
                                    msg.show();

                                    //setTheme();
                                    showWordlist();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // on cancel button action
                            }
                        });
                dialogTheme.show();
            }
        });


      /*  btncaidat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Xulynutmenu();


            }
        });*/


        btnlichsu.setOnClickListener(new OnClickListener() {
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
        });
    }

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


    /*    lstWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

                Intent i = new Intent(v.getContext(), ShowContent.class);
                i.putExtra("word", mAdapter.getItem(arg2));
                i.putExtra("id", mLSTCurrentWordId.get(arg2));
                i.putExtra("db", "anh_viet");
                startActivityForResult(i, SHOW_CONTENT_CODE);
                //
            }
        });*/

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(view.getContext(), ShowContent.class);
                i.putExtra("word",mLSTCurrentWord.get(position));
                i.putExtra("id", mLSTCurrentWordId.get(position));
                i.putExtra("db", "anh_viet");
                startActivityForResult(i, SHOW_CONTENT_CODE);
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


                      //  mAdapter.add(strWord);

                        i++;
                    } while (result.moveToNext());
                }

                result.close();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("No data, please copy data to folder Sdcard/MyDict/db")
                        .setTitle("error")
                        .setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .show();
            }
            //lstWord.setAdapter(mAdapter);
            lv=(ListView) findViewById(R.id.lstWord);

            lv.setAdapter(new CustomListviewAdapter(this, mLSTCurrentWord, mauTextTheoChuDe));



            String loaichudetheme = prefs.getString("loaichude", "").toString();
            if (loaichudetheme.equals("White")) {
                lv.setDivider(new ColorDrawable(Color.parseColor("#E6E6E6")));
            } else if(loaichudetheme.equals("Pink")){
                lv.setDivider(new ColorDrawable(Color.parseColor("#FFD1FF")));
            }else if(loaichudetheme.equals("Love")){
                lv.setDivider(new ColorDrawable(Color.parseColor("#FF6699")));
            }
            else {
                lv.setDivider(new ColorDrawable(Color.parseColor("#000A00")));
            }
            lv.setDividerHeight(1);

        } catch (Exception ex) {
            Log.e(MAIN_TAG, "Error = " + ex.toString());
        }
        edWord.setEnabled(true);
    }
}