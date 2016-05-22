package mobile.feedme;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Order;

public class OrderActivity extends MenuActivity implements AdapterView.OnItemSelectedListener {
    //Origin
    static final Integer BUY        = 0b1;
    static final Integer SELL       = 0b10;
    //Status
    static final Integer INPROGRESS = 0b100;
    static final Integer ACCEPT     = 0b1000;
    static final Integer REFUSE     = 0b10000;
    static final Integer CANCEL     = 0b100000;
    static final Integer DONE       = 0b1000000;

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

    protected LinkedHashMap<Integer, List<Order>> _buy;
    protected LinkedHashMap<Integer, List<Order>> _sell;

    protected LinkedHashMap<Integer, LinkedHashMap<Integer, List<Order>>> _allOrders = new LinkedHashMap<Integer, LinkedHashMap<Integer, List<Order>>>();

    protected Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenuItemEnabled(MenuActivity.ORDERS, false);
        super.initialize(R.layout.activity_order, false, true);
        super.setTitle("Orders");

        final AdapterView.OnItemSelectedListener listener = this;
        _spinner = (Spinner) findViewById(R.id.order_spinner);
        _spinner.post(new Runnable() {
            public void run() {
                _spinner.setOnItemSelectedListener(listener);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Api.getOrderHistoric(this);
    }

    public void buildAndDisplayOrders(LinkedHashMap<Integer, List<Order>> sell, LinkedHashMap<Integer, List<Order>> buy)
    {
        _sell = sell;
        _buy = buy;
        _allOrders.put(OrderActivity.SELL, _sell);
        _allOrders.put(OrderActivity.BUY, _buy);
        this.buildOrderList(_spinner.getSelectedItemPosition() == 0 ? OrderActivity.BUY : OrderActivity.SELL);
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

    protected void buildOrderList(int type)
    {
        if (_allOrders.size() == 0)
            return;

        LinkedHashMap<Integer, List<Order>> orders = _allOrders.get(type);
        List<Map.Entry<Order, Integer>> sortedOrder = new ArrayList<Map.Entry<Order, Integer>>();

        for (Map.Entry<Integer, List<Order>> orderList : orders.entrySet())
        {
            for (Order elem : orderList.getValue())
                sortedOrder.add(new AbstractMap.SimpleEntry<Order, Integer>(elem, type | orderList.getKey()));
        }
        // Now sort by address instead of name (default).
        Collections.sort(sortedOrder, new Comparator<Map.Entry<Order, Integer>>() {
            public int compare(Map.Entry<Order, Integer> one, Map.Entry<Order, Integer> other) {
                return one.getKey().DateCreate.compareTo(other.getKey().DateCreate);
            }
        });
        return;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.buildOrderList(i == 0 ? OrderActivity.BUY : OrderActivity.SELL);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //clear ?
    }
}
