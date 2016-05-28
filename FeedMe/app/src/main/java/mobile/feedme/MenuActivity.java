package mobile.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.telerik.android.primitives.widget.sidedrawer.RadSideDrawer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by stevy_000 on 5/7/2016.
 */
public class MenuActivity extends FragmentActivity implements View.OnClickListener, ListView.OnItemClickListener {
    public static final String DISHMAP = "Dish Map";
    public static final String SELLDISH = "Sell your dish";
    public static final String ORDERS = "My orders";
    public static final String SETTINGS = "Settings";
    public static final String LOGOUT = "Logout";

    protected RadSideDrawer       drawer;
    protected SwipeRefreshLayout  swipeRefreshLayout;

    private Map<String, Boolean>  menuItemEnable;
    private Map<String, Integer>  menuItemIcon;
    public MenuActivity()
    {
        menuItemEnable = new LinkedHashMap<String, Boolean>(5);
        menuItemEnable.put(DISHMAP, true);
        menuItemEnable.put(SELLDISH, true);
        menuItemEnable.put(ORDERS, true);
        menuItemEnable.put(SETTINGS, true);
        menuItemEnable.put(LOGOUT,true);

        menuItemIcon = new LinkedHashMap<String, Integer>(5);
        menuItemIcon.put(DISHMAP, R.drawable.ic_dishmap);
        menuItemIcon.put(SELLDISH, R.drawable.ic_selldish);
        menuItemIcon.put(ORDERS, R.drawable.ic_orders);
        menuItemIcon.put(SETTINGS, R.drawable.ic_menu_settings);
        menuItemIcon.put(LOGOUT, R.drawable.ic_logout);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_menu);
    }

    protected  void initialize(int contentId, boolean isRefreshable, boolean showMenu)
    {
        LayoutInflater factory = getLayoutInflater();

        ViewGroup rootPanel = (ViewGroup)findViewById(R.id.rootPanel);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        View toolbarMenu = factory.inflate(R.layout.content_menu_toolbar, linearLayout, false);
        if (isRefreshable)
        {
            View toolbar = toolbarMenu.findViewById(R.id.menu_toolbar);
            swipeRefreshLayout = new SwipeRefreshLayout(getApplicationContext());
            swipeRefreshLayout.setLayoutParams(toolbar.getLayoutParams());
            swipeRefreshLayout.addView(toolbarMenu);
            linearLayout.addView(swipeRefreshLayout);
        }
        else
        {
            linearLayout.addView(toolbarMenu);
        }


        View externalView = factory.inflate(contentId, linearLayout, false);
        linearLayout.addView(externalView);

        this.drawer = new RadSideDrawer(this);
        this.drawer.setMainContent(linearLayout);
        this.drawer.setDrawerContent(R.layout.content_side_menu);

        if (!showMenu)
        {
            this.drawer.setIsLocked(true);
            ImageButton button = (ImageButton)toolbarMenu.findViewById(R.id.menu_button);
            button.setEnabled(false);
            button.setVisibility(View.GONE);
        }
        else
            this.setMenuList();

        rootPanel.addView(this.drawer);

        this.setButtonsListener();
    }

    protected void setButtonsListener()
    {
        ImageButton imageButton = (ImageButton)findViewById(R.id.menu_button);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.menu_button:
                this.onMenuClick();
                break;
        }
    }

    protected void onMenuClick()
    {
        this.drawer.setIsOpen(true);
    }

    protected void setTitle(String title)
    {
        TextView menuTitle = (TextView)findViewById(R.id.toolbar_title);
        menuTitle.setText(title);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String elem = ((Map.Entry<String,Integer>)adapterView.getItemAtPosition(i)).getKey();

        if (Objects.equals(elem, MenuActivity.LOGOUT))
            Api.logOut(this);
        else if (Objects.equals(elem, MenuActivity.SELLDISH))
            startActivity(new Intent(getApplicationContext(), AddMeal.class));
        else if (Objects.equals(elem, MenuActivity.DISHMAP))
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        else if (Objects.equals(elem, MenuActivity.ORDERS))
            startActivity(new Intent(getApplicationContext(), OrderActivity.class));
    }

    public void setMenuItemEnabled(String elem, Boolean enable)
    {
        if (this.menuItemEnable.containsKey(elem))
            this.menuItemEnable.put(elem, enable);
    }

    protected void setMenuList()
    {
        ListView menuList = (ListView)this.drawer.findViewById(R.id.menu_list);
        ArrayList<Map.Entry<String, Integer>> menuItems = new ArrayList<Map.Entry<String, Integer>>();

        for (Map.Entry<String, Boolean> entry : this.menuItemEnable.entrySet())
        {
            if (entry.getValue())
                menuItems.add(new AbstractMap.SimpleEntry<String, Integer>(entry.getKey(), this.menuItemIcon.get(entry.getKey())));
        }

        MenuListAdapter adapter = new MenuListAdapter(getApplicationContext(), R.layout.icon_text_cell, menuItems);

        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(this);
    }
}
