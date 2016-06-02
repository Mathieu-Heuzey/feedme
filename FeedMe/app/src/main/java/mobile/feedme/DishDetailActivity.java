package mobile.feedme;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import mobile.feedme.POCO.Dish;

public class DishDetailActivity extends MenuActivity {

    Dish dish;
    public Button btnClick;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 1111;
    Calendar myCalendar = Calendar.getInstance();

    public void submitDate(View vies)
    {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        return;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM-dd"; //In which you need put here
        Date  tmp = myCalendar.getTime();
        String fDate = new SimpleDateFormat("MM-dd").format(tmp);
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, tmp);
        TextView output2 = (TextView) findViewById(R.id.output2);
        output2.setText("You will pick your dish : " + fDate);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_dish_detail, false, true);
        super.setTitle("Dish");
        Intent i = getIntent();
        this.dish = i.<Dish>getParcelableExtra("Dish");
        Log.e("DishDetail: ", dish.toString());

        ImageView image = (ImageView)findViewById(R.id.imageDish);
        ImageLoader.getInstance().displayImage(dish.MainImage, image);

        TextView name = (TextView) findViewById(R.id.textViewDishName);
        name.setText(dish.Name);

        TextView cookerName = (TextView) findViewById(R.id.textViewCookerName);
        cookerName.setText(dish.Utilisateur.Firstname +" "+ dish.Utilisateur.Lastname);

        TextView description = (TextView) findViewById(R.id.textViewDescription);
        description.setText(dish.Description);

        TextView street = (TextView) findViewById(R.id.textViewRue);
        street.setText(dish.Adress.Road);

        TextView partRestante = (TextView) findViewById(R.id.textViewPartRestante);
        partRestante.setText("Number Left :     " + Integer.toString(dish.NbPart));

        TextView cp = (TextView) findViewById(R.id.textViewCP);
        cp.setText(dish.Adress.PostalCode);

        TextView country = (TextView) findViewById(R.id.textViewVille);
        country.setText(dish.Adress.Country);

//        if (dish.SizePart != null) {
//            TextView poid = (TextView) findViewById(R.id.textViewPoid);
//            String convert = String.valueOf(dish.SizePart);
//            poid.setText(convert);
//        }

        TextView price = (TextView) findViewById(R.id.textViewPrice);
        String convert3 = String.valueOf(dish.Price);
        price.setText("Price :         " + convert3 + " €");


        TextView poid = (TextView) findViewById(R.id.textViewHeight);
        poid.setText("Weight :     " + dish.SizePart + " grammes");

        /********* display current time on screen End ********/

        this.buildDynamicLayout();

    }

    @Override
    protected TimePickerDialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            TextView timeText = (TextView) findViewById(R.id.output);
            timeText.setText(updateTime(hour, minute));
        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).toString();
        return "You will pick up your dish around : " + aTime;
    }

    public void plusButton(View vies)
    {
        // On augmente le nombre de part souhaité
        // On diminue le nombre de part dispo
        // on multiplie le poid par la quantité souhaité
        // on multiplie le prix par la quantité souhaité

        TextView TextViewPartNumber = (TextView) findViewById(R.id.PortionNumber);
        String StringPartNumber = TextViewPartNumber.getText().toString();
        int IntPartNumber = Integer.parseInt(StringPartNumber);

        IntPartNumber += 1;

        double   price = dish.Price;
        int      left = dish.NbPart;


        price *= IntPartNumber;
        left -= IntPartNumber;
        if (left < 0)
            return;

        TextViewPartNumber.setText(String.valueOf(IntPartNumber));
        TextView TextViewPartRestante = (TextView) findViewById(R.id.textViewPartRestante);
        TextView TextViewPrice = (TextView) findViewById(R.id.textViewPrice);

        TextViewPrice.setText("Price :         " + String.valueOf(price) + " €" );

        TextViewPartRestante.setText("Number left :     " +String.valueOf(left));

    }

    public void minusButton(View vies) {
        TextView TextViewPartNumber = (TextView) findViewById(R.id.PortionNumber);
        String StringPartNumber = TextViewPartNumber.getText().toString();
        int IntPartNumber = Integer.parseInt(StringPartNumber);
        IntPartNumber -= 1;
        double price = dish.Price;
        int left = dish.NbPart;
        price *= IntPartNumber;
        left -= IntPartNumber;
        if (IntPartNumber < 1)
            return;
        TextViewPartNumber.setText(String.valueOf(IntPartNumber));
        TextView TextViewPartRestante = (TextView) findViewById(R.id.textViewPartRestante);
        TextView TextViewPrice = (TextView) findViewById(R.id.textViewPrice);
        TextViewPrice.setText("Price :         " + String.valueOf(price) + " €");
        TextViewPartRestante.setText("Number left :     " + String.valueOf(left));
    }

    protected View createForm(ViewGroup parent)
    {
        View ret = getLayoutInflater().inflate(R.layout.dishdetail_form, parent, false);

        //Pickup date text
        TextView output2 = (TextView) ret.findViewById(R.id.output2);
        output2.setText("You will pick your dish : ");

        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //Pickup interval time
            String dateString = sdf.format(dish.PickUpStartTime);
            String dateString2 = sdf.format(dish.PickUpEndTime);
            TextView pickUpDate = (TextView) ret.findViewById(R.id.textViewPickUpDate);
            pickUpDate.setText("You can pick your dish bewtwwen : " + dateString2 + " and " + dateString);
            //Expiration date
            TextView expirationDate = (TextView) ret.findViewById(R.id.textViewExpirationDate);
            expirationDate.setText("This dish will expire : " + sdf.format(dish.DateExpiration));
        }

        //Input number of part
        TextView partNumber = (TextView) ret.findViewById(R.id.PortionNumber);
        partNumber.setText("1");

        //Pickup time text
        TextView timeText = (TextView) ret.findViewById(R.id.output);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        // set current time into output textview
        timeText.setText(updateTime(hour, minute));

        //Button Hour picker listener
        btnClick = (Button) ret.findViewById(R.id.btnHourPickerBuyer);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        return ret;
    }

    protected void buildDynamicLayout()
    {
        View toBeInserted;
        ViewGroup parent = (ViewGroup)this.findViewById(R.id.dishdetail_dynamiclayout_container);

        Calendar expirationDate = new GregorianCalendar();
        expirationDate.setTime(dish.DateExpiration);

        if (dish.Statut.equals("Finish"))
        {
            toBeInserted = getLayoutInflater().inflate(R.layout.dishdetail_finish, parent, false);
            TextView text = (TextView)toBeInserted.findViewById(R.id.dishdetail_finish_text);
            if (dish.NbPart <= 0 && !Calendar.getInstance().after(expirationDate))
                text.setText("Victim of its success !");
            else
                text.setText("This dish is no longer available !");
        }
        else if (dish.Utilisateur.UtilisateurId.equals(Api.loggedUser.UtilisateurId)) // C'est mon plat : on affiche de quoi le cancel
        {
            toBeInserted = getLayoutInflater().inflate(R.layout.dishdetail_owner, parent, false);
            Button remove = (Button)toBeInserted.findViewById(R.id.dishdetail_owner_remove);

            final Activity caller = this;
            final Integer  dishId = this.dish.DishId;
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Api.removeDish(caller, dishId);
                }
            });
        }
        else //Date d'expiration non passé + c'est pas mon plat: on affiche le form
        {
            toBeInserted = this.createForm(parent);
        }
        parent.removeAllViews();
        parent.addView(toBeInserted);
    }
}
