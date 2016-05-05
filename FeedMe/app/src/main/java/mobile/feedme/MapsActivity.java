package mobile.feedme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobile.feedme.POCO.Dish;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private HashMap<String, Dish> MarkerIdToDish = new HashMap<String, Dish>();

//    MyLocationListener locationListener;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            mMap.setMyLocationEnabled(true);
            Location location = MyLocationListener.getCurrentPosition(getApplicationContext());

            if (location != null) {
                LatLng myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
            }
        }

        mMap.setOnInfoWindowClickListener(this);
//        locationListener.setMap(mMap);

        Api.getAllDishAndCallDisplay(this);
    }

    public void addMeal(View view) {
        // Do something in response to button
        startActivity(new Intent(getApplicationContext(), AddMeal.class));
    }


    public void showFoodOnMap(ArrayList<Dish> dishes) {

        for (Dish dish : dishes) {
            LatLng mLatLngDish = MyLocationListener.getLocationFromAddress(getApplicationContext(),dish.Adress.Road + " " + dish.Adress.PostalCode + " " + dish.Adress.Country);
            if (mLatLngDish != null)
            {
                Marker marker = mMap.addMarker(new MarkerOptions().position(mLatLngDish).title(dish.Name).snippet(dish.Description));
                this.MarkerIdToDish.put(marker.getId(), dish);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        if (this.MarkerIdToDish.containsKey(marker.getId()))
        {
            Intent i = new Intent(this, DishDetailActivity.class);
            i.putExtra("Dish", this.MarkerIdToDish.get(marker.getId()));
            startActivity(i);
        }

//        marker.setTitle("clicked");
//        marker.showInfoWindow();
    }
}
