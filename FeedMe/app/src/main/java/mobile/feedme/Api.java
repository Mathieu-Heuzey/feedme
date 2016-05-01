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

    public static boolean login(String login, String pwd)
    {
        Log.d("login : ", login);
        Log.d("pwd : ", pwd);
        //Requete a la fonction de l'api qui fait un select sur la table user
        if (login.equals(pwd) == true)
            return true;
        return false;
    }

    public static void registerRequest(Register caller, RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post( R.string.apiUrl + "Account/Register", params, new AsyncHttpResponseHandler() {
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


    public static void getAllDishAndCallDisplay(final MapsActivity mapView)
    {
        AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.get(R.string.apiUrl + "Dishes",  new RequestParams(), new JsonHttpResponseHandler() {
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

                mapView.showFoodOnMap(dishes);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Log.d("ShowPerson", "ERROR");
                Log.e("Retour dish error : ", res.toString());
                Toast.makeText(mapView.getApplicationContext(), "Could not connect to the network !", Toast.LENGTH_LONG).show();
            }
        });

    }
}
