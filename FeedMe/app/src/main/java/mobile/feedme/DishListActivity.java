package mobile.feedme;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.telerik.android.common.Util;

import org.json.JSONArray;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mobile.feedme.POCO.Dish;
import mobile.feedme.POCO.Utilisateur;

public class DishListActivity extends MenuActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
    private static final Integer INPROGRESS = 1;
    private static final Integer FINISH = 2;


    private Utilisateur _user;

    private ArrayList<Map.Entry<Dish, Integer>> _allDishes = new ArrayList<>();


    protected DishListAdapter _adapter;
    protected SwipeRefreshLayout _refresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setMenuItemEnabled(MenuActivity.MYDISHES, false);
        super.initialize(R.layout.activity_dish_list, false, true);

        // pour envoyer utilisateur de l'autre coté :
//        Intent i = new Intent(this, DishListActivity.class);
//        i.putExtra("User",  theUser);
//        startActivity(i);
        Intent i = getIntent();
        this._user = i.<Utilisateur>getParcelableExtra("User");
        if (this._user.UtilisateurId.equals(Api.loggedUser.UtilisateurId)) {
            super.setTitle("My dish list");
        }
        else {
            super.setTitle(String.format("Dish list of %s", this._user.Firstname));
        }

        _refresher = (SwipeRefreshLayout)findViewById(R.id.dishlist_refresher);
        _refresher.setOnRefreshListener(this);


        ListView dishList = (ListView)findViewById(R.id.dishlist_list);
        dishList.setOnScrollListener(this);
        dishList.setEmptyView(findViewById(R.id.dishlist_empty_text));

        _adapter = new DishListAdapter(getApplicationContext(), R.layout.dishlist_cell_view, new ArrayList<Map.Entry<Dish, Integer>>());
        dishList.setAdapter(_adapter);
        {
            final Activity caller = this;
            dishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Dish clickedDish = ((Map.Entry<Dish, Integer>) parent.getItemAtPosition(position)).getKey();
                    Intent i = new Intent(caller, DishDetailActivity.class);
                    i.putExtra("Dish", clickedDish);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.refreshDishes();
    }

    public void buildAndRefreshList(JSONArray jsonDishes)
    {
        _allDishes.clear();
        for (int i = 0; i < jsonDishes.length(); ++i)
        {
            Dish dish = Dish.JSONParse(jsonDishes.optJSONObject(i));
            if (dish != null)
                _allDishes.add(new AbstractMap.SimpleEntry<>(dish, dish.Statut.equals("In progress") ? DishListActivity.INPROGRESS : DishListActivity.FINISH));
        }

        //Sorting by descending datecreate
        Collections.sort(_allDishes, new Comparator<Map.Entry<Dish, Integer>>() {
            public int compare(Map.Entry<Dish, Integer> one, Map.Entry<Dish, Integer> other) {
                return other.getKey().DateCreate.compareTo(one.getKey().DateCreate);
            }
        });

        this.refreshAdapterData(_allDishes);
    }

    private void refreshAdapterData(List<Map.Entry<Dish, Integer>> dishes)
    {
        _adapter.clear();
        _adapter.addAll(dishes);
        _adapter.notifyDataSetChanged();
    }

    protected void refreshDishes()
    {
        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        _refresher.post(new Runnable() {
            @Override
            public void run() {
                _refresher.setRefreshing(true);
            }
        });
        Api.getDishesFromUserId(this, _user.UtilisateurId);
    }

    public void refreshDishDone()
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
