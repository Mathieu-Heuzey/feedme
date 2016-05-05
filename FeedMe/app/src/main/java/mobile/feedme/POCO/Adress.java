package mobile.feedme.POCO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Quentin on 4/30/2016.
 */
public class Adress implements Parcelable {
    public int AddressId;
    public String Road;
    public String PostalCode;
    public String Country;
    public String Latitude;
    public String Longitude;

    public Adress() { }

    public static Adress JSONParse(JSONObject jsonDish)
    {
        Adress address = new Adress();

        address.AddressId = jsonDish.optInt("AddressId");
        address.Road = jsonDish.optString("Road");
        address.PostalCode = jsonDish.optString("PostalCode");
        address.Country = jsonDish.optString("Country");
        address.Latitude = jsonDish.optString("Latitude");
        address.Longitude = jsonDish.optString("Longitude");

        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.AddressId);
        parcel.writeString(this.Road);
        parcel.writeString(this.PostalCode);
        parcel.writeString(this.Country);
        parcel.writeString(this.Latitude);
        parcel.writeString(this.Longitude);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Adress> CREATOR = new Parcelable.Creator<Adress>() {
        public Adress createFromParcel(Parcel in) {
            return new Adress(in);
        }

        public Adress[] newArray(int size) {
            return new Adress[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Adress(Parcel in) {
        this.AddressId = in.readInt();
        this.Road = in.readString();
        this.PostalCode = in.readString();
        this.Country = in.readString();
        this.Latitude = in.readString();
        this.Longitude = in.readString();
    }
}
