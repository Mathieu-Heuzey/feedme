package mobile.feedme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Quentin on 5/1/2016.
 */
public class FeedMe extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Api.Initialize(getResources().getString(R.string.serverBaseUrl), getResources().getString(R.string.apiUrl));
    }
}
