package mobile.feedme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


//TODO mettre ca tout vide, juste faire le check de token dans le oncreate
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("text", MODE_PRIVATE);
        if (pref.contains("token"))
        {
            Api.setToken(pref.getString("token", ""));
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        else
            startActivity(new Intent(getApplicationContext(), SingIn.class));

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void buttonLoginOnClick(View v)
    {
        startActivity(new Intent(getApplicationContext(), SingIn.class));
//        Button  button=(Button) v;
//        ((Button) v).setText("Clicked");
    }

    public void buttonRegisterOnClick(View v)
    {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
