package mobile.feedme;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateo on 15/03/2016.
 */
public class Api {

    public void registerUser(List<String> data)
    {
        //Requete a la fonction de l'api qui ajoute un user apres ca registration
    }

    public boolean login(String login, String pwd)
    {
        Log.d("login : ", login);
        Log.d("pwd : ", pwd);
        //Requete a la fonction de l'api qui fait un select sur la table user
        if (login.equals(pwd) == true)
            return true;
        return false;
    }
    public List<Meal> getMeal()
    {
        List<Meal> myList = new ArrayList<Meal>();
        // Requete a la fonction qui fait un select sur la table meal
        // Recup le JSON et on le fou dans une list de meal comme pour les sdf dessou
        /*
         for(int i=0; i < jsonArray.length(); i++){
                Person person = new Person();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                person.setPersonneId(jsonObject.getString("PersonneId"));
                person.setLatitude(Double.parseDouble(jsonObject.getString("Latitude")));
                person.setLongitude(Double.parseDouble(jsonObject.getString("Longitude")));
                person.setTitre(jsonObject.getString("Titre"));
                person.setDescription(jsonObject.getString("Description"));
                listPerson.add(person);
         */
        return myList;
    }

    public boolean addMeal(List<String> data)
    {
        return true;
    }
}
