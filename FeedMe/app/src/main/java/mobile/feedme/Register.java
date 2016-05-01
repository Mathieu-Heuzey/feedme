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

public class Register extends AppCompatActivity {

    public Api api = new Api();
    public Crypto crypto = new Crypto();
    public Utilisateur user = new Utilisateur();
    public Checker check = new Checker();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    public boolean setNewUser()
    {
        EditText ETpassword1 = (EditText) (findViewById(R.id.EditTextPassword1));
        String password1 = ETpassword1.getText().toString();
        EditText ETpassword2 = (EditText) (findViewById(R.id.EditTextPassword2));
        String password2 = ETpassword2.getText().toString();

        if (check.CheckMdp(password1, password2))
        {
            if (password1.isEmpty() || password2.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_LONG).show();
                return false;
            }
            user.Password = password1;
            EditText EditTextMail = (EditText) (findViewById(R.id.editTextMail));
            String mail = EditTextMail.getText().toString();
            if (check.CheckMail(mail))
            {
                user.Email = mail;
                EditText EditTextName = (EditText) (findViewById(R.id.editTextNom));
                user.Lastname = EditTextName.getText().toString();
                EditText EditTextPrenom2 = (EditText) (findViewById(R.id.editTextPrenom));
                user.Firstname = EditTextPrenom2.getText().toString();
                EditText EditTextLogin = (EditText) (findViewById(R.id.editTextLogin));
                user.Login = EditTextLogin.getText().toString();
                EditText EditTextNumber = (EditText) (findViewById(R.id.editTextTel));
                user.Phone = EditTextNumber.getText().toString();
                EditText EditTextCP = (EditText) (findViewById(R.id.editTextCP));
                user.Adress = new Adress();
                user.Adress.PostalCode = EditTextCP.getText().toString();
                EditText EditTextRoad = (EditText) (findViewById(R.id.editTextRue));
                user.Adress.Road = EditTextCP.getText().toString();
                user.Adress.Country = "France";
                Location location = MyLocationListener.getCurrentPosition(getBaseContext());
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();
                user.Adress.Longitude = longitude.toString();
                user.Adress.Latitude = latitude.toString();
                return true;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Email isn't valid !", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Passwords aren't the same !", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public RequestParams setParam()
    {
        RequestParams params = new RequestParams();
        String location = "{'AdressId' : 0, 'Road' : " + user.Adress.Road +
                ",'PostalCode' : " + user.Adress.PostalCode +
                ",'Country' : 'France', " +
                ",'Latitude' : "+user.Adress.Latitude +
                ", 'Longitude': " + user.Adress.Longitude + "}";
        params.put("UtilisateurId", 0);
        params.put("AdressId", 0);
        params.put("Firstname", user.Firstname);
        params.put("Lastname", user.Lastname);
        params.put("Adress", location);
        params.put("Phone", user.Phone);
        params.put("Username", user.Login);
        params.put("Password", crypto.md5(user.Password));
        params.put("Email", user.Email);
        return  params;
    }
    public void register(View view)
    {
        if (setNewUser())
        {
            Log.e("l objet en base", setParam().toString());
            AsyncHttpClient client = new AsyncHttpClient();
            // Pour les test
            RequestParams params2 = new RequestParams();
            params2.put("Email", "Mathieu@gmail.com");
            params2.put("Password", "t6Qoo@");
            params2.put("ConfirmPassword", "t6Qoo@");
            Api.registerRequest(this, params2);

            // LA version final
//            Api.registerRequest(this, setParam());

//        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
    }
}
