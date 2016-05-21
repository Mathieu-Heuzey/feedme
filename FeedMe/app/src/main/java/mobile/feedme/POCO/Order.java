package mobile.feedme.POCO;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stevy_000 on 5/21/2016.
 */
public class Order  {
    public int OrderId;
    public Dish DishOrdered;
    public Utilisateur Buyer;
    public int NbPart;
    public double TotalPrice;
    public String Statut;
    public String ValidationCode;
    public Date DateExpiration;
    public Date PickupTime;

    public Order()
    {
        Buyer = new Utilisateur();
        DishOrdered = new Dish();
    }

    public static Order JSONParse(JSONObject jsonOrder) {
        Order order = new Order();

        order.OrderId = jsonOrder.optInt("OrderId");
        order.DishOrdered = Dish.JSONParse(jsonOrder.optJSONObject("Dish"));
        order.NbPart = jsonOrder.optInt("NbPart");
        order.TotalPrice = jsonOrder.optDouble("TotalPrice");
        order.Statut = jsonOrder.optString("Statut");
        order.ValidationCode = jsonOrder.optString("ValidationCode");
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        try {
            order.PickupTime = format.parse(jsonOrder.optString("PickUpTime"));
            order.DateExpiration = format.parse(jsonOrder.optString("DateExpiration"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return order;
    }
}
