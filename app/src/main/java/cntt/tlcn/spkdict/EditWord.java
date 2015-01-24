package cntt.tlcn.spkdict;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditWord extends Activity {

    DB_Adapter mDb;

    private String word;
    private String content;
    private int id;

    private EditText ed1;
    private EditText ed2;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editword);


        Intent i = this.getIntent();
        id = i.getIntExtra("id", -1);
        word = i.getStringExtra("word");
        content = i.getStringExtra("content");

        ed1 = (EditText) findViewById(R.id.editadd11);
        ed2 = (EditText) findViewById(R.id.editadd22);

        bt1 = (Button) findViewById(R.id.button1);

        ed1.setText(word);
        ed2.setText(content);

        mDb = new DB_Adapter(this);
        bt1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDb.openDB();
                mDb.edit(id, ed1.getText().toString(), ed2.getText().toString());

                Toast msg = Toast.makeText(EditWord.this, "Update sussecfull", Toast.LENGTH_LONG);
                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset());
                msg.show();

            }
        });


    }


}
