package mobile.feedme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SingIn extends AppCompatActivity {

    public Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void    signIn(View view)
    {
        EditText et1 = (EditText) (findViewById(R.id.editTextLogin));
        String log = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.EditTextPassword));
        String password = et2.getText().toString();


        if (this.api.login(log, password))
        {
            //on ouvre la map
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Password / Login doesn't match", Toast.LENGTH_SHORT).show();
        }
    }
}
