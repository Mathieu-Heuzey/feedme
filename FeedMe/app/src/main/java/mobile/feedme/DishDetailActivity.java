package mobile.feedme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import mobile.feedme.POCO.Dish;

public class DishDetailActivity extends MenuActivity {

    Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_dish_detail, false, true);
        super.setTitle("Dish");

        Intent i = getIntent();
        Dish dish = i.<Dish>getParcelableExtra("Dish");
        this.dish = dish;
        Log.e("DishDetail: ", dish.toString());

        TextView name = (TextView) findViewById(R.id.textViewDishName);
        name.setText(dish.Name);

        TextView cookerName = (TextView) findViewById(R.id.textViewCookerName);
        cookerName.setText(dish.Utilisateur.Firstname + dish.Utilisateur.Lastname);

        TextView description = (TextView) findViewById(R.id.textViewDescription);
        description.setText(dish.Description);

        TextView street = (TextView) findViewById(R.id.textViewRue);
        street.setText(dish.Adress.Road);

        TextView partRestante = (TextView) findViewById(R.id.textViewPartRestante);
        partRestante.setText("Number Left : " + Integer.toString(dish.NbPart));

        TextView cp = (TextView) findViewById(R.id.textViewCP);
        cp.setText(dish.Adress.PostalCode);

        TextView country = (TextView) findViewById(R.id.textViewVille);
        country.setText(dish.Adress.Country);

        TextView partNumber = (TextView) findViewById(R.id.PortionNumber);
        partNumber.setText("1");
//        if (dish.SizePart != null) {
//            TextView poid = (TextView) findViewById(R.id.textViewPoid);
//            String convert = String.valueOf(dish.SizePart);
//            poid.setText(convert);
//        }

        TextView price = (TextView) findViewById(R.id.textViewPrice);
        String convert3 = String.valueOf(dish.Price);
        price.setText("Price : " + convert3 + " €");

        TextView pickUpDate = (TextView) findViewById(R.id.textViewPickUpDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString = sdf.format(dish.PickUpStartTime);
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString2 = sdf2.format(dish.PickUpEndTime);
        pickUpDate.setText("You can pick your dish bewtwwen : " + dateString2 + " and " + dateString);

        TextView expirationDate = (TextView) findViewById(R.id.textViewExpirationDate);
        String expiration = String.valueOf(dish.DateExpiration);
        expirationDate.setText("This dish will expire : " + expiration);

        TextView poid = (TextView) findViewById(R.id.textViewHeight);
        String convert2 = String.valueOf(dish.NbPart);
        poid.setText("Weight : " + convert2  + " grammes");
    }

    public void plusButton(View vies)
    {
        //        On augmente le nombre de part souhaité
        // On diminue le nombre de part dispo
        // on multiplie le poid par la quantité souhaité
        // on multiplie le prix par la quantité souhaité

        TextView TextViewPartNumber = (TextView) findViewById(R.id.PortionNumber);
        String StringPartNumber = TextViewPartNumber.getText().toString();
        int IntPartNumber = Integer.parseInt(StringPartNumber);

        IntPartNumber += 1;

        double   poid = dish.SizePart;
        double   price = dish.Price;
        int      left = dish.NbPart;


        poid *= IntPartNumber;
        price *= IntPartNumber;
        left -= IntPartNumber;
        if (left < 0)
            return;

        TextViewPartNumber.setText(String.valueOf(IntPartNumber));
        TextView TextViewPoid = (TextView) findViewById(R.id.textViewHeight);
        TextView TextViewPartRestante = (TextView) findViewById(R.id.textViewPartRestante);
        TextView TextViewPrice = (TextView) findViewById(R.id.textViewPrice);

        TextViewPoid.setText("Weight : " + String.valueOf(poid) + " grammes");
        TextViewPrice.setText("Price : " + String.valueOf(price) + " €" );

        TextViewPartRestante.setText("Number left :     " +String.valueOf(left));
    }

    public void minusButton(View vies)
    {
        TextView TextViewPartNumber = (TextView) findViewById(R.id.PortionNumber);
        String StringPartNumber = TextViewPartNumber.getText().toString();
        int IntPartNumber = Integer.parseInt(StringPartNumber);
        IntPartNumber -= 1;
        double   poid = dish.SizePart;
        double   price = dish.Price;
        int      left = dish.NbPart;
        poid *= IntPartNumber;
        price *= IntPartNumber;
        left -= IntPartNumber;
        if (IntPartNumber < 0)
            return;
        TextViewPartNumber.setText(String.valueOf(IntPartNumber));
        TextView TextViewPoid = (TextView) findViewById(R.id.textViewHeight);
        TextView TextViewPartRestante = (TextView) findViewById(R.id.textViewPartRestante);
        TextView TextViewPrice = (TextView) findViewById(R.id.textViewPrice);
        TextViewPoid.setText("Weight :     " + String.valueOf(poid) + " grammes");
        TextViewPrice.setText("Price :    " + String.valueOf(price) + " €" );
        TextViewPartRestante.setText("Number left :" + String.valueOf(left));

    }

    public void submitForm(View vies)
    {
        return;
    }
}
