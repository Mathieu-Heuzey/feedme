package mobile.feedme;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Api api = new Api();
    public Meal meal = new Meal();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnInfoWindowClickListener(this);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng mLatLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(mLatLng).title("My Location").snippet("and snipet").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 13));

        showFoodOnMap();
    }

    public void addMeal(View view) {
        // Do something in response to button
        startActivity(new Intent(getApplicationContext(), AddMeal.class));
    }


    private void showFoodOnMap() {
        List<Meal> listMeal =  this.api.getMeal();

        for (Meal person : listMeal)
        {
            LatLng mLatLngMeal = new LatLng(meal.getLatitude(), meal.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(mLatLngMeal).title(meal.getTitre()).snippet(meal.getDescription()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLngMeal));
        }
    }
}
