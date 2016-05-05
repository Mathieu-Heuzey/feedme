package mobile.feedme.POCO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Quentin on 4/30/2016.
 */
public class Utilisateur implements Parcelable {
    public String UtilisateurId;
    public String Firstname;
    public String Lastname;
    public Adress Adress;
    public String Phone;
    public String Password;
    public String Email;

    public Utilisateur()
    {
        Adress = new Adress();
    }

    public static Utilisateur JSONParse(JSONObject jsonUser)
    {
        Utilisateur user = new Utilisateur();

        user.UtilisateurId = jsonUser.optString("UtilisateurId");
        user.Firstname = jsonUser.optString("Firstname");
        user.Lastname = jsonUser.optString("Lastname");
        user.Phone = jsonUser.optString("Phone");
        user.Password = jsonUser.optString("Password");
        user.Email = jsonUser.optString("Email");

        JSONObject jsonAdress = jsonUser.optJSONObject("Address");
        if (jsonAdress != null)
            user.Adress = mobile.feedme.POCO.Adress.JSONParse(jsonAdress);
        else {
            //TODO faire une vraie gestion
            user.Adress = new Adress();
            user.Adress.Country = "France";
            user.Adress.PostalCode = "75011";
            user.Adress.Road = "18 rue de la Presentation";
        }
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.UtilisateurId);
        parcel.writeString(this.Firstname);
        parcel.writeString(this.Lastname);
        parcel.writeString(this.Phone);
        parcel.writeString(this.Password);
        parcel.writeString(this.Email);
        parcel.writeParcelable(this.Adress, flags);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Utilisateur> CREATOR = new Parcelable.Creator<Utilisateur>() {
        public Utilisateur createFromParcel(Parcel in) {
            return new Utilisateur(in);
        }

        public Utilisateur[] newArray(int size) {
            return new Utilisateur[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Utilisateur(Parcel in) {
        this.UtilisateurId = in.readString();
        this.Firstname = in.readString();
        this.Lastname = in.readString();
        this.Phone = in.readString();
        this.Password = in.readString();
        this.Email = in.readString();
        this.Adress = in.<Adress>readParcelable(Adress.class.getClassLoader());
    }
}
