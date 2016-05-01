package mobile.feedme.POCO;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mobile.feedme.MyLocationListener;

/**
 * Created by Quentin on 4/30/2016.
 */
public class Dish {
    public int DishId;
    public String Name;
    public double Price;
    public String Description;
    public Adress Adress;
    public int NbPart;
    public double SizePart;
    public Utilisateur Utilisateur;
    public String Statut;
    public Date DateExpiration;
    public Date PickUpTime;
    public Date DateCreate;

    public static Dish JSONParse(JSONObject jsonDish)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        Dish dish = new Dish();
        dish.DishId = jsonDish.optInt("DishId");
        dish.Name = jsonDish.optString("Name");
        dish.Price = jsonDish.optDouble("Price");
        dish.Description = jsonDish.optString("Description");
        dish.NbPart = jsonDish.optInt("NbPart");
        dish.SizePart = jsonDish.optDouble("SizePart");
        dish.Statut = jsonDish.optString("Statut");

        JSONObject jsonAdress = jsonDish.optJSONObject("Address");
        if (jsonAdress != null)
            dish.Adress = mobile.feedme.POCO.Adress.JSONParse(jsonAdress);
        else {
            //TODO faire une vraie gestion
            dish.Adress = new Adress();
            dish.Adress.Country = "France";
            dish.Adress.PostalCode = "75011";
            dish.Adress.Road = "18 rue de la Presentation";
        }



        try {
            dish.DateExpiration = format.parse(jsonDish.optString("DateExpiration"));
            dish.PickUpTime = format.parse(jsonDish.optString("PickUpTime"));
            dish.DateCreate = format.parse(jsonDish.optString("DateCreate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dish;
    }
}
