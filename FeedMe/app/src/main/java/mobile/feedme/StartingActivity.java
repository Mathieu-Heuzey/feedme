package mobile.feedme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedme);

        Api.Initialize(getResources().getString(R.string.serverBaseUrl), getResources().getString(R.string.apiUrl));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("text", MODE_PRIVATE);

        if (pref.contains("token"))
        {
            Api.setToken(pref.getString("token", ""));
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        else
            startActivity(new Intent(getApplicationContext(), SingIn.class));

    }
}
