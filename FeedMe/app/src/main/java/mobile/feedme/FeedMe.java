package mobile.feedme;

import android.app.Application;

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
