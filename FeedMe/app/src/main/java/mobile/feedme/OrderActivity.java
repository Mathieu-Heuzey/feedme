package mobile.feedme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenuItemEnabled(MenuActivity.ORDERS, false);
        super.initialize(R.layout.activity_order, false, true);

        super.setTitle("Orders");

        Api.getOrderHistoric(this);
    }
}
