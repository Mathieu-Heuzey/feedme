package mobile.feedme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Api.Initialize(getResources().getString(R.string.serverBaseUrl), getResources().getString(R.string.apiUrl));

        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loader)
                .showImageOnFail(R.drawable.pizza)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("text", MODE_PRIVATE);

        if (pref.contains("token"))
        {
            Api.setToken(pref.getString("token", ""));
            if (pref.contains("user_id"))
                Api.loggedUser.UtilisateurId = pref.getString("user_id", "");
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        else
            startActivity(new Intent(getApplicationContext(), SingIn.class));
        this.finish();
    }
}
