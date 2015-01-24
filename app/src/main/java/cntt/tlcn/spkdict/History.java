package cntt.tlcn.spkdict;

import java.util.ArrayList;
import java.util.Arrays;

import cntt.tlcn.spkdict.R;

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

public class History extends Activity {

    private static final String HISTORY_TAG = "[Spkdict - HistoryView] ";

    private ListView mLSTHistory = null;

    private ArrayList<Integer> lstId = null;
    private ArrayAdapter<String> aptList = null;

    private ArrayList<String> mWordHistory = null;

    private ImageButton btnxoa;
    private ImageButton btnback;

    private SharedPreferences prefs;
    static final private int SHOW_CONTENT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        //// lay gia tri re luu trong bo nho may
        String strHistory = prefs.getString("history", "");
        Log.i(HISTORY_TAG, "History loaded");
        if (strHistory != null && !strHistory.equals("")) {
            mWordHistory = new ArrayList<String>(Arrays.asList(strHistory.split(",")));
        } else {
            mWordHistory = new ArrayList<String>();
        }
        ///
        //khoi tao list view
        mLSTHistory = (ListView) findViewById(R.id.lstHistory);
        lstId = new ArrayList<Integer>();
        aptList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        lstId = new ArrayList<Integer>();

        lstId.clear();
        aptList.clear();
        //----------------
        if (mWordHistory != null && mWordHistory.size() > 0) {
            try {
                for (int i = 0; i < mWordHistory.size(); i++) {

                    String arrPart[] = mWordHistory.get(i).split("::");
                    if (arrPart.length == 2) {
                        lstId.add(i, Integer.parseInt(arrPart[0]));
                        aptList.add(arrPart[1]);
                    } else {
                        Log.i(HISTORY_TAG, "Wrong entry: " + mWordHistory.get(i));
                    }
                }
            } catch (Exception ex) {
                Log.i(HISTORY_TAG, "Wrong entry found!");
            }
        }

        mLSTHistory.setAdapter(aptList);


        mLSTHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                Intent i = new Intent(v.getContext(), ShowContent.class);
                i.putExtra("id", lstId.get(arg2));
                startActivityForResult(i, SHOW_CONTENT_CODE);
            }
        });

        btnxoa = (ImageButton) findViewById(R.id.btndelete);
        btnxoa.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                thongbaoxoaall();

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

    public void thongbaoxoaall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure delete all history")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // xu ly xoa
                        mWordHistory.clear();
                        aptList.clear();
                        mLSTHistory.setAdapter(aptList);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("history", "");
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
}
