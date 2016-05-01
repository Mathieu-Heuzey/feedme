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

    private static String baseServerURL;
    private static String baseApiURL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static String Token = null;
    public static String TokenType = null;

   public static void Initialize(String serverURL, String apiURL)
   {
       baseServerURL = serverURL;
       baseApiURL = apiURL;
   }

    public static void Authentificate(final ILogger caller, String username, String password) {
        RequestParams params = new RequestParams();

        params.put("username", username);
        params.put("password", password);
        params.put("grant_type", "password");

        client.post(baseServerURL + "Token", params, new JsonHttpResponseHandler() {
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

                //set the header for the future request
                client.addHeader("Authorization", TokenType + " " + Token);
                //Continue to map
                caller.loginSuccessfull();
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
            }
        });
    }

//    public static void getUserInfo()

    public static void registerRequest(Register caller, RequestParams params)
    {
        client.post( baseApiURL + "Account/Register", params, new AsyncHttpResponseHandler() {
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
        client.get(baseApiURL + "Dishes",  new RequestParams(), new JsonHttpResponseHandler() {
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
