package mobile.feedme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.telerik.android.common.Util;

import mobile.feedme.POCO.Dish;
import mobile.feedme.POCO.Utilisateur;

public class DishListActivity extends MenuActivity {
    private Utilisateur user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Rajouter le truc ou genre on met pas dans le menu
        super.initialize(R.layout.activity_dish_list, false, true);

        // pour envoyer utilisateur de l'autre coté :
//        Intent i = new Intent(this, DishListActivity.class);
//        i.putExtra("User",  theUser);
//        startActivity(i);
        Intent i = getIntent();
        this.user = i.<Utilisateur>getParcelableExtra("User");
        if (this.user.UtilisateurId.equals(Api.loggedUser.UtilisateurId)) {
            super.setTitle("My dish list");
        }
        else {
            super.setTitle(String.format("Dish list of %s", this.user.Firstname));
        }


    }
}
