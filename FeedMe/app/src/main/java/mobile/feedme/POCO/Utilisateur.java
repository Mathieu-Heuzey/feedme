package mobile.feedme.POCO;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Quentin on 4/30/2016.
 */
public class Utilisateur {
    public String UtilisateurId;
    public String Firstname;
    public String Lastname;
    public Adress Adress;
    public String Phone;
    public String Username;
    public String Password;
    public String Email;
    public Date DateCreate;

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
}
