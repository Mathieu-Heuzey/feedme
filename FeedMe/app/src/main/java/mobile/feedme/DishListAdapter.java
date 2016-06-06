package mobile.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Dish;
import mobile.feedme.POCO.Order;

/**
 * Created by Quentin on 6/6/2016.
 */
public class DishListAdapter extends ArrayAdapter<Map.Entry<Dish, Integer>> {
    public DishListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public DishListAdapter(Context context, int resource, List<Map.Entry<Dish, Integer>> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;

        if (convertView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.dishlist_cell_view, parent, false);
        }

        Map.Entry<Dish, Integer> item = super.getItem(position);
        Dish dish = item.getKey();

        TextView title = (TextView)rowView.findViewById(R.id.dishlist_title);
        TextView desc = (TextView)rowView.findViewById(R.id.dishlist_desc);
        TextView nbpart = (TextView)rowView.findViewById(R.id.dishlist_nbpart);
        TextView statut = (TextView)rowView.findViewById(R.id.dishlist_statut);
        ImageView image = (ImageView)rowView.findViewById(R.id.dishdetail_image);

        ImageLoader.getInstance().displayImage(dish.MainImage, image);

        title.setText(dish.Name);
        desc.setText(dish.Description);
        nbpart.setText(String.format("%d portions remaining", dish.NbPart));
        statut.setText(dish.Statut.equals("In progress") ? "In sell" : "Finish");
        return rowView;
    }
}
