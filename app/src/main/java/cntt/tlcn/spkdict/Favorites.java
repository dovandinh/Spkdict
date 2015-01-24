package cntt.tlcn.spkdict;

import java.util.ArrayList;
import java.util.Arrays;

import cntt.tlcn.spkdict.R;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Favorites extends Activity {
    private static final String BOOKMACK_TAG = "[Spkdict - HistoryView] ";
    private ListView mLSTBookmack = null;
    private ArrayList<String> lstDict = null;
    private ArrayList<Integer> lstId = null;

    private ImageButton btndeleteAll;
    private ImageButton btnback;

    private ArrayAdapter<String> aptList = null;

    private ArrayList<String> mWordBookmack = null;

    static final private int SHOW_CONTENT_CODE = 1;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        btndeleteAll = (ImageButton) findViewById(R.id.btndeletetatca);
        btndeleteAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                thongbaoxoaall();
            }
        });

        String strHistory = prefs.getString("bookmack", "");
        Log.i(BOOKMACK_TAG, "Bookmack loaded");
        if (strHistory != null && !strHistory.equals("")) {
            mWordBookmack = new ArrayList<String>(Arrays.asList(strHistory.split(",")));
        } else {
            mWordBookmack = new ArrayList<String>();
        }


        ///
        //khoi tao list view
        mLSTBookmack = (ListView) findViewById(R.id.listView1);
        lstId = new ArrayList<Integer>();
        aptList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        lstId = new ArrayList<Integer>();

        lstId.clear();
        aptList.clear();

        //----------------
        if (mWordBookmack != null && mWordBookmack.size() > 0) {
            try {
                for (int i = 0; i < mWordBookmack.size(); i++) {

                    String arrPart[] = mWordBookmack.get(i).split("::");
                    if (arrPart.length == 2) {
                        lstId.add(i, Integer.parseInt(arrPart[0]));
                        aptList.add(arrPart[1]);
                    } else {
                        Log.i(BOOKMACK_TAG, "Wrong entry: " + mWordBookmack.get(i));
                    }
                }
            } catch (Exception ex) {
                Log.i(BOOKMACK_TAG, "Wrong entry found!");
            }
        }

        mLSTBookmack.setAdapter(aptList);

        mLSTBookmack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                Intent i = new Intent(v.getContext(), ShowContent.class);
                i.putExtra("id", lstId.get(arg2));
                startActivityForResult(i, SHOW_CONTENT_CODE);
            }
        });

        mLSTBookmack.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                thongbaoxoa1tu(lstId.get(arg2).toString() + "::" + aptList.getItem(arg2).toString() + ",", aptList.getItem(arg2).toString(), arg2);

                return false;

            }
        });
        btnback = (ImageButton) findViewById(R.id.btnback2);
        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }

    public void thongbaoxoa1tu(final String word, final String wordxoa, final int IDxoa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure delete: " + wordxoa)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // xu ly xoa
                        mLSTBookmack.setAdapter(aptList);

                        String strHistory = prefs.getString("bookmack", "") + ",";
                        SharedPreferences.Editor editor = prefs.edit();
                        strHistory.replace(word, "");
                        String newLink = replace15(strHistory, word, "");

                        editor.putString("bookmack", newLink);
                        editor.commit();
                        aptList.remove(wordxoa);
                        lstId.remove(IDxoa);
                        mLSTBookmack.setAdapter(aptList);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void thongbaoxoaall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure delete all bookmack, if you want delete a word, you can long click it")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // xu ly xoa
                        mWordBookmack.clear();
                        aptList.clear();
                        mLSTBookmack.setAdapter(aptList);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("bookmack", "");
                        editor.commit();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static String replace15(String aInput, String aOldPattern, String aNewPattern) {
        return aInput.replace(aOldPattern, aNewPattern);
    }

}
