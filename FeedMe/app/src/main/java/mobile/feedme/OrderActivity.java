package mobile.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Dish;
import mobile.feedme.POCO.Order;

public class OrderActivity extends MenuActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
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
    protected OrderListAdapter _adapter;

    protected SwipeRefreshLayout _refresher;

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

        _refresher = (SwipeRefreshLayout)findViewById(R.id.order_refresher);
        _refresher.setOnRefreshListener(this);

        ListView orderList = (ListView)this.drawer.findViewById(R.id.order_list);
        orderList.setOnScrollListener(this);
        orderList.setEmptyView(findViewById(R.id.order_empty_text));

        _adapter = new OrderListAdapter(getApplicationContext(), R.layout.order_basic_view, new ArrayList<Map.Entry<Order, Integer>>());
        _adapter.setClickListener(this);
        orderList.setAdapter(_adapter);
        orderList.setClickable(false);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.refreshOrder();


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
        // Now sort by DateCreate
        Collections.sort(sortedOrder, new Comparator<Map.Entry<Order, Integer>>() {
            public int compare(Map.Entry<Order, Integer> one, Map.Entry<Order, Integer> other) {
                return other.getKey().DateCreate.compareTo(one.getKey().DateCreate);
            }
        });
        this.refreshAdapterData(sortedOrder);
    }

    private void refreshAdapterData(List<Map.Entry<Order, Integer>> orders)
    {
        _adapter.clear();
        _adapter.addAll(orders);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.buildOrderList(i == 0 ? OrderActivity.BUY : OrderActivity.SELL);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //clear ?
    }

    @Override
    public void onClick(View v)
    {
        Tag tag = (Tag)v.getTag();
        if (tag == null) {
            super.onClick(v);
            return;
        }
        if (tag.Type == Tag.DISH)
        {
            Intent i = new Intent(this, DishDetailActivity.class);
            i.putExtra("Dish", (Dish)tag.Content);
            startActivity(i);
        }
        else if (tag.Type == Tag.ORDER_CANCEL)
        {
            Api.orderCancel(this, ((Order)tag.Content).OrderId);
        }
        else if (tag.Type == Tag.ORDER_CONFIRM)
        {
            Api.orderAccept(this, ((Order)tag.Content).OrderId);
        }
        else if (tag.Type == Tag.ORDER_REFUSE)
        {
            Api.orderRefuse(this, ((Order)tag.Content).OrderId);
        }
        else if (tag.Type == Tag.ORDER_CODEVALIDATION)
        {
            EditText input = (EditText)((View)v.getParent()).findViewById(R.id.order_sell_accept_code);
            Api.orderDone(this, ((Order)tag.Content).OrderId, input.getText().toString());
        }
    }

    public void refreshOrder()
    {
        _refresher.post(new Runnable() {
            @Override
            public void run() {
                _refresher.setRefreshing(true);
            }
        });
        Api.getOrderHistoric(this);
    }

    @Override
    public void onRefresh() {
        this.refreshOrder();
    }

    public void refreshOrderDone()
    {
        _refresher.post(new Runnable() {
            @Override
            public void run() {
                _refresher.setRefreshing(false);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        //osef
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        _refresher.setEnabled(!absListView.canScrollVertically(-1));
    }
}
