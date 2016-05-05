package mobile.feedme.POCO;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Dish implements Parcelable {
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

    public Dish()
    {
        DateExpiration = new Date();
        DateCreate = new Date();
        PickUpTime = new Date();
        Adress = new Adress();
        Utilisateur = new Utilisateur();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.DishId);
        parcel.writeString(this.Name);
        parcel.writeDouble(this.Price);
        parcel.writeString(this.Description);
        parcel.writeParcelable(this.Adress, flags);
        parcel.writeInt(this.NbPart);
        parcel.writeDouble(this.SizePart);
        parcel.writeParcelable(this.Utilisateur, flags);
        parcel.writeString(this.Statut);
        parcel.writeString(this.DateExpiration.toString());
        parcel.writeString(this.PickUpTime.toString());
        parcel.writeString(this.DateCreate.toString());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>() {
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Dish(Parcel in) {
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        this.DishId = in.readInt();
        this.Name = in.readString();
        this.Price = in.readDouble();
        this.Description = in.readString();
        this.Adress = in.<Adress>readParcelable(Adress.class.getClassLoader());
        this.NbPart = in.readInt();
        this.SizePart = in.readDouble();
        this.Utilisateur = in.<Utilisateur>readParcelable(Utilisateur.class.getClassLoader());
        this.Statut = in.readString();
        try {
            this.DateExpiration = format.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            //Euh c'est la merde je sais pas on fait quoi la
            this.DateExpiration = new Date();
        }
        try {
            this.PickUpTime = format.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            //Euh c'est la merde je sais pas on fait quoi la
            this.PickUpTime = new Date();
        }
        try {
            this.DateCreate = format.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
            //Euh c'est la merde je sais pas on fait quoi la
            this.DateCreate = new Date();
        }
    }
}
