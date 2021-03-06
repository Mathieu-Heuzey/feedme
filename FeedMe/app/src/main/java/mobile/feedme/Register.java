package mobile.feedme;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import mobile.feedme.POCO.Adress;
import mobile.feedme.POCO.Utilisateur;

public class Register extends MenuActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_register, false, false);
        super.setTitle("Register");
        AppCompatButton button = (AppCompatButton)findViewById(R.id.Bconfirmez);
        button.setOnClickListener(this);
        EditText prenom = (EditText) (findViewById(R.id.editTextPrenom));
        prenom.setError("Your last name is required");

        EditText nom = (EditText) (findViewById(R.id.editTextNom));
        nom.setError("Your first name is required");

        EditText pwd1 = (EditText) (findViewById(R.id.EditTextPassword1));
        pwd1.setError("Your password is required");

        EditText pwd2 = (EditText) (findViewById(R.id.EditTextPassword2));
        pwd2.setError("Your password confirmation is required");

        EditText tel = (EditText) (findViewById(R.id.editTextTel));
        tel.setError("Your phone is required");

        EditText mail = (EditText) (findViewById(R.id.editTextMail));
        mail.setError("Your mail is required");
    }

    public void register(View view)
    {
        RequestParams params = retrieveParams();
        if (params != null)
        {
            Api.registerRequest(this, params);
        }
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
        if (firstName.trim().isEmpty())
        {
            Toast.makeText(this, "You did not enter your first name", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (lastName.trim().isEmpty())
        {
            Toast.makeText(this, "You did not enter your last name", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "You did not enter your password", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "You did not enter your password confirmation", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!Checker.CheckMdp(password, confirmPassword)) {
            Toast.makeText(this, "Your password and confirmationa ren't equal", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (phone.trim().isEmpty())
        {
            Toast.makeText(this, "You did not enter your phone", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (phone.trim().length() < 9)
        {
            Toast.makeText(this, "Your phone is too short", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (mail.trim().isEmpty()) {
            Toast.makeText(this, "You did not enter your mail", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!Checker.CheckMail(mail)) {
            Toast.makeText(this, "Your email is not valid", Toast.LENGTH_SHORT).show();
            return null;
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!%^&+=}{¨£ù*µ§:/;.,?*-><])(?=\\S+$).{6,}$";
        if(!password.matches(pattern))
        {
            Toast.makeText(this, "Your password doesn't reach the condition, you need at least one digit, one lower case, one upper case, a special char and at least 6 places though", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Bconfirmez)
        {
            this.register(view);
        }
    }
}

