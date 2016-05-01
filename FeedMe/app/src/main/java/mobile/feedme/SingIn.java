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
    }

    public void    signIn(View view)
    {
        EditText et1 = (EditText) (findViewById(R.id.editTextLogin));
        String username = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.EditTextPassword));
        String password = et2.getText().toString();

        Api.Authentificate(this, username, password);
     }

    public void loginSuccess()
    {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

    public void loginError(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    }
}













