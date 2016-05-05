package mobile.feedme;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import mobile.feedme.POCO.Adress;
import mobile.feedme.POCO.Utilisateur;

public class Register extends AppCompatActivity implements ILogger {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void register(View view)
    {
        RequestParams params = retrieveParams();
        if (params != null)
        {
            Api.registerRequest(this, params);
        }
    }

    @Override
    public void loginSuccessfull() {
//        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }

    @Override
    public void loginError(String msg) {
  //      Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoUpdated(Utilisateur user) {

    }

    private RequestParams retrieveParams()
    {
        RequestParams params = new RequestParams();

        //Required fields
        String password = ((EditText)findViewById(R.id.EditTextPassword1)).getText().toString();
        String confirmPassword = ((EditText)findViewById(R.id.EditTextPassword2)).getText().toString();
        String mail =  ((EditText)findViewById(R.id.editTextMail)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.editTextNom)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.editTextPrenom)).getText().toString();
        String phone = ((EditText)findViewById(R.id.editTextTel)).getText().toString();

        //Ok c'est mega moche, mais en vrai pas trop le choix
        if (!Checker.CheckMdp(password, confirmPassword) || password.isEmpty() || confirmPassword.isEmpty()) {
            //TODO le ptit message d'erreur
            return null;
        }
        if (!Checker.CheckMail(mail)) {
            //TODO le ptit message d'erreur
            return null;
        }
        if (lastName.trim().isEmpty())
        {
            //TODO le ptit message d'erreur
            return null;
        }
        if (firstName.trim().isEmpty())
        {
            //TODO le ptit message d'erreur
            return null;
        }
        if (phone.trim().isEmpty())
        {
            //TODO le ptit message d'erreur
            return null;
        }

        params.put("Password", password);
        params.put("ConfirmPassword", confirmPassword);
        params.put("Email", mail);
        params.put("Firstname", firstName);
        params.put("Lastname", lastName);
        params.put("PhoneNumber", phone);
        params.put("Address", "");

        //Not used anymore //TODO remove the input
        //EditText EditTextLogin = (EditText) (findViewById(R.id.editTextLogin));
        //user.Login = EditTextLogin.getText().toString();

        //TODO envoyer l'adresse frero, en checkant plein de truc tmtc
//                user.Adress.PostalCode = EditTextCP.getText().toString();
//                EditText EditTextRoad = (EditText) (findViewById(R.id.editTextRue));
//                user.Adress.Road = EditTextCP.getText().toString();
//                user.Adress.Country = "France";
//                Location location = MyLocationListener.getCurrentPosition(getBaseContext());
//                Double longitude = location.getLongitude();
//                Double latitude = location.getLatitude();
//                user.Adress.Longitude = longitude.toString();
//                user.Adress.Latitude = latitude.toString();

        return params;
    }
}
