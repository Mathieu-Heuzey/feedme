package mobile.feedme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Order;

/**
 * Created by stevy_000 on 5/28/2016.
 */
public class OrderListAdapter extends ArrayAdapter<Map.Entry<Order, Integer>> {
    public OrderListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public OrderListAdapter(Context context, int resource, List<Map.Entry<Order, Integer>> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.order_basic_view, parent, false);
        }

        Map.Entry<Order, Integer> item = super.getItem(position);
        Order order = item.getKey();

        TextView orderDishTitleView = (TextView)rowView.findViewById(R.id.order_dish_title);
        TextView orderDishDescView = (TextView)rowView.findViewById(R.id.order_dish_desc);

        orderDishTitleView.setText(order.DishOrdered.Name);
        orderDishDescView.setText(order.DishOrdered.Description);

        return rowView;
    }


}
