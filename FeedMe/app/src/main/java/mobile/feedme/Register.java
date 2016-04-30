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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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
        EditText et1 = (EditText) (findViewById(R.id.editTextNom));
        String nom = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.editTextPrenom));
        String prenom = et2.getText().toString();
        EditText et3 = (EditText) (findViewById(R.id.editTextLogin));
        String login = et3.getText().toString();
        EditText et4 = (EditText) (findViewById(R.id.EditTextPassword1));
        String password = et4.getText().toString();
//        EditText et5 = (EditText) (findViewById(R.id.editTextLcoation));
 //       String location = et5.getText().toString();
        EditText et6 = (EditText) (findViewById(R.id.editTextTel));
        String tel = et6.getText().toString();


        List<String> data = new ArrayList<String>();

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

        Log.e("ADebugTag", "Value: " + 1);
//        Toast.makeText(getApplicationContext(),"Jusqua la tout va bien", Toast.LENGTH_SHORT).show();
        client.post("http://163.5.84.232/WebService/api/Utilisateurs", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                //on ouvre la map
//                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                Log.e("ADebugTag", "Value: " + 2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e("ADebugTag", "Value: " + 3);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // Toast.makeText(getApplicationContext(),
//                        "Password / Login doesn't match", Toast.LENGTH_SHORT).show();
//                Log.d("ShowPerson", "ERROR");
            }
        });
        Log.e("ADebugTag", "Value: " + 4);


//        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
}
