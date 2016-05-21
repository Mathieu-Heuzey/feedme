package mobile.feedme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

/**
 * Created by Quentin on 5/1/2016.
 */
public class FeedMe extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
