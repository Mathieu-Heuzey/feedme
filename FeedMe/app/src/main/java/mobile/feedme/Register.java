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

public class Register extends AppCompatActivity {

    public Api api = new Api();
    public Crypto crypto = new Crypto();
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

    public void register(View view)
    {
        EditText EditTextName = (EditText) (findViewById(R.id.editTextNom));
        String nom = EditTextName.getText().toString();
        EditText EditTextPrenom2 = (EditText) (findViewById(R.id.editTextPrenom));
        String prenom = EditTextPrenom2.getText().toString();
        EditText et3 = (EditText) (findViewById(R.id.editTextLogin));
        String login = et3.getText().toString();
        EditText et4 = (EditText) (findViewById(R.id.EditTextPassword1));
        String password = et4.getText().toString();
//        EditText et5 = (EditText) (findViewById(R.id.editTextLcoation));
 //       String location = et5.getText().toString();
        EditText et6 = (EditText) (findViewById(R.id.editTextTel));
        String tel = et6.getText().toString();
        EditText et7 = (EditText) (findViewById(R.id.EditTextPassword2));
        String password2 = et4.getText().toString();

        EditText EditTextMail = (EditText) (findViewById(R.id.editTextMail));
        String mail = EditTextMail.getText().toString();

// First name , last name , mail, pawssord, phone

        Location location3 = MyLocationListener.getCurrentPosition(getBaseContext());
        Double longitude = location3.getLongitude();
        Double latitude = location3.getLatitude();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String location2 = "{'AdressId' : 0, 'Road' : 'dtc', 'PostalCode' : '13013', 'Country' : 'France', 'Latitude' : 'latitude', 'Longitude': '2,2'}";
        params.put("UtilisateurId", 0);
        params.put("AdressId", 0);
        params.put("Firstname", prenom);
        params.put("Lastname", nom);
        params.put("Adress", location2);
        params.put("Phone", tel);
        params.put("Username", login);
        params.put("Password", crypto.md5(password));
        params.put("Email", "mathieu.heuzey@gmail.com");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        params.put("DateCreate", dateFormat);

        RequestParams params2 = new RequestParams();

        params2.put("Email", mail);
        params2.put("Password", password);
        params2.put("ConfirmPassword", password);


        Api.registerRequest(this, params2);
//        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
}
