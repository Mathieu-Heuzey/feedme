package mobile.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void register(View view)
    {
        EditText et1 = (EditText) (findViewById(R.id.editTextNom));
        String nom = et1.getText().toString();
        EditText et2 = (EditText) (findViewById(R.id.editTextPrenom));
        String prenom = et2.getText().toString();
        EditText et3 = (EditText) (findViewById(R.id.editTextLogin));
        String login = et3.getText().toString();
        EditText et4 = (EditText) (findViewById(R.id.EditTextPassword1));
        String password = et4.getText().toString();
        EditText et5 = (EditText) (findViewById(R.id.editTextLcoation));
        String location = et5.getText().toString();
        EditText et6 = (EditText) (findViewById(R.id.editTextTel));
        String tel = et6.getText().toString();

        // requete a l'api pour ajouter le nouvel user
        //et on lance sur la map
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }
}
