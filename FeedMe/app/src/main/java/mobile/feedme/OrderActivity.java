package mobile.feedme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Order;

public class OrderActivity extends MenuActivity {
    static final Integer INPROGRESS = 0;
    static final Integer ACCEPT = 1;
    static final Integer REFUSE = 2;
    static final Integer CANCEL = 3;
    static final Integer DONE = 4;

    static final LinkedHashMap<Integer, String> SELLSTATUS = new LinkedHashMap<Integer, String>() {{
        put(INPROGRESS, "To valid");
        put(ACCEPT, "Accept");
        put(REFUSE, "Refuse");
        put(CANCEL, "Cancel");
        put(DONE, "Done");
    }};
    static final LinkedHashMap<Integer, String> BUYSTATUS = new LinkedHashMap<Integer, String>() {{
        put(INPROGRESS, "In progress");
        put(ACCEPT, "Accept");
        put(REFUSE, "Refuse");
        put(CANCEL, "Cancel");
        put(DONE, "Done");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenuItemEnabled(MenuActivity.ORDERS, false);
        super.initialize(R.layout.activity_order, false, true);

        super.setTitle("Orders");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Api.getOrderHistoric(this);
    }

    public void buildAndDisplayOrders(LinkedHashMap<Integer, List<Order>> sell, LinkedHashMap<Integer, List<Order>> buy)
    {
        return;
    }

    public LinkedHashMap<Integer, List<Order>> buildOrderMapFromJSON(LinkedHashMap<Integer, String> status, JSONObject json)
    {
        LinkedHashMap<Integer, List<Order>> ret = new LinkedHashMap<Integer, List<Order>>();
        JSONArray tmp;
        for (Map.Entry<Integer, String> value : status.entrySet())
        {
            List<Order> list = new ArrayList<Order>();
            tmp = json.optJSONArray(value.getValue());
            for (int i = 0; i < tmp.length(); ++i)
            {
                list.add(Order.JSONParse(tmp.optJSONObject(i)));
            }
            ret.put(value.getKey(), list);
        }
        return ret;
    }
}
