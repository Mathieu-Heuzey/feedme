package mobile.feedme;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddMeal extends MenuActivity {

    public Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_add_meal, true, true);
    }

    public void saveDescription(View view) {
        EditText titre = (EditText) (findViewById(R.id.editTextTitre));
        String titreStr = titre.getText().toString();

        EditText desc = (EditText) (findViewById(R.id.TFdesc));
        String descStr = desc.getText().toString();

        EditText prix = (EditText) (findViewById(R.id.EditTextPrix));
        String prixStr = prix.getText().toString();

        EditText poid = (EditText) (findViewById(R.id.EditTextPoid));
        String poidStr = poid.getText().toString();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Log.d("ADebugTag", "Value: " + titre);
        Log.d("ADebugTag", "Value: " + desc);
        Log.d("ADebugTag", "Value: " + prix);
        Log.d("ADebugTag", "Value: " + poid);
        Log.d("ADebugTag", "Value: " + longitude);
        Log.d("ADebugTag", "Value: " + latitude);

        List<String> data = new ArrayList<String>();
//        this.api.addMeal(data);
        Handler handler = new Handler();
        int millisDelay = 2000;
        handler.postDelayed(task, millisDelay);
    }

    private Runnable task = new Runnable() {
        public void run() {
            // Execute your delayed code
            finish();
        }
    };


}
