package mobile.feedme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telerik.android.primitives.widget.sidedrawer.RadSideDrawer;

/**
 * Created by stevy_000 on 5/7/2016.
 */
public class MenuActivity extends FragmentActivity
{
    protected RadSideDrawer       drawer;
    protected SwipeRefreshLayout  swipeRefreshLayout;

    @Override
    protected  void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
    }

    protected  void initialize(int contentId, boolean isRefreshable)
    {
        setContentView(R.layout.activity_menu);

        ViewGroup rootPanel = (ViewGroup)this.findViewById(R.id.rootPanel);
        View toolbarMenu = findViewById(R.id.toolbarMenu);
        if (isRefreshable)
        {
            swipeRefreshLayout = new SwipeRefreshLayout(getApplicationContext(), null);
            swipeRefreshLayout.setLayoutParams(findViewById(R.id.toolbarMenu).getLayoutParams());
            rootPanel.removeView(toolbarMenu);
            swipeRefreshLayout.addView(toolbarMenu);
            rootPanel.addView(swipeRefreshLayout);
        }

        this.drawer = new RadSideDrawer(this);
        this.drawer.setMainContent(contentId);
        this.drawer.setDrawerContent(R.layout.content_side_menu);

        rootPanel.addView(this.drawer);
    }

}
