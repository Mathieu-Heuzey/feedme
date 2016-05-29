package mobile.feedme;

import android.app.Activity;
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
    protected View.OnClickListener _caller;

    public OrderListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public OrderListAdapter(Context context, int resource, List<Map.Entry<Order, Integer>> items) {
        super(context, resource, items);
    }

    public void setClickListener(View.OnClickListener caller)
    {
        _caller = caller;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.order_basic_view, parent, false);
        }

        Map.Entry<Order, Integer> item = super.getItem(position);
        Order order = item.getKey();

        View dishContainer = rowView.findViewById(R.id.order_dish_container);
        TextView orderDishTitleView = (TextView)rowView.findViewById(R.id.order_dish_title);
        TextView orderDishDescView = (TextView)rowView.findViewById(R.id.order_dish_desc);
        TextView orderBuyerText = (TextView)rowView.findViewById(R.id.order_buyer_text);
        TextView orderNbPartText = (TextView)rowView.findViewById(R.id.order_nbpart);
        TextView orderTotalPriceText = (TextView)rowView.findViewById(R.id.order_price);

        dishContainer.setTag(new Tag(Tag.DISH, order.DishOrdered));
        dishContainer.setOnClickListener(_caller);
        orderDishTitleView.setText(order.DishOrdered.Name);
        orderDishDescView.setText(order.DishOrdered.Description);
        orderBuyerText.setText(
                ((item.getValue() & OrderActivity.SELL) != 0 ?
                String.format("Ordered by : %s %s", order.Buyer.Firstname, order.Buyer.Lastname) : //If the order is a SELL one
                String.format("Seller : %s %s", order.DishOrdered.Utilisateur.Firstname, order.DishOrdered.Utilisateur.Lastname)) // else
        );

        orderNbPartText.setText(String.format("%d portion ordered", order.NbPart));
        orderTotalPriceText.setText(String.format("%.2f€", order.TotalPrice));
        return rowView;
    }


}
