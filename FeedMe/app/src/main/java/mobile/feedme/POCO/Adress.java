package mobile.feedme.POCO;

import org.json.JSONObject;

/**
 * Created by Quentin on 4/30/2016.
 */
public class Adress {
    public int AddressId;
    public String Road;
    public String PostalCode;
    public String Country;
    public String Latitude;
    public String Longitude;


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

}
