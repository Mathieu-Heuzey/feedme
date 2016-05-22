package mobile.feedme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mobile.feedme.POCO.Dish;
import mobile.feedme.POCO.Order;
import mobile.feedme.POCO.Utilisateur;

/**
 * Created by Mateo on 15/03/2016.
 */
public class Api {

    private static String baseServerURL;
    private static String baseApiURL;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static String Token = null;
    private static String TokenType = null;

    private static ProgressDialog progressDialog;

    public static Utilisateur loggedUser = null;

   public static void Initialize(String serverURL, String apiURL)
   {
       baseServerURL = serverURL;
       baseApiURL = apiURL;
   }

    public static void setToken(String token)
    {
        Token = token;
        TokenType = "bearer";
        client.addHeader("Authorization", TokenType + " " + Token);
    }

    //must be called after an authentification error (code 401)
    public static void removeToken(Context context)
    {
        Token = null;
        client.removeHeader("Authorization");
        SharedPreferences pref = context.getSharedPreferences("text", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.remove("token");
        editor.apply();
    }

    public static void saveCurrentToken(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("text", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", Token);
        editor.apply();
    }

    private static void ShowProgressDialog(Context context, String title, String desc, Boolean isCancelable)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(desc);
        progressDialog.setCancelable(isCancelable);
        progressDialog.show();
    }

    private static void HideProgressDialog()
    {
        progressDialog.hide();
    }

    public static void Authentificate(final ILogger caller, final Activity context, String username, String password) {
        RequestParams params = new RequestParams();

        params.put("username", username);
        params.put("password", password);
        params.put("grant_type", "password");

        client.post(baseServerURL + "Token", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Api.ShowProgressDialog(context, "Logging in...", "Please wait while we log you in...", false);
            }

            @Override
            public void onFinish() {
                Api.HideProgressDialog();
            }

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
                //save the token in the sharedpreferences
                Api.saveCurrentToken(context);
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

    public static void logOut(final Activity caller)
    {
        //Osef de la reponse, dans tout les cas on supprime le token, au pire si la requete n'as pas marché, l'utilisateur recupereras le meme token a la prochaine authentification
        client.post(baseApiURL + "/Account/Logout", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable t) {
            }
        });
        Api.removeToken(caller.getApplicationContext());
        Toast.makeText(caller.getApplicationContext(), "You have been logged out !", Toast.LENGTH_LONG).show();
        caller.startActivity(new Intent(caller.getApplicationContext(), SingIn.class));

    }

    public static void getUserInfo(final ILogger caller, final Activity context)
    {
        //if Token == null -> erreur

        client.get(baseApiURL + "/Account/UserInfo", new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Api.ShowProgressDialog(context, "Logging in...", "Logged in ! Retrieving your information...", false);
            }

            @Override
            public void onFinish() {
                Api.HideProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loggedUser = Utilisateur.JSONParse(response);
                caller.userInfoUpdated(loggedUser);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                if (statusCode == 0)
                    caller.loginError("Network is unreacheable");
                else {
                    caller.loginError("Invalid credentials");
                }
            }
        });
    }

    public static void registerRequest(final Register caller, RequestParams params)
    {
        client.post(baseApiURL + "Account/Register", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Api.ShowProgressDialog(caller, "Registering...", "Please wait we register you in...", false);
            }

            @Override
            public void onFinish() {
                Api.HideProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Toast.makeText(caller.getApplicationContext(), "You have been registered ! You can log in now", Toast.LENGTH_LONG).show();
                caller.startActivity(new Intent(caller.getApplicationContext(), SingIn.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable t) {
                if (statusCode == 0) {
                    Toast.makeText(caller.getApplicationContext(), "Network is unreachable", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(caller.getApplicationContext(), "Error while registering ! Pleasy retry", Toast.LENGTH_LONG).show();
                    if (res != null) {
                        try {
                            Log.e("Register fail : ", new String(res, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void addMealRequest(final AddMeal caller, RequestParams params)
    {
        client.post(baseApiURL + "Dishes", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Api.ShowProgressDialog(caller, "Adding Dishes...", "Please wait while we adding your dish...", false);
            }

            @Override
            public void onFinish() {
                Api.HideProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Toast.makeText(caller.getApplicationContext(), "Your Dish have been added", Toast.LENGTH_LONG).show();
                caller.startActivity(new Intent(caller.getApplicationContext(), MapsActivity.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable t) {
                if (statusCode == 0) {
                    Toast.makeText(caller.getApplicationContext(), "Network is unreachable", Toast.LENGTH_LONG).show();
                } else if (statusCode == 401) {
                    Api.removeToken(caller.getApplicationContext());
                    Toast.makeText(caller.getApplicationContext(), "You must log you in !", Toast.LENGTH_LONG).show();
                    caller.startActivity(new Intent(caller.getApplicationContext(), SingIn.class));
                } else {
                    Toast.makeText(caller.getApplicationContext(), "Server error ! Please retry later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void getAllDishAndCallDisplay(final MapsActivity caller)
    {
        client.get(baseApiURL + "Dishes?page=map",  new RequestParams(), new JsonHttpResponseHandler() {
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
                if (statusCode == 0)
                {
                    Toast.makeText(caller.getApplicationContext(), "Network is unreachable", Toast.LENGTH_LONG).show();
                }
                else if (statusCode == 401) {
                    Api.removeToken(caller.getApplicationContext());
                    Toast.makeText(caller.getApplicationContext(), "You must log you in !", Toast.LENGTH_LONG).show();
                    caller.startActivity(new Intent(caller.getApplicationContext(), SingIn.class));
                }
                else
                {
                    Toast.makeText(caller.getApplicationContext(), "Server error ! Please retry later", Toast.LENGTH_LONG).show();
                }
                caller.refreshingDone();
            }
        });
    }

    public static void getOrderHistoric(final OrderActivity caller)
    {
        client.get(baseApiURL + "Historic",  new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(byte[] responseBody) throws JSONException {
                String jsonString = getResponseString(responseBody, getCharset());

                jsonString = jsonString.replace("\\", "").replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
                jsonString = jsonString.substring(1, jsonString.length() - 1);

                return new JSONObject(jsonString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                LinkedHashMap<Integer, List<Order>> sell;
                LinkedHashMap<Integer, List<Order>> buy;

                JSONObject sellJson = response.optJSONObject("Sell");
                JSONObject buyJson = response.optJSONObject("Buy");

                //Build map of order key = status, value = list of Order
                sell = caller.buildOrderMapFromJSON(OrderActivity.SELLSTATUS, sellJson);
                buy = caller.buildOrderMapFromJSON(OrderActivity.BUYSTATUS, buyJson);

                caller.buildAndDisplayOrders(sell, buy);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                if (statusCode == 0)
                {
                    Toast.makeText(caller.getApplicationContext(), "Network is unreachable", Toast.LENGTH_LONG).show();
                }
                else if (statusCode == 401) {
                    Api.removeToken(caller.getApplicationContext());
                    Toast.makeText(caller.getApplicationContext(), "You must log you in !", Toast.LENGTH_LONG).show();
                    caller.startActivity(new Intent(caller.getApplicationContext(), SingIn.class));
                }
                else
                {
                    Toast.makeText(caller.getApplicationContext(), "Server error ! Please retry later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
