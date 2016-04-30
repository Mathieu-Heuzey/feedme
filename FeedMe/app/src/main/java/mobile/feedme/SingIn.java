package mobile.feedme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.security.*;

import cz.msebera.android.httpclient.Header;

public class SingIn extends AppCompatActivity {

    public Api api = new Api();
    public Crypto crypto = new Crypto();


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
        //le bypass
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        EditText et1 = (EditText) (findViewById(R.id.editTextLogin));
        String log = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.EditTextPassword));
        String password = et2.getText().toString();


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", log);
        params.put("password", crypto.md5(password));
       // Log.d("password md5", crypto.md5(password));
        Log.d("ADebugTag", "Value: " + 1);

        String messages ="";

        messages = messages.concat("http://163.5.84.232/WebService/api/Utilisateurs?email=");
        messages = messages.concat(log);
        messages = messages.concat("&password=");
        messages = messages.concat(crypto.md5(password));

        Log.d("La requete", messages);
        client.post(messages, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                //on ouvre la map
//                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                Log.d("ADebugTag", "Value: " + 2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d("ADebugTag", "Value: " + 3);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
               // Toast.makeText(getApplicationContext(),
//                        "Password / Login doesn't match", Toast.LENGTH_SHORT).show();
//                Log.d("ShowPerson", "ERROR");
            }
        });
        Log.d("ADebugTag", "Value: " + 4);
     }
    }













