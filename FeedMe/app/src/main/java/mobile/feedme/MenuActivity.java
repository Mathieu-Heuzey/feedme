package mobile.feedme;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

import com.telerik.android.primitives.widget.sidedrawer.RadSideDrawer;

/**
 * Created by stevy_000 on 5/7/2016.
 */
public class MenuActivity extends FragmentActivity
{
    protected RadSideDrawer       drawer;
    protected MySwipeRefreshLayout  swipeRefreshLayout;

    @Override
    protected  void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
    }

    protected  void initialize(int contentId, boolean isRefreshable)
    {
        setContentView(R.layout.activity_menu);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        swipeRefreshLayout = (MySwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(isRefreshable);

        this.drawer = new RadSideDrawer(this);
        this.drawer.setMainContent(contentId);
        this.drawer.setDrawerContent(R.layout.content_side_menu);

        ViewGroup rootPanel = (ViewGroup)this.findViewById(R.id.rootPanel);
        rootPanel.addView(this.drawer);
    }

}
