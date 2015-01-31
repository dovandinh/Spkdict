package cntt.tlcn.spkdict;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sakura on 1/24/2015.
 */
public class CustomListviewAdapter extends BaseAdapter{
    ArrayList<String> result = null;
    Context context;
    String colorText = "";
    private static LayoutInflater inflater=null;
    public CustomListviewAdapter(Activity mainActivity, ArrayList<String> prgmNameList, String color) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        colorText = color;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_listview, null);
        holder.tv=(TextView) rowView.findViewById(R.id.wordtext);
        holder.tv.setText(result.get(position));

        if(colorText.equals("WHITE")) {
             holder.tv.setTextColor(Color.BLUE);
        }
        else if (colorText.equals("PINK")) {
            holder.tv.setTextColor(Color.BLUE);
        }else if (colorText.equals("LOVE")) {
            holder.tv.setTextColor(Color.WHITE);
        }
        else {
            holder.tv.setTextColor(Color.WHITE);
        }


        /*rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result.get(position), Toast.LENGTH_LONG).show();
            }
        });*/
        return rowView;
    }

}