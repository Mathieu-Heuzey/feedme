package mobile.feedme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by stevy_000 on 5/15/2016.
 */
public class MenuListAdapter extends ArrayAdapter<Map.Entry<String, Integer>> {

    public MenuListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MenuListAdapter(Context context, int resource, List<Map.Entry<String, Integer>> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.icon_text_cell, parent, false);
        }

        Map.Entry<String, Integer> item = super.getItem(position);

        TextView text1 = (TextView) rowView.findViewById(R.id.row_text);
        ImageView icon = (ImageView) rowView.findViewById(R.id.row_icon);

        text1.setText(item.getKey());
        icon.setImageResource(item.getValue());
        icon.setColorFilter(Color.argb(255, 255, 0, 0)); // Red Tint

        return rowView;
    }
}
