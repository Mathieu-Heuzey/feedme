package mobile.feedme;

import android.preference.PreferenceActivity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import mobile.feedme.POCO.Dish;

/**
 * Created by Mateo on 15/03/2016.
 */
public class Api {

    public static String Token;
    public static String TokenType;

    public static boolean login(String login, String pwd)
    {
        Log.d("login : ", login);
        Log.d("pwd : ", pwd);
        //Requete a la fonction de l'api qui fait un select sur la table user
        if (login.equals(pwd) == true)
            return true;
        return false;
    }

    public static void Authentificate(final SingIn caller, String username, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", username);
        params.put("password", password);
        params.put("grant_type", "password");

        client.post(caller.getResources().getString(R.string.serverBaseUrl) + "Token", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("Login success : ", response.toString());
                try {
                    Token = response.getString("access_token");
                    TokenType = response.getString("token_type");
                } catch (JSONException e) {
                    e.printStackTrace();
                    //display error, but should never occur
                    return;
                }

                //Continue to map
                caller.loginSuccess();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                if (statusCode == 0)
                    caller.loginError("Network is unreacheable");
                else {
                    try {
                        caller.loginError(response.getString("error_description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        caller.loginError("Error while trying to log in");
                    }
                    Log.e("Login error : ", response.toString());
                }

                //Message a preciser en fonction de la reponse
            }
        });
    }

    public static void registerRequest(Register caller, RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post( caller.getResources().getString(R.string.apiUrl) + "Account/Register", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //on ouvre la map
//                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                Log.e("ADebugTag", Integer.toString(statusCode));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable t) {
                if (res != null)
                    Log.e("ADebugTag", res.toString());
            }

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String response) {
//            }

//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
//            }
        });
    }


    public static void getAllDishAndCallDisplay(final MapsActivity caller)
    {
        AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.get(caller.getResources().getString(R.string.apiUrl) + "Dishes",  new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Dish> dishes = new ArrayList<Dish>();
                for (int i = 0; i < response.length(); ++i)
                {
                    try {
                        JSONObject jsonDish = response.getJSONObject(i);
                        Dish dish = Dish.JSONParse(jsonDish);
                        if (dish != null)
                            dishes.add(dish);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        break;
                    }

                }

                caller.showFoodOnMap(dishes);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Log.d("ShowPerson", "ERROR");
                Log.e("Retour dish error : ", res.toString());
                Toast.makeText(caller.getApplicationContext(), "Could not connect to the network !", Toast.LENGTH_LONG).show();
            }
        });

    }
}
