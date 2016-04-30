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
        try {
            dish.DishId = jsonDish.getInt("DishId");
            dish.Name = jsonDish.getString("Name");
            dish.Price = jsonDish.getDouble("Price");
            dish.Description = jsonDish.getString("Description");
            dish.NbPart = jsonDish.getInt("NbPart");
            dish.SizePart = jsonDish.getDouble("SizePart");
            dish.Statut = jsonDish.getString("Statut");
            dish.DateExpiration = format.parse(jsonDish.getString("DateExpiration"));
            dish.PickUpTime = format.parse(jsonDish.getString("PickUpTime"));
            dish.DateCreate = format.parse(jsonDish.getString("DateCreate"));

            dish.Adress = new Adress();
            dish.Adress.Country = "France";
            dish.Adress.PostalCode = "75011";
            dish.Adress.Road = "13 rue de la Presentation";

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return dish;
    }
}
