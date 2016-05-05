package mobile.feedme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import mobile.feedme.POCO.Dish;

public class DishDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        Intent i = getIntent();
        Dish dish = i.<Dish>getParcelableExtra("Dish");

        Log.e("DishDetail: ", dish.toString());
    }
}
