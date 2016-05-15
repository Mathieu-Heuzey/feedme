package mobile.feedme;

import android.app.ProgressDialog;
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
import mobile.feedme.POCO.Utilisateur;

public class SingIn extends MenuActivity implements ILogger {

    public Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_sing_in, false, false);
        super.setTitle("Log in");
    }

    public void    signIn(View view)
    {
        EditText et1 = (EditText) (findViewById(R.id.editTextLogin));
        String username = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.EditTextPassword));
        String password = et2.getText().toString();

        Api.Authentificate(this, this, username, password);
     }

    public void    openRegister(View v)
    {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }


    @Override
    public void loginSuccessfull()
    {
        Api.getUserInfo(this, this);
    }

    @Override
    public void loginError(String msg)
    {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoUpdated(Utilisateur user)
    {
        Toast.makeText(getBaseContext(), "Welcome back " + user.Firstname, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getBaseContext(), MapsActivity.class));
    }

    @Override
    public void errorUserInfo()
    {
        //
    }
}













