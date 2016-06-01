package mobile.feedme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
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

        ImageView dishImage = (ImageView)rowView.findViewById(R.id.order_dish_image);
        View dishContainer = rowView.findViewById(R.id.order_dish_container);
        TextView orderDishTitleView = (TextView)rowView.findViewById(R.id.order_dish_title);
        TextView orderDishDescView = (TextView)rowView.findViewById(R.id.order_dish_desc);
        TextView orderBuyerText = (TextView)rowView.findViewById(R.id.order_buyer_text);
        TextView orderNbPartText = (TextView)rowView.findViewById(R.id.order_nbpart);
        TextView orderTotalPriceText = (TextView)rowView.findViewById(R.id.order_price);
        TextView orderPickupTimeText = (TextView)rowView.findViewById(R.id.order_pickup);
        TextView orderStatutText = (TextView)rowView.findViewById(R.id.order_statut);
        ViewGroup orderInteractionLayout = (ViewGroup)rowView.findViewById(R.id.order_interactionlayout_container);

        ImageLoader.getInstance().displayImage(order.DishOrdered.MainImage, dishImage);

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
        {
            DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            DateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            orderPickupTimeText.setText(String.format("The desired pickup time is the %s at %s", formatDate.format(order.PickupTime), formatTime.format(order.PickupTime)));
        }
        String statut = "";
        if ((item.getValue() & OrderActivity.INPROGRESS) != 0)
            statut = "The order is in progress";
        else if ((item.getValue() & OrderActivity.ACCEPT) != 0)
            statut = "The order has been accepted !";
        else if ((item.getValue() & OrderActivity.REFUSE) != 0)
            statut = "The order has been refused by the seller";
        else if ((item.getValue() & OrderActivity.CANCEL) != 0)
            statut = "The order has been cancelled !";
        else if ((item.getValue() & OrderActivity.DONE) != 0)
            statut = "The order is done";
        orderStatutText.setText(statut);

        orderInteractionLayout.addView(this.generateInteractionLayout(order, item.getValue(), orderInteractionLayout));

        return rowView;
    }

    protected View generateInteractionLayout(Order order, int type, ViewGroup parent)
    {
        if ((type & OrderActivity.BUY) != 0)
            return this.generateInteractionLayoutBuyer(order, type, parent);
        else if ((type & OrderActivity.SELL) != 0)
            return this.generateInteractionLayoutSeller(order, type, parent);
        else // should never happen
            return new View(this.getContext());
    }

    private View generateInteractionLayoutBuyer(Order order, int type, ViewGroup parent)
    {
        View ret;
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        if ((type & OrderActivity.INPROGRESS) != 0)
        {
            ret = inflater.inflate(R.layout.order_buy_inprogress, parent, false);
            Button cancel = (Button)ret.findViewById(R.id.order_buy_inprogress_cancel);
            cancel.setTag(new Tag(Tag.ORDER_CANCEL, order));
            cancel.setOnClickListener(_caller);
        }
        else if ((type & OrderActivity.ACCEPT) != 0)
        {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy' at 'HH:mm:", Locale.FRANCE);
            ret = inflater.inflate(R.layout.order_buy_accept, parent, false);
            TextView pickup = (TextView)ret.findViewById(R.id.order_buy_accept_pickup);
            TextView code = (TextView)ret.findViewById(R.id.order_buy_accept_code);
            TextView phone = (TextView)ret.findViewById(R.id.order_buy_accept_phone);
            pickup.setText(format.format(order.PickupTime));
            code.setText(order.ValidationCode);
            phone.setText(order.DishOrdered.Utilisateur.Phone);
        }
        else if ((type & OrderActivity.REFUSE) != 0)
        {
            ret = inflater.inflate(R.layout.order_buy_refuse, parent, false);
        }
        else if ((type & OrderActivity.CANCEL) != 0)
        {
            ret = inflater.inflate(R.layout.order_buy_cancel, parent, false);
        }
        else if ((type & OrderActivity.DONE) != 0)
        {
            ret = inflater.inflate(R.layout.order_buy_done, parent, false);
        }
        else //should never happen, return empty view
            ret =  new View(this.getContext());
        return ret;
    }

    private View generateInteractionLayoutSeller(Order order, int type, ViewGroup parent)
    {
        View ret;
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        if ((type & OrderActivity.INPROGRESS) != 0)
        {
            ret = inflater.inflate(R.layout.order_sell_inprogress, parent, false);
            TextView phone = (TextView)ret.findViewById(R.id.order_sell_inprogress_phone);
            Button confirm = (Button)ret.findViewById(R.id.order_sell_inprogress_confirm);
            Button refuse = (Button)ret.findViewById(R.id.order_sell_inprogress_refuse);
            phone.setText(order.Buyer.Phone);
            confirm.setTag(new Tag(Tag.ORDER_CONFIRM, order));
            confirm.setOnClickListener(_caller);
            refuse.setTag(new Tag(Tag.ORDER_REFUSE, order));
            refuse.setOnClickListener(_caller);
        }
        else if ((type & OrderActivity.ACCEPT) != 0)
        {
            ret = inflater.inflate(R.layout.order_sell_accept, parent, false);
            Button submit = (Button)ret.findViewById(R.id.order_sell_accept_submit);
            submit.setTag(new Tag(Tag.ORDER_CODEVALIDATION, order));
            submit.setOnClickListener(_caller);
        }
        else if ((type & OrderActivity.REFUSE) != 0)
        {
            ret = inflater.inflate(R.layout.order_sell_refuse, parent, false);
        }
        else if ((type & OrderActivity.CANCEL) != 0)
        {
            ret = inflater.inflate(R.layout.order_sell_cancel, parent, false);
        }
        else if ((type & OrderActivity.DONE) != 0)
        {
            ret = inflater.inflate(R.layout.order_sell_done, parent, false);
        }
        else //should never happen, return empty view
            ret =  new View(this.getContext());
        return ret;
    }
}
