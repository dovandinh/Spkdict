package cntt.tlcn.spkdict;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddWord extends Activity {
    DB_Adapter mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addword);

        final EditText ed1 = (EditText) findViewById(R.id.editadd1);
        final EditText ed2 = (EditText) findViewById(R.id.editadd2);
        Button bt1 = (Button) findViewById(R.id.button1);

        mDb = new DB_Adapter(this);
        View.OnClickListener bt1OnClick = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mDb.openDB();
                mDb.insert(ed1.getText().toString(), ed2.getText().toString());
                Toast msg = Toast.makeText(AddWord.this, "Add word sussecfull", Toast.LENGTH_LONG);
                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());
                msg.show();
            }
        };
        bt1.setOnClickListener(bt1OnClick);
    }

}
