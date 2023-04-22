package it.antoniotes.unishare.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.antoniotes.unishare.R;

public class HomeGridAdapter extends BaseAdapter {
    Context context;
    String[] subjects;
    int[] image;

    LayoutInflater inflater;

    public HomeGridAdapter(Context context, String[] subjects, int[] image) {
        this.context = context;
        this.subjects = subjects;
        this.image = image;
    }

    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        ImageView img = convertView.findViewById(R.id.grid_image);
        TextView text = convertView.findViewById(R.id.item_name);

        img.setImageResource(image[position]);
        text.setText(subjects[position]);

        return convertView;
    }
}
