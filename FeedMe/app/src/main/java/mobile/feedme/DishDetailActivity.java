package mobile.feedme;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mobile.feedme.POCO.Dish;

public class DishDetailActivity extends MenuActivity {

    Dish dish;
    private TextView output;
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
        output = (TextView) findViewById(R.id.output);
        Intent i = getIntent();
        this.dish = i.<Dish>getParcelableExtra("Dish");
        Log.e("DishDetail: ", dish.toString());

        ImageView image = (ImageView)findViewById(R.id.imageDish);
        ImageLoader.getInstance().displayImage(dish.MainImage, image);

        TextView output2 = (TextView) findViewById(R.id.output2);
        output2.setText("You will pick your dish : ");
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

        TextView partNumber = (TextView) findViewById(R.id.PortionNumber);
        partNumber.setText("1");
//        if (dish.SizePart != null) {
//            TextView poid = (TextView) findViewById(R.id.textViewPoid);
//            String convert = String.valueOf(dish.SizePart);
//            poid.setText(convert);
//        }

        TextView price = (TextView) findViewById(R.id.textViewPrice);
        String convert3 = String.valueOf(dish.Price);
        price.setText("Price :         " + convert3 + " €");

        TextView pickUpDate = (TextView) findViewById(R.id.textViewPickUpDate);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dateString = sdf.format(dish.PickUpStartTime);

        String dateString2 = sdf.format(dish.PickUpEndTime);

        pickUpDate.setText("You can pick your dish bewtwwen : " + dateString2 + " and " + dateString);

        TextView expirationDate = (TextView) findViewById(R.id.textViewExpirationDate);

        String expiration = sdf.format(dish.DateExpiration);
        expirationDate.setText("This dish will expire : " + expiration);

        TextView poid = (TextView) findViewById(R.id.textViewHeight);
        poid.setText("Weight :     " + dish.SizePart  + " grammes");

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);

        /********* display current time on screen End ********/

        // Add Button Click Listener
        addButtonClickListener();
    }
    public void addButtonClickListener() {

        btnClick = (Button) findViewById(R.id.btnHourPickerBuyer);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

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

            updateTime(hour, minute);

        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
            output.setText("You will pick up your dish around : " + aTime);
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
        if (IntPartNumber < 1)
            return;
        TextViewPartNumber.setText(String.valueOf(IntPartNumber));
        TextView TextViewPoid = (TextView) findViewById(R.id.textViewHeight);
        TextView TextViewPartRestante = (TextView) findViewById(R.id.textViewPartRestante);
        TextView TextViewPrice = (TextView) findViewById(R.id.textViewPrice);
        TextViewPoid.setText("Weight :     " + String.valueOf(poid) + " grammes");
        TextViewPrice.setText("Price :         " + String.valueOf(price) + " €" );
        TextViewPartRestante.setText("Number left :     " + String.valueOf(left));

    }

    public void submitForm(View vies)
    {
        //dish.nbPart
//        output = (TextView) findViewById(R.id.output);
//        String PickUpAvailability = output.getText().toString();
        return;
    }
}
